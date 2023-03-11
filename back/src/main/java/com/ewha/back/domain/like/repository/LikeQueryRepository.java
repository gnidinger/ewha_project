// package com.ewha.back.domain.like.repository;
//
// import static com.ewha.back.domain.like.entity.LikeType.*;
// import static com.ewha.back.domain.like.entity.QLike.*;
//
// import org.springframework.stereotype.Repository;
//
// import com.ewha.back.domain.comment.entity.Comment;
// import com.ewha.back.domain.feed.entity.Feed;
// import com.ewha.back.domain.like.entity.Like;
// import com.ewha.back.domain.user.entity.User;
// import com.querydsl.jpa.impl.JPAQueryFactory;
//
// import lombok.RequiredArgsConstructor;
//
// @Repository
// @RequiredArgsConstructor
// public class LikeQueryRepository {
// 	private final JPAQueryFactory jpaQueryFactory;
//
// 	public Like findFeedLikeByFeedAndUser(Feed findFeed, User findUser) {
//
// 		return jpaQueryFactory.selectFrom(like)
// 			.where(like.likeType.eq(FEED).and(like.feed.eq(findFeed)).and(like.user.eq(findUser)))
// 			.fetchFirst();
// 	}
//
// 	public Like findCommentLikeByFeedAndUser(Comment findComment, User findUser) {
//
// 		return jpaQueryFactory.selectFrom(like)
// 			.where(like.likeType.eq(COMMENT).and(like.comment.eq(findComment)).and(like.user.eq(findUser)))
// 			.fetchFirst();
// 	}
// }
