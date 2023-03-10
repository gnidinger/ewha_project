// package com.ewha.back.Controller;
//
// import com.ewha.back.domain.category.entity.CategoryType;
// import com.ewha.back.domain.comment.dto.CommentDto;
// import com.ewha.back.domain.comment.entity.Comment;
// import com.ewha.back.domain.comment.mapper.CommentMapper;
// import com.ewha.back.domain.comment.service.CommentService;
// import com.ewha.back.domain.feed.dto.FeedDto;
// import com.ewha.back.domain.feed.entity.Feed;
// import com.ewha.back.domain.feed.mapper.FeedMapper;
// import com.ewha.back.domain.feed.service.FeedService;
// import com.ewha.back.domain.like.service.LikeService;
// import com.ewha.back.domain.user.dto.UserDto;
// import com.ewha.back.domain.user.entity.User;
// import com.ewha.back.domain.user.mapper.UserMapper;
// import com.google.gson.Gson;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.Mockito;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
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
// import static org.mockito.ArgumentMatchers.anyLong;
// import static org.mockito.BDDMockito.given;
// import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
// import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
// import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
// import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
// import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
// @Transactional
// //@SpringBootTest
// @AutoConfigureMockMvc
// @AutoConfigureRestDocs
// public class LikeControllerRestDocs {
//
//     @Autowired
//     private MockMvc mockMvc;
//     @Autowired
//     private Gson gson;
//     @MockBean
//     private LikeService likeService;
//     @MockBean
//     private FeedService feedService;
//     @MockBean
//     private CommentService commentService;
//     @MockBean
//     private UserMapper userMapper;
//     @MockBean
//     private FeedMapper feedMapper;
//     @MockBean
//     private CommentMapper commentMapper;
//
//     @BeforeEach
//     private void init() {
//
//         UserDto.PostResponse userResponse =
//                 UserDto.PostResponse.builder()
//                         .userId("testuser")
//                         .nickname("?????????")
//                         .ariFactor(36.5)
//                         .role(List.of("ROLE_USER"))
//                         .profileImage("profile Image")
//                         .build();
//
//         given(userMapper.userToUserPostResponse(Mockito.any())).willReturn(userResponse);
//     }
//
//     @Test
//     void postFeedLikeTest() throws Exception {
//
//         Long feedId = 1L;
//
//         FeedDto.Response response =
//                 FeedDto.Response.builder()
//                         .feedId(1L)
//                         .categories(List.of(CategoryType.CULTURE))
//                         .userInfo(userMapper.userToUserPostResponse(new User()))
//                         .title("?????? ??????")
//                         .body("?????? ??????")
//                         .isLiked(false)
//                         .likeCount(1L)
//                         .viewCount(1L)
//                         .imagePath("????????? ??????")
//                         .comments(new ArrayList<>())
//                         .createdAt(LocalDateTime.now())
//                         .modifiedAt(LocalDateTime.now())
//                         .build();
//
//         given(feedService.findVerifiedFeed(anyLong())).willReturn(Feed.builder().build());
//         given(likeService.createFeedLike(anyLong())).willReturn(Feed.builder().build());
//         given(feedMapper.feedToFeedResponse(Mockito.any(Feed.class))).willReturn(response);
//
//         ResultActions actions =
//                 mockMvc.perform(
//                         RestDocumentationRequestBuilders.patch("/feeds/{feed_id}/like", feedId)
//                                 .accept(MediaType.APPLICATION_JSON)
//                 );
//         actions
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.data.title").value(response.getTitle()))
//                 .andDo(document(
//                         "Post_Feed_Like",
//                         getDocumentRequest(),
//                         getDocumentResponse(),
//                         pathParameters(
//                                 parameterWithName("feed_id").description("?????? ??????")
//                         ),
//                         responseFields(
//                                 List.of(
//                                         fieldWithPath("data.").type(JsonFieldType.OBJECT).description("?????? ?????????"),
//                                         fieldWithPath("data.feedId").type(JsonFieldType.NUMBER).description("?????? ??????"),
//                                         fieldWithPath("data.categories[]").type(JsonFieldType.ARRAY).description("?????? ????????????"),
//                                         fieldWithPath("data.userInfo.userId").type(JsonFieldType.STRING).description("????????? ?????????"),
//                                         fieldWithPath("data.userInfo.nickname").type(JsonFieldType.STRING).description("????????? ?????????"),
//                                         fieldWithPath("data.userInfo.ariFactor").type(JsonFieldType.NUMBER).description("????????????"),
//                                         fieldWithPath("data.userInfo.role[]").type(JsonFieldType.ARRAY).description("????????? ??????"),
//                                         fieldWithPath("data.userInfo.profileImage").type(JsonFieldType.STRING).description("????????? ????????? ??????"),
//                                         fieldWithPath("data.title").type(JsonFieldType.STRING).description("?????? ??????"),
//                                         fieldWithPath("data.body").type(JsonFieldType.STRING).description("?????? ??????"),
//                                         fieldWithPath("data.isLiked").type(JsonFieldType.BOOLEAN).description("?????? ????????? ??????"),
//                                         fieldWithPath("data.likeCount").type(JsonFieldType.NUMBER).description("????????? ?????? ?????????"),
//                                         fieldWithPath("data.viewCount").type(JsonFieldType.NUMBER).description("?????? ?????????"),
//                                         fieldWithPath("data.imagePath").type(JsonFieldType.STRING).description("????????? ??????"),
//                                         fieldWithPath("data.comments[]").type(JsonFieldType.ARRAY).description("??????"),
//                                         fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("?????? ??????"),
//                                         fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("????????? ?????? ??????")
//                                 )
//                         )));
//     }
//
//     @Test
//     void deleteFeedLikeTest() throws Exception {
//
//         Long feedId = 1L;
//
//         FeedDto.Response response =
//                 FeedDto.Response.builder()
//                         .feedId(1L)
//                         .categories(List.of(CategoryType.CULTURE))
//                         .userInfo(userMapper.userToUserPostResponse(new User()))
//                         .title("?????? ??????")
//                         .body("?????? ??????")
//                         .isLiked(false)
//                         .likeCount(0L)
//                         .viewCount(1L)
//                         .imagePath("????????? ??????")
//                         .comments(new ArrayList<>())
//                         .createdAt(LocalDateTime.now())
//                         .modifiedAt(LocalDateTime.now())
//                         .build();
//
//         given(likeService.deleteFeedLike(anyLong())).willReturn(Feed.builder().build());
//         given(feedMapper.feedToFeedResponse(Mockito.any(Feed.class))).willReturn(response);
//
//         ResultActions actions =
//                 mockMvc.perform(
//                         RestDocumentationRequestBuilders.patch("/feeds/{feed_id}/dislike", feedId)
//                                 .accept(MediaType.APPLICATION_JSON)
//                 );
//         actions
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.data.title").value(response.getTitle()))
//                 .andDo(document(
//                         "Delete_Feed_Like",
//                         getDocumentRequest(),
//                         getDocumentResponse(),
//                         pathParameters(
//                                 parameterWithName("feed_id").description("?????? ??????")
//                         ),
//                         responseFields(
//                                 List.of(
//                                         fieldWithPath("data.").type(JsonFieldType.OBJECT).description("?????? ?????????"),
//                                         fieldWithPath("data.feedId").type(JsonFieldType.NUMBER).description("?????? ??????"),
//                                         fieldWithPath("data.categories[]").type(JsonFieldType.ARRAY).description("?????? ????????????"),
//                                         fieldWithPath("data.userInfo.userId").type(JsonFieldType.STRING).description("????????? ?????????"),
//                                         fieldWithPath("data.userInfo.nickname").type(JsonFieldType.STRING).description("????????? ?????????"),
//                                         fieldWithPath("data.userInfo.ariFactor").type(JsonFieldType.NUMBER).description("????????????"),
//                                         fieldWithPath("data.userInfo.role[]").type(JsonFieldType.ARRAY).description("????????? ??????"),
//                                         fieldWithPath("data.userInfo.profileImage").type(JsonFieldType.STRING).description("????????? ????????? ??????"),
//                                         fieldWithPath("data.title").type(JsonFieldType.STRING).description("?????? ??????"),
//                                         fieldWithPath("data.body").type(JsonFieldType.STRING).description("?????? ??????"),
//                                         fieldWithPath("data.isLiked").type(JsonFieldType.BOOLEAN).description("?????? ????????? ??????"),
//                                         fieldWithPath("data.likeCount").type(JsonFieldType.NUMBER).description("????????? ?????? ?????????"),
//                                         fieldWithPath("data.viewCount").type(JsonFieldType.NUMBER).description("?????? ?????????"),
//                                         fieldWithPath("data.imagePath").type(JsonFieldType.STRING).description("????????? ??????"),
//                                         fieldWithPath("data.comments[]").type(JsonFieldType.ARRAY).description("??????"),
//                                         fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("?????? ??????"),
//                                         fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("????????? ?????? ??????")
//                                 )
//                         )));
//     }
//
//     @Test
//     void postCommentLikeTest() throws Exception {
//
//         Long commentId = 1L;
//
//         CommentDto.Response response =
//                 CommentDto.Response.builder()
//                         .commentId(1L)
//                         .feedId(1L)
//                         .userInfo(userMapper.userToUserPostResponse(new User()))
//                         .body("?????? ??????")
//                         .likeCount(1L)
//                         .createdAt(LocalDateTime.now())
//                         .modifiedAt(LocalDateTime.now())
//                         .build();
//
//         given(commentService.findVerifiedComment(anyLong())).willReturn(Comment.builder().build());
//         given(likeService.createCommentLike(anyLong())).willReturn(Comment.builder().build());
//         given(commentMapper.commentToCommentResponse(Mockito.any(Comment.class))).willReturn(response);
//
//         ResultActions actions =
//                 mockMvc.perform(
//                         RestDocumentationRequestBuilders.patch("/comments/{comment_id}/like", commentId)
//                                 .accept(MediaType.APPLICATION_JSON)
//                 );
//         actions
//                 .andExpect(status().isOk())
//                 .andDo(document(
//                         "Post_Comment_Like",
//                         getDocumentRequest(),
//                         getDocumentResponse(),
//                         pathParameters(
//                                 parameterWithName("comment_id").description("?????? ??????")
//                         ),
//                         responseFields(
//                                 List.of(
//                                         fieldWithPath("data.").type(JsonFieldType.OBJECT).description("?????? ?????????"),
//                                         fieldWithPath("data.commentId").type(JsonFieldType.NUMBER).description("?????? ??????"),
//                                         fieldWithPath("data.feedId").type(JsonFieldType.NUMBER).description("?????? ??????"),
//                                         fieldWithPath("data.userInfo.userId").type(JsonFieldType.STRING).description("????????? ?????????"),
//                                         fieldWithPath("data.userInfo.nickname").type(JsonFieldType.STRING).description("????????? ?????????"),
//                                         fieldWithPath("data.userInfo.ariFactor").type(JsonFieldType.NUMBER).description("????????????"),
//                                         fieldWithPath("data.userInfo.role[]").type(JsonFieldType.ARRAY).description("????????? ??????"),
//                                         fieldWithPath("data.userInfo.profileImage").type(JsonFieldType.STRING).description("????????? ????????? ??????"),
//                                         fieldWithPath("data.body").type(JsonFieldType.STRING).description("?????? ??????"),
//                                         fieldWithPath("data.likeCount").type(JsonFieldType.NUMBER).description("????????? ?????? ?????????"),
//                                         fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("?????? ??????"),
//                                         fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("????????? ?????? ??????")
//                                 )
//                         )));
//     }
//
//     @Test
//     void deleteCommentLikeTest() throws Exception {
//
//         Long commentId = 1L;
//
//         CommentDto.Response response =
//                 CommentDto.Response.builder()
//                         .commentId(1L)
//                         .feedId(1L)
//                         .userInfo(userMapper.userToUserPostResponse(new User()))
//                         .body("?????? ??????")
//                         .likeCount(0L)
//                         .createdAt(LocalDateTime.now())
//                         .modifiedAt(LocalDateTime.now())
//                         .build();
//
//         given(likeService.deleteCommentLike(anyLong())).willReturn(Comment.builder().build());
//         given(commentMapper.commentToCommentResponse(Mockito.any(Comment.class))).willReturn(response);
//
//         ResultActions actions =
//                 mockMvc.perform(
//                         RestDocumentationRequestBuilders.patch("/comments/{comment_id}/dislike", commentId)
//                                 .accept(MediaType.APPLICATION_JSON)
//                 );
//         actions
//                 .andExpect(status().isOk())
//                 .andDo(document(
//                         "Delete_Comment_Like",
//                         getDocumentRequest(),
//                         getDocumentResponse(),
//                         pathParameters(
//                                 parameterWithName("comment_id").description("?????? ??????")
//                         ),
//                         responseFields(
//                                 List.of(
//                                         fieldWithPath("data.").type(JsonFieldType.OBJECT).description("?????? ?????????"),
//                                         fieldWithPath("data.commentId").type(JsonFieldType.NUMBER).description("?????? ??????"),
//                                         fieldWithPath("data.feedId").type(JsonFieldType.NUMBER).description("?????? ??????"),
//                                         fieldWithPath("data.userInfo.userId").type(JsonFieldType.STRING).description("????????? ?????????"),
//                                         fieldWithPath("data.userInfo.nickname").type(JsonFieldType.STRING).description("????????? ?????????"),
//                                         fieldWithPath("data.userInfo.ariFactor").type(JsonFieldType.NUMBER).description("????????????"),
//                                         fieldWithPath("data.userInfo.role[]").type(JsonFieldType.ARRAY).description("????????? ??????"),
//                                         fieldWithPath("data.userInfo.profileImage").type(JsonFieldType.STRING).description("????????? ????????? ??????"),
//                                         fieldWithPath("data.body").type(JsonFieldType.STRING).description("?????? ??????"),
//                                         fieldWithPath("data.likeCount").type(JsonFieldType.NUMBER).description("????????? ?????? ?????????"),
//                                         fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("?????? ??????"),
//                                         fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("????????? ?????? ??????")
//                                 )
//                         )));
//     }
// }
