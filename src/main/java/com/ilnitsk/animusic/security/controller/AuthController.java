package com.ilnitsk.animusic.security.controller;

import com.ilnitsk.animusic.security.dto.AuthRequest;
import com.ilnitsk.animusic.security.dto.JwtResponse;
import com.ilnitsk.animusic.security.dto.RegisterRequest;
import com.ilnitsk.animusic.security.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Slf4j
@Tag(name = "REST API для авторизации", description = "Предоставляет методы для аутентификации и авторизации")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Метод для регистрации пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная регистрация."),
            @ApiResponse(responseCode = "400", description = "Email уже занят"),
            @ApiResponse(responseCode = "500", description = "Ошибка на стороне сервера")
    })
    public JwtResponse registerUser(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    @Operation(summary = "Метод для аутентификации пользователя в системе.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный вход в систему."),
            @ApiResponse(responseCode = "403", description = "Ошибка во время аутентификации!"),
            @ApiResponse(responseCode = "500", description = "Ошибка на стороне сервера")
    })
    public JwtResponse authenticate(@RequestBody AuthRequest request) {
        return authService.authenticate(request);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Метод для получения новой пары токенов.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное обновление токенов."),
            @ApiResponse(responseCode = "403", description = "Ошибка во время аутентификации!"),
            @ApiResponse(responseCode = "500", description = "Ошибка на стороне сервера")
    })
    public JwtResponse refresh(@RequestBody String refreshToken) {
        return authService.updateToken(refreshToken);
    }
}
