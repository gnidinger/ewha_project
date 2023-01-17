package com.ewha.back.domain.feed.repository;

import com.ewha.back.domain.feed.entity.Feed;
import com.ewha.back.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ewha.back.domain.category.entity.QCategory.category;
import static com.ewha.back.domain.feed.entity.QFeed.feed;
import static com.ewha.back.domain.feed.entity.QFeedCategory.feedCategory;

@Repository
@RequiredArgsConstructor
public class FeedQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public Page<Feed> findFeedListByUser(User user , Pageable pageable) {
        List<Feed> feedList = jpaQueryFactory
                .select(feed)
                .from(feed)
                .where(feed.user.eq(user))
                .orderBy(feed.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(feed.count())
                .from(feed)
                .fetchOne();

        return new PageImpl<>(feedList, pageable, total);
    }

    public Page<Feed> findNewestFeedList(Pageable pageable) {
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

        return new PageImpl<>(feedList, pageable, total);
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
                .fetchOne();

        return new PageImpl<>(feedList, pageable, total);
    }
}
