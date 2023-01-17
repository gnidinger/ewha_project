package com.ewha.back.domain.user.mapper;


import com.ewha.back.domain.user.dto.UserDto;
import com.ewha.back.domain.user.entity.User;
import com.ewha.back.global.security.dto.LoginDto;
import org.mapstruct.Mapper;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User userPostToUser(UserDto.Post postDto);

    User userInfotoUser(UserDto.UserInfo userInfo);

    UserDto.Response userToUserResponse(User user);

    UserDto.PostResponse userToUserPostResponse(User user);

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
