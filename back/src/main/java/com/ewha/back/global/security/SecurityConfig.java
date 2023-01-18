package com.ewha.back.global.security;

import com.ewha.back.domain.user.mapper.UserMapper;
import com.ewha.back.global.security.cookieManager.CookieManager;
import com.ewha.back.global.security.filter.JwtAuthenticationFilter;
import com.ewha.back.global.security.filter.JwtVerificationFilter;
import com.ewha.back.global.security.handler.*;
import com.ewha.back.global.security.jwtTokenizer.JwtTokenizer;
import com.ewha.back.global.security.oAuth.handler.OAuth2AuthenticationFailureHandler;
import com.ewha.back.global.security.oAuth.handler.OAuth2AuthenticationSuccessHandler;
import com.ewha.back.global.security.oAuth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.ewha.back.global.security.oAuth.service.OAuth2PrincipalUserService;
import com.ewha.back.global.security.util.CustomAuthorityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity(debug = true)
public class SecurityConfig {
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils authorityUtils;
    private final UserMapper userMapper;
    private final CookieManager cookieManager;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository;
    private final OAuth2PrincipalUserService oAuth2PrincipalUserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
//    private final OAuth2UserServiceImpl oAuth2UserService;
//    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().sameOrigin()
                .and()
                .csrf().disable()
                .cors(withDefaults())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .exceptionHandling()
                .authenticationEntryPoint(new UserAuthenticationEntryPoint())
                .accessDeniedHandler(new UserAccessDeniedHandler())
                .and()
                .apply(new CustomFilterConfigurer())
                .and()
                .logout()
                .logoutUrl("/logout")
                .addLogoutHandler(new UserLogoutHandler(jwtTokenizer, cookieManager))
                .logoutSuccessHandler(new UserLogoutSuccessHandler())
                .deleteCookies("refreshToken")
                .deleteCookies("visit_cookie")
                .and()
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll())
                .oauth2Login() // OAuth2 로그인 설정 시작점
                .authorizationEndpoint()
                .baseUri("/oauth2/authorization")
                .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository)
                .and()
                .redirectionEndpoint()
                .baseUri("/*/oauth2/*")
                .and()
                .userInfoEndpoint() // OAuth2 로그인 성공 이후 사용자 정보를 가져올 때 설정 담당
                .userService(oAuth2PrincipalUserService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler);
        return http.build();
    }

    // CORS 정책은 corsConfig에서 설정

    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);

            JwtAuthenticationFilter jwtAuthenticationFilter =
                    new JwtAuthenticationFilter(authenticationManager, jwtTokenizer, userMapper, cookieManager);
            jwtAuthenticationFilter.setFilterProcessesUrl("/login");
            jwtAuthenticationFilter.setAuthenticationSuccessHandler(new UserAuthenticationSuccessHandler());
            jwtAuthenticationFilter.setAuthenticationFailureHandler(new UserAuthenticationFailureHandler());

            JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtTokenizer, authorityUtils);

            builder.addFilter(jwtAuthenticationFilter)
                    .addFilterAfter(jwtVerificationFilter, JwtAuthenticationFilter.class);
        }
    }
}
