package br.com.caminhodasaguas.api.services;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.caminhodasaguas.api.DTO.ResponseDTO;
import br.com.caminhodasaguas.api.DTO.customs.ExperienceTourismCustomDTO;
import br.com.caminhodasaguas.api.DTO.request.ExperienceTourismEditRequestDTO;
import br.com.caminhodasaguas.api.DTO.request.ExperienceTourismRequestDTO;
import br.com.caminhodasaguas.api.configs.exceptions.ExperienceTourismAlreadyRegisteredException;
import br.com.caminhodasaguas.api.configs.exceptions.ExperienceTourismNotFoundException;
import br.com.caminhodasaguas.api.configs.exceptions.MaxSizeInvalidException;
import br.com.caminhodasaguas.api.configs.exceptions.PhoneInvalidException;
import br.com.caminhodasaguas.api.domains.ExperienceTourismDomain;
import br.com.caminhodasaguas.api.domains.items.ItemDomainExperienceTourism;
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

    public ResponseDTO<List<ExperienceTourismCustomDTO>> findAll() {
        List<ExperienceTourismDomain> domains = experienceTourismRepository.findAll();
        return new ResponseDTO<List<ExperienceTourismCustomDTO>>(ExperienceTourismMapper.toDTOList(domains));
    }

    public ResponseDTO<ExperienceTourismCustomDTO> findById(UUID id) {
        ExperienceTourismDomain domain = existTourism(id);
        return new ResponseDTO<ExperienceTourismCustomDTO>(ExperienceTourismMapper.toDTO(domain));
    }

    public void delete(UUID id) {
        ExperienceTourismDomain domain = existTourism(id);
        experienceTourismRepository.delete(domain);
    }

    @Transactional
    public ResponseDTO<ExperienceTourismCustomDTO> update(UUID id,
            ExperienceTourismEditRequestDTO experienceTourismEditRequestDTO) throws IOException {
        ExperienceTourismDomain experienceTourism = existTourism(id);
        validation(experienceTourismEditRequestDTO.phone(), experienceTourismEditRequestDTO.highlights());
        String url = OnlyDigitsUtils.normalize(experienceTourismEditRequestDTO.name());

        ExperienceTourismDomain update = ExperienceTourismDomain.edit(
                experienceTourism,
                experienceTourismEditRequestDTO.name(),
                FormatDescription.format(experienceTourismEditRequestDTO.description()),
                experienceTourismEditRequestDTO.phone(),
                experienceTourismEditRequestDTO.instagram(),
                experienceTourismEditRequestDTO.site(),
                url);

        if (experienceTourismEditRequestDTO.img() != null && !experienceTourismEditRequestDTO.img().isEmpty()) {
            String urlCapa = upload(bucketName, experienceTourismEditRequestDTO.img());
            update.setImg(urlCapa);
        }

        Set<UUID> uuids = experienceTourismEditRequestDTO.highlights_exists() == null
                ? update.getHighlights().stream().map(ItemDomainExperienceTourism::getId).collect(Collectors.toSet())
                : new HashSet<>(experienceTourismEditRequestDTO.highlights_exists());

        update.getHighlights().removeIf(item -> !uuids.contains(item.getId()));

        if (experienceTourismEditRequestDTO.highlights() != null) {
            for (MultipartFile f : experienceTourismEditRequestDTO.highlights()) {
                String urlHighlights = upload(bucketName, f);
                update.addHighlights(urlHighlights);
            }
        }

        ExperienceTourismDomain save = experienceTourismRepository.save(update);
        return new ResponseDTO<ExperienceTourismCustomDTO>(ExperienceTourismMapper.toDTO(save));
    }

    @Transactional
    public ResponseDTO<ExperienceTourismCustomDTO> save(ExperienceTourismRequestDTO experienceTourismRequestDTO)
            throws IOException {
        validation(experienceTourismRequestDTO.phone(), experienceTourismRequestDTO.highlights());
        String url = OnlyDigitsUtils.normalize(experienceTourismRequestDTO.name());

        existExperienceTourismUrl(url);

        ExperienceTourismDomain experienceTourism = ExperienceTourismDomain.draft(
                experienceTourismRequestDTO.name(),
                FormatDescription.format(experienceTourismRequestDTO.description()),
                experienceTourismRequestDTO.phone(),
                experienceTourismRequestDTO.instagram(),
                experienceTourismRequestDTO.site(),
                url);

        String img = upload(bucketName, experienceTourismRequestDTO.img());
        experienceTourism.setImg(img);

        experienceTourismRequestDTO.highlights()
                .forEach(multipartFile -> {
                    try {
                        String highlight = upload(bucketName, multipartFile);
                        experienceTourism.addHighlights(highlight);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        ExperienceTourismDomain save = experienceTourismRepository.save(experienceTourism);
        return new ResponseDTO<ExperienceTourismCustomDTO>(ExperienceTourismMapper.toDTO(save));
    }

    private void validation(String phone, List<MultipartFile> files) {
        if (!ValidationValueUtils.isValidAnyPhone(phone)) {
            logger.info("Phone is invalid with value: {}", phone);
            throw new PhoneInvalidException("Telefone inválido.");
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
