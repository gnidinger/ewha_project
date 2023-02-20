package com.ewha.back.domain.feed.mapper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.ewha.back.domain.category.dto.CategoryDto;
import com.ewha.back.domain.category.entity.Category;
import com.ewha.back.domain.category.entity.CategoryType;
import com.ewha.back.domain.comment.dto.CommentDto;
import com.ewha.back.domain.feed.dto.FeedDto;
import com.ewha.back.domain.feed.entity.Feed;
import com.ewha.back.domain.feed.entity.FeedCategory;
import com.ewha.back.domain.like.entity.Like;
import com.ewha.back.domain.user.dto.UserDto;
import com.ewha.back.domain.user.entity.User;
import com.ewha.back.global.config.CustomPage;

@Mapper(componentModel = "spring")
public interface FeedMapper {

	default Feed feedPostToFeed(FeedDto.Post postFeed) {
		return Feed.builder()
			.title(postFeed.getTitle())
			.feedCategories(getFeedCategoriesFromResponseDto(postFeed.getCategories()))
			.body(postFeed.getBody())
			.build();
	}

	default Feed feedPatchToFeed(FeedDto.Patch patchFeed) {
		return Feed.builder()
			.title(patchFeed.getTitle())
			.feedCategories(getFeedCategoriesFromResponseDto(patchFeed.getCategories()))
			.body(patchFeed.getBody())
			.build();
	}

	default FeedDto.Response feedGetToFeedResponse(Feed feed, Boolean isLikedFeed, List<Like> commentLikeList) {

		User findUser = feed.getUser();

		UserDto.PostResponse postResponse =
			UserDto.PostResponse.builder()
				.userId(findUser.getUserId())
				.nickname(findUser.getNickname())
				.ariFactor(findUser.getAriFactor())
				.role(findUser.getRole())
				.profileImage(findUser.getProfileImage())
				.build();

		List<CommentDto.FeedCommentResponse> commentsList = new ArrayList<>();

		if (feed.getComments() == null) {
			commentsList = null;
		} else {
			commentsList =
				feed.getComments().stream()
					.map(comment -> {
						CommentDto.FeedCommentResponse.FeedCommentResponseBuilder commentResponseBuilder = CommentDto.FeedCommentResponse.builder();
						commentResponseBuilder.commentId(comment.getId());
						commentResponseBuilder.feedId(comment.getFeed().getId());
						commentResponseBuilder.userInfo(
							UserDto.FeedCommentResponse.builder()
								.userId(comment.getUser().getUserId())
								.nickname(comment.getUser().getNickname())
								.profileImage(comment.getUser().getProfileImage())
								.build()
						);
						commentResponseBuilder.body(comment.getBody());
						commentResponseBuilder.likeCount(comment.getLikeCount());
						commentResponseBuilder.createdAt(comment.getCreatedAt());
						commentResponseBuilder.createdAt(comment.getModifiedAt());
						return commentResponseBuilder.build();
					})
					.sorted(Comparator.comparing(CommentDto.FeedCommentResponse::getCommentId))
					.collect(Collectors.toList());
		}

		List<CommentDto.FeedCommentResponse> finalCommentsList = commentsList;

			commentLikeList.forEach(like -> {
				if (like != null) {
					Long index = like.getComment().getId();
					finalCommentsList.stream()
						.filter(feedCommentResponse -> feedCommentResponse.getCommentId().equals(index))
						.forEach(feedCommentResponse -> feedCommentResponse.setIsLikedComment(true));
					// finalCommentsList.get(Math.toIntExact(index)).setIsLikedComment(true);
				}
			});

		return FeedDto.Response.builder()
			.feedId(feed.getId())
			.categories(feed.getFeedCategories().stream()
				.map(a -> a.getCategory().getCategoryType())
				.collect(Collectors.toList()))
			.userInfo(postResponse)
			.title(feed.getTitle())
			.body(feed.getBody())
			.isLiked(isLikedFeed)
			.likeCount(feed.getLikeCount())
			.viewCount(feed.getViewCount())
			.imagePath(feed.getImagePath())
			.thumbnailPath(feed.getThumbnailPath())
			.comments(finalCommentsList)
			.createdAt(feed.getCreatedAt())
			.modifiedAt(feed.getModifiedAt())
			.build();
	}

