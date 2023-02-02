package com.ewha.back.domain.user.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ewha.back.domain.comment.dto.CommentDto;
import com.ewha.back.domain.comment.entity.Comment;
import com.ewha.back.domain.comment.mapper.CommentMapper;
import com.ewha.back.domain.feed.dto.FeedDto;
import com.ewha.back.domain.feed.entity.Feed;
import com.ewha.back.domain.feed.mapper.FeedMapper;
import com.ewha.back.domain.image.service.AwsS3Service;
import com.ewha.back.domain.question.dto.QuestionDto;
import com.ewha.back.domain.question.entity.Question;
import com.ewha.back.domain.question.mapper.QuestionMapper;
import com.ewha.back.domain.user.dto.UserDto;
import com.ewha.back.domain.user.entity.User;
import com.ewha.back.domain.user.mapper.UserMapper;
import com.ewha.back.domain.user.service.UserService;
import com.ewha.back.global.dto.SingleResponseDto;
import com.ewha.back.global.exception.BusinessLogicException;
import com.ewha.back.global.exception.ExceptionCode;
import com.ewha.back.global.security.dto.LoginDto;

import lombok.RequiredArgsConstructor;

@RequestMapping
@RestController
@RequiredArgsConstructor
public class UserController {
	private final UserMapper userMapper;
	private final UserService userService;
	private final FeedMapper feedMapper;
	private final CommentMapper commentMapper;
	private final QuestionMapper questionMapper;
	private final AwsS3Service awsS3Service;

	@GetMapping("/oauth/signin")
	@ResponseBody
	public String oauthLoginInfo(Authentication authentication,
		@AuthenticationPrincipal OAuth2User oAuth2UserPrincipal) {
		OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
		Map<String, Object> attributes = oAuth2User.getAttributes();
		System.out.println(attributes);
		// PrincipalOauth2UserService의 getAttributes내용과 같음

		Map<String, Object> attributes1 = oAuth2UserPrincipal.getAttributes();
		// attributes == attributes1

		return attributes.toString();     //세션에 담긴 user 가져올 수 있음
	}

	@PostMapping("/users/signup")
	public ResponseEntity postUser(@Valid @RequestBody UserDto.Post postDto) {

		User user = userMapper.userPostToUser(postDto);
		User createdUser = userService.createUser(user);
		UserDto.PostResponse response = userMapper.userToUserPostResponse(createdUser);

		return new ResponseEntity<>(
			new SingleResponseDto<>(response), HttpStatus.CREATED);
	}

	@PatchMapping("/user/firstlogin")
	public ResponseEntity firstLoginUser(@Valid @RequestBody LoginDto.PatchDto firstPatchDto) {

		User firstLoginUser = userService.onFirstLogin(firstPatchDto);
		UserDto.Response response = userMapper.userToUserResponse(firstLoginUser);

		return new ResponseEntity<>(
			new SingleResponseDto<>(response), HttpStatus.OK);
	}

	@PatchMapping("/mypage/patch")
	public ResponseEntity patchUser(@RequestParam(value = "image") @Nullable MultipartFile multipartFile,
		@Valid @RequestPart(value = "patch") UserDto.UserInfo userInfo) throws Exception {

		List<String> imagePath = null;

		User updatedUser = userService.updateUser(userInfo);

		if (multipartFile != null) {
			imagePath = awsS3Service.updateORDeleteUserImageFromS3(updatedUser.getId(), multipartFile);
			updatedUser.setProfileImage(imagePath.get(0));
			updatedUser.setThumbnailPath(imagePath.get(1));
		}

		UserDto.Response response = userMapper.userToUserResponse(updatedUser);

		return new ResponseEntity<>(
			new SingleResponseDto<>(response), HttpStatus.OK);
	}

	@PatchMapping("/mypage/patch/password")
	public void patchPassword(@Valid @RequestBody UserDto.Password password) {
		userService.updatePassword(password);
	}

	@GetMapping("/users/{user_id}")
	public ResponseEntity getUserPage(@PathVariable("user_id") Long userId) {

		User findUser = userService.getUser(userId);
		UserDto.Response response = userMapper.userToUserResponse(findUser);

		return new ResponseEntity<>(
			new SingleResponseDto<>(response), HttpStatus.OK);
	}

	@DeleteMapping("/mypage/signout")
	public ResponseEntity deleteUser() {

		userService.deleteUser();

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/*
	 * 마이 페이지에서 표시될 정보. 나의 게시글, 댓글, 북마크, 대답한 질문
	 */

	@GetMapping("/mypage")
	public ResponseEntity getMyPage() {

		User findUser = userService.getMyInfo();
		UserDto.UserInfoResponse response = userMapper.userToUserInfoResponse(findUser);

		return new ResponseEntity<>(
			new SingleResponseDto<>(response), HttpStatus.OK);
	}

	@GetMapping("/mypage/myfeeds")
	public ResponseEntity getUserFeeds(@RequestParam(name = "page", defaultValue = "1") int page) {

		Page<Feed> feedList = userService.findUserFeeds(page);
		PageImpl<FeedDto.ListResponse> responses = feedMapper.myFeedsToPageResponse(feedList);

		return new ResponseEntity<>(
			new SingleResponseDto<>(responses), HttpStatus.OK);
	}

	@GetMapping("/mypage/mycomments")
	public ResponseEntity getUserComments(@RequestParam(name = "page", defaultValue = "1") int page) {

		Page<Comment> commentList = userService.findUserComments(page);
		PageImpl<CommentDto.ListResponse> responses = commentMapper.myCommentsToPageResponse(commentList);

		return new ResponseEntity<>(
			new SingleResponseDto<>(responses), HttpStatus.OK);
	}

	@GetMapping("/mypage/mybookmarks")
	public ResponseEntity getUserBookmarks(@PathParam("page") int page) {
		return null;
	}

	@GetMapping("/mypage/myquestions")
	public ResponseEntity getUserQuestions(@PathParam("page") int page) {

		Page<Question> questionList = userService.findUserQuestions(page);
		PageImpl<QuestionDto.AnsweredResponse> responses = questionMapper.myQuestionsToPageResponse(questionList);

		return new ResponseEntity<>(
			new SingleResponseDto<>(responses), HttpStatus.OK);
	}

}
