package me.metropanties.groupwork.util;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;

public final class JWTUtils {

    private static final String HEADER = "Bearer ";

    public static boolean checkAuthorizationHeader(@NotNull HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        return header != null && header.startsWith(HEADER);
    }

    @NotNull
    public static String getAuthorizationToken(@NotNull String header) {
        return header.substring(HEADER.length());
    }

}
