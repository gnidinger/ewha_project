// package com.ewha.back.Controller;
//
// import com.ewha.back.domain.category.entity.CategoryType;
// import com.ewha.back.domain.comment.dto.CommentDto;
// import com.ewha.back.domain.comment.mapper.CommentMapper;
// import com.ewha.back.domain.feed.dto.FeedDto;
// import com.ewha.back.domain.feed.mapper.FeedMapper;
// import com.ewha.back.domain.question.dto.QuestionDto;
// import com.ewha.back.domain.question.mapper.QuestionMapper;
// import com.ewha.back.domain.user.dto.UserDto;
// import com.ewha.back.domain.user.entity.User;
// import com.ewha.back.domain.user.entity.enums.AgeType;
// import com.ewha.back.domain.user.entity.enums.GenderType;
// import com.ewha.back.domain.user.mapper.UserMapper;
// import com.ewha.back.domain.user.service.UserService;
// import com.ewha.back.global.security.dto.LoginDto;
// import com.google.gson.Gson;
// import org.junit.jupiter.api.Test;
// import org.mockito.Mockito;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.data.domain.PageImpl;
// import org.springframework.http.MediaType;
// import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
// import org.springframework.restdocs.payload.JsonFieldType;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.ResultActions;
// import org.springframework.transaction.annotation.Transactional;
//
// import java.time.LocalDateTime;
// import java.util.ArrayList;
// import java.util.List;
//
// import static com.ewha.back.Controller.utils.ApiDocumentUtils.getDocumentRequest;
// import static com.ewha.back.Controller.utils.ApiDocumentUtils.getDocumentResponse;
// import static org.mockito.ArgumentMatchers.anyInt;
// import static org.mockito.ArgumentMatchers.anyLong;
// import static org.mockito.BDDMockito.given;
// import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
// import static org.springframework.restdocs.payload.PayloadDocumentation.*;
// import static org.springframework.restdocs.request.RequestDocumentation.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
// @Transactional
// @SpringBootTest
// @AutoConfigureMockMvc
// @AutoConfigureRestDocs
// public class UserControllerRestDocs {
//
//     @Autowired
//     private MockMvc mockMvc;
//     @Autowired
//     private Gson gson;
//     @MockBean
//     private UserMapper userMapper;
//     @MockBean
//     private UserService userService;
//     @MockBean
//     private FeedMapper feedMapper;
//     @MockBean
//     private CommentMapper commentMapper;
//     @MockBean
//     private QuestionMapper questionMapper;
//
//     @Test
//     void postUserTest() throws Exception {
//
//         UserDto.Post post =
//                 UserDto.Post.builder()
//                         .userId("testuser")
//                         .nickname("닉네임")
//                         .password("12345678a!")
//                         .profileImage("profile Image")
//                         .build();
//
//         String content = gson.toJson(post);
//
//         UserDto.PostResponse response =
//                 UserDto.PostResponse.builder()
//                         .userId("testuser")
//                         .nickname("닉네임")
//                         .ariFactor(36.5)
//                         .role(List.of("ROLE_USER"))
//                         .profileImage("profile Image")
//                         .build();
//
//         given(userMapper.userPostToUser(Mockito.any(UserDto.Post.class))).willReturn(new User());
//         given(userService.createUser(Mockito.any(User.class))).willReturn(new User());
//         given(userMapper.userToUserPostResponse(Mockito.any(User.class))).willReturn(response);
//
//         ResultActions actions =
//                 mockMvc.perform(
//                         RestDocumentationRequestBuilders.post("/users/signup")
//                                 .accept(MediaType.APPLICATION_JSON)
//                                 .contentType(MediaType.APPLICATION_JSON)
//                                 .content(content)
//                 );
//
//         actions
//                 .andExpect(status().isCreated())
//                 .andExpect(jsonPath("$.data.userId").value(post.getUserId()))
//                 .andExpect(jsonPath("$.data.nickname").value(post.getNickname()))
//                 .andDo(document(
//                         "Post_User",
//                         getDocumentRequest(),
//                         getDocumentResponse(),
//                         requestFields(
//                                 List.of(
//                                         fieldWithPath("userId").type(JsonFieldType.STRING).description("회원 아이디"),
//                                         fieldWithPath("nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
//                                         fieldWithPath("password").type(JsonFieldType.STRING).description("회원 비밀번호"),
//                                         fieldWithPath("profileImage").type(JsonFieldType.STRING).description("회원 프로필 사진")
//                                 )
//                         ),
//                         responseFields(
//                                 List.of(
//                                         fieldWithPath("data.").type(JsonFieldType.OBJECT).description("결과 데이터"),
//                                         fieldWithPath("data.userId").type(JsonFieldType.STRING).description("회원 아이디"),
//                                         fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
//                                         fieldWithPath("data.ariFactor").type(JsonFieldType.NUMBER).description("아리지수"),
//                                         fieldWithPath("data.role[]").type(JsonFieldType.ARRAY).description("회원 등급"),
//                                         fieldWithPath("data.profileImage").type(JsonFieldType.STRING).description("회원 프로필 사진")
//                                 )
//                         )));
//     }
//
//     @Test
//     void firstLoginUserTest() throws Exception {
//
//         LoginDto.PatchDto patch =
//                 LoginDto.PatchDto.builder()
//                         .genderType(GenderType.FEMALE)
//                         .ageType(AgeType.THIRTIES)
//                         .categories(List.of("HEALTH", "FAMILY", "CUISINE"))
//                         .build();
//
//         String content = gson.toJson(patch);
//
//         UserDto.Response response =
//                 UserDto.Response.builder()
//                         .userId("testuser")
//                         .nickname("닉네임")
//                         .genderType(GenderType.FEMALE)
//                         .ageType(AgeType.THIRTIES)
//                         .ariFactor(36.5)
//                         .role(List.of("ROLE_USER"))
//                         .profileImage("profile Image")
//                         .build();
//
//         given(userService.onFirstLogin(Mockito.any(LoginDto.PatchDto.class))).willReturn(new User());
//         given(userMapper.userToUserResponse(Mockito.any(User.class))).willReturn(response);
//
//         ResultActions actions =
//                 mockMvc.perform(
//                         RestDocumentationRequestBuilders.patch("/user/firstlogin")
//                                 .accept(MediaType.APPLICATION_JSON)
//                                 .contentType(MediaType.APPLICATION_JSON)
//                                 .content(content)
//                 );
//
//         actions
//                 .andExpect(status().isOk())
//                 .andDo(document(
//                         "First_Login_User",
//                         getDocumentRequest(),
//                         getDocumentResponse(),
//                         requestFields(
//                                 List.of(
//                                         fieldWithPath("genderType").type(JsonFieldType.STRING).description("회원 성별"),
//                                         fieldWithPath("ageType").type(JsonFieldType.STRING).description("회원 연령대"),
//                                         fieldWithPath("categories").type(JsonFieldType.ARRAY).description("회원 프로필 사진")
//                                 )
//                         ),
//                         responseFields(
//                                 List.of(
//                                         fieldWithPath("data.").type(JsonFieldType.OBJECT).description("결과 데이터"),
//                                         fieldWithPath("data.userId").type(JsonFieldType.STRING).description("회원 아이디"),
//                                         fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
//                                         fieldWithPath("data.genderType").type(JsonFieldType.STRING).description("회원 성별"),
//                                         fieldWithPath("data.ageType").type(JsonFieldType.STRING).description("회원 연령대"),
//                                         fieldWithPath("data.ariFactor").type(JsonFieldType.NUMBER).description("아리지수"),
//                                         fieldWithPath("data.role[]").type(JsonFieldType.ARRAY).description("회원 등급"),
//                                         fieldWithPath("data.profileImage").type(JsonFieldType.STRING).description("회원 프로필 사진")
//                                 )
//                         )));
//     }
//
//     @Test
//     void patchUserTest() throws Exception {
//
//         UserDto.UserInfo patch =
//                 UserDto.UserInfo.builder()
//                         .nickname("닉네임")
//                         .introduction("자기 소개")
//                         .genderType(GenderType.FEMALE)
//                         .ageType(AgeType.THIRTIES)
//                         .profileImage("profile Image")
//                         .categories(List.of("HEALTH", "FAMILY", "CUISINE"))
//                         .build();
//
//         String content = gson.toJson(patch);
//
//         UserDto.Response response =
//                 UserDto.Response.builder()
//                         .userId("testuser")
//                         .nickname("닉네임")
//                         .genderType(GenderType.FEMALE)
//                         .ageType(AgeType.THIRTIES)
//                         .ariFactor(36.5)
//                         .role(List.of("ROLE_USER"))
//                         .profileImage("profile Image")
//                         .build();
//
//         given(userService.updateUser(Mockito.any(UserDto.UserInfo.class))).willReturn(new User());
//         given(userMapper.userToUserResponse(Mockito.any(User.class))).willReturn(response);
//
//         ResultActions actions =
//                 mockMvc.perform(
//                         RestDocumentationRequestBuilders.patch("/mypage/userinfo")
//                                 .header("Authorization", "Token")
//                                 .accept(MediaType.APPLICATION_JSON)
//                                 .contentType(MediaType.APPLICATION_JSON)
//                                 .content(content)
//                 );
//
//         actions
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.data.nickname").value(patch.getNickname()))
//                 .andDo(document(
//                         "Patch_User",
//                         getDocumentRequest(),
//                         getDocumentResponse(),
//                         requestFields(
//                                 List.of(
//                                         fieldWithPath("nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
//                                         fieldWithPath("introduction").type(JsonFieldType.STRING).description("회원 자기 소개"),
//                                         fieldWithPath("genderType").type(JsonFieldType.STRING).description("회원 성별"),
//                                         fieldWithPath("ageType").type(JsonFieldType.STRING).description("회원 연령대"),
//                                         fieldWithPath("profileImage").type(JsonFieldType.STRING).description("회원 프로필 사진"),
//                                         fieldWithPath("categories").type(JsonFieldType.ARRAY).description("회원 프로필 사진")
//                                 )
//                         ),
//                         responseFields(
//                                 List.of(
//                                         fieldWithPath("data.").type(JsonFieldType.OBJECT).description("결과 데이터"),
//                                         fieldWithPath("data.userId").type(JsonFieldType.STRING).description("회원 아이디"),
//                                         fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
//                                         fieldWithPath("data.genderType").type(JsonFieldType.STRING).description("회원 성별"),
//                                         fieldWithPath("data.ageType").type(JsonFieldType.STRING).description("회원 연령대"),
//                                         fieldWithPath("data.ariFactor").type(JsonFieldType.NUMBER).description("아리지수"),
//                                         fieldWithPath("data.role[]").type(JsonFieldType.ARRAY).description("회원 등급"),
//                                         fieldWithPath("data.profileImage").type(JsonFieldType.STRING).description("회원 프로필 사진")
//                                 )
//                         )));
//     }
//
//     @Test
//     void getUserTest() throws Exception {
//
//         Long userId = 1L;
//
//         UserDto.Response response =
//                 UserDto.Response.builder()
//                         .userId("testuser")
//                         .nickname("닉네임")
//                         .genderType(GenderType.FEMALE)
//                         .ageType(AgeType.THIRTIES)
//                         .ariFactor(36.5)
//                         .role(List.of("ROLE_USER"))
//                         .profileImage("profile Image")
//                         .build();
//
//         given(userService.getUser(anyLong())).willReturn(new User());
//         given(userMapper.userToUserResponse(Mockito.any(User.class))).willReturn(response);
//
//         ResultActions actions =
//                 mockMvc.perform(
//                         RestDocumentationRequestBuilders.get("/users/{user_id}", userId)
//                                 .accept(MediaType.APPLICATION_JSON)
//                 );
//
//         actions
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.data.userId").value(response.getUserId()))
//                 .andDo(document(
//                         "Get_User",
//                         getDocumentRequest(),
//                         getDocumentResponse(),
//                         pathParameters(
//                                 parameterWithName("user_id").description("유저 번호")
//                         ),
//                         responseFields(
//                                 List.of(
//                                         fieldWithPath("data.").type(JsonFieldType.OBJECT).description("결과 데이터"),
//                                         fieldWithPath("data.userId").type(JsonFieldType.STRING).description("유저 아이디"),
//                                         fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
//                                         fieldWithPath("data.genderType").type(JsonFieldType.STRING).description("회원 성별"),
//                                         fieldWithPath("data.ageType").type(JsonFieldType.STRING).description("회원 연령대"),
//                                         fieldWithPath("data.ariFactor").type(JsonFieldType.NUMBER).description("아리지수"),
//                                         fieldWithPath("data.role[]").type(JsonFieldType.ARRAY).description("회원 등급"),
//                                         fieldWithPath("data.profileImage").type(JsonFieldType.STRING).description("회원 프로필 사진")
//                                 )
//                         )));
//     }
//
//     @Test
//     void getMyPageTest() throws Exception {
//
//         UserDto.UserInfoResponse response =
//                 UserDto.UserInfoResponse.builder()
//                         .userId("testuser")
//                         .nickname("닉네임")
//                         .introduction("자기 소개")
//                         .genderType(GenderType.FEMALE)
//                         .ageType(AgeType.THIRTIES)
//                         .ariFactor(36.5)
//                         .profileImage("profile Image")
//                         .categories(List.of("HEALTH", "FAMILY", "CUISINE"))
//                         .build();
//
//         given(userService.getMyInfo()).willReturn(new User());
//         given(userMapper.userToUserInfoResponse(Mockito.any(User.class))).willReturn(response);
//
//         ResultActions actions =
//                 mockMvc.perform(
//                         RestDocumentationRequestBuilders.get("/mypage")
//                                 .accept(MediaType.APPLICATION_JSON)
//                 );
//
//         actions
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.data.userId").value(response.getUserId()))
//                 .andDo(document(
//                         "Get_MyPage",
//                         getDocumentRequest(),
//                         getDocumentResponse(),
//                         responseFields(
//                                 List.of(
//                                         fieldWithPath("data.").type(JsonFieldType.OBJECT).description("결과 데이터"),
//                                         fieldWithPath("data.userId").type(JsonFieldType.STRING).description("유저 아이디"),
//                                         fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
//                                         fieldWithPath("data.introduction").type(JsonFieldType.STRING).description("회원 자기 소개"),
//                                         fieldWithPath("data.genderType").type(JsonFieldType.STRING).description("회원 성별"),
//                                         fieldWithPath("data.ageType").type(JsonFieldType.STRING).description("회원 연령대"),
//                                         fieldWithPath("data.ariFactor").type(JsonFieldType.NUMBER).description("아리지수"),
//                                         fieldWithPath("data.profileImage").type(JsonFieldType.STRING).description("회원 프로필 사진"),
//                                         fieldWithPath("data.categories[]").type(JsonFieldType.ARRAY).description("회원 관심사")
//                                 )
//                         )));
//
//     }
//
//     @Test
//     void getUserFeedsTest() throws Exception {
//
//         int page = 1;
//
//         FeedDto.ListResponse feed_1 =
//                 FeedDto.ListResponse.builder()
//                         .feedId(1L)
//                         .title("피드 제목 1")
//                         .commentCount(1)
//                         .userId("testuser1")
//                         .categories(List.of(CategoryType.CUISINE))
//                         .likeCount(1L)
//                         .viewCount(1L)
//                         .createdAt(LocalDateTime.now())
//                         .build();
//
//         FeedDto.ListResponse feed_2 =
//                 FeedDto.ListResponse.builder()
//                         .feedId(2L)
//                         .title("피드 제목 2")
//                         .commentCount(2)
//                         .userId("testuser1")
//                         .categories(List.of(CategoryType.CULTURE))
//                         .likeCount(2L)
//                         .viewCount(2L)
//                         .createdAt(LocalDateTime.now())
//                         .build();
//
//         PageImpl<FeedDto.ListResponse> responses = new PageImpl<>(List.of(feed_2, feed_1));
//
//         given(userService.findUserFeeds(anyInt())).willReturn(new PageImpl<>(new ArrayList<>()));
//         given(feedMapper.myFeedsToPageResponse(Mockito.any())).willReturn(responses);
//
//         ResultActions actions =
//                 mockMvc.perform(
//                         RestDocumentationRequestBuilders.get("/mypage/myfeeds?page={page}", page)
//                                 .accept(MediaType.APPLICATION_JSON)
//                 );
//         actions
//                 .andExpect(status().isOk())
//                 .andDo(document(
//                         "Get_User_Feeds",
//                         getDocumentRequest(),
//                         getDocumentResponse(),
//                         requestParameters(
//                                 parameterWithName("page").description("페이지 번호")
//                         ),
//                         responseFields(
//                                 List.of(
//                                         fieldWithPath("data.").type(JsonFieldType.OBJECT).description("결과 데이터"),
//                                         fieldWithPath("data.content[].feedId").type(JsonFieldType.NUMBER).description("피드 번호"),
//                                         fieldWithPath("data.content[].userId").type(JsonFieldType.STRING).description("작성자 아이디"),
//                                         fieldWithPath("data.content[].title").type(JsonFieldType.STRING).description("피드 제목"),
//                                         fieldWithPath("data.content[].commentCount").type(JsonFieldType.NUMBER).description("댓글 개수"),
//                                         fieldWithPath("data.content[].categories[]").type(JsonFieldType.ARRAY).description("피드 카테고리"),
//                                         fieldWithPath("data.content[].likeCount").type(JsonFieldType.NUMBER).description("피드 좋아요"),
//                                         fieldWithPath("data.content[].viewCount").type(JsonFieldType.NUMBER).description("피드 조회수"),
//                                         fieldWithPath("data.content[].createdAt").type(JsonFieldType.STRING).description("작성 날짜"),
//                                         fieldWithPath("data.pageable").type(JsonFieldType.STRING).description("Pageble 설정"),
//                                         fieldWithPath("data.size").type(JsonFieldType.NUMBER).description("페이지 크기"),
//                                         fieldWithPath("data.number").type(JsonFieldType.NUMBER).description("페이지 번호"),
//                                         fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수"),
//                                         fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER).description("총 피드 수"),
//                                         fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN).description("Pageble Empty"),
//                                         fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN).description("Pageble Unsorted"),
//                                         fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN).description("Pageble Sorted"),
//                                         fieldWithPath("data.first").type(JsonFieldType.BOOLEAN).description("Pageble First"),
//                                         fieldWithPath("data.last").type(JsonFieldType.BOOLEAN).description("Pageble Last"),
//                                         fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER).description("페이지 요소 개수"),
//                                         fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN).description("페이지 Empty 여부")
//                                 )
//                         )));
//
//     }
//
//     @Test
//     void getUserCommentsTest() throws Exception {
//
//         int page = 1;
//
//         CommentDto.ListResponse comment_1 =
//                 CommentDto.ListResponse.builder()
//                         .commentId(1L)
//                         .feedId(1L)
//                         .body("댓글 내용")
//                         .likeCount(1L)
//                         .createdAt(LocalDateTime.now())
//                         .build();
//
//         CommentDto.ListResponse comment_2 =
//                 CommentDto.ListResponse.builder()
//                         .commentId(2L)
//                         .feedId(2L)
//                         .body("댓글 내용")
//                         .likeCount(2L)
//                         .createdAt(LocalDateTime.now())
//                         .build();
//
//         PageImpl<CommentDto.ListResponse> responses = new PageImpl<>(List.of(comment_2, comment_1));
//
//         given(userService.findUserComments(anyInt())).willReturn(new PageImpl<>(new ArrayList<>()));
//         given(commentMapper.myCommentsToPageResponse(Mockito.any())).willReturn(responses);
//
//         ResultActions actions =
//                 mockMvc.perform(
//                         RestDocumentationRequestBuilders.get("/mypage/mycomments?page={page}", page)
//                                 .accept(MediaType.APPLICATION_JSON)
//                 );
//         actions
//                 .andExpect(status().isOk())
//                 .andDo(document(
//                         "Get_User_Comments",
//                         getDocumentRequest(),
//                         getDocumentResponse(),
//                         requestParameters(
//                                 parameterWithName("page").description("페이지 번호")
//                         ),
//                         responseFields(
//                                 List.of(
//                                         fieldWithPath("data.").type(JsonFieldType.OBJECT).description("결과 데이터"),
//                                         fieldWithPath("data.content[].commentId").type(JsonFieldType.NUMBER).description("댓글 번호"),
//                                         fieldWithPath("data.content[].feedId").type(JsonFieldType.NUMBER).description("피드 번호"),
//                                         fieldWithPath("data.content[].body").type(JsonFieldType.STRING).description("댓글 내용"),
//                                         fieldWithPath("data.content[].likeCount").type(JsonFieldType.NUMBER).description("피드 좋아요"),
//                                         fieldWithPath("data.content[].createdAt").type(JsonFieldType.STRING).description("작성 날짜"),
//                                         fieldWithPath("data.pageable").type(JsonFieldType.STRING).description("Pageble 설정"),
//                                         fieldWithPath("data.size").type(JsonFieldType.NUMBER).description("페이지 크기"),
//                                         fieldWithPath("data.number").type(JsonFieldType.NUMBER).description("페이지 번호"),
//                                         fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수"),
//                                         fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER).description("총 피드 수"),
//                                         fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN).description("Pageble Empty"),
//                                         fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN).description("Pageble Unsorted"),
//                                         fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN).description("Pageble Sorted"),
//                                         fieldWithPath("data.first").type(JsonFieldType.BOOLEAN).description("Pageble First"),
//                                         fieldWithPath("data.last").type(JsonFieldType.BOOLEAN).description("Pageble Last"),
//                                         fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER).description("페이지 요소 개수"),
//                                         fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN).description("페이지 Empty 여부")
//                                 )
//                         )));
//
//     }
//
//     @Test
//     void getUserQuestionsTest() throws Exception {
//
//         int page = 1;
//
//         QuestionDto.AnsweredResponse question_1 =
//                 QuestionDto.AnsweredResponse.builder()
//                         .questionId(1L)
//                         .title("질문 제목1")
//                         .body("질문 내용1")
//                         .imagePath("이미지 주소1")
//                         .answerBody("정답1")
//                         .userAnswer("사용자 답변1")
//                         .build();
//
//         QuestionDto.AnsweredResponse question_2 =
//                 QuestionDto.AnsweredResponse.builder()
//                         .questionId(2L)
//                         .title("질문 제목2")
//                         .body("질문 내용2")
//                         .imagePath("이미지 주소2")
//                         .answerBody("정답2")
//                         .userAnswer("사용자 답변2")
//                         .build();
//
//         PageImpl<QuestionDto.AnsweredResponse> responses = new PageImpl<>(List.of(question_2, question_1));
//
//         given(userService.findUserQuestions(anyInt())).willReturn(new PageImpl<>(new ArrayList<>()));
//         given(questionMapper.myQuestionsToPageResponse(Mockito.any())).willReturn(responses);
//
//         ResultActions actions =
//                 mockMvc.perform(
//                         RestDocumentationRequestBuilders.get("/mypage/myquestions?page={page}", page)
//                                 .accept(MediaType.APPLICATION_JSON)
//                 );
//         actions
//                 .andExpect(status().isOk())
//                 .andDo(document(
//                         "Get_User_Questions",
//                         getDocumentRequest(),
//                         getDocumentResponse(),
//                         requestParameters(
//                                 parameterWithName("page").description("페이지 번호")
//                         ),
//                         responseFields(
//                                 List.of(
//                                         fieldWithPath("data.").type(JsonFieldType.OBJECT).description("결과 데이터"),
//                                         fieldWithPath("data.content[].questionId").type(JsonFieldType.NUMBER).description("질문 번호"),
//                                         fieldWithPath("data.content[].title").type(JsonFieldType.STRING).description("질문 제목"),
//                                         fieldWithPath("data.content[].body").type(JsonFieldType.STRING).description("질문 내용"),
//                                         fieldWithPath("data.content[].imagePath").type(JsonFieldType.STRING).description("이미지 주소"),
//                                         fieldWithPath("data.content[].answerBody").type(JsonFieldType.STRING).description("정답"),
//                                         fieldWithPath("data.content[].userAnswer").type(JsonFieldType.STRING).description("사용자 답변"),
//                                         fieldWithPath("data.pageable").type(JsonFieldType.STRING).description("Pageble 설정"),
//                                         fieldWithPath("data.size").type(JsonFieldType.NUMBER).description("페이지 크기"),
//                                         fieldWithPath("data.number").type(JsonFieldType.NUMBER).description("페이지 번호"),
//                                         fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수"),
//                                         fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER).description("총 피드 수"),
//                                         fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN).description("Pageble Empty"),
//                                         fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN).description("Pageble Unsorted"),
//                                         fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN).description("Pageble Sorted"),
//                                         fieldWithPath("data.first").type(JsonFieldType.BOOLEAN).description("Pageble First"),
//                                         fieldWithPath("data.last").type(JsonFieldType.BOOLEAN).description("Pageble Last"),
//                                         fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER).description("페이지 요소 개수"),
//                                         fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN).description("페이지 Empty 여부")
//                                 )
//                         )));
//     }
//
//     @Test
//     void deleteUserTest() throws Exception {
//
//         given(userService.deleteUser()).willReturn(true);
//
//         mockMvc.perform(
//                         RestDocumentationRequestBuilders.delete("/mypage/signout")
//                 )
//                 .andExpect(status().isNoContent())
//                 .andDo(
//                         document(
//                                 "Delete_User",
//                                 getDocumentRequest(),
//                                 getDocumentResponse()
//                         )
//                 );
//     }
//
// }