	default FeedDto.Response feedGetWithoutLoginToFeedResponse(Feed feed) {

		User findUser = feed.getUser();

		UserDto.PostResponse postResponse =
			UserDto.PostResponse.builder()
				.userId(findUser.getUserId())
				.nickname(findUser.getNickname())
				.ariFactor(findUser.getAriFactor())
				.role(findUser.getRole())
				.profileImage(findUser.getProfileImage())
				.build();

		List<CommentDto.FeedCommentResponse> commentsList = new ArrayList<>();

		if (feed.getComments() == null)
			commentsList = null;

		else {
			commentsList =
				feed.getComments().stream()
					.map(comment -> {
						return CommentDto.FeedCommentResponse.builder()
							.commentId(comment.getId())
							.feedId(comment.getFeed().getId())
							.userInfo(
								UserDto.FeedCommentResponse.builder()
									.userId(comment.getUser().getUserId())
									.nickname(comment.getUser().getNickname())
									.profileImage(comment.getUser().getProfileImage())
									.build()
							)
							.body(comment.getBody())
							.likeCount(comment.getLikeCount())
							.createdAt(comment.getCreatedAt())
							.modifiedAt(comment.getModifiedAt())
							.build();
					}).collect(Collectors.toList());
		}

		return FeedDto.Response.builder()
			.feedId(feed.getId())
			.categories(feed.getFeedCategories().stream()
				.map(a -> a.getCategory().getCategoryType())
				.collect(Collectors.toList()))
			.userInfo(postResponse)
			.title(feed.getTitle())
			.body(feed.getBody())
			.isLiked(null)
			.likeCount(feed.getLikeCount())
			.viewCount(feed.getViewCount())
			.imagePath(feed.getImagePath())
			.thumbnailPath(feed.getThumbnailPath())
			.comments(commentsList)
			.createdAt(feed.getCreatedAt())
			.modifiedAt(feed.getModifiedAt())
			.build();
	}
	// default FeedDto.Response feedToFeedResponse(Feed feed) {
	//
	// 	User findUser = feed.getUser();
	//
	// 	UserDto.PostResponse postResponse =
	// 		UserDto.PostResponse.builder()
	// 			.userId(findUser.getUserId())
	// 			.nickname(findUser.getNickname())
	// 			.ariFactor(findUser.getAriFactor())
	// 			.role(findUser.getRole())
	// 			.profileImage(findUser.getProfileImage())
	// 			.build();
	//
	// 	List<CommentDto.FeedCommentResponse> commentsList = new ArrayList<>();
	//
	// 	if (feed.getComments() == null)
	// 		commentsList = null;
	//
	// 	else {
	// 		commentsList =
	// 			feed.getComments().stream()
	// 				.map(comment -> {
	// 					return CommentDto.FeedCommentResponse.builder()
	// 						.commentId(comment.getId())
	// 						.feedId(comment.getFeed().getId())
	// 						.userInfo(
	// 							UserDto.FeedCommentResponse.builder()
	// 								.userId(comment.getUser().getUserId())
	// 								.nickname(comment.getUser().getNickname())
	// 								.profileImage(comment.getUser().getProfileImage())
	// 								.build()
	// 						)
	// 						.body(comment.getBody())
	// 						.likeCount(comment.getLikeCount())
	// 						.createdAt(comment.getCreatedAt())
	// 						.modifiedAt(comment.getModifiedAt())
	// 						.build();
	// 				}).collect(Collectors.toList());
	// 	}
	//
	// 	return FeedDto.Response.builder()
	// 		.feedId(feed.getId())
	// 		.categories(feed.getFeedCategories().stream()
	// 			.map(a -> a.getCategory().getCategoryType())
	// 			.collect(Collectors.toList()))
	// 		.userInfo(postResponse)
	// 		.title(feed.getTitle())
	// 		.body(feed.getBody())
	// 		.isLiked(feed.getIsLiked())
	// 		.likeCount(feed.getLikeCount())
	// 		.viewCount(feed.getViewCount())
	// 		.imagePath(feed.getImagePath())
	// 		.thumbnailPath(feed.getThumbnailPath())
	// 		.comments(commentsList)
	// 		.createdAt(feed.getCreatedAt())
	// 		.modifiedAt(feed.getModifiedAt())
	// 		.build();
	// }

	private static List<FeedCategory> getFeedCategoriesFromResponseDto(List<CategoryDto.Response> responseList) {
		List<FeedCategory> feedCategories = responseList.stream()
			.map(response ->
				FeedCategory.builder()
					.category(Category.builder()
						.categoryType(response.getCategoryType())
						.build())
					.build())
			.collect(Collectors.toList());
		return feedCategories;
	}

	default PageImpl<FeedDto.ListResponse> myFeedsToPageResponse(Page<Feed> feedList) {

		if (feedList == null)
			return null;

		return new PageImpl<>(feedList.stream()
			.map(feed -> {
				return FeedDto.ListResponse.builder()
					.feedId(feed.getId())
					.title(feed.getTitle())
					.body(feed.getBody())
					.categories(feed.getFeedCategories().stream()
						.map(feedCategory -> feedCategory.getCategory().getCategoryType())
						.collect(Collectors.toList()))
					.likeCount(feed.getLikeCount())
					.viewCount(feed.getViewCount())
					.createdAt(feed.getCreatedAt())
					.build();
			}).collect(Collectors.toList()));
	}

	default PageImpl<FeedDto.ListResponse> newFeedsToPageResponse(Page<Feed> feedList) {

		if (feedList == null)
			return null;

		return new PageImpl<>(feedList.stream()
			.map(feed -> {
				return FeedDto.ListResponse.builder()
					.feedId(feed.getId())
					.title(feed.getTitle())
					.body(feed.getBody())
					.commentCount(feed.getComments().size())
					.categories(feed.getFeedCategories().stream()
						.map(feedCategory -> feedCategory.getCategory().getCategoryType())
						.collect(Collectors.toList()))
					.likeCount(feed.getLikeCount())
					.viewCount(feed.getViewCount())
					.createdAt(feed.getCreatedAt())
					.build();
			}).collect(Collectors.toList()));
	}

	default CustomPage<FeedDto.ListResponse> TESTnewFeedsToPageResponse(CustomPage<Feed> feedList) {

		if (feedList == null)
			return null;

		return new CustomPage<>(feedList.stream()
			.map(feed -> {
				return FeedDto.ListResponse.builder()
					.feedId(feed.getId())
					.title(feed.getTitle())
					.body(feed.getBody())
					.commentCount(feed.getComments().size())
					.categories(feed.getFeedCategories().stream()
						.map(feedCategory -> feedCategory.getCategory().getCategoryType())
						.collect(Collectors.toList()))
					.likeCount(feed.getLikeCount())
					.viewCount(feed.getViewCount())
					.createdAt(feed.getCreatedAt())
					.build();
			}).collect(Collectors.toList()));
	}

}
