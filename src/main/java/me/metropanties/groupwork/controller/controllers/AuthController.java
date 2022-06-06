package me.metropanties.groupwork.controller.controllers;

import lombok.RequiredArgsConstructor;
import me.metropanties.groupwork.controller.ControllerConstants;
import me.metropanties.groupwork.entity.User;
import me.metropanties.groupwork.exception.UsernameAlreadyExists;
import me.metropanties.groupwork.object.AuthCredentials;
import me.metropanties.groupwork.object.RegistryObject;
import me.metropanties.groupwork.object.TokenRefreshRequest;
import me.metropanties.groupwork.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping(ControllerConstants.API_ENDPOINT + "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody @NotNull RegistryObject registry) {
        try {
            userService.saveUser(new User(registry.username(), registry.password()));
            Map<String, Object> body = Map.of(
                    "message", "Registration successful!",
                    "code", HttpStatus.CREATED.value()
            );
            return new ResponseEntity<>(body, HttpStatus.CREATED);
        } catch (UsernameAlreadyExists e) {
            Map<String, Object> body = Map.of(
                    "message", e.getMessage(),
                    "code", HttpStatus.CONFLICT.value()
            );
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public void login(@RequestBody AuthCredentials credentials) {

    }

    /*
     * TODO: Handling of refresh tokens.
     */
    @PostMapping("/refresh")
    public ResponseEntity<Object> refreshToken(@NotNull TokenRefreshRequest request) {
        return null;
    }

}
