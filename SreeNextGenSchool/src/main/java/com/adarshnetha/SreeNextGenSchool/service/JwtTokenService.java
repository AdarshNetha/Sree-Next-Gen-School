package com.adarshnetha.SreeNextGenSchool.service;

import com.adarshnetha.SreeNextGenSchool.entity.AuthToken;
import com.adarshnetha.SreeNextGenSchool.exception.TokenExpiredException;
import com.adarshnetha.SreeNextGenSchool.repository.AuthTokenRepository;
import jakarta.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HexFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenService {

    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final String TOKEN_EXPIRED_MESSAGE = "Token expired";
    private static final Pattern SUBJECT_PATTERN = Pattern.compile("\"sub\"\\s*:\\s*\"([^\"]+)\"");
    private static final Pattern EXPIRES_AT_PATTERN = Pattern.compile("\"exp\"\\s*:\\s*(\\d+)");

    private final AuthTokenRepository authTokenRepository;
    private final byte[] secret;
    private final long expirationMinutes;

    public JwtTokenService(
            AuthTokenRepository authTokenRepository,
            @Value("${jwt.secret:sree-next-gen-school-default-secret-change-me}") String secret,
            @Value("${jwt.expiration-minutes:30}") long expirationMinutes
    ) {
        this.authTokenRepository = authTokenRepository;
        this.secret = sha256(secret);
        this.expirationMinutes = expirationMinutes;
    }

    @Transactional
    public TokenDetails generateToken(String username) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(expirationMinutes, ChronoUnit.MINUTES);
        String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        String payload = "{\"sub\":\"" + escapeJson(username) + "\",\"iat\":" + issuedAt.getEpochSecond()
                + ",\"exp\":" + expiresAt.getEpochSecond() + "}";
        String unsignedToken = base64UrlEncode(header.getBytes(StandardCharsets.UTF_8)) + "."
                + base64UrlEncode(payload.getBytes(StandardCharsets.UTF_8));
        String token = unsignedToken + "." + sign(unsignedToken);

        AuthToken authToken = new AuthToken();
        authToken.setTokenHash(hashToken(token));
        authToken.setUsername(username);
        authToken.setCreatedAt(issuedAt);
        authToken.setExpiresAt(expiresAt);
        authToken.setRevoked(false);
        authTokenRepository.save(authToken);

        return new TokenDetails(token, expiresAt);
    }

    @Transactional
    public void expireToken(String token) {
        AuthToken authToken = authTokenRepository.findByTokenHash(hashToken(token))
                .orElseThrow(() -> new TokenExpiredException(TOKEN_EXPIRED_MESSAGE));
        authToken.setRevoked(true);
        authToken.setRevokedAt(Instant.now());
        authTokenRepository.save(authToken);
    }

    public String validateAndGetUsername(String token) {
        JwtPayload payload = parseAndVerify(token);
        Instant now = Instant.now();
        if (payload.expiresAt().isBefore(now) || payload.expiresAt().equals(now)) {
            throw new TokenExpiredException(TOKEN_EXPIRED_MESSAGE);
        }

        AuthToken authToken = authTokenRepository.findByTokenHash(hashToken(token))
                .orElseThrow(() -> new TokenExpiredException(TOKEN_EXPIRED_MESSAGE));
        if (authToken.isRevoked() || authToken.getExpiresAt().isBefore(now) || authToken.getExpiresAt().equals(now)) {
            throw new TokenExpiredException(TOKEN_EXPIRED_MESSAGE);
        }

        return payload.username();
    }

    public String extractToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new TokenExpiredException(TOKEN_EXPIRED_MESSAGE);
        }
        return authorizationHeader.substring(7);
    }

    private JwtPayload parseAndVerify(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new TokenExpiredException(TOKEN_EXPIRED_MESSAGE);
        }

        String unsignedToken = parts[0] + "." + parts[1];
        if (!MessageDigest.isEqual(sign(unsignedToken).getBytes(StandardCharsets.UTF_8), parts[2].getBytes(StandardCharsets.UTF_8))) {
            throw new TokenExpiredException(TOKEN_EXPIRED_MESSAGE);
        }

        String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
        Matcher subjectMatcher = SUBJECT_PATTERN.matcher(payload);
        Matcher expiresAtMatcher = EXPIRES_AT_PATTERN.matcher(payload);
        if (!subjectMatcher.find() || !expiresAtMatcher.find()) {
            throw new TokenExpiredException(TOKEN_EXPIRED_MESSAGE);
        }

        return new JwtPayload(subjectMatcher.group(1), Instant.ofEpochSecond(Long.parseLong(expiresAtMatcher.group(1))));
    }

    private String sign(String value) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(secret, HMAC_ALGORITHM));
            return base64UrlEncode(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to sign token", exception);
        }
    }

    private String hashToken(String token) {
        return HexFormat.of().formatHex(sha256(token));
    }

    private byte[] sha256(String value) {
        try {
            return MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to hash value", exception);
        }
    }

    private String base64UrlEncode(byte[] value) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value);
    }

    private String escapeJson(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    public record TokenDetails(String token, Instant expiresAt) {
    }

    private record JwtPayload(String username, Instant expiresAt) {
    }
}
