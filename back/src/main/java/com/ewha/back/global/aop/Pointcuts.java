package com.ewha.back.global.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class Pointcuts {
    @Pointcut("execution(* com.ewha.back.domain.like.controller.LikeController.postFeedLike(..))")
    public void createFeedLike () {}

    @Pointcut("execution(* com.ewha.back.domain.like.controller.LikeController.postCommentLike(..))")
    public void createCommentLike () {}

    @Pointcut("createFeedLike() || createCommentLike()")
    public void createLike () {}

    @Pointcut("execution(* com.ewha.back.domain.comment.controller.CommentController.postComment(..))")
    public void createFeedComment () {}
}
