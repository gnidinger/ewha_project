package com.ewha.back.domain.user.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;

import com.ewha.back.domain.user.dto.UserDto;
import com.ewha.back.domain.user.entity.User;
import com.ewha.back.global.security.dto.LoginDto;

@Mapper(componentModel = "spring")
public interface UserMapper {
	User userPostToUser(UserDto.Post postDto);

	User userInfotoUser(UserDto.UserInfo userInfo);

	UserDto.Response userToUserResponse(User user);

	UserDto.PostResponse userToUserPostResponse(User user);

	default List<UserDto.VerifyResponse> listToVerifyResponse(List<List<String>> list) {

		List<UserDto.VerifyResponse> responses = new ArrayList<>();

		list.forEach(stringList -> {
			UserDto.VerifyResponse.VerifyResponseBuilder verifyResponse = UserDto.VerifyResponse.builder();

			verifyResponse.status(stringList.get(0));
			verifyResponse.message(stringList.get(1));
			verifyResponse.field(stringList.get(2));
			verifyResponse.rejectedValue(stringList.get(3));
			verifyResponse.reason(stringList.get(4));

			responses.add(verifyResponse.build());
		});

		return responses;
	}

	default UserDto.UserInfoResponse userToUserInfoResponse(User user) {

		if (user == null) {
			return null;
		}

		UserDto.UserInfoResponse.UserInfoResponseBuilder userInfoResponse = UserDto.UserInfoResponse.builder();

		userInfoResponse.userId(user.getUserId());
		userInfoResponse.nickname(user.getNickname());
		userInfoResponse.introduction(user.getIntroduction());
		userInfoResponse.genderType(user.getGenderType());
		userInfoResponse.ageType(user.getAgeType());
		userInfoResponse.ariFactor(user.getAriFactor());
		userInfoResponse.profileImage(user.getProfileImage());
		userInfoResponse.categories(
			user.getUserCategories().stream()
				.map(userCategory -> userCategory.getCategory().getCategoryType().toString())
				.collect(Collectors.toList())
		);

		return userInfoResponse.build();
	}

	LoginDto.ResponseDto userToLoginResponse(User user);
}
