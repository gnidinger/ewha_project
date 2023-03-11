package com.ewha.back.Controller;

import com.ewha.back.domain.user.entity.User;
import com.ewha.back.domain.user.entity.enums.AgeType;
import com.ewha.back.domain.user.entity.enums.GenderType;
import com.ewha.back.global.security.dto.LoginDto;
import com.ewha.back.global.security.service.UserDetailsServiceImpl;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ewha.back.Controller.utils.ApiDocumentUtils.getDocumentRequest;
import static com.ewha.back.Controller.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class SecurityConfigRestDocs {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Gson gson;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void loginTest() throws Exception {

        LoginDto.PostDto post =
                LoginDto.PostDto.builder()
                        .userId("testuser")
                        .password("12345678a!")
                        .build();

        String content = gson.toJson(post);

        User user =
                User.builder()
                        .id(1L)
                        .userId("testuser")
                        .isFirstLogin(false)
                        .password(bCryptPasswordEncoder.encode("12345678a!"))
                        .role(List.of("ROLE_USER"))
                        .nickname("닉네임")
                        .ariFactor(36.5)
                        .role(List.of("ROLE_USER"))
                        .genderType(GenderType.FEMALE)
                        .ageType(AgeType.THIRTIES)
                        .build();


        UserDetailsServiceImpl.UserDetailsImpl userDetails = new UserDetailsServiceImpl.UserDetailsImpl(user);

        given(userDetailsService.loadUserByUsername(anyString())).willReturn(userDetails);

        ResultActions actions =
                mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/login")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(post.getUserId()))
                .andDo(document(
                        "User_Login",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                List.of(
                                        fieldWithPath("userId").type(JsonFieldType.STRING).description("회원 아이디"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("회원 비밀번호")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("회원 넘버"),
                                        fieldWithPath("userId").type(JsonFieldType.STRING).description("회원 아이디"),
                                        fieldWithPath("isFirstLogin").type(JsonFieldType.BOOLEAN).description("최초 로그인 여부"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
                                        fieldWithPath("ariFactor").type(JsonFieldType.NUMBER).description("아리지수"),
                                        fieldWithPath("role[]").type(JsonFieldType.ARRAY).description("회원 등급")
                                )
                        )
                ));
    }
}
