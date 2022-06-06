package me.metropanties.groupwork.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

public interface S3Service {

    void uploadFile(@NotNull String fileName, @NotNull MultipartFile file);

    void deleteFile(@NotNull String fileName);

    boolean fileExists(@NotNull String fileName);

    byte[] getFile(@NotNull String fileName);

}
