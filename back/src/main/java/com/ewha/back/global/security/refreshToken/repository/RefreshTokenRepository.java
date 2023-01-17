package com.ewha.back.global.security.refreshToken.repository;

import com.ewha.back.global.security.refreshToken.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

@Transactional
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByTokenValue(String tokenValue);

    @Query(nativeQuery = true, value =
            "SELECT * FROM REFRESH_TOKEN " +
                    "WHERE EMAIL = :email"
    )
    Optional<RefreshToken> findUserTokenByEmail(String email);

    @Query(nativeQuery = true, value =
            "SELECT EMAIL FROM REFRESH_TOKEN " +
                    "WHERE TOKEN_VALUE = :token"
    )
    String findUserEmailByToken(String token);

    Optional<RefreshToken> deleteByTokenValue(String tokenValue);
}
