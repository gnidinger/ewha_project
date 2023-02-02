package com.ewha.back.domain.feed.service;

import com.ewha.back.domain.category.entity.Category;
import com.ewha.back.domain.category.service.CategoryService;
import com.ewha.back.domain.comment.entity.Comment;
import com.ewha.back.domain.comment.repository.CommentRepository;
import com.ewha.back.domain.feed.entity.Feed;
import com.ewha.back.domain.feed.entity.FeedCategory;
import com.ewha.back.domain.feed.repository.FeedCategoryRepository;
import com.ewha.back.domain.feed.repository.FeedQueryRepository;
import com.ewha.back.domain.feed.repository.FeedRepository;
import com.ewha.back.domain.like.repository.LikeRepository;
import com.ewha.back.domain.user.entity.User;
import com.ewha.back.domain.user.service.UserService;
import com.ewha.back.global.exception.BusinessLogicException;
import com.ewha.back.global.exception.ExceptionCode;

import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ewha.back.global.config.CacheConstant.NEWEST_FEEDS;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedService {

	private final UserService userService;
	private final CategoryService categoryService;
	private final FeedCategoryRepository feedCategoryRepository;
	private final FeedRepository feedRepository;
	private final FeedQueryRepository feedQueryRepository;
	private final CommentRepository commentRepository;
	private final LikeRepository likeRepository;

	public Feed createFeed(Feed feed) {

		User findUser = userService.getLoginUser();

		Feed savedFeed = Feed.builder()
			.user(findUser)
			.title(feed.getTitle())
			.body(feed.getBody())
			.viewCount(0L)
			.likeCount(0L)
			.build();

		categoryToFeed(feed, savedFeed);

		return feedRepository.save(savedFeed);
	}

	public Feed updateFeed(Feed feed, Long feedId) {

		User findUser = userService.getLoginUser();

		Feed findFeed = findVerifiedFeed(feedId);

		if (findUser.equals(findFeed.getUser())) {

			findFeed.updateFeed(feed);

			feedCategoryRepository.deleteAllByFeedId(feedId);

			categoryToFeed(feed, findFeed);

			return feedRepository.save(findFeed);
		} else
			throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED);
	}

	private void categoryToFeed(Feed feed, Feed savedFeed) {
		feed.getFeedCategories().stream()
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

	public Feed updateView(Long feedId) {

		Feed findFeed = findVerifiedFeed(feedId);
		findFeed.addView();
		return feedRepository.save(findFeed);
	}

	public Feed isLikedComments(Long feedId) {

		User findUser = userService.getLoginUser();

		Feed findFeed = findVerifiedFeed(feedId);

		Feed isLikedFeed = isLikedFeed(findFeed, findUser);

		List<Comment> isLikedComments = isLikedFeed.getComments().stream()
			.map(comment -> isLikedComment(comment, findUser))
			.collect(Collectors.toList());

		isLikedFeed.setComments(isLikedComments);

		return feedRepository.save(isLikedFeed);
	}

	public Feed isLikedFeed(Feed feed, User user) {

		Boolean isLiked;

		if (likeRepository.findByFeedAndUser(feed, user) == null)
			isLiked = false;
		else
			isLiked = true;

		feed.setIsLiked(isLiked);

		return feed;
	}

	public Comment isLikedComment(Comment comment, User user) {

		Boolean isLiked;

		if (likeRepository.findByCommentAndUser(comment, user) == null)
			isLiked = false;
		else
			isLiked = true;

		comment.setIsLiked(isLiked);

		return comment;
	}

	@Cacheable(key = "#page", value = NEWEST_FEEDS)
	public Page<Feed> findNewestFeeds(int page) {

		PageRequest pageRequest = PageRequest.of(page - 1, 10);

		return feedQueryRepository.findNewestFeedList(pageRequest);
	}

	public Page<Feed> findCategoryFeeds(String categoryName, int page) {

		PageRequest pageRequest = PageRequest.of(page - 1, 10);

		return feedQueryRepository.findCategoryFeedList(categoryName, pageRequest);
	}

	public void deleteFeed(Long feedId) {

		User findUser = userService.getLoginUser();

		Feed findFeed = findVerifiedFeed(feedId);

		if (findUser.equals(findFeed.getUser())) {
			commentRepository.deleteAllByFeedId(feedId);
			feedRepository.delete(findFeed);
		}
	}

	public void deleteFeeds() {

		User findUser = userService.getLoginUser();

		Long id = findUser.getId();

		findUser.getFeeds().stream()
			.map(Feed::getId)
			.forEach(commentRepository::deleteAllByFeedId);

		feedRepository.deleteAllByUserId(id);
	}

	public Feed findVerifiedFeed(Long feedId) {

		Optional<Feed> optionalPairing = feedRepository.findById(feedId);
		return optionalPairing.orElseThrow(() ->
			new BusinessLogicException(ExceptionCode.FEED_NOT_FOUND));
	}

}
