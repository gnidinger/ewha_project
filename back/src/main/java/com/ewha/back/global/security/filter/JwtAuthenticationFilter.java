package com.ewha.back.global.security.filter;

import com.ewha.back.domain.user.entity.User;
import com.ewha.back.domain.user.mapper.UserMapper;
import com.ewha.back.global.security.cookieManager.CookieManager;
import com.ewha.back.global.security.dto.LoginDto;
import com.ewha.back.global.security.jwtTokenizer.JwtTokenizer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenizer jwtTokenizer;
    private final UserMapper userMapper;
    private final CookieManager cookieManager;

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        ObjectMapper objectMapper = new ObjectMapper();
        LoginDto.PostDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.PostDto.class);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUserId(), loginDto.getPassword());

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws ServletException, IOException {
        User user = (User) authResult.getPrincipal();

        String accessToken = jwtTokenizer.delegateAccessToken(user);
        response.setHeader("Authorization", "Bearer " + accessToken);

        String refreshToken = jwtTokenizer.delegateRefreshToken(user);
        jwtTokenizer.addRefreshToken(user.getUserId(), refreshToken);

        // refresh Token을 헤더에 Set-Cookie 해주기
        ResponseCookie cookie = cookieManager.createCookie("refreshToken", refreshToken);
        response.setHeader("Set-Cookie", cookie.toString());

        // 로그인 시 필요한 정보 담기
        LoginDto.ResponseDto responseDto = userMapper.userToLoginResponse(user);
        String json = new Gson().toJson(responseDto);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);

        this.getSuccessHandler().onAuthenticationSuccess(request, response, authResult); // 핸들러 호출
    }
}
