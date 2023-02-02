package com.ewha.back.domain.user.repository;

import static com.ewha.back.domain.user.entity.QUserCategory.*;

import org.springframework.stereotype.Repository;

import com.ewha.back.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserCategoryQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

	public void deleteByUser(User findUser) {
		jpaQueryFactory
			.delete(userCategory)
			.where(userCategory.user.eq(findUser))
			.execute();
	}

}
