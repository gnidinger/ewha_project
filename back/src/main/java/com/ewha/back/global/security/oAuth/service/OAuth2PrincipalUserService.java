package com.ewha.back.global.security.oAuth.service;

import com.ewha.back.domain.user.entity.User;
import com.ewha.back.domain.user.entity.enums.AgeType;
import com.ewha.back.domain.user.entity.enums.GenderType;
import com.ewha.back.domain.user.entity.enums.Role;
import com.ewha.back.domain.user.repository.UserRepository;
import com.ewha.back.domain.user.service.UserService;
import com.ewha.back.global.security.oAuth.userInfo.KakaoUserInfo;
import com.ewha.back.global.security.oAuth.userInfo.NaverUserInfo;
import com.ewha.back.global.security.oAuth.userInfo.OAuth2UserInfo;
import com.ewha.back.global.security.oAuth.userInfo.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.ewha.back.domain.user.entity.enums.Role.ROLE_USER;

@Service
@Transactional
@RequiredArgsConstructor
public class OAuth2PrincipalUserService extends DefaultOAuth2UserService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2UserInfo oAuth2UserInfo = null;	//추가
        String provider = userRequest.getClientRegistration().getRegistrationId();    //google

        if (provider.equals("naver")) {
            oAuth2UserInfo = new NaverUserInfo(oAuth2User.getAttributes());
        } else if (provider.equals("kakao")) {    //추가
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        }

        String providerId = oAuth2UserInfo.getProviderId(); // 네이버를 위해 추가
        String nickname = oAuth2UserInfo.getName(); // 사용자 이름을 닉네임으로 사용(중복 가능?)
        String uuid = UUID.randomUUID().toString().substring(0, 6);
        String userId = oAuth2UserInfo.getProvider() + uuid; // 사용자가 입력한 적은 없지만 만들어준다

        String uuid2 = UUID.randomUUID().toString().substring(0, 6);
        String password = bCryptPasswordEncoder.encode("패스워드"+uuid2);  // 사용자가 입력한 적은 없지만 만들어준다

        String email = oAuth2UserInfo.getEmail(); //수정
        Role role = ROLE_USER;

        User findUser = userRepository.findByNickname(nickname);

        //DB에 없는 사용자라면 회원가입처리
        if(findUser == null){
//            findUser = User.oauth2Register()
//                    .nickname(nickname)
//                    .password(password)
//                    .email(email)
//                    .ariFactor(36.5)
//                    .role(List.of("ROLE_USER"))
//                    .provider(provider)
//                    .providerId(providerId)
//                    .build();
            findUser = User.builder()
                    .userId(userId)
                    .nickname(nickname)
                    .password(password)
                    .email(email)
                    .ariFactor(36.5)
                    .role(List.of("ROLE_USER"))
                    .isFirstLogin(true)
                    .provider(provider)
                    .providerId(providerId)
                    .build();

            userRepository.save(findUser);
        }
        return new PrincipalDetails(findUser, oAuth2UserInfo);
    }
}
