package com.ewha.back.domain.user.dto;

import com.ewha.back.domain.user.entity.enums.AgeType;
import com.ewha.back.domain.user.entity.enums.GenderType;
import lombok.*;

import javax.validation.constraints.*;
import java.util.List;

public class UserDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Post {
        @NotBlank(message = "아이디를 입력해주세요.")
        @Pattern(regexp = "[0-9a-z\\s]{6,12}", message = "6~12자의 영문, 숫자만 사용 가능합니다.")
        private String userId;
        @NotBlank(message = "닉네임을 입력해주세요.")
        @Pattern(regexp = "[0-9a-zA-Zㄱ-ㅎ가-힣\\s]{3,20}", message = "3~20자의 한글, 영문, 숫자만 사용 가능합니다.")
        private String nickname;
        @NotBlank(message = "패스워드를 입력해주세요.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$", message = "8~16자 영문, 숫자, 특수문자(@$!%*?&)만 사용 가능합니다.")
        private String password;
        private String profileImage; // 프로필 이미지

    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostResponse {
        private String userId;
        private String nickname;
        private Double ariFactor;
        private List<String> role;
        private String profileImage;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserInfo {
        @NotNull
        private String nickname;
        private String introduction;
        @NotNull
        private GenderType genderType;
        @NotNull
        private AgeType ageType;
        private String profileImage;
        @NotEmpty
        @Size(max = 3, message = "장르는 최대 3개까지 선택 가능합니다")
        private List<String> categories;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserInfoResponse {
        private String userId;
        private String nickname;
        private String introduction;
        private GenderType genderType;
        private AgeType ageType;
        private Double ariFactor;
        private String profileImage;
        private List<String> categories;
    }

    @Getter
    @NoArgsConstructor
    public static class Password {
        @NotBlank(message = "패스워드를 입력해주세요.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$", message = "8~16자 영문, 숫자, 특수문자(@$!%*?&)만 사용 가능합니다.")
        private String password;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String userId;
        private String nickname;
        private GenderType genderType;
        private AgeType ageType;
        private Double ariFactor;
        private List<String> role;
        private String profileImage;
        //private ProviderType providerType; // OAuth2 반영 안함
    }
}
