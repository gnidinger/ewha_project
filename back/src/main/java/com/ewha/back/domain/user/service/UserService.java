package com.ewha.back.domain.user.service;

import com.ewha.back.domain.category.entity.Category;
import com.ewha.back.domain.category.entity.CategoryType;
import com.ewha.back.domain.comment.entity.Comment;
import com.ewha.back.domain.comment.repository.CommentQueryRepository;
import com.ewha.back.domain.feed.entity.Feed;
import com.ewha.back.domain.feed.repository.FeedQueryRepository;
import com.ewha.back.domain.question.entity.Question;
import com.ewha.back.domain.question.repository.QuestionQueryRepository;
import com.ewha.back.domain.user.dto.UserDto;
import com.ewha.back.domain.user.entity.User;
import com.ewha.back.domain.user.entity.UserCategory;
import com.ewha.back.domain.user.repository.UserCategoryQueryRepository;
import com.ewha.back.domain.user.repository.UserCategoryRepository;
import com.ewha.back.domain.user.repository.UserRepository;
import com.ewha.back.global.exception.BusinessLogicException;
import com.ewha.back.global.exception.ExceptionCode;
import com.ewha.back.global.security.dto.LoginDto;
import com.ewha.back.global.security.refreshToken.repository.RefreshTokenRepository;
import com.ewha.back.global.security.util.CustomAuthorityUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserCategoryRepository userCategoryRepository;
    private final UserCategoryQueryRepository userCategoryQueryRepository;
    private final FeedQueryRepository feedQueryRepository;
    private final CommentQueryRepository commentQueryRepository;
    private final QuestionQueryRepository questionQueryRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CustomAuthorityUtils customAuthorityUtils;

    @Transactional
    public User createUser(User user) {

        verifyUserId(user.getUserId());

        String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        List<String> roles = customAuthorityUtils.createRoles(user.getUserId());

        User savedUser = User.builder()
                .userId(user.getUserId())
                .password(encryptedPassword)
                .nickname(user.getNickname())
                .role(roles)
                .ariFactor(36.5)
                .isFirstLogin(true)
                .build();

        return userRepository.save(savedUser);
    }

    public Boolean verifyUserId(String userId) {
        if (userRepository.existsByUserId(userId)) return true;
        else throw new BusinessLogicException(ExceptionCode.USER_ID_EXISTS);
    }

    public Boolean verifyNickname(String nickname) {
        if (userRepository.existsByNickname(nickname)) return true;
        else throw new BusinessLogicException(ExceptionCode.NICKNAME_EXISTS);
    }

    public Boolean verifyPassword(String password) {
        User findUser = getLoginUser();
        return findUser.verifyPassword(bCryptPasswordEncoder, password);
    }

    @Transactional
    public User updateUser(UserDto.UserInfo userInfo) {

        User findUser = getLoginUser();

        userCategoryQueryRepository.deleteByUserId(findUser.getId());

        List<UserCategory> userCategories = userInfo.getCategories().stream()
                .map(a -> {
                    UserCategory userCategory = new UserCategory();
                    Category category = new Category();
                    category.setId((long)CategoryType.valueOf(a).ordinal() + 1L);
                    userCategory.addUser(findUser);
                    userCategory.addCategory(category);
                    return userCategory;
                }).collect(Collectors.toList());
        findUser.setUserCategories(userCategories);

        findUser.updateUserInfo(userInfo);

        return userRepository.save(findUser);
    }

    @Transactional
    public void updatePassword(String password) {

        User findUser = getLoginUser();

        if (verifyPassword(password)) throw new BusinessLogicException(ExceptionCode.PASSWORD_CANNOT_CHANGE);
        else {
            findUser.setPassword(bCryptPasswordEncoder.encode(password));
            userRepository.save(findUser);
        }
    }

    @Transactional(readOnly = true)
    public User getUser(Long userId) {

        User findUser = getLoginUser();

        return findVerifiedUser(userId);
    }

    @Transactional(readOnly = true)
    public User getMyInfo() {

        User findUser = getLoginUser();

        return findUser;
    }

    @Transactional
    public boolean deleteUser() {

        User findUser = getLoginUser();

        userRepository.deleteById(findUser.getId());

        return true;
    }

    @Transactional
    public User onFirstLogin(LoginDto.PatchDto patchDto) {

        User findUser = getLoginUser();

        findUser.setGenderType(patchDto.getGenderType());
        findUser.setAgeType(patchDto.getAgeType());

        List<UserCategory> userCategories = patchDto.getCategories().stream()
                .map(a -> {
                    UserCategory userCategory = new UserCategory();
                    Category category = new Category();
                    category.setId((long)CategoryType.valueOf(a).ordinal() + 1L);
                    userCategory.addUser(findUser);
                    userCategory.addCategory(category);
                    return userCategory;
                }).collect(Collectors.toList());
        findUser.setUserCategories(userCategories);

        return userRepository.save(findUser);
    }

    @Transactional(readOnly = true)
    public User findByUserId(String userId) {
        Optional<User> optionalUser = userRepository.findByUserId(userId);
        User user = optionalUser.orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        return user;
    }

    public User findVerifiedUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public User getLoginUser() { // 로그인된 유저 가져오기

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null || authentication.getName().equals("anonymousUser"))
            throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED);

        Optional<User> optionalUser = userRepository.findByUserId(authentication.getName());
        User user = optionalUser.orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        return user;
    }

    /*
     * 마이 페이지에서 표시될 정보. 나의 게시글, 댓글, 북마크, 대답한 질문
     */

    public Page<Feed> findUserFeeds(int page) {

        User findUser = getLoginUser();

        PageRequest pageRequest = PageRequest.of(page - 1, 10);

        return feedQueryRepository.findFeedListByUser(findUser, pageRequest);
    }

    public Page<Comment> findUserComments(int page) {

        User findUser = getLoginUser();

        PageRequest pageRequest = PageRequest.of(page - 1, 10);

        return commentQueryRepository.findCommentListByUser(findUser, pageRequest);
    }

    public Page<Question> findUserQuestions(int page) {

        User findUser = getLoginUser();

        PageRequest pageRequest = PageRequest.of(page - 1, 10);

        return questionQueryRepository.findQuestionListByUser(findUser, pageRequest);
    }
}
