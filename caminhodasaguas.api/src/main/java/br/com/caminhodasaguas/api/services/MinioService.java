package br.com.caminhodasaguas.api.services;

import br.com.caminhodasaguas.api.configs.exceptions.MinioHandlerException;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class MinioService {

    private Logger logger = LoggerFactory.getLogger(MinioService.class);

    @Autowired
    private MinioClient minioClient;

    @Value("${spring.minio.url}")
    private String urlMinio;

    public String uploadFile(String bucketName, String objectName, MultipartFile multipartFile, String contentType) {
        try {
            createBucketIfNotExists(bucketName);
            uploadToStorage(bucketName, objectName, multipartFile, contentType);
        } catch (Exception e) {
            logger.error("Erro ao processar upload do arquivo: {}", e.getMessage(), e);
            throw new MinioHandlerException("Erro ao processar upload do arquivo: " + e.getMessage());
        }
        return getUrl(bucketName, objectName);
    }

    private void createBucketIfNotExists(String bucketName) throws Exception {
        boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!bucketExists) {
            logger.info("Bucket {} não existe. Criando...", bucketName);
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }

        logger.info("Bucket {} já existe ou foi criado com sucesso.", bucketName);
    }

    private void uploadToStorage(String bucketName, String objectName, MultipartFile multipartFile, String contentType)
            throws Exception {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(multipartFile.getInputStream(), multipartFile.getSize(), -1)
                            .contentType(contentType)
                            .build()
            );
            logger.info("Arquivo {} enviado com sucesso para o bucket {}", objectName, bucketName);
        } catch (IOException e) {
            logger.error("Erro ao ler dados do arquivo: {}", e.getMessage(), e);
            throw new MinioHandlerException("Erro ao ler dados do arquivo: " + e.getMessage());
        }
    }

    private String getUrl(String bucketName, String objectName) {
        return String.format(
                "%s/%s/%s",
                urlMinio,
                bucketName,
                objectName
        );
    }

}
