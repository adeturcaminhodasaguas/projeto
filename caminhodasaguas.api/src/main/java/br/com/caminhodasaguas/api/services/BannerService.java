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
import br.com.caminhodasaguas.api.DTO.customs.BannerCustomDTO;
import br.com.caminhodasaguas.api.DTO.request.BannerEditRequestDTO;
import br.com.caminhodasaguas.api.DTO.request.BannerRequestDTO;
import br.com.caminhodasaguas.api.configs.exceptions.BannerException;
import br.com.caminhodasaguas.api.configs.exceptions.BannerNotFoundException;
import br.com.caminhodasaguas.api.domains.BannerDomain;
import br.com.caminhodasaguas.api.mappers.BannerMapper;
import br.com.caminhodasaguas.api.repositories.BannerRepository;
import br.com.caminhodasaguas.api.utils.OnlyDigitsUtils;

@Service
public class BannerService {

    Logger logger = LoggerFactory.getLogger(BannerService.class);

    @Autowired
    private BannerRepository bannerRepository;

    @Autowired
    private MinioService minioService;

    @Value("${spring.minio.bucket}")
    private String bucketName;

    public ResponseDTO<BannerCustomDTO> save(BannerRequestDTO bannerRequestDTO) {
        try {
            String imgUrl = upload(bucketName, bannerRequestDTO.img());

            BannerDomain banner = BannerDomain.draft(
                    imgUrl, bannerRequestDTO.position(),
                    bannerRequestDTO.link(),
                    bannerRequestDTO.altText());

            BannerDomain save = bannerRepository.save(banner);
            logger.info("Banner saved with id: {}", save.getId());
            return new ResponseDTO<BannerCustomDTO>(BannerMapper.toDTO(save));
        } catch (IOException e) {
            logger.error("Error uploading image", e);
            throw new BannerException("Error uploading image");
        }
    }

    public ResponseDTO<List<BannerCustomDTO>> findAll() {
        List<BannerDomain> banners = bannerRepository.findAll();
        return new ResponseDTO<List<BannerCustomDTO>>(BannerMapper.toDTOList(banners));
    }

    public ResponseDTO<BannerCustomDTO> update(UUID id, BannerEditRequestDTO bannerRequestDTO) {
        BannerDomain banner = existBanner(id);
        try {
            banner = BannerDomain.edit(
                    banner,
                    bannerRequestDTO.position(),
                    bannerRequestDTO.link(),
                    bannerRequestDTO.altText());

            MultipartFile img = bannerRequestDTO.img();
            if (img != null && !img.isEmpty()) {
                String imgUrl = upload(bucketName, img);
                banner.setImg(imgUrl);
            }

            BannerDomain updatedBanner = bannerRepository.save(banner);
            logger.info("Banner updated with id: {}", updatedBanner.getId());
            return new ResponseDTO<BannerCustomDTO>(BannerMapper.toDTO(updatedBanner));
        } catch (IOException e) {
            logger.error("Error uploading image", e);
            throw new BannerException("Error uploading image");
        }
    }

    public ResponseDTO<BannerCustomDTO> findById(UUID id) {
        BannerDomain banner = existBanner(id);
        logger.info("Banner found with id: {}", id);
        return new ResponseDTO<BannerCustomDTO>(BannerMapper.toDTO(banner));
    }

    public void delete(UUID id) {
        BannerDomain banner = existBanner(id);
        bannerRepository.delete(banner);
        logger.info("Banner deleted with id: {}", id);
    }

    private BannerDomain existBanner(UUID id) {
        return bannerRepository.findById(id)
                .orElseThrow(() -> {
                    logger.info("Banner não encontrado.");
                    throw new BannerNotFoundException("Banner não encontrado.");
                });
    }

    private String upload(String bucketName, MultipartFile img) throws IOException {
        String name = OnlyDigitsUtils.normalize(img.getOriginalFilename()) + ".webp";
        return minioService.uploadFile(
                bucketName,
                name,
                img,
                "image/webp");
    }

}
