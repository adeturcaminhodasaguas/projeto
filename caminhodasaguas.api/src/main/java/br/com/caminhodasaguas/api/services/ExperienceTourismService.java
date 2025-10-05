package br.com.caminhodasaguas.api.services;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.caminhodasaguas.api.DTO.ExperienceTourismDTO;
import br.com.caminhodasaguas.api.DTO.ResponseDTO;
import br.com.caminhodasaguas.api.configs.exceptions.ExperienceTourismAlreadyRegisteredException;
import br.com.caminhodasaguas.api.configs.exceptions.ExperienceTourismNotFoundException;
import br.com.caminhodasaguas.api.configs.exceptions.MaxSizeInvalidException;
import br.com.caminhodasaguas.api.configs.exceptions.PhoneInvalidException;
import br.com.caminhodasaguas.api.domains.ExperienceTourismDomain;
import br.com.caminhodasaguas.api.mappers.ExperienceTourismMapper;
import br.com.caminhodasaguas.api.repositories.ExperienceTourismRepository;
import br.com.caminhodasaguas.api.utils.FormatDescription;
import br.com.caminhodasaguas.api.utils.OnlyDigitsUtils;
import br.com.caminhodasaguas.api.utils.ValidationValueUtils;
import jakarta.transaction.Transactional;

@Service
public class ExperienceTourismService {

    Logger logger = LoggerFactory.getLogger(ExperienceTourismService.class);

    @Autowired
    private ExperienceTourismRepository experienceTourismRepository;

    @Autowired
    private MinioService minioService;

    @Value("${spring.minio.bucket}")
    private String bucketName;

    @Value("${spring.image.max-size}")
    private Integer MAX_SIZE;

    public ResponseDTO<List<ExperienceTourismDTO>> findAll() {
        List<ExperienceTourismDomain> domains = experienceTourismRepository.findAll();
        return new ResponseDTO<List<ExperienceTourismDTO>>(ExperienceTourismMapper.toDTOList(domains));
    }

    public ResponseDTO<ExperienceTourismDTO> findById(UUID id) {
        ExperienceTourismDomain domain = existTourism(id);
        return new ResponseDTO<ExperienceTourismDTO>(ExperienceTourismMapper.toDTO(domain));
    }

    public void delete(UUID id) {
        ExperienceTourismDomain domain = existTourism(id);
        experienceTourismRepository.delete(domain);
    }

    @Transactional
    public ResponseDTO<ExperienceTourismDTO> update(UUID id,
            ExperienceTourismDTO experienceTourismEditRequestDTO) throws IOException {
        ExperienceTourismDomain experienceTourism = existTourism(id);
        validation(experienceTourismEditRequestDTO.phone(), experienceTourismEditRequestDTO.new_highlights());
        String url = OnlyDigitsUtils.normalize(experienceTourismEditRequestDTO.name());

        ExperienceTourismDomain update = ExperienceTourismDomain.edit(
                experienceTourism,
                experienceTourismEditRequestDTO.name(),
                FormatDescription.format(experienceTourismEditRequestDTO.description()),
                experienceTourismEditRequestDTO.phone(),
                experienceTourismEditRequestDTO.instagram(),
                experienceTourismEditRequestDTO.site(),
                url, 
                experienceTourismEditRequestDTO.municipality()
                );


          if(experienceTourismEditRequestDTO.deleted_highlights() != null) {
            update.getHighlights().removeIf(item -> {
                boolean match = experienceTourismEditRequestDTO.deleted_highlights().contains(item.getId());
                if (match) item.setExperienceTourismDomain(null);
                return match;
            });
        }

        if (experienceTourismEditRequestDTO.new_highlights() != null) {
            for (MultipartFile f : experienceTourismEditRequestDTO.new_highlights()) {
                String urlHighlights = upload(bucketName, f);
                update.addHighlights(urlHighlights);
            }
        }

        ExperienceTourismDomain save = experienceTourismRepository.save(update);
        return new ResponseDTO<ExperienceTourismDTO>(ExperienceTourismMapper.toDTO(save));
    }

    @Transactional
    public ResponseDTO<ExperienceTourismDTO> save(ExperienceTourismDTO experienceTourismRequestDTO)
            throws IOException {
        validation(experienceTourismRequestDTO.phone(), experienceTourismRequestDTO.new_highlights());
        String url = OnlyDigitsUtils.normalize(experienceTourismRequestDTO.name());

        existExperienceTourismUrl(url);

        ExperienceTourismDomain experienceTourism = ExperienceTourismDomain.draft(
                experienceTourismRequestDTO.name(),
                FormatDescription.format(experienceTourismRequestDTO.description()),
                experienceTourismRequestDTO.phone(),
                experienceTourismRequestDTO.instagram(),
                experienceTourismRequestDTO.site(),
                url,
                experienceTourismRequestDTO.municipality()
                );

        experienceTourismRequestDTO.new_highlights()
                .forEach(multipartFile -> {
                    try {
                        String highlight = upload(bucketName, multipartFile);
                        experienceTourism.addHighlights(highlight);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        ExperienceTourismDomain save = experienceTourismRepository.save(experienceTourism);
        return new ResponseDTO<ExperienceTourismDTO>(ExperienceTourismMapper.toDTO(save));
    }

    private void validation(String phone, List<MultipartFile> files) {
        if (!ValidationValueUtils.isValidAnyPhone(phone)) {
            logger.info("Phone is invalid with value: {}", phone);
            throw new PhoneInvalidException("Telefone inválido.");
        }

        if (files == null || files.isEmpty()) {
            return;
        }

        if (!ValidationValueUtils.isLengthValid(files, MAX_SIZE)) {
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

    private ExperienceTourismDomain existTourism(UUID id) {
        return experienceTourismRepository.findById(id)
                .orElseThrow(() -> {
                    logger.info("Experiência de turismo não encontrada.");
                    throw new ExperienceTourismNotFoundException("Experiência de turismo não encontrada.");
                });
    }

    private void existExperienceTourismUrl(String url) {
        experienceTourismRepository.findByUrl(url)
                .ifPresent(experienceTourismDomain -> {
                    logger.info("Experiência de turismo já cadastrada com o nome: {}",
                            experienceTourismDomain.getName());
                    throw new ExperienceTourismAlreadyRegisteredException("Experiência de turismo já cadastrada.");
                });
    }

}