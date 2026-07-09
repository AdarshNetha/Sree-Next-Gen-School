package com.adarshnetha.SreeNextGenSchool.repository;

import com.adarshnetha.SreeNextGenSchool.entity.AuthToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthTokenRepository extends JpaRepository<AuthToken, Long> {

    Optional<AuthToken> findByTokenHash(String tokenHash);
}
