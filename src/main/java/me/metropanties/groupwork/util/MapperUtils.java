package me.metropanties.groupwork.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@Component
@SuppressWarnings("unused")
@RequiredArgsConstructor
public class MapperUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapperUtils.class);

    private final ObjectMapper mapper;

    private static ObjectMapper MAPPER;

    @PostConstruct
    public void init() {
        MapperUtils.MAPPER = mapper;
    }

    @Nullable
    public static <T> T mapObject(@NotNull Object object, Class<T> tClass) {
        try {
            return MAPPER.convertValue(object, tClass);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Failed converting DTO to user entity!", e);
        }
        return null;
    }

    @Nullable
    public static <T> T read(@NotNull InputStream inputStream, Class<T> tClass) {
        try {
            return MAPPER.readValue(inputStream, tClass);
        } catch (IOException e) {
            LOGGER.error("Failed reading value!", e);
        }
        return null;
    }

    public static void write(@NotNull HttpServletResponse response, Object value) {
        try {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            MAPPER.writeValue(response.getOutputStream(), value);
        } catch (IOException e) {
            LOGGER.error("Failed writing value!", e);
        }
    }

}
