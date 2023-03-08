package com.ewha.back.domain.user.repository;

import static com.ewha.back.domain.user.entity.QUser.*;

import java.time.LocalDateTime;

import org.springframework.stereotype.Repository;

import com.ewha.back.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

	public void deleteNotVerifiedUsers() {
		jpaQueryFactory.delete(user)
			.where(user.isVerified.eq(false))
			.where(user.createdAt.month().eq(LocalDateTime.now().getMonthValue() - 3))
			.execute();
	}

	public User findByProviderAndProviderId(String provider, String providerId) {
		return jpaQueryFactory.selectFrom(user)
			.where(user.provider.eq(provider).and(user.providerId.eq(providerId)))
			.fetchOne();
	}
}
