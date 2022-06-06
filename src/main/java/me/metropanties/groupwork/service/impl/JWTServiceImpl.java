package me.metropanties.groupwork.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.RequiredArgsConstructor;
import me.metropanties.groupwork.entity.Role;
import me.metropanties.groupwork.repository.UserRepository;
import me.metropanties.groupwork.service.JWTService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class JWTServiceImpl implements JWTService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JWTServiceImpl.class);

    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    private final UserRepository userRepository;

    @Override
    public String create(@NotNull JWTCreator.Builder builder) {
        return builder.withIssuedAt(new Date(System.currentTimeMillis()))
                .withIssuedAt(new Date())
                .sign(algorithm);
    }

    @Override
    public String refresh(@NotNull String token) {
        DecodedJWT decodedJWT = decode(token);
        if (decodedJWT == null)
            return null;

        String username = decodedJWT.getSubject();
        if (username == null)
            return null;

        return userRepository.findByUsername(username).map(user -> {
            JWTCreator.Builder builder = JWT.create()
                    .withIssuer(decodedJWT.getIssuer() != null ? decodedJWT.getIssuer() : "http://localhost:8080")
                    .withSubject(username)
                    .withClaim("roles", user.getRoles().stream().map(Role::getName).toList());
            return create(builder);
        }).orElse(null);
    }

    @Override
    public DecodedJWT decode(@NotNull String token) {
        try {
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            LOGGER.error("An error occurred when decoding JWT token!", e);
        }
        return null;
    }

}
