package com.ewha.back.Controller.constant;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageImpl;

import com.ewha.back.domain.category.entity.CategoryType;
import com.ewha.back.domain.comment.dto.CommentDto;
import com.ewha.back.domain.feed.dto.FeedDto;
import com.ewha.back.domain.question.dto.QuestionDto;
import com.ewha.back.domain.user.dto.UserDto;
import com.ewha.back.domain.user.entity.enums.AgeType;
import com.ewha.back.domain.user.entity.enums.GenderType;
import com.ewha.back.global.security.dto.LoginDto;

public class UserControllerConstant {

	public static final UserDto.Verify VERIFY_DTO =
		UserDto.Verify.builder()
			.userId("testuser")
			.nickname("닉네임")
			.password("12345678a!")
			.build();

	public static final UserDto.Post POST_USER_DTO =
		UserDto.Post.builder()
			.userId("testuser")
			.nickname("닉네임")
			.password("12345678a!")
			.profileImage("프로필 사진")
			.phoneNumber("01012345678")
			.birthday("19901212")
			.centerCode(100001L)
			.build();

	public static final UserDto.PostResponse POST_USER_RESPONSE_DTO =
		UserDto.PostResponse.builder()
			.id(1L)
			.userId("testuser")
			.nickname("닉네임")
			.ariFactor(36.5)
			.role(List.of("ROLE_USER"))
			.profileImage("profile Image")
			.phoneNumber("01012345678")
			.birthday("19901212")
			.centerCode(100001L)
			.build();

	public static final LoginDto.PatchDto LOGIN_PATCH_USER_DTO =
		LoginDto.PatchDto.builder()
			.genderType(GenderType.FEMALE)
			.ageType(AgeType.THIRTIES)
			.categories(List.of(CategoryType.CULTURE))
			.build();

	public static final UserDto.Response USER_RESPONSE_DTO =
		UserDto.Response.builder()
			.id(1L)
			.userId("testuser")
			.nickname("닉네임")
			.genderType(GenderType.FEMALE)
			.ageType(AgeType.THIRTIES)
			.categoryTypes(List.of(CategoryType.CULTURE))
			.ariFactor(36.5)
			.role(List.of("ROLE_USER"))
			.profileImage("프로필 사진")
			.thumbnailPath("섬네일 경로")
			.build();

	public static final UserDto.UserInfo PATCH_USER_DTO =
		UserDto.UserInfo.builder()
			.nickname("닉네임")
			.introduction("자기 소개")
			.genderType(GenderType.FEMALE)
			.ageType(AgeType.THIRTIES)
			.profileImage("profile Image")
			.categories(List.of(CategoryType.CULTURE))
			.build();

	public static final UserDto.Password CHANGE_PASSWORD_DTO =
		UserDto.Password.builder()
			.userId("testuser")
			.newPassword("12345678a!")
			.newPasswordRepeat("12345678a!")
			.build();

	public static final UserDto.UserInfoResponse MY_PAGE_INFO_RESPONSE_DTO =
		UserDto.UserInfoResponse.builder()
			.userId("testuser")
			.nickname("닉네임")
			.introduction("자기 소개")
			.genderType(GenderType.FEMALE)
			.ageType(AgeType.THIRTIES)
			.ariFactor(36.5)
			.profileImage("프로필 사진")
			.thumbnailPath("섬네일 경로")
			.categories(List.of("HEALTH", "FAMILY", "CUISINE"))
			.phoneNumber("01012345678")
			.birthday("19901212")
			.centerCode(100001L)
			.isFirstLogin(false)
			.build();

	public static final FeedDto.ListResponse USER_FEED_RESPONSE_DTO =
		FeedDto.ListResponse.builder()
			.feedId(1L)
			.title("피드 제목")
			.commentCount(1)
			.userId("testuser")
			.categories(List.of(CategoryType.CUISINE))
			// .body("피드 내용")
			.likeCount(1L)
			.viewCount(1L)
			.createdAt(LocalDateTime.now())
			.modifiedAt(LocalDateTime.now())
			.build();

	public static final PageImpl<FeedDto.ListResponse> USER_FEED_RESPONSE_PAGE =
		new PageImpl<>(List.of(USER_FEED_RESPONSE_DTO, USER_FEED_RESPONSE_DTO));

	public static final CommentDto.ListResponse USER_COMMENT_RESPONSE_DTO =
		CommentDto.ListResponse.builder()
			.commentId(1L)
			.feedId(1L)
			.body("댓글 내용")
			.likeCount(1L)
			.createdAt(LocalDateTime.now())
			.modifiedAt(LocalDateTime.now())
			.build();

	public static final PageImpl<CommentDto.ListResponse> USER_COMMENT_RESPONSE_PAGE =
		new PageImpl<>(List.of(USER_COMMENT_RESPONSE_DTO, USER_COMMENT_RESPONSE_DTO));

	public static final QuestionDto.AnsweredResponse USER_ANSWERED_QUESTION_DTO =
		QuestionDto.AnsweredResponse.builder()
			.questionId(1L)
			.title("질문 제목")
			.body("질문 내용")
			.imagePath("이미지 주소")
			.thumbnailPath("섬네일 주소")
			.answerBody("정답")
			.userAnswer("사용자 답변")
			.build();

	public static final PageImpl<QuestionDto.AnsweredResponse> USER_ANSWERED_QUESTION_PAGE =
		new PageImpl<>(List.of(USER_ANSWERED_QUESTION_DTO, USER_ANSWERED_QUESTION_DTO));
}
