package com.ewha.back.domain.user.repository;

import static com.ewha.back.domain.user.entity.QUserCategory.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserCategoryQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public Long deleteByUserId(Long userId) {
        return jpaQueryFactory
                .delete(userCategory)
                .where(userCategory.user.id.eq(userId))
                .execute();
    }

}
