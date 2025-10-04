package br.com.caminhodasaguas.api.services;

import br.com.caminhodasaguas.api.DTO.MunicipalityDTO;
import br.com.caminhodasaguas.api.DTO.ResponseDTO;
import br.com.caminhodasaguas.api.configs.exceptions.MaxSizeInvalidException;
import br.com.caminhodasaguas.api.configs.exceptions.MunicipalityAlreadyRegisteredException;
import br.com.caminhodasaguas.api.configs.exceptions.PhoneInvalidException;
import br.com.caminhodasaguas.api.domains.MunicipalityDomain;
import br.com.caminhodasaguas.api.mappers.MunicipalityMapper;
import br.com.caminhodasaguas.api.repositories.MunicipalityRepository;
import br.com.caminhodasaguas.api.utils.FormatDescription;
import br.com.caminhodasaguas.api.utils.OnlyDigitsUtils;
import br.com.caminhodasaguas.api.utils.ValidationValueUtils;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class MunicipalityService {

    Logger logger = LoggerFactory.getLogger(MunicipalityService.class);

    @Autowired
    private MinioService minioService;

    @Autowired
    private MunicipalityRepository municipalityRepository;

    @Value("${spring.minio.bucket}")
    private String bucketName;

    @Value("${spring.image.max-size}")
    private Integer MAX_SIZE;

    public ResponseDTO<List<MunicipalityDTO>> findAll() {
        List<MunicipalityDomain> domains = municipalityRepository.findAll();
        return new ResponseDTO<List<MunicipalityDTO>>(MunicipalityMapper.toDTOList(domains));
    }

    public ResponseDTO<MunicipalityDTO> findById(UUID id){
        MunicipalityDomain domain = existMunicipality(id);
        return new ResponseDTO<MunicipalityDTO>(MunicipalityMapper.toDTO(domain));
    }

    public void delete(UUID id){
        MunicipalityDomain domain = existMunicipality(id);
        municipalityRepository.delete(domain);
    }

    @Transactional
    public ResponseDTO<MunicipalityDTO> update(UUID id, MunicipalityDTO municipalityEditRequestDTO) throws IOException {
        MunicipalityDomain municipality = existMunicipality(id);
        validation(municipalityEditRequestDTO.phone(), municipalityEditRequestDTO.new_highlights());
        String url = OnlyDigitsUtils.normalize(municipalityEditRequestDTO.name());

        MunicipalityDomain update = MunicipalityDomain.edit(
                municipality,
                municipalityEditRequestDTO.name(),
                FormatDescription.format(municipalityEditRequestDTO.description()),
                municipalityEditRequestDTO.phone(),
                municipalityEditRequestDTO.instagram(),
                municipalityEditRequestDTO.site(),
                url
        );

        if(municipalityEditRequestDTO.deleted_highlights() != null) {
            update.getHighlights().removeIf(item -> {
                boolean match = municipalityEditRequestDTO.deleted_highlights().contains(item.getId());
                if (match) item.setMunicipalityDomain(null);
                return match;
            });
        }

        if (municipalityEditRequestDTO.new_highlights() != null) {
            for (MultipartFile f : municipalityEditRequestDTO.new_highlights()) {
                String urlHighlights = upload(bucketName, f);
                update.addHighlights(urlHighlights);
            }
        }

        MunicipalityDomain save = municipalityRepository.save(municipality);
        return new ResponseDTO<MunicipalityDTO>(MunicipalityMapper.toDTO(save));
    }

    @Transactional
    public ResponseDTO<MunicipalityDTO> save(MunicipalityDTO municipalityDTO) throws IOException {
        validation(municipalityDTO.phone(), municipalityDTO.new_highlights());
        String url = OnlyDigitsUtils.normalize(municipalityDTO.name());

        existMunicipalityUrl(url);

        MunicipalityDomain municipality = MunicipalityDomain.draft(
                municipalityDTO.name(),
                FormatDescription.format(municipalityDTO.description()),
                municipalityDTO.phone(),
                municipalityDTO.instagram(),
                municipalityDTO.site(),
                OnlyDigitsUtils.normalize(url)
        );

        municipalityDTO.new_highlights()
                .forEach(multipartFile -> {
                    try {
                        String highlight = upload(bucketName, multipartFile);
                        municipality.addHighlights(highlight);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        MunicipalityDomain save = municipalityRepository.save(municipality);
        return new ResponseDTO<MunicipalityDTO>(MunicipalityMapper.toDTO(save));
    }

    private void validation(String phone, List<MultipartFile> files) {
        if (!ValidationValueUtils.isValidAnyPhone(phone)) {
            logger.info("Phone is invalid with value: {}", phone);
            throw new PhoneInvalidException("Telefone inválido.");
        }

        if(files == null || files.isEmpty()) {
           return;
        }

        if(!ValidationValueUtils.isLengthValid(files, MAX_SIZE)) {
            logger.info("File is invalid with max size: {}", files.size());
            throw new MaxSizeInvalidException("Tamanho maximo 6 fotos.");
        }
    }

    private String upload(String bucketName, MultipartFile img) throws IOException {
        String name = OnlyDigitsUtils.normalize(img.getOriginalFilename()) + ".webp";
        return minioService.uploadFile(
                bucketName,
                name,
                img,
                "image/webp");
    }

    private MunicipalityDomain existMunicipality(UUID id) {
        return municipalityRepository.findById(id)
                .orElseThrow(() -> {
                    logger.info("Municipio não encontrado.");
                    throw new MunicipalityAlreadyRegisteredException("Municipio não encontrado.");
                });
    }

    private void existMunicipalityUrl(String url) {
        municipalityRepository.findByUrl(url)
                .ifPresent(municipalityDomain -> {
                    logger.info("Municipio já cadastrado com o nome: {}", municipalityDomain.getName());
                    throw new MunicipalityAlreadyRegisteredException("Municipio já cadastrado.");
                });
    }

}
