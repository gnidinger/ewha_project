package com.ewha.back.domain.image.repository;

import static com.ewha.back.domain.image.entity.QImage.*;

import org.springframework.stereotype.Repository;

import com.ewha.back.domain.image.entity.Image;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

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

	public void deleteById(Long imageId) {

		jpaQueryFactory
			.delete(image)
			.where(image.id.eq(imageId))
			.execute();
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

	public Image findByStoredImageName(String imageName) {

		return jpaQueryFactory
			.selectFrom(image)
			.where(image.storedImageName.eq(imageName))
			.fetchOne();
	}
}
