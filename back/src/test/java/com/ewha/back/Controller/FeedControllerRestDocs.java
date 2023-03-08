// package com.ewha.back.Controller;
//
// import com.ewha.back.domain.category.dto.CategoryDto;
// import com.ewha.back.domain.category.entity.CategoryType;
// import com.ewha.back.domain.feed.dto.FeedDto;
// import com.ewha.back.domain.feed.entity.Feed;
// import com.ewha.back.domain.feed.mapper.FeedMapper;
// import com.ewha.back.domain.feed.service.FeedService;
// import com.ewha.back.domain.user.dto.UserDto;
// import com.ewha.back.domain.user.entity.User;
// import com.ewha.back.domain.user.mapper.UserMapper;
// import com.ewha.back.global.security.jwtTokenizer.JwtTokenizer;
// import com.google.gson.Gson;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
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
// import javax.servlet.http.HttpServletRequest;
// import java.time.LocalDateTime;
// import java.util.ArrayList;
// import java.util.List;
//
// import static com.ewha.back.Controller.utils.ApiDocumentUtils.getDocumentRequest;
// import static com.ewha.back.Controller.utils.ApiDocumentUtils.getDocumentResponse;
// import static org.mockito.ArgumentMatchers.*;
// import static org.mockito.BDDMockito.given;
// import static org.mockito.Mockito.doNothing;
// import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
// import static org.springframework.restdocs.payload.PayloadDocumentation.*;
// import static org.springframework.restdocs.request.RequestDocumentation.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
// import org.springframework.web.bind.annotation.RequestPart;
//
// @Transactional
// @SpringBootTest
// @AutoConfigureMockMvc
// @AutoConfigureRestDocs
// public class FeedControllerRestDocs {
//
//     @Autowired
//     private MockMvc mockMvc;
//     @Autowired
//     private Gson gson;
//     @MockBean
//     private UserMapper userMapper;
//     @MockBean
//     private FeedMapper feedMapper;
//     @MockBean
//     private FeedService feedService;
//     @MockBean
//     private JwtTokenizer jwtTokenizer;
//
//     @BeforeEach
//     private void init() {
//
//         UserDto.PostResponse userResponse =
//                 UserDto.PostResponse.builder()
//                         .userId("testuser")
//                         .nickname("닉네임")
//                         .ariFactor(36.5)
//                         .role(List.of("ROLE_USER"))
//                         .profileImage("profile Image")
//                         .build();
//
//         given(userMapper.userToUserPostResponse(Mockito.any())).willReturn(userResponse);
//     }
//
//     @Test
//     void postFeedTest() throws Exception {
//
//         CategoryDto.Response category =
//                 CategoryDto.Response.builder()
//                         .categoryType(CategoryType.CULTURE)
//                         .build();
//
//         FeedDto.Post post =
//                 FeedDto.Post.builder()
//                         .title("피드 제목")
//                         .body("피드 내용")
//                         .categories(List.of(category))
//                         .build();
//
//         String content = gson.toJson(post);
//
//         FeedDto.Response response =
//                 FeedDto.Response.builder()
//                         .feedId(1L)
//                         .categories(List.of(CategoryType.CULTURE))
//                         .userInfo(userMapper.userToUserPostResponse(new User()))
//                         .title("피드 제목")
//                         .body("피드 내용")
//                         .isLiked(false)
//                         .likeCount(0L)
//                         .viewCount(1L)
//                         .imagePath("이미지 주소")
//                         .comments(new ArrayList<>())
//                         .createdAt(LocalDateTime.now())
//                         .modifiedAt(LocalDateTime.now())
//                         .build();
//
//         given(feedMapper.feedPostToFeed(Mockito.any(FeedDto.Post.class))).willReturn(Feed.builder().build());
//         given(feedService.createFeed(Mockito.any(Feed.class))).willReturn(Feed.builder().build());
//         given(feedMapper.feedToFeedResponse(Mockito.any(Feed.class))).willReturn(response);
//
//         ResultActions actions =
//                 mockMvc.perform(
//                         RestDocumentationRequestBuilders.post("/feeds/add")
//                                 .param("postFeed", post)
//                                 .accept(MediaType.APPLICATION_JSON)
//                                 .contentType(MediaType.APPLICATION_JSON)
//                                 .content(content)
//                 );
//
//         actions
//                 .andExpect(status().isCreated())
//                 .andExpect(jsonPath("$.data.title").value(post.getTitle()))
//                 .andExpect(jsonPath("$.data.body").value(post.getBody()))
//                 .andDo(document(
//                         "Post_Feed",
//                         getDocumentRequest(),
//                         getDocumentResponse(),
//                         requestFields(
//                                 List.of(
//                                         fieldWithPath("title").type(JsonFieldType.STRING).description("피드 제목"),
//                                         fieldWithPath("body").type(JsonFieldType.STRING).description("피드 내용"),
//                                         fieldWithPath("categories[]").type(JsonFieldType.ARRAY).description("피드 카테고리"),
//                                         fieldWithPath("categories[].categoryType").type(JsonFieldType.STRING).description("카테고리 타입")
//                                 )
//                         ),
//                         responseFields(
//                                 List.of(
//                                         fieldWithPath("data.").type(JsonFieldType.OBJECT).description("결과 데이터"),
//                                         fieldWithPath("data.feedId").type(JsonFieldType.NUMBER).description("피드 번호"),
//                                         fieldWithPath("data.categories[]").type(JsonFieldType.ARRAY).description("피드 카테고리"),
//                                         fieldWithPath("data.userInfo.userId").type(JsonFieldType.STRING).description("작성자 아이디"),
//                                         fieldWithPath("data.userInfo.nickname").type(JsonFieldType.STRING).description("작성자 닉네임"),
//                                         fieldWithPath("data.userInfo.ariFactor").type(JsonFieldType.NUMBER).description("아리지수"),
//                                         fieldWithPath("data.userInfo.role[]").type(JsonFieldType.ARRAY).description("작성자 역할"),
//                                         fieldWithPath("data.userInfo.profileImage").type(JsonFieldType.STRING).description("작성자 프로필 사진"),
//                                         fieldWithPath("data.title").type(JsonFieldType.STRING).description("피드 제목"),
//                                         fieldWithPath("data.body").type(JsonFieldType.STRING).description("피드 내용"),
//                                         fieldWithPath("data.isLiked").type(JsonFieldType.BOOLEAN).description("피드 좋아요 여부"),
//                                         fieldWithPath("data.likeCount").type(JsonFieldType.NUMBER).description("피드 좋아요"),
//                                         fieldWithPath("data.viewCount").type(JsonFieldType.NUMBER).description("피드 조회수"),
//                                         fieldWithPath("data.imagePath").type(JsonFieldType.STRING).description("이미지 주소"),
//                                         fieldWithPath("data.comments[]").type(JsonFieldType.ARRAY).description("댓글"),
//                                         fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("작성 날짜"),
//                                         fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("마지막 수정 날짜")
//                                 )
//                         )));
//
//     }
//
//     @Test
//     void patchFeedTest() throws Exception {
//
//         Long feedId = 1L;
//
//         CategoryDto.Response category =
//                 CategoryDto.Response.builder()
//                         .categoryType(CategoryType.CULTURE)
//                         .build();
//
//         FeedDto.Patch patch =
//                 FeedDto.Patch.builder()
//                         .feedId(1L)
//                         .title("수정된 제목")
//                         .categories(List.of(category))
//                         .body("수정된 내용")
//                         .imagePath("이미지 주소")
//                         .build();
//
//         String content = gson.toJson(patch);
//
//         FeedDto.Response response =
//                 FeedDto.Response.builder()
//                         .feedId(1L)
//                         .categories(List.of(CategoryType.CULTURE))
//                         .userInfo(userMapper.userToUserPostResponse(new User()))
//                         .title("수정된 제목")
//                         .body("수정된 내용")
//                         .isLiked(false)
//                         .likeCount(0L)
//                         .viewCount(1L)
//                         .imagePath("이미지 주소")
//                         .comments(new ArrayList<>())
//                         .createdAt(LocalDateTime.now())
//                         .modifiedAt(LocalDateTime.now())
//                         .build();
//
//         given(feedMapper.feedPatchToFeed(Mockito.any(FeedDto.Patch.class))).willReturn(Feed.builder().build());
//         given(feedService.updateFeed(Mockito.any(Feed.class), anyLong())).willReturn(Feed.builder().build());
//         given(feedMapper.feedToFeedResponse(Mockito.any(Feed.class))).willReturn(response);
//
//         ResultActions actions =
//                 mockMvc.perform(
//                         RestDocumentationRequestBuilders.patch("/feeds/{feed_id}/edit", feedId)
//                                 .accept(MediaType.APPLICATION_JSON)
//                                 .contentType(MediaType.APPLICATION_JSON)
//                                 .content(content)
//                 );
//
//         actions
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.data.title").value(patch.getTitle()))
//                 .andDo(document(
//                         "Patch_Feed",
//                         getDocumentRequest(),
//                         getDocumentResponse(),
//                         pathParameters(
//                                 parameterWithName("feed_id").description("피드 번호")
//                         ),
//                         requestFields(
//                                 List.of(
//                                         fieldWithPath("feedId").type(JsonFieldType.NUMBER).description("피드 번호"),
//                                         fieldWithPath("title").type(JsonFieldType.STRING).description("수정된 제목"),
//                                         fieldWithPath("body").type(JsonFieldType.STRING).description("수정된 내용"),
//                                         fieldWithPath("categories[]").type(JsonFieldType.ARRAY).description("피드 카테고리"),
//                                         fieldWithPath("categories[].categoryType").type(JsonFieldType.STRING).description("카테고리 타입"),
//                                         fieldWithPath("imagePath").type(JsonFieldType.STRING).description("이미지 주소")
//                                 )
//                         ),
//                         responseFields(
//                                 List.of(
//                                         fieldWithPath("data.").type(JsonFieldType.OBJECT).description("결과 데이터"),
//                                         fieldWithPath("data.feedId").type(JsonFieldType.NUMBER).description("피드 번호"),
//                                         fieldWithPath("data.categories[]").type(JsonFieldType.ARRAY).description("피드 카테고리"),
//                                         fieldWithPath("data.userInfo.userId").type(JsonFieldType.STRING).description("작성자 아이디"),
//                                         fieldWithPath("data.userInfo.nickname").type(JsonFieldType.STRING).description("작성자 닉네임"),
//                                         fieldWithPath("data.userInfo.ariFactor").type(JsonFieldType.NUMBER).description("아리지수"),
//                                         fieldWithPath("data.userInfo.role[]").type(JsonFieldType.ARRAY).description("작성자 역할"),
//                                         fieldWithPath("data.userInfo.profileImage").type(JsonFieldType.STRING).description("작성자 프로필 사진"),
//                                         fieldWithPath("data.title").type(JsonFieldType.STRING).description("수정된 제목"),
//                                         fieldWithPath("data.body").type(JsonFieldType.STRING).description("수정된 내용"),
//                                         fieldWithPath("data.isLiked").type(JsonFieldType.BOOLEAN).description("피드 좋아요 여부"),
//                                         fieldWithPath("data.likeCount").type(JsonFieldType.NUMBER).description("피드 좋아요"),
//                                         fieldWithPath("data.viewCount").type(JsonFieldType.NUMBER).description("피드 조회수"),
//                                         fieldWithPath("data.imagePath").type(JsonFieldType.STRING).description("이미지 주소"),
//                                         fieldWithPath("data.comments[]").type(JsonFieldType.ARRAY).description("댓글"),
//                                         fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("작성 날짜"),
//                                         fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("마지막 수정 날짜")
//                                 )
//                         )));
//     }
//
//     @Test
//     void getFeedWithAuthTest() throws Exception {
//
//         Long feedId = 1L;
//
//         FeedDto.Response response =
//                 FeedDto.Response.builder()
//                         .feedId(1L)
//                         .categories(List.of(CategoryType.CULTURE))
//                         .userInfo(userMapper.userToUserPostResponse(new User()))
//                         .title("피드 제목")
//                         .body("피드 내용")
//                         .isLiked(false)
//                         .likeCount(0L)
//                         .viewCount(1L)
//                         .imagePath("이미지 주소")
//                         .comments(new ArrayList<>())
//                         .createdAt(LocalDateTime.now())
//                         .modifiedAt(LocalDateTime.now())
//                         .build();
//
//         given(jwtTokenizer.checkUserWithToken(Mockito.any(HttpServletRequest.class), anyString())).willReturn(true);
//         given(feedService.updateView(anyLong())).willReturn(Feed.builder().build());
//         given(feedService.isLikedComments(anyLong())).willReturn(Feed.builder().build());
//         given(feedMapper.feedToFeedResponse(Mockito.any(Feed.class))).willReturn(response);
//
//         ResultActions actions =
//                 mockMvc.perform(
//                         RestDocumentationRequestBuilders.get("/feeds/{feed_id}", feedId)
//                                 .accept(MediaType.APPLICATION_JSON)
//                 );
//
//         actions
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.data.title").value(response.getTitle()))
//                 .andDo(document(
//                         "Get_Feed_With_Auth",
//                         getDocumentRequest(),
//                         getDocumentResponse(),
//                         pathParameters(
//                                 parameterWithName("feed_id").description("피드 번호")
//                         ),
//                         responseFields(
//                                 List.of(
//                                         fieldWithPath("data.").type(JsonFieldType.OBJECT).description("결과 데이터"),
//                                         fieldWithPath("data.feedId").type(JsonFieldType.NUMBER).description("피드 번호"),
//                                         fieldWithPath("data.categories[]").type(JsonFieldType.ARRAY).description("피드 카테고리"),
//                                         fieldWithPath("data.userInfo.userId").type(JsonFieldType.STRING).description("작성자 아이디"),
//                                         fieldWithPath("data.userInfo.nickname").type(JsonFieldType.STRING).description("작성자 닉네임"),
//                                         fieldWithPath("data.userInfo.ariFactor").type(JsonFieldType.NUMBER).description("아리지수"),
//                                         fieldWithPath("data.userInfo.role[]").type(JsonFieldType.ARRAY).description("작성자 역할"),
//                                         fieldWithPath("data.userInfo.profileImage").type(JsonFieldType.STRING).description("작성자 프로필 사진"),
//                                         fieldWithPath("data.title").type(JsonFieldType.STRING).description("피드 제목"),
//                                         fieldWithPath("data.body").type(JsonFieldType.STRING).description("피드 내용"),
//                                         fieldWithPath("data.isLiked").type(JsonFieldType.BOOLEAN).description("피드 좋아요 여부"),
//                                         fieldWithPath("data.likeCount").type(JsonFieldType.NUMBER).description("피드 좋아요"),
//                                         fieldWithPath("data.viewCount").type(JsonFieldType.NUMBER).description("피드 조회수"),
//                                         fieldWithPath("data.imagePath").type(JsonFieldType.STRING).description("이미지 주소"),
//                                         fieldWithPath("data.comments[]").type(JsonFieldType.ARRAY).description("댓글"),
//                                         fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("작성 날짜"),
//                                         fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("마지막 수정 날짜")
//                                 )
//                         )));
//     }
//
//     @Test
//     @DisplayName("로그인 하지 않은 유저의 경우 피드, 코멘트 좋아요 여부를 null로 반환한다.")
//     void getFeedWithoutAuthTest() throws Exception {
//
//         Long feedId = 1L;
//
//         FeedDto.Response response =
//                 FeedDto.Response.builder()
//                         .feedId(1L)
//                         .categories(List.of(CategoryType.CULTURE))
//                         .userInfo(userMapper.userToUserPostResponse(new User()))
//                         .title("피드 제목")
//                         .body("피드 내용")
//                         .isLiked(null)
//                         .likeCount(0L)
//                         .viewCount(1L)
//                         .imagePath("이미지 주소")
//                         .comments(new ArrayList<>())
//                         .createdAt(LocalDateTime.now())
//                         .modifiedAt(LocalDateTime.now())
//                         .build();
//
//         given(jwtTokenizer.checkUserWithToken(Mockito.any(HttpServletRequest.class), anyString())).willReturn(false);
//         given(feedService.updateView(anyLong())).willReturn(Feed.builder().build());
//         given(feedService.isLikedComments(anyLong())).willReturn(Feed.builder().build());
//         given(feedMapper.feedToFeedResponse(Mockito.any(Feed.class))).willReturn(response);
//
//         ResultActions actions =
//                 mockMvc.perform(
//                         RestDocumentationRequestBuilders.get("/feeds/{feed_id}", feedId)
//                                 .accept(MediaType.APPLICATION_JSON)
//                 );
//
//         actions
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.data.title").value(response.getTitle()))
//                 .andDo(document(
//                         "Get_Feed_Without_Auth",
//                         getDocumentRequest(),
//                         getDocumentResponse(),
//                         pathParameters(
//                                 parameterWithName("feed_id").description("피드 번호")
//                         ),
//                         responseFields(
//                                 List.of(
//                                         fieldWithPath("data.").type(JsonFieldType.OBJECT).description("결과 데이터"),
//                                         fieldWithPath("data.feedId").type(JsonFieldType.NUMBER).description("피드 번호"),
//                                         fieldWithPath("data.categories[]").type(JsonFieldType.ARRAY).description("피드 카테고리"),
//                                         fieldWithPath("data.userInfo.userId").type(JsonFieldType.STRING).description("작성자 아이디"),
//                                         fieldWithPath("data.userInfo.nickname").type(JsonFieldType.STRING).description("작성자 닉네임"),
//                                         fieldWithPath("data.userInfo.ariFactor").type(JsonFieldType.NUMBER).description("아리지수"),
//                                         fieldWithPath("data.userInfo.role[]").type(JsonFieldType.ARRAY).description("작성자 역할"),
//                                         fieldWithPath("data.userInfo.profileImage").type(JsonFieldType.STRING).description("작성자 프로필 사진"),
//                                         fieldWithPath("data.title").type(JsonFieldType.STRING).description("피드 제목"),
//                                         fieldWithPath("data.body").type(JsonFieldType.STRING).description("피드 내용"),
//                                         fieldWithPath("data.isLiked").type(JsonFieldType.NULL).description("피드 좋아요 여부"),
//                                         fieldWithPath("data.likeCount").type(JsonFieldType.NUMBER).description("피드 좋아요"),
//                                         fieldWithPath("data.viewCount").type(JsonFieldType.NUMBER).description("피드 조회수"),
//                                         fieldWithPath("data.imagePath").type(JsonFieldType.STRING).description("이미지 주소"),
//                                         fieldWithPath("data.comments[]").type(JsonFieldType.ARRAY).description("댓글"),
//                                         fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("작성 날짜"),
//                                         fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("마지막 수정 날짜")
//                                 )
//                         )));
//     }
//
//     @Test
//     void getNewestFeedsTest() throws Exception {
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
//                         .userId("testuser2")
//                         .categories(List.of(CategoryType.CULTURE))
//                         .likeCount(2L)
//                         .viewCount(2L)
//                         .createdAt(LocalDateTime.now())
//                         .build();
//
//         PageImpl<FeedDto.ListResponse> responses = new PageImpl<>(List.of(feed_2, feed_1));
//
//         given(feedService.findNewestFeeds(anyInt())).willReturn(new PageImpl<>(new ArrayList<>()));
//         given(feedMapper.newFeedsToPageResponse(Mockito.any())).willReturn(responses);
//
//         ResultActions actions =
//                 mockMvc.perform(
//                         RestDocumentationRequestBuilders.get("/feeds/newest?page={page}", page)
//                                 .accept(MediaType.APPLICATION_JSON)
//                 );
//
//         actions
//                 .andExpect(status().isOk())
//                 .andDo(document(
//                         "Get_Newest_Feeds",
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
//     void getCategoryFeedsTest() throws Exception {
//
//         String categoryName = "CULTURE";
//         int page = 1;
//
//         FeedDto.ListResponse feed_1 =
//                 FeedDto.ListResponse.builder()
//                         .feedId(1L)
//                         .title("피드 제목 1")
//                         .commentCount(1)
//                         .userId("testuser1")
//                         .categories(List.of(CategoryType.CULTURE))
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
//                         .userId("testuser2")
//                         .categories(List.of(CategoryType.CULTURE))
//                         .likeCount(2L)
//                         .viewCount(2L)
//                         .createdAt(LocalDateTime.now())
//                         .build();
//
//         PageImpl<FeedDto.ListResponse> responses = new PageImpl<>(List.of(feed_2, feed_1));
//
//         given(feedService.findNewestFeeds(anyInt())).willReturn(new PageImpl<>(new ArrayList<>()));
//         given(feedMapper.newFeedsToPageResponse(Mockito.any())).willReturn(responses);
//
//         ResultActions actions =
//                 mockMvc.perform(
//                         RestDocumentationRequestBuilders.get("/feeds/newest?category={categoryName}&&page={page}", categoryName, page)
//                                 .accept(MediaType.APPLICATION_JSON)
//                 );
//         actions
//                 .andExpect(status().isOk())
//                 .andDo(document(
//                         "Get_Category_Feeds",
//                         getDocumentRequest(),
//                         getDocumentResponse(),
//                         requestParameters(
//                                 parameterWithName("category").description("피드 카테고리"),
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
//     void deleteFeedTest() throws Exception {
//
//         Long feedId = 1L;
//
//         doNothing().when(feedService).deleteFeed(anyLong());
//
//         mockMvc.perform(
//                 RestDocumentationRequestBuilders.delete("/feeds/{feed_id}/delete", feedId)
//         )
//                 .andExpect(status().isNoContent())
//                 .andDo(
//                         document(
//                                 "Delete_Feed",
//                                 getDocumentRequest(),
//                                 getDocumentResponse(),
//                                 pathParameters(
//                                         parameterWithName("feed_id").description("피드 번호")
//                                 )
//                         )
//                 );
//     }
// }
