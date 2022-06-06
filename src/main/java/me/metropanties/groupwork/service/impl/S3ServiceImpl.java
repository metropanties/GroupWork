package me.metropanties.groupwork.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import me.metropanties.groupwork.exception.S3ObjectAlreadyExists;
import me.metropanties.groupwork.exception.S3ObjectNotFound;
import me.metropanties.groupwork.service.S3Service;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(S3ServiceImpl.class);

    private final AmazonS3 s3;

    @Value("${s3.bucket.name}")
    private String bucketName;

    @Override
    public void uploadFile(@NotNull String fileName, @NotNull MultipartFile file) {
        if (fileExists(fileName))
            throw new S3ObjectAlreadyExists(String.format("File with name %s already exists!", file));

        ObjectMetadata metadata = new ObjectMetadata();
        if (file.getContentType() == null)
            throw new IllegalArgumentException("File content type cannot be null!");

        if (!file.getContentType().equals(MediaType.IMAGE_PNG_VALUE) || !file.getContentType().equals(MediaType.IMAGE_JPEG_VALUE))
            return;

        metadata.setUserMetadata(Map.of(
                "Content-Type", file.getContentType(),
                "Content-Length", String.valueOf(file.getSize())
        ));

        try {
            s3.putObject(bucketName, fileName, file.getInputStream(), metadata);
        } catch (IOException e) {
            LOGGER.error("An error occurred when uploading file!", e);
        }
    }

    @Override
    public void deleteFile(@NotNull String fileName) {
        if (!fileExists(fileName))
            throw new S3ObjectNotFound(String.format("%s file not found!", fileName));

        s3.deleteObject(bucketName, fileName);
    }

    @Override
    public boolean fileExists(@NotNull String fileName) {
        return s3.doesObjectExist(bucketName, fileName);
    }

    @Override
    public byte[] getFile(@NotNull String fileName) {
        if (!fileExists(fileName))
            return new byte[0];

        S3Object object = s3.getObject(bucketName, fileName);
        try {
            return IOUtils.toByteArray(object.getObjectContent());
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return new byte[0];
        }
    }

}
