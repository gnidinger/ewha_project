package com.ewha.back.domain.user.repository;

import static com.ewha.back.domain.user.entity.QUser.*;

import java.time.LocalDateTime;

import org.springframework.stereotype.Repository;

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
}
