package com.ewha.back.domain.feed.repository;

import com.ewha.back.domain.feed.entity.FeedCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FeedCategoryRepository extends JpaRepository<FeedCategory, Long> {

    @Modifying
    @Query(nativeQuery = true, value ="DELETE FROM FEED_CATEGORY WHERE FEED_ID = :feedId")
    void deleteAllByFeedId(Long feedId);
}
