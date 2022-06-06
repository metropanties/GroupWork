package me.metropanties.groupwork.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import me.metropanties.groupwork.entity.User;
import me.metropanties.groupwork.util.MapperUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);

    private final AuthenticationManager authenticationManager;
    private final Algorithm algorithm;

    @Override
    public Authentication attemptAuthentication(@NotNull HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            User user = MapperUtils.read(request.getInputStream(), User.class);
            if (user == null || user.getDisabled())
                return null;

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            LOGGER.error("An error occurred when attempting to authenticate!", e);
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, @NotNull Authentication auth) {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) auth.getPrincipal();
        if (user == null) {
            LOGGER.error("Retrieved user from principal is null!");
            return;
        }

        String accessToken = JWT.create()
                .withIssuer(request.getRequestURL().toString())
                .withSubject(user.getUsername())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .sign(algorithm);

        MapperUtils.write(response, Map.of(
                "access_token", accessToken
        ));
    }

}
