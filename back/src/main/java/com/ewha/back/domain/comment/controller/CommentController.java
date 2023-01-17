package com.ewha.back.domain.comment.controller;

import com.ewha.back.domain.comment.dto.CommentDto;
import com.ewha.back.domain.comment.entity.Comment;
import com.ewha.back.domain.comment.mapper.CommentMapper;
import com.ewha.back.domain.comment.service.CommentService;
import com.ewha.back.global.dto.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Validated
@RestController
@RequestMapping
@RequiredArgsConstructor
public class CommentController {
    private final CommentMapper commentMapper;
    private final CommentService commentService;

    @PostMapping("/feeds/{feed_id}/comments/add")
    public ResponseEntity postComment(@PathVariable("feed_id") Long feedId,
                                      @Valid @RequestBody CommentDto.Post postComment) {

        Comment comment = commentMapper.commentPostToComment(postComment);
        Comment createdComment = commentService.createComment(comment, feedId);
        CommentDto.Response response = commentMapper.commentToCommentResponse(createdComment);

        return new ResponseEntity<>(
                new SingleResponseDto<>(response), HttpStatus.CREATED
        );
    }

    @PatchMapping("/comments/{comment_id}/edit")
    public ResponseEntity patchComment(@PathVariable("comment_id") @Positive Long commentId,
                                       @Valid @RequestBody CommentDto.Patch patchComment) {

        Comment comment = commentMapper.commentPatchToComment(patchComment);
        Comment updatedComment = commentService.updateComment(comment, commentId);
        CommentDto.Response response = commentMapper.commentToCommentResponse(updatedComment);

        return new ResponseEntity<>(
                new SingleResponseDto<>(response), HttpStatus.OK
        );
    }

    @GetMapping("/comments/{comment_id}")
    public ResponseEntity getComment(
            @PathVariable("comment_id") @Positive Long commentId) {

        Comment comment = commentService.findVerifiedComment(commentId);
        CommentDto.Response response = commentMapper.commentToCommentResponse(comment);

        return new ResponseEntity<>(
                new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @GetMapping("/feeds/{feed_id}/comments")
    public ResponseEntity getFeedComments(@PathVariable("feed_id") @Positive Long feedId,
                                          @RequestParam(name = "page", defaultValue = "1") int page) {

        Page<Comment> commentList = commentService.getFeedComments(feedId, page);
        PageImpl<CommentDto.Response> responses = commentMapper.feedCommentsToPageResponse(commentList);

        return new ResponseEntity<>(
                new SingleResponseDto<>(responses), HttpStatus.OK);
    }

    @DeleteMapping("/comments/{comment_id}/delete")
    public ResponseEntity deleteComment(
            @PathVariable("comment_id") @Positive Long commentId) {

        commentService.deleteComment(commentId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
