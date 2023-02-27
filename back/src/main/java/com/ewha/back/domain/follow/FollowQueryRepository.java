package com.ewha.back.domain.follow;

import static com.ewha.back.domain.follow.QFollow.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.ewha.back.domain.comment.entity.Comment;
import com.ewha.back.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FollowQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

	// public Page<User> findFollowersByUserId(Long userId, Pageable pageable) {
	//
	// 	jpaQueryFactory.selectFrom(follow)
	// 		.where(follow.followedUser.)
	// }
}
