package com.adarshnetha.SreeNextGenSchool.controller;

import com.adarshnetha.SreeNextGenSchool.dto.AuthRequestDto;
import com.adarshnetha.SreeNextGenSchool.dto.AuthResponseDto;
import com.adarshnetha.SreeNextGenSchool.dto.ResponseStructure;
import com.adarshnetha.SreeNextGenSchool.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseStructure<AuthResponseDto>> login(@Valid @RequestBody AuthRequestDto dto) {
        return authService.login(dto);
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseStructure<Object>> logout(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) {
        return authService.logout(authorizationHeader);
    }
}
