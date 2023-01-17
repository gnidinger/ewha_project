package com.ewha.back.domain.comment.repository;

import com.ewha.back.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM COMMENT WHERE FEED_ID = :feedId")
    void deleteAllByFeedId(long feedId);

    @Modifying
    @Query(nativeQuery = true, value ="DELETE FROM COMMENT WHERE ID = :id")
    void deleteAllByUserId(Long id);
}
