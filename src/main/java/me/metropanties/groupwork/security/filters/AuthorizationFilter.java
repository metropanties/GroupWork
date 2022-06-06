package me.metropanties.groupwork.security.filters;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import me.metropanties.groupwork.util.MapperUtils;
import me.metropanties.groupwork.util.JWTUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {

    private final JWTVerifier verifier;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filter) throws ServletException, IOException {
        if (request.getServletPath().equalsIgnoreCase("/api/v1/auth/login")) {
            filter.doFilter(request, response);
            return;
        }

        if (JWTUtils.checkAuthorizationHeader(request)) {
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            String token = JWTUtils.getAuthorizationToken(authorizationHeader);
            try {
                DecodedJWT decodedJWT = verifier.verify(token);
                String username = decodedJWT.getSubject();
                String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                List<SimpleGrantedAuthority> authorities = Arrays.stream(roles).map(SimpleGrantedAuthority::new).toList();

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                filter.doFilter(request, response);
                return;
            } catch (SignatureVerificationException e) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                MapperUtils.write(response, Map.of(
                        "message", "Please provide a valid JWT token!"
                ));
            }
        }

        filter.doFilter(request, response);
    }

}
