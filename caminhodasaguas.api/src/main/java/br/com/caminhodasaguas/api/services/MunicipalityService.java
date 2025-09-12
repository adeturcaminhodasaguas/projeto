package br.com.caminhodasaguas.api.services;

import br.com.caminhodasaguas.api.DTO.request.MunicipalityEditRequestDTO;
import br.com.caminhodasaguas.api.DTO.request.MunicipalityRequestDTO;
import br.com.caminhodasaguas.api.DTO.ResponseDTO;
import br.com.caminhodasaguas.api.DTO.customs.MunicipalityCustomDTO;
import br.com.caminhodasaguas.api.configs.exceptions.MaxSizeInvalidException;
import br.com.caminhodasaguas.api.configs.exceptions.MunicipalityAlreadyRegisteredException;
import br.com.caminhodasaguas.api.configs.exceptions.PhoneInvalidException;
import br.com.caminhodasaguas.api.domains.MunicipalityDomain;
import br.com.caminhodasaguas.api.domains.items.ItemDomainMunicipality;
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
import java.util.stream.Collectors;

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

    public ResponseDTO<List<MunicipalityCustomDTO>> findAll() {
        List<MunicipalityDomain> domains = municipalityRepository.findAll();
        return new ResponseDTO<List<MunicipalityCustomDTO>>(MunicipalityMapper.toDTOList(domains));
    }

    public ResponseDTO<MunicipalityCustomDTO> findById(UUID id){
        MunicipalityDomain domain = existMunicipality(id);
        return new ResponseDTO<MunicipalityCustomDTO>(MunicipalityMapper.toDTO(domain));
    }

    public void delete(UUID id){
        MunicipalityDomain domain = existMunicipality(id);
        municipalityRepository.delete(domain);
    }

    @Transactional
    public ResponseDTO<MunicipalityCustomDTO> update(UUID id, MunicipalityEditRequestDTO municipalityEditRequestDTO) throws IOException {
        MunicipalityDomain municipality = existMunicipality(id);
        validation(municipalityEditRequestDTO.phone(), municipalityEditRequestDTO.highlights());
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

        if (municipalityEditRequestDTO.img() != null && !municipalityEditRequestDTO.img().isEmpty()) {
            String urlCapa = upload(bucketName, municipalityEditRequestDTO.img());
            update.setImg(urlCapa);
        }

        Set<UUID> uuids = municipalityEditRequestDTO.highlights_exists() == null
                ? update.getHighlights().stream().map(ItemDomainMunicipality::getId).collect(Collectors.toSet())
                : new HashSet<>(municipalityEditRequestDTO.highlights_exists());

        update.getHighlights().removeIf(item -> !uuids.contains(item.getId()));

        if (municipalityEditRequestDTO.highlights() != null) {
            for (MultipartFile f : municipalityEditRequestDTO.highlights()) {
                String urlHighlights = upload(bucketName, f);
                update.addHighlights(urlHighlights);
            }
        }

        MunicipalityDomain save = municipalityRepository.save(municipality);
        return new ResponseDTO<MunicipalityCustomDTO>(MunicipalityMapper.toDTO(save));
    }

    @Transactional
    public ResponseDTO<MunicipalityCustomDTO> save(MunicipalityRequestDTO municipalityRequestDTO) throws IOException {
        validation(municipalityRequestDTO.phone(), municipalityRequestDTO.highlights());
        String url = OnlyDigitsUtils.normalize(municipalityRequestDTO.name());

        existMunicipalityUrl(url);

        MunicipalityDomain municipality = MunicipalityDomain.draft(
                municipalityRequestDTO.name(),
                FormatDescription.format(municipalityRequestDTO.description()),
                municipalityRequestDTO.phone(),
                municipalityRequestDTO.instagram(),
                municipalityRequestDTO.site(),
                OnlyDigitsUtils.normalize(url)
        );

        String img = upload(bucketName, municipalityRequestDTO.img());
        municipality.setImg(img);

        municipalityRequestDTO.highlights()
                .forEach(multipartFile -> {
                    try {
                        String highlight = upload(bucketName, multipartFile);
                        municipality.addHighlights(highlight);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        MunicipalityDomain save = municipalityRepository.save(municipality);
        return new ResponseDTO<MunicipalityCustomDTO>(MunicipalityMapper.toDTO(save));
    }

    private void validation(String phone, List<MultipartFile> files) {
        if (!ValidationValueUtils.isValidAnyPhone(phone)) {
            logger.info("Phone is invalid with value: {}", phone);
            throw new PhoneInvalidException("Telefone inválido.");
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
