package com.ewha.back.domain.user.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ewha.back.domain.comment.entity.Comment;
import com.ewha.back.domain.feed.entity.Feed;
import com.ewha.back.domain.question.entity.Question;
import com.ewha.back.domain.user.dto.UserDto;
import com.ewha.back.domain.user.entity.User;
import com.ewha.back.global.security.dto.LoginDto;

public interface UserService {
	User createUser(User user);

	List<List<String>> verifyVerifyDto(UserDto.Verify verifyDto);

	List<String> verifyUserId(String userId);

	List<String> verifyNickname(String nickname);

	List<String> verifyPassword(String password);

	Boolean verifyUserIdForSms(String userId);

	Boolean verifyLoginUserPassword(String password);

	User updateUser(UserDto.UserInfo userInfo);

	void updatePassword(UserDto.Password password);

	User getUser(Long userId);

	User getMyInfo();

	void deleteUser();

	User onFirstLogin(LoginDto.PatchDto patchDto);

	Boolean verifyNicknameAndPhoneNumber(String nickname, String phoneNumber);

	Boolean verifyUserIdAndPhoneNumber(String userId, String phoneNumber);

	User findByNickname(String nickname);

	User findByUserId(String userId);

	User findVerifiedUser(Long id);

	User getLoginUser();

	Page<Feed> findUserFeeds(int page);

	Page<Comment> findUserComments(int page);

	Page<Feed> findUserFeedLikes(int page);

	Page<Question> findUserQuestions(int page);

	void saveUser(User user);
}
