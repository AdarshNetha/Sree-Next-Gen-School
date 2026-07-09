package com.adarshnetha.SreeNextGenSchool.service;

import com.adarshnetha.SreeNextGenSchool.dto.AuthRequestDto;
import com.adarshnetha.SreeNextGenSchool.dto.AuthResponseDto;
import com.adarshnetha.SreeNextGenSchool.dto.ResponseStructure;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    public AuthService(AuthenticationManager authenticationManager, JwtTokenService jwtTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
    }

    public ResponseEntity<ResponseStructure<AuthResponseDto>> login(AuthRequestDto dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.username(), dto.password())
        );
        JwtTokenService.TokenDetails tokenDetails = jwtTokenService.generateToken(authentication.getName());
        AuthResponseDto authResponseDto = new AuthResponseDto(
                authentication.getName(),
                tokenDetails.token(),
                tokenDetails.expiresAt(),
                "Login successful"
        );
        ResponseStructure<AuthResponseDto> responseStructure = new ResponseStructure<>(
                "Login successful",
                authResponseDto,
                HttpStatus.OK.value()
        );
        return ResponseEntity.status(HttpStatus.OK).body(responseStructure);
    }

    public ResponseEntity<ResponseStructure<Object>> logout(String authorizationHeader) {
        String token = jwtTokenService.extractToken(authorizationHeader);
        jwtTokenService.expireToken(token);
        ResponseStructure<Object> responseStructure = new ResponseStructure<>(
                "Logout successful. Token expired",
                null,
                HttpStatus.OK.value()
        );
        return ResponseEntity.status(HttpStatus.OK).body(responseStructure);
    }
}
