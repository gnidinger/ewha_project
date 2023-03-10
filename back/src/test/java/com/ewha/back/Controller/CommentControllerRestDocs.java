// package com.ewha.back.Controller;
//
//
// import com.ewha.back.domain.comment.dto.CommentDto;
// import com.ewha.back.domain.comment.entity.Comment;
// import com.ewha.back.domain.comment.mapper.CommentMapper;
// import com.ewha.back.domain.comment.service.CommentService;
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
// import static org.mockito.Mockito.doNothing;
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
// public class CommentControllerRestDocs {
//
//     @Autowired
//     private MockMvc mockMvc;
//     @Autowired
//     private Gson gson;
//     @MockBean
//     private UserMapper userMapper;
//     @MockBean
//     private CommentMapper commentMapper;
//     @MockBean
//     private CommentService commentService;
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
//     void postCommentTest() throws Exception {
//
//         Long feedId = 1L;
//
//         CommentDto.Post post =
//                 CommentDto.Post.builder()
//                         .body("?????? ??????")
//                         .build();
//
//         String content = gson.toJson(post);
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
//         given(commentMapper.commentPostToComment(Mockito.any(CommentDto.Post.class))).willReturn(Comment.builder().build());
//         given(commentService.createComment(Mockito.any(Comment.class), anyLong())).willReturn(Comment.builder().build());
//         given(commentMapper.commentToCommentResponse(Mockito.any(Comment.class))).willReturn(response);
//
//         ResultActions actions =
//                 mockMvc.perform(
//                         RestDocumentationRequestBuilders.post("/feeds/{feed_id}/comments/add", feedId)
//                                 .accept(MediaType.APPLICATION_JSON)
//                                 .contentType(MediaType.APPLICATION_JSON)
//                                 .content(content)
//                 );
//
//         actions
//                 .andExpect(status().isCreated())
//                 .andExpect(jsonPath("$.data.body").value(post.getBody()))
//                 .andDo(document(
//                         "Post_Comment",
//                         getDocumentRequest(),
//                         getDocumentResponse(),
//                         pathParameters(
//                                 parameterWithName("feed_id").description("?????? ??????")
//                         ),
//                         requestFields(
//                                 List.of(
//                                         fieldWithPath("body").type(JsonFieldType.STRING).description("?????? ??????")
//                                 )
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
//                                         fieldWithPath("data.likeCount").type(JsonFieldType.NUMBER).description("?????? ?????????"),
//                                         fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("?????? ??????"),
//                                         fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("????????? ?????? ??????")
//                                 )
//                         )));
//     }
//
//     @Test
//     void patchCommentTest() throws Exception {
//
//         Long commentId = 1L;
//
//         CommentDto.Patch patch =
//                 CommentDto.Patch.builder()
//                         .body("????????? ?????? ??????")
//                         .build();
//
//         String content = gson.toJson(patch);
//
//         CommentDto.Response response =
//                 CommentDto.Response.builder()
//                         .commentId(1L)
//                         .feedId(1L)
//                         .userInfo(userMapper.userToUserPostResponse(new User()))
//                         .body("????????? ?????? ??????")
//                         .likeCount(1L)
//                         .createdAt(LocalDateTime.now())
//                         .modifiedAt(LocalDateTime.now())
//                         .build();
//
//         given(commentMapper.commentPatchToComment(Mockito.any(CommentDto.Patch.class))).willReturn(Comment.builder().build());
//         given(commentService.updateComment(Mockito.any(Comment.class), anyLong())).willReturn(Comment.builder().build());
//         given(commentMapper.commentToCommentResponse(Mockito.any(Comment.class))).willReturn(response);
//
//         ResultActions actions =
//                 mockMvc.perform(
//                         RestDocumentationRequestBuilders.patch("/comments/{comment_id}/edit", commentId)
//                                 .accept(MediaType.APPLICATION_JSON)
//                                 .contentType(MediaType.APPLICATION_JSON)
//                                 .content(content)
//                 );
//
//         actions
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.data.body").value(patch.getBody()))
//                 .andDo(document(
//                         "Patch_Comment",
//                         getDocumentRequest(),
//                         getDocumentResponse(),
//                         pathParameters(
//                                 parameterWithName("comment_id").description("?????? ??????")
//                         ),
//                         requestFields(
//                                 List.of(
//                                         fieldWithPath("body").type(JsonFieldType.STRING).description("????????? ?????? ??????")
//                                 )
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
//                                         fieldWithPath("data.body").type(JsonFieldType.STRING).description("????????? ?????? ??????"),
//                                         fieldWithPath("data.likeCount").type(JsonFieldType.NUMBER).description("?????? ?????????"),
//                                         fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("?????? ??????"),
//                                         fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("????????? ?????? ??????")
//                                 )
//                         )));
//     }
//
//     @Test
//     void getCommentTest() throws Exception {
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
//         given(commentMapper.commentToCommentResponse(Mockito.any(Comment.class))).willReturn(response);
//
//         ResultActions actions =
//                 mockMvc.perform(
//                         RestDocumentationRequestBuilders.get("/comments/{comment_id}", commentId)
//                                 .accept(MediaType.APPLICATION_JSON)
//                 );
//         actions
//                 .andExpect(status().isOk())
//                 .andDo(document(
//                         "Get_Comment",
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
//                                         fieldWithPath("data.likeCount").type(JsonFieldType.NUMBER).description("?????? ?????????"),
//                                         fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("?????? ??????"),
//                                         fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("????????? ?????? ??????")
//                                 )
//                         )));
//     }
//
//     @Test
//     void getFeedCommentsTest() throws Exception {
//
//         Long feedId = 1L;
//         int page = 1;
//
//         CommentDto.Response comment_1 =
//                 CommentDto.Response.builder()
//                         .commentId(1L)
//                         .feedId(1L)
//                         .userInfo(userMapper.userToUserPostResponse(new User()))
//                         .body("?????? ??????1")
//                         .likeCount(1L)
//                         .createdAt(LocalDateTime.now())
//                         .modifiedAt(LocalDateTime.now())
//                         .build();
//
//         CommentDto.Response comment_2 =
//                 CommentDto.Response.builder()
//                         .commentId(1L)
//                         .feedId(1L)
//                         .userInfo(userMapper.userToUserPostResponse(new User()))
//                         .body("?????? ??????2")
//                         .likeCount(1L)
//                         .createdAt(LocalDateTime.now())
//                         .modifiedAt(LocalDateTime.now())
//                         .build();
//
//         PageImpl<CommentDto.Response> responses = new PageImpl<>(List.of(comment_2, comment_1));
//
//         given(commentService.getFeedComments(anyLong(), anyInt())).willReturn(new PageImpl<>(new ArrayList<>()));
//         given(commentMapper.feedCommentsToPageResponse(Mockito.any())).willReturn(responses);
//
//         ResultActions actions =
//                 mockMvc.perform(
//                         RestDocumentationRequestBuilders.get("/feeds/{feed_id}/comments?page={page}", feedId, page)
//                                 .accept(MediaType.APPLICATION_JSON)
//                 );
//         actions
//                 .andExpect(status().isOk())
//                 .andDo(document(
//                         "Get_Feed_Comments",
//                         getDocumentRequest(),
//                         getDocumentResponse(),
//                         pathParameters(
//                                 parameterWithName("feed_id").description("?????? ??????")
//                         ),
//                         requestParameters(
//                                 parameterWithName("page").description("????????? ??????")
//                         ),
//                         responseFields(
//                                 List.of(
//                                         fieldWithPath("data.").type(JsonFieldType.OBJECT).description("?????? ?????????"),
//                                         fieldWithPath("data.content[].commentId").type(JsonFieldType.NUMBER).description("?????? ??????"),
//                                         fieldWithPath("data.content[].feedId").type(JsonFieldType.NUMBER).description("?????? ??????"),
//                                         fieldWithPath("data.content[].userInfo.userId").type(JsonFieldType.STRING).description("????????? ?????????"),
//                                         fieldWithPath("data.content[].userInfo.nickname").type(JsonFieldType.STRING).description("????????? ?????????"),
//                                         fieldWithPath("data.content[].userInfo.ariFactor").type(JsonFieldType.NUMBER).description("????????????"),
//                                         fieldWithPath("data.content[].userInfo.role[]").type(JsonFieldType.ARRAY).description("????????? ??????"),
//                                         fieldWithPath("data.content[].userInfo.profileImage").type(JsonFieldType.STRING).description("????????? ????????? ??????"),
//                                         fieldWithPath("data.content[].body").type(JsonFieldType.STRING).description("?????? ??????"),
//                                         fieldWithPath("data.content[].likeCount").type(JsonFieldType.NUMBER).description("?????? ?????????"),
//                                         fieldWithPath("data.content[].createdAt").type(JsonFieldType.STRING).description("?????? ??????"),
//                                         fieldWithPath("data.content[].modifiedAt").type(JsonFieldType.STRING).description("????????? ?????? ??????"),
//                                         fieldWithPath("data.pageable").type(JsonFieldType.STRING).description("Pageble ??????"),
//                                         fieldWithPath("data.size").type(JsonFieldType.NUMBER).description("????????? ??????"),
//                                         fieldWithPath("data.number").type(JsonFieldType.NUMBER).description("????????? ??????"),
//                                         fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER).description("??? ????????? ???"),
//                                         fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER).description("??? ?????? ???"),
//                                         fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN).description("Pageble Empty"),
//                                         fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN).description("Pageble Unsorted"),
//                                         fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN).description("Pageble Sorted"),
//                                         fieldWithPath("data.first").type(JsonFieldType.BOOLEAN).description("Pageble First"),
//                                         fieldWithPath("data.last").type(JsonFieldType.BOOLEAN).description("Pageble Last"),
//                                         fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER).description("????????? ?????? ??????"),
//                                         fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN).description("????????? Empty ??????")
//                                 )
//                         )));
//     }
//
//     @Test
//     void deleteCommentsTest() throws Exception {
//
//         Long commentId = 1L;
//
//         doNothing().when(commentService).deleteComment(anyLong());
//
//         mockMvc.perform(
//                         RestDocumentationRequestBuilders.delete("/comments/{comment_id}/delete", commentId)
//                 )
//                 .andExpect(status().isNoContent())
//                 .andDo(
//                         document(
//                                 "Delete_Comment",
//                                 getDocumentRequest(),
//                                 getDocumentResponse(),
//                                 pathParameters(
//                                         parameterWithName("comment_id").description("?????? ??????")
//                                 )
//                         )
//                 );
//     }
// }
