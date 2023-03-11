// package com.ewha.back.domain.like.repository;
//
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;
//
// import com.ewha.back.domain.comment.entity.Comment;
// import com.ewha.back.domain.feed.entity.Feed;
// import com.ewha.back.domain.like.entity.Like;
// import com.ewha.back.domain.user.entity.User;
//
// @Repository
// public interface LikeRepository extends JpaRepository<Like, Long> {
//
// 	Like findByFeedAndUser(Feed feed, User user);
//
// 	Like findByCommentAndUser(Comment comment, User user);
// }
