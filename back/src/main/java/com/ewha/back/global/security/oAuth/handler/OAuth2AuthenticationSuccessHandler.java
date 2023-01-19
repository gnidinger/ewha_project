package com.ewha.back.global.security.oAuth.handler;

import com.ewha.back.domain.user.entity.User;
import com.ewha.back.domain.user.mapper.UserMapper;
import com.ewha.back.domain.user.repository.UserRepository;
import com.ewha.back.domain.user.service.UserService;
import com.ewha.back.global.security.cookieManager.CookieManager;
import com.ewha.back.global.security.dto.LoginDto;
import com.ewha.back.global.security.jwtTokenizer.JwtTokenizer;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtTokenizer jwtTokenizer;
    private final CookieManager cookieManager;
    private final UserMapper userMapper;
    private final Gson gson;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String nickname;

        if (((oAuth2User.getAttributes().get("properties"))) == null) {
            nickname = (String)oAuth2User.getAttributes().get("nickname");
        } else nickname = (String)((Map)oAuth2User.getAttributes().get("properties")).get("nickname");

        log.info("User Nickname:" + nickname);

        String registrationId = response.getHeader("registrationId");

        User user = userRepository.findByNickname(nickname);

        String accessToken = jwtTokenizer.delegateAccessToken(user);

        // refresh Token을 헤더에 Set-Cookie 해주기
        String refreshToken = jwtTokenizer.delegateRefreshToken(user);
        jwtTokenizer.addRefreshToken(user.getUserId(), refreshToken);

        ResponseCookie cookie = cookieManager.createCookie("refreshToken", refreshToken);


        // 로그인 시 필요한 정보 담기
        LoginDto.ResponseDto responseDto = userMapper.userToLoginResponse(user);

        // check
        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setHeader("Set-Cookie", cookie.toString());

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        PrintWriter writer = response.getWriter();
        writer.println(gson.toJson(responseDto));

        System.out.println("----------------------------------------------------");
        System.out.println(accessToken);
        System.out.println(refreshToken);
        System.out.println("----------------------------------------------------");

//        kakaoController.oAuthCallBack(request, response, authentication);

//        getRedirectStrategy().sendRedirect(request, response, uri);
    }

    private URI createURI(MultiValueMap<String, String> queryParams) {
        return UriComponentsBuilder
                .newInstance()
                .scheme("http")
                .host("localhost")
                .path("/receive-token.html")
                .queryParams(queryParams)
                .build()
                .toUri();
    }
}

