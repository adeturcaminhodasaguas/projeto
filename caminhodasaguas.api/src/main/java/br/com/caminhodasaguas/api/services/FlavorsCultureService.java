package br.com.caminhodasaguas.api.services;

import br.com.caminhodasaguas.api.DTO.FlavorsCultureDTO;
import br.com.caminhodasaguas.api.DTO.ResponseDTO;
import br.com.caminhodasaguas.api.configs.exceptions.FlavorsCultureAlreadyRegisteredException;
import br.com.caminhodasaguas.api.configs.exceptions.FlavorsCultureNotFoundException;
import br.com.caminhodasaguas.api.configs.exceptions.MaxSizeInvalidException;
import br.com.caminhodasaguas.api.configs.exceptions.PhoneInvalidException;
import br.com.caminhodasaguas.api.domains.FlavorsCultureDomain;
import br.com.caminhodasaguas.api.mappers.FlavorsCultureMapper;
import br.com.caminhodasaguas.api.repositories.FlavorsCultureRepository;
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
public class FlavorsCultureService {

    Logger logger = LoggerFactory.getLogger(FlavorsCultureService.class);

    @Autowired
    private MinioService minioService;

    @Autowired
    private FlavorsCultureRepository flavorsCultureRepository;

    @Value("${spring.minio.bucket}")
    private String bucketName;

    @Value("${spring.image.max-size}")
    private Integer MAX_SIZE;

    public ResponseDTO<List<FlavorsCultureDTO>> findAll() {
        List<FlavorsCultureDomain> domains = flavorsCultureRepository.findAll();
        return new ResponseDTO<List<FlavorsCultureDTO>>(FlavorsCultureMapper.toDTOList(domains));
    }

    public ResponseDTO<FlavorsCultureDTO> findById(UUID id){
        FlavorsCultureDomain domain = existFlavorsCulture(id);
        return new ResponseDTO<FlavorsCultureDTO>(FlavorsCultureMapper.toDTO(domain));
    }

    public void delete(UUID id){
        FlavorsCultureDomain domain = existFlavorsCulture(id);
        flavorsCultureRepository.delete(domain);
    }

    @Transactional
    public ResponseDTO<FlavorsCultureDTO> update(UUID id, FlavorsCultureDTO flavorsCultureEditRequestDTO) throws IOException {
        FlavorsCultureDomain flavorsCulture = existFlavorsCulture(id);
        validation(flavorsCultureEditRequestDTO.phone(), flavorsCultureEditRequestDTO.new_highlights());
        String url = OnlyDigitsUtils.normalize(flavorsCultureEditRequestDTO.name());

        FlavorsCultureDomain update = FlavorsCultureDomain.edit(
                flavorsCulture,
                flavorsCultureEditRequestDTO.name(),
                FormatDescription.format(flavorsCultureEditRequestDTO.description()),
                flavorsCultureEditRequestDTO.phone(),
                flavorsCultureEditRequestDTO.instagram(),
                flavorsCultureEditRequestDTO.site(),
                url
        );

        if(flavorsCultureEditRequestDTO.deleted_highlights() != null) {
            update.getHighlights().removeIf(item -> {
                boolean match = flavorsCultureEditRequestDTO.deleted_highlights().contains(item.getId());
                if (match) item.setFlavorsCultureDomain(null);
                return match;
            });
        }

        if (flavorsCultureEditRequestDTO.new_highlights() != null) {
            for (MultipartFile f : flavorsCultureEditRequestDTO.new_highlights()) {
                String urlHighlights = upload(bucketName, f);
                update.addHighlights(urlHighlights);
            }
        }

        FlavorsCultureDomain save = flavorsCultureRepository.save(flavorsCulture);
        return new ResponseDTO<FlavorsCultureDTO>(FlavorsCultureMapper.toDTO(save));
    }

    @Transactional
    public ResponseDTO<FlavorsCultureDTO> save(FlavorsCultureDTO flavorsCultureDTO) throws IOException {
        validation(flavorsCultureDTO.phone(), flavorsCultureDTO.new_highlights());
        String url = OnlyDigitsUtils.normalize(flavorsCultureDTO.name());

        existFlavorsCultureUrl(url);

        FlavorsCultureDomain flavorsCulture = FlavorsCultureDomain.draft(
                flavorsCultureDTO.name(),
                FormatDescription.format(flavorsCultureDTO.description()),
                flavorsCultureDTO.phone(),
                flavorsCultureDTO.instagram(),
                flavorsCultureDTO.site(),
                OnlyDigitsUtils.normalize(url)
        );

        flavorsCultureDTO.new_highlights()
                .forEach(multipartFile -> {
                    try {
                        String highlight = upload(bucketName, multipartFile);
                        flavorsCulture.addHighlights(highlight);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        FlavorsCultureDomain save = flavorsCultureRepository.save(flavorsCulture);
        return new ResponseDTO<FlavorsCultureDTO>(FlavorsCultureMapper.toDTO(save));
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

    private FlavorsCultureDomain existFlavorsCulture(UUID id) {
        return flavorsCultureRepository.findById(id)
                .orElseThrow(() -> {
                    logger.info("Sabores e Cultura não encontrado.");
                    throw new FlavorsCultureNotFoundException("Sabores e Cultura não encontrado.");
                });
    }

    private void existFlavorsCultureUrl(String url) {
        flavorsCultureRepository.findByUrl(url)
                .ifPresent(flavorsCultureDomain -> {
                    logger.info("Sabores e Cultura já cadastrado com o nome: {}", flavorsCultureDomain.getName());
                    throw new FlavorsCultureAlreadyRegisteredException("Sabores e Cultura já cadastrado.");
                });
    }

}
