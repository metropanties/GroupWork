package me.metropanties.groupwork.service;

import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface JWTService {

    String create(@NotNull JWTCreator.Builder builder);

    @Nullable
    String refresh(@NotNull String token);

    @Nullable
    DecodedJWT decode(@NotNull String token);

}
