package com.ewha.back.domain.feed.mapper;

import com.ewha.back.domain.category.dto.CategoryDto;
import com.ewha.back.domain.category.entity.Category;
import com.ewha.back.domain.comment.dto.CommentDto;
import com.ewha.back.domain.feed.dto.FeedDto;
import com.ewha.back.domain.feed.entity.Feed;
import com.ewha.back.domain.feed.entity.FeedCategory;
import com.ewha.back.domain.user.dto.UserDto;
import com.ewha.back.domain.user.entity.User;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    default FeedDto.Response feedToFeedResponse(Feed feed) {

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

        if (feed.getComments() == null) commentsList = null;

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
                .isLiked(feed.getIsLiked())
                .likeCount(feed.getLikeCount())
                .viewCount(feed.getViewCount())
                .imagePath(feed.getImagePath())
                .thumbnailPath(feed.getThumbnailPath())
                .comments(commentsList)
                .createdAt(feed.getCreatedAt())
                .modifiedAt(feed.getModifiedAt())
                .build();
    }

    private static List<FeedCategory> getFeedCategoriesFromResponseDto(List<CategoryDto.Response> responseList) {
        List<FeedCategory> feedCategories = responseList.stream()
                .map(response ->
                        FeedCategory.builder().
                                category(Category.builder()
                                        .categoryType(response.getCategoryType())
                                        .build())
                                .build())
                .collect(Collectors.toList());
        return feedCategories;
    }

    default PageImpl<FeedDto.ListResponse> myFeedsToPageResponse(Page<Feed> feedList) {

        if (feedList == null) return null;

        return new PageImpl<>(feedList.stream()
                .map(feed -> {
                    return FeedDto.ListResponse.builder()
                            .feedId(feed.getId())
                            .title(feed.getTitle())
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

        if (feedList == null) return null;

        return new PageImpl<>(feedList.stream()
                .map(feed -> {
                    return FeedDto.ListResponse.builder()
                            .feedId(feed.getId())
                            .title(feed.getTitle())
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
