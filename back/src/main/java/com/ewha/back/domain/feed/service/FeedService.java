package com.ewha.back.domain.feed.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ewha.back.domain.feed.entity.Feed;
import com.ewha.back.domain.like.entity.Like;
import com.ewha.back.global.config.CustomPage;

public interface FeedService {
	Feed createFeed(Feed feed);

	Feed updateFeed(Feed feed, Long feedId);

	Feed updateView(Long feedId);

	List<Like> isLikedComments(Long feedId);

	CustomPage<Feed> findNewestFeeds(int page);

	Page<Feed> findCategoryFeeds(String categoryName, int page);

	void deleteFeed(Long feedId);

	void deleteFeeds();

	Feed findVerifiedFeed(Long feedId);

	void saveFeed(Feed feed);
}
