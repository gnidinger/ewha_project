package com.ewha.back.Controller.constant;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageImpl;

import com.ewha.back.domain.category.dto.CategoryDto;
import com.ewha.back.domain.category.entity.CategoryType;
import com.ewha.back.domain.feed.dto.FeedDto;
import com.ewha.back.domain.user.dto.UserDto;
import com.ewha.back.domain.user.entity.User;
import com.ewha.back.global.config.CustomPage;

public class FeedControllerConstant {

	public static final UserDto.BasicResponse USER_BASIC_RESPONSE =
		UserDto.BasicResponse.builder()
			.id(1L)
			.userId("testuser")
			.nickname("닉네임")
			.ariFactor(36.5)
			.profileImage("프로필 이미지")
			.thumbnailPath("섬네일 이미지")
			.centerCode(123456L)
			.build();

	public static final  CategoryDto.Response CATEGORY_DTO =
		CategoryDto.Response.builder()
			.categoryType(CategoryType.CULTURE)
			.build();

	public static final FeedDto.Post POST_FEED_DTO =
		FeedDto.Post.builder()
			.title("피드 제목")
			.body("피드 내용")
			.categories(List.of(CATEGORY_DTO))
			.build();

	public static final FeedDto.Response POST_FEED_RESPONSE_DTO =
		FeedDto.Response.builder()
			.feedId(1L)
			.categories(List.of(CategoryType.CULTURE))
			.userInfo(USER_BASIC_RESPONSE)
			.title("피드 제목")
			.body("피드 내용")
			.isLiked(false)
			.likeCount(0L)
			.viewCount(1L)
			.imagePath("이미지 주소")
			.thumbnailPath("섬네일 주소")
			.comments(new ArrayList<>())
			.createdAt(LocalDateTime.now())
			.modifiedAt(LocalDateTime.now())
			.build();

	public static final FeedDto.Patch PATCH_FEED_DTO =
		FeedDto.Patch.builder()
			.title("피드 제목")
			.body("피드 내용")
			.categories(List.of(CATEGORY_DTO))
			.imagePath("이미지 주소")
			.build();

	public static final FeedDto.Response GET_FEED_RESPONSE_DTO =
		FeedDto.Response.builder()
			.feedId(1L)
			.categories(List.of(CategoryType.CULTURE))
			.userInfo(USER_BASIC_RESPONSE)
			.title("피드 제목")
			.body("피드 내용")
			.isMyFeed(false)
			.isLiked(true)
			.likeCount(0L)
			.viewCount(1L)
			.imagePath("이미지 주소")
			.thumbnailPath("섬네일 주소")
			.comments(new ArrayList<>())
			.createdAt(LocalDateTime.now())
			.modifiedAt(LocalDateTime.now())
			.build();
	public static final FeedDto.ListResponse FEED_LIST_RESPONSE =
		FeedDto.ListResponse.builder()
			.feedId(1L)
			.userId("testuser")
			.categories(List.of(CategoryType.CUISINE))
			.title("피드 제목")
			.commentCount(1)
			.likeCount(1L)
			.viewCount(1L)
			.createdAt(LocalDateTime.now())
			.modifiedAt(LocalDateTime.now())
			.build();

	public static final CustomPage<FeedDto.ListResponse> FEED_LIST_RESPONSE_PAGE =
		new CustomPage<>(List.of(FEED_LIST_RESPONSE, FEED_LIST_RESPONSE));
}
