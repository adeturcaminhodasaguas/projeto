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

import br.com.caminhodasaguas.api.DTO.ResponseDTO;
import br.com.caminhodasaguas.api.DTO.TestimonialsDTO;
import br.com.caminhodasaguas.api.configs.exceptions.IntegerInvalidException;
import br.com.caminhodasaguas.api.configs.exceptions.TestimonialsNotFoundException;
import br.com.caminhodasaguas.api.domains.TestimonialsDomain;
import br.com.caminhodasaguas.api.mappers.TestimonialsMappers;
import br.com.caminhodasaguas.api.repositories.TestimonialsRepository;
import br.com.caminhodasaguas.api.utils.FormatDescription;
import br.com.caminhodasaguas.api.utils.OnlyDigitsUtils;
import br.com.caminhodasaguas.api.utils.ValidationValueUtils;

@Service
public class TestimonialsService {

    Logger logger = LoggerFactory.getLogger(TestimonialsService.class);

    @Autowired
    private TestimonialsRepository testimonialsRepository;

    @Autowired
    private MinioService minioService;

    @Value("${spring.minio.bucket}")
    private String bucketName;

    public ResponseDTO<TestimonialsDTO> save(TestimonialsDTO testimonialsDTO) throws IOException {
        validation(testimonialsDTO);

        TestimonialsDomain domain = TestimonialsDomain.draft(
                testimonialsDTO.name(),
                FormatDescription.format(testimonialsDTO.description()),
                testimonialsDTO.city(),
                testimonialsDTO.stars()
        );

        if(testimonialsDTO.img() != null) {
            String imgUrl = upload(bucketName, testimonialsDTO.img());
            domain.setImg(imgUrl);
        }

        TestimonialsDomain dto = testimonialsRepository.save(domain);

        logger.info("Depoimento salvo com o nome {}", dto.getName());

        return new ResponseDTO<TestimonialsDTO>(TestimonialsMappers.toDTO(dto));
    }

    public ResponseDTO<TestimonialsDTO> update(UUID id, TestimonialsDTO testimonialsDTO) throws IOException {
        validation(testimonialsDTO);

        TestimonialsDomain domain = existingTestimonial(id);

        TestimonialsDomain update = TestimonialsDomain.edit(
            domain,
            testimonialsDTO.name(),
            testimonialsDTO.description(),
            testimonialsDTO.city(),
            testimonialsDTO.stars()
        );

        if(testimonialsDTO.img() != null) {
            String imgUrl = upload(bucketName, testimonialsDTO.img());
            update.setImg(imgUrl);
        }

        TestimonialsDomain dto = testimonialsRepository.save(update);

        logger.info("Depoimento atualizado com o nome {}", dto.getName());

        return new ResponseDTO<TestimonialsDTO>(TestimonialsMappers.toDTO(dto));
    }

    public ResponseDTO<List<TestimonialsDTO>> findAll(){
        List<TestimonialsDomain> domains = testimonialsRepository.findAll();
        
        logger.info("Listando um total de {} testemunhos", domains.size());

        return new ResponseDTO<List<TestimonialsDTO>>(TestimonialsMappers.toDTOList(domains));
    }

    public ResponseDTO<TestimonialsDTO> findById(UUID id){
        TestimonialsDomain domain = existingTestimonial(id);

        logger.info("Testimonial encontrado com o nome de {}", domain.getName());

         return new ResponseDTO<TestimonialsDTO>(TestimonialsMappers.toDTO(domain));
    }

    public void delete(UUID id){
        TestimonialsDomain domain = existingTestimonial(id);
        testimonialsRepository.delete(domain);
        logger.info("Depoimento deletado com o nome de {}", domain.getName());
    }

    public TestimonialsDomain existingTestimonial(UUID id){
        return testimonialsRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Depoimento não encontrado: {}", id);
                    throw new TestimonialsNotFoundException("Depoimento não encontrado");
                });
    }

    private void validation(TestimonialsDTO testimonialsDTO) {
        if (!ValidationValueUtils.isInteger(testimonialsDTO.stars()) || testimonialsDTO.stars() > 5) {
            logger.error("Número de estrelas inválido: {}", testimonialsDTO.stars());
            throw new IntegerInvalidException("Número de estrelas inválido. Deve ser um número inteiro entre 0 e 5.");
        }
    }

    private String upload(String bucketName, MultipartFile img) throws IOException {
        String name = OnlyDigitsUtils.normalize(img.getOriginalFilename()) + ".webp";
        return minioService.uploadFile(
                bucketName,
                name,
                img,
                "image/webp"
        );
    }

}