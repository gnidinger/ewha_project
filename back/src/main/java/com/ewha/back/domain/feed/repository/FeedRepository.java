package com.ewha.back.domain.feed.repository;

import com.ewha.back.domain.feed.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    @Modifying
    @Query(nativeQuery = true, value ="DELETE FROM FEED WHERE ID = :id")
    void deleteAllByUserId(Long id);
}
