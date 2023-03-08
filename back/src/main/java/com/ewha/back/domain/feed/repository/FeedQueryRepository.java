package com.ewha.back.domain.feed.repository;

import static com.ewha.back.domain.category.entity.QCategory.*;
import static com.ewha.back.domain.feed.entity.QFeed.*;
import static com.ewha.back.domain.feed.entity.QFeedCategory.*;
import static com.ewha.back.domain.like.entity.QLike.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.ewha.back.domain.feed.entity.Feed;
import com.ewha.back.domain.user.entity.User;
import com.ewha.back.global.config.CustomPage;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FeedQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

	public Page<Feed> findFeedListByUser(User user, Pageable pageable) {

		List<Feed> feedList = jpaQueryFactory
			.select(feed)
			.from(feed)
			.where(feed.user.eq(user))
			.orderBy(feed.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		return new PageImpl<>(feedList, pageable, feedList.size());
	}

	public Page<Feed> findFeedLikesListByUser(User user, Pageable pageable) {

		List<Feed> feedList = jpaQueryFactory
			.selectFrom(feed)
			.join(feed.likes, like)
			.where(like.user.eq(user))
			.orderBy(feed.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		return new PageImpl<>(feedList, pageable, feedList.size());
	}

	public CustomPage<Feed> findNewestFeedList(Pageable pageable) {

		List<Feed> feedList = jpaQueryFactory
			.selectFrom(feed)
			.orderBy(feed.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(feed.count())
			.from(feed)
			.fetchOne();

		System.out.println(total);

		return new CustomPage<>(feedList, pageable, total);
	}

	public Page<Feed> findCategoryFeedList(String categoryName, Pageable pageable) {

		List<Feed> feedList = jpaQueryFactory
			.selectFrom(feed)
			.join(feed.feedCategories, feedCategory)
			.join(feedCategory.category, category)
			.where(category.categoryType.stringValue().eq(categoryName))
			.orderBy(feed.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(feed.count())
			.from(feed)
			.join(feed.feedCategories, feedCategory)
			.join(feedCategory.category, category)
			.where(category.categoryType.stringValue().eq(categoryName))
			.fetchOne();

		return new PageImpl<>(feedList, pageable, total);
	}

	public Page<Feed> findAllSearchResultPage(String queryParam, Pageable pageable) {

		List<Feed> feedList = jpaQueryFactory
			.selectFrom(feed)
			.where(feed.title.contains(queryParam).or(feed.body.contains(queryParam)))
			.orderBy(feed.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(feed.count())
			.from(feed)
			.where(feed.title.contains(queryParam).or(feed.body.contains(queryParam)))
			.fetchOne();

		return new PageImpl<>(feedList, pageable, total);
	}

	public Page<Feed> findCategorySearchResultPage(String categoryParam, String queryParam, Pageable pageable) {

		List<Feed> feedList = jpaQueryFactory
			.selectFrom(feed)
			.join(feed.feedCategories, feedCategory)
			.join(feedCategory.category, category)
			.where(category.categoryType.stringValue().eq(categoryParam))
			.where(feed.title.contains(queryParam).or(feed.body.contains(queryParam)))
			.orderBy(feed.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(feed.count())
			.from(feed)
			.where(category.categoryType.stringValue().eq(categoryParam))
			.where(feed.title.contains(queryParam).or(feed.body.contains(queryParam)))
			.fetchOne();

		return new PageImpl<>(feedList, pageable, total);
	}

	public void deleteAllByUser(User findUser) {
		jpaQueryFactory.delete(feed)
			.where(feed.user.eq(findUser))
			.execute();
	}

}
