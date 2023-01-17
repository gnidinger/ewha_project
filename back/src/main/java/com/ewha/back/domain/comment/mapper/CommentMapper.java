package com.ewha.back.domain.comment.mapper;

import com.ewha.back.domain.comment.dto.CommentDto;
import com.ewha.back.domain.comment.entity.Comment;
import com.ewha.back.domain.feed.dto.FeedDto;
import com.ewha.back.domain.feed.entity.Feed;
import com.ewha.back.domain.user.dto.UserDto;
import com.ewha.back.domain.user.entity.User;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment commentPostToComment(CommentDto.Post postComment);
    Comment commentPatchToComment(CommentDto.Patch patchComment);

    default CommentDto.Response commentToCommentResponse(Comment comment) {

        User user = comment.getUser();

        return CommentDto.Response.builder()
                .commentId(comment.getId())
                .feedId(comment.getFeed().getId())
                .userInfo(UserDto.PostResponse.builder()
                        .userId(user.getUserId())
                        .nickname(user.getNickname())
                        .role(user.getRole())
                        .profileImage(user.getProfileImage())
                        .build())
                .body(comment.getBody())
                .likeCount(comment.getLikeCount())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                .build();
    }

    default PageImpl<CommentDto.ListResponse> myCommentsToPageResponse(Page<Comment> commentList) {

        if (commentList == null) return null;

        return new PageImpl<>(commentList.stream()
                .map(comment -> {
                    return CommentDto.ListResponse.builder()
                            .commentId(comment.getId())
                            .body(comment.getBody())
                            .likeCount(comment.getLikeCount())
                            .createdAt(comment.getCreatedAt())
                            .build();
                }).collect(Collectors.toList()));
    }

    default PageImpl<CommentDto.Response> feedCommentsToPageResponse(Page<Comment> commentList) {

        if (commentList == null) return null;

        return new PageImpl<>(commentList.stream()
                .map(this::commentToCommentResponse)
                .collect(Collectors.toList()));
    }
}
