package br.com.caminhodasaguas.api.services;

import br.com.caminhodasaguas.api.DTO.BlogDTO;
import br.com.caminhodasaguas.api.DTO.ResponseDTO;
import br.com.caminhodasaguas.api.configs.exceptions.BlogAlreadyRegisteredException;
import br.com.caminhodasaguas.api.configs.exceptions.MaxSizeInvalidException;
import br.com.caminhodasaguas.api.configs.exceptions.PhoneInvalidException;
import br.com.caminhodasaguas.api.domains.BlogDomain;
import br.com.caminhodasaguas.api.mappers.BlogMapper;
import br.com.caminhodasaguas.api.repositories.BlogRepository;
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
public class BlogService {

    Logger logger = LoggerFactory.getLogger(BlogService.class);

    @Autowired
    private MinioService minioService;

    @Autowired
    private BlogRepository blogRepository;

    @Value("${spring.minio.bucket}")
    private String bucketName;

    @Value("${spring.image.max-size}")
    private Integer MAX_SIZE;

    public ResponseDTO<List<BlogDTO>> findAll() {
        List<BlogDomain> domains = blogRepository.findAll();
        return new ResponseDTO<List<BlogDTO>>(BlogMapper.toDTOList(domains));
    }

    public ResponseDTO<BlogDTO> findById(UUID id) {
        BlogDomain domain = existBlog(id);
        return new ResponseDTO<BlogDTO>(BlogMapper.toDTO(domain));
    }

    public void delete(UUID id) {
        BlogDomain domain = existBlog(id);
        blogRepository.delete(domain);
    }

    @Transactional
    public ResponseDTO<BlogDTO> update(UUID id, BlogDTO blogEditRequestDTO) throws IOException {
        BlogDomain blog = existBlog(id);
        validation(blogEditRequestDTO.phone(), blogEditRequestDTO.new_highlights());
        String url = OnlyDigitsUtils.normalize(blogEditRequestDTO.name());

        BlogDomain update = BlogDomain.edit(
                blog,
                blogEditRequestDTO.name(),
                FormatDescription.format(blogEditRequestDTO.description()),
                blogEditRequestDTO.phone(),
                blogEditRequestDTO.instagram(),
                blogEditRequestDTO.site(),
                url);

        if (blogEditRequestDTO.deleted_highlights() != null) {
            update.getHighlights().removeIf(item -> {
                boolean match = blogEditRequestDTO.deleted_highlights().contains(item.getId());
                if (match)
                    item.setBlogDomain(null);
                return match;
            });
        }

        if (blogEditRequestDTO.new_highlights() != null) {
            for (MultipartFile f : blogEditRequestDTO.new_highlights()) {
                String urlHighlights = upload(bucketName, f);
                update.addHighlights(urlHighlights);
            }
        }

        BlogDomain save = blogRepository.save(blog);
        return new ResponseDTO<BlogDTO>(BlogMapper.toDTO(save));
    }

    @Transactional
    public ResponseDTO<BlogDTO> save(BlogDTO blogDTO) throws IOException {
        validation(blogDTO.phone(), blogDTO.new_highlights());
        String url = OnlyDigitsUtils.normalize(blogDTO.name());

        existBlogUrl(url);

        BlogDomain blog = BlogDomain.draft(
                blogDTO.name(),
                FormatDescription.format(blogDTO.description()),
                blogDTO.phone(),
                blogDTO.instagram(),
                blogDTO.site(),
                OnlyDigitsUtils.normalize(url));

        blogDTO.new_highlights()
                .forEach(multipartFile -> {
                    try {
                        String highlight = upload(bucketName, multipartFile);
                        blog.addHighlights(highlight);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        BlogDomain save = blogRepository.save(blog);
        return new ResponseDTO<BlogDTO>(BlogMapper.toDTO(save));
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

    private BlogDomain existBlog(UUID id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> {
                    logger.info("Blog não encontrado.");
                    throw new BlogAlreadyRegisteredException("Blog não encontrado.");
                });
    }

    private void existBlogUrl(String url) {
        blogRepository.findByUrl(url)
                .ifPresent(blogDomain -> {
                    logger.info("Blog já cadastrado com o nome: {}", blogDomain.getName());
                    throw new BlogAlreadyRegisteredException("Blog já cadastrado.");
                });
    }

}
