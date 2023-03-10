package com.ewha.back.domain.user.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

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
import com.ewha.back.global.dto.MultiResponseDto;
import com.ewha.back.global.dto.SingleResponseDto;
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
		// PrincipalOauth2UserService??? getAttributes????????? ??????

		Map<String, Object> attributes1 = oAuth2UserPrincipal.getAttributes();
		// attributes == attributes1

		return attributes.toString();     //????????? ?????? user ????????? ??? ??????
	}

	@PostMapping("/users/verification")
	public ResponseEntity verifyDto(@Valid @RequestBody UserDto.Verify verifyDto) {

		List<List<String>> list = userService.verifyVerifyDto(verifyDto);

		if (list.isEmpty()) {
			return ResponseEntity.ok(verifyDto);
		} else {
			List<UserDto.VerifyResponse> responses = userMapper.listToVerifyResponse(list);

			return new ResponseEntity<>(
				new SingleResponseDto<>(responses), HttpStatus.BAD_REQUEST);
		}
	}

	// @PostMapping("/users/verification")
	// public ResponseEntity verifyDto(@Valid @RequestBody UserDto.Verify verifyDto) {
	// 	userService.verifyUserId(verifyDto.getUserId());
	// 	// userService.verifyNickname(verifyDto.getNickname());
	//
	// 	return new ResponseEntity<>(
	// 		new SingleResponseDto<>(verifyDto), HttpStatus.OK);
	// }

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

		User loginUser = userService.getLoginUser();
		User updatedUser = userService.updateUser(userInfo);

		if (loginUser.getProfileImage() != null && userInfo.getProfileImage() != null && multipartFile == null
			&& userInfo.getProfileImage().equals(loginUser.getProfileImage())) {
			updatedUser.setProfileImage(updatedUser.getProfileImage());
			updatedUser.setThumbnailPath(updatedUser.getThumbnailPath());
		} else if (loginUser.getProfileImage() != null && userInfo.getProfileImage() == null && multipartFile != null) {
			imagePath = awsS3Service.updateORDeleteFeedImageFromS3(updatedUser.getId(), multipartFile);
			updatedUser.setProfileImage(imagePath.get(0));
			updatedUser.setThumbnailPath(imagePath.get(1));
		} else if (loginUser.getProfileImage() == null && userInfo.getProfileImage() == null && multipartFile != null) {
			imagePath = awsS3Service.uploadImageToS3(multipartFile, updatedUser.getId());
			updatedUser.setProfileImage(imagePath.get(0));
			updatedUser.setThumbnailPath(imagePath.get(1));
		} else if (loginUser.getProfileImage() != null && multipartFile == null && userInfo.getProfileImage() == null) {
			awsS3Service.updateORDeleteUserImageFromS3(updatedUser.getId(), multipartFile);
			updatedUser.setProfileImage(null);
			updatedUser.setThumbnailPath(null);
		}

		userService.saveUser(updatedUser);

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
	 * ?????? ??????????????? ????????? ??????. ?????? ?????????, ??????, ?????????, ????????? ??????
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

		return ResponseEntity.ok(new MultiResponseDto<>(responses.getContent(), feedList));
	}

	@GetMapping("/mypage/mycomments")
	public ResponseEntity getUserComments(@RequestParam(name = "page", defaultValue = "1") int page) {

		Page<Comment> commentList = userService.findUserComments(page);
		PageImpl<CommentDto.ListResponse> responses = commentMapper.myCommentsToPageResponse(commentList);

		return ResponseEntity.ok(new MultiResponseDto<>(responses.getContent(), commentList));
	}

	@GetMapping("/mypage/myfeedlikes")
	public ResponseEntity getUserFeedLikes(@RequestParam(name = "page", defaultValue = "1") int page) {

		Page<Feed> feedList = userService.findUserFeedLikes(page);
		PageImpl<FeedDto.ListResponse> responses = feedMapper.myFeedsToPageResponse(feedList);

		return ResponseEntity.ok(new MultiResponseDto<>(responses.getContent(), feedList));
	}

	@GetMapping("/mypage/myquestions")
	public ResponseEntity getUserQuestions(@RequestParam(name = "page", defaultValue = "1") int page) {

		Page<Question> questionList = userService.findUserQuestions(page);
		PageImpl<QuestionDto.AnsweredResponse> responses = questionMapper.myQuestionsToPageResponse(questionList);

		return ResponseEntity.ok(new MultiResponseDto<>(responses.getContent(), questionList));
	}

}
