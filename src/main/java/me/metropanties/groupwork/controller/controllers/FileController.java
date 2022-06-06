package me.metropanties.groupwork.controller.controllers;

import lombok.RequiredArgsConstructor;
import me.metropanties.groupwork.controller.ControllerConstants;
import me.metropanties.groupwork.service.S3Service;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping(ControllerConstants.API_ENDPOINT + "/files")
@RequiredArgsConstructor
public class FileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

    private final S3Service s3Service;

    @GetMapping("/{fileName}")
    public ResponseEntity<Object> getProfileImage(@NotNull HttpServletResponse response, @PathVariable String fileName) {
        try {
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
            if (!s3Service.fileExists(fileName)) {
                byte[] fileBytes = s3Service.getFile("default-avatar.png");
                StreamUtils.copy(fileBytes, response.getOutputStream());
                return new ResponseEntity<>(HttpStatus.FOUND);
            }

            byte[] fileBytes = s3Service.getFile(fileName);
            StreamUtils.copy(fileBytes, response.getOutputStream());
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
