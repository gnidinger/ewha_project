package com.ewha.back.domain.image.repository;

import com.ewha.back.domain.image.entity.Image;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.ewha.back.domain.feed.entity.QFeed.feed;
import static com.ewha.back.domain.image.entity.QImage.image;

@Repository
@RequiredArgsConstructor
public class ImageQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public Image findByFeedId(Long feedId) {

        return jpaQueryFactory
                .selectFrom(image)
                .where(image.feed.id.eq(feedId))
                .fetchFirst();
    }

    public Image findByUserId(Long userId) {

        return jpaQueryFactory
                .selectFrom(image)
                .where(image.user.id.eq(userId))
                .fetchFirst();
    }

    public void deleteByFeedId(Long feedId) {

        jpaQueryFactory
                .delete(image)
                .where(image.feed.id.eq(feedId))
                .execute();
    }

    public void deleteByImagePath(String imagePath) {

        jpaQueryFactory
                .delete(image)
                .where(image.storedPath.eq(imagePath))
                .execute();
    }
}
