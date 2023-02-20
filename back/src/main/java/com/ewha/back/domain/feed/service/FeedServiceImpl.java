package com.ewha.back.domain.feed.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ewha.back.domain.category.entity.Category;
import com.ewha.back.domain.category.service.CategoryService;
import com.ewha.back.domain.comment.repository.CommentRepository;
import com.ewha.back.domain.feed.entity.Feed;
import com.ewha.back.domain.feed.entity.FeedCategory;
import com.ewha.back.domain.feed.repository.FeedCategoryRepository;
import com.ewha.back.domain.feed.repository.FeedQueryRepository;
import com.ewha.back.domain.feed.repository.FeedRepository;
import com.ewha.back.domain.like.entity.Like;
import com.ewha.back.domain.like.repository.LikeQueryRepository;
import com.ewha.back.domain.user.entity.User;
import com.ewha.back.domain.user.service.UserService;
import com.ewha.back.global.config.CustomPage;
import com.ewha.back.global.exception.BusinessLogicException;
import com.ewha.back.global.exception.ExceptionCode;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {

	private final UserService userService;
	private final CategoryService categoryService;
	private final FeedCategoryRepository feedCategoryRepository;
	private final FeedRepository feedRepository;
	private final FeedQueryRepository feedQueryRepository;
	private final CommentRepository commentRepository;
	private final LikeQueryRepository likeQueryRepository;

	@Override
	public Feed createFeed(Feed feed) {

		User findUser = userService.getLoginUser();

		Feed savedFeed = Feed.builder()
			.user(findUser)
			.title(feed.getTitle())
			.body(feed.getBody())
			// .feedCategories(feed.getFeedCategories())
			.viewCount(0L)
			.likeCount(0L)
			.build();

		categoryToFeed(feed, savedFeed);

		return feedRepository.save(savedFeed);
	}

	@Override
	public Feed updateFeed(Feed feed, Long feedId) {

		User findUser = userService.getLoginUser();

		Feed findFeed = findVerifiedFeed(feedId);

		if (findUser.equals(findFeed.getUser())) {

			findFeed.updateFeed(feed);

			feedCategoryRepository.deleteAllByFeedId(feedId);

			categoryToFeed(feed, findFeed);

			return feedRepository.save(findFeed);
		} else {
			throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED);
		}
	}

	private void categoryToFeed(Feed feed, Feed savedFeed) {
		feed.getFeedCategories()
			.forEach(feedCategory -> {
				Category category = categoryService.findVerifiedCategory(feedCategory.getCategory().getCategoryType());
				FeedCategory savedFeedCategory =
					FeedCategory.builder()
						.feed(savedFeed)
						.category(category)
						.build();
				feedCategoryRepository.save(savedFeedCategory);
			});
	}

	@Override
	public Feed updateView(Long feedId) {

		Feed findFeed = findVerifiedFeed(feedId);
		findFeed.addView();
		return feedRepository.save(findFeed);
	}

	@Override
	public List<Like> isLikedComments(Long feedId) {

		User findUser = userService.getLoginUser();

		Feed findFeed = findVerifiedFeed(feedId);

		return findFeed.getComments().stream()
			.map(comment -> likeQueryRepository.findCommentLikeByFeedAndUser(comment, findUser))
			// .sorted(Comparator.comparing(a -> a.getFeed().getId()))
			.collect(Collectors.toList());
	}

	@Override
	public CustomPage<Feed> findNewestFeeds(int page) {

		PageRequest pageRequest = PageRequest.of(page - 1, 10);

		return feedQueryRepository.findNewestFeedList(pageRequest);
	}

	@Override
	public Page<Feed> findCategoryFeeds(String categoryName, int page) {

		PageRequest pageRequest = PageRequest.of(page - 1, 10);

		return feedQueryRepository.findCategoryFeedList(categoryName, pageRequest);
	}

	@Override
	public void deleteFeed(Long feedId) {

		User findUser = userService.getLoginUser();

		Feed findFeed = findVerifiedFeed(feedId);

		if (findUser.equals(findFeed.getUser())) {
			commentRepository.deleteAllByFeedId(feedId);
			feedRepository.delete(findFeed);
		}
	}

	@Override
	public void deleteFeeds() {

		User findUser = userService.getLoginUser();

		Long id = findUser.getId();

		findUser.getFeeds().stream()
			.map(Feed::getId)
			.forEach(commentRepository::deleteAllByFeedId);

		feedRepository.deleteAllByUserId(id);
	}

	public Feed findFeedByFeedId(Long feedId) {
		return findVerifiedFeed(feedId);
	}
	@Override
	public Feed findVerifiedFeed(Long feedId) {

		Optional<Feed> optionalFeed = feedRepository.findById(feedId);
		return optionalFeed.orElseThrow(() ->
			new BusinessLogicException(ExceptionCode.FEED_NOT_FOUND));
	}

	@Override
	public void saveFeed(Feed feed) {
		feedRepository.save(feed);
	}
}
