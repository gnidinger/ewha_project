package com.ewha.back.global.security.refreshToken.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
//@RedisHash(value = "refreshToken")
public class RefreshToken {
    private String userId;
    @Id
    private String tokenValue;

    @Builder
    public RefreshToken(String userId, String tokenValue) {
        this.userId = userId;
        this.tokenValue = tokenValue;
    }
}
