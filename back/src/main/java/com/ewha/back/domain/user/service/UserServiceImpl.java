package com.ewha.back.domain.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ewha.back.domain.category.entity.Category;
import com.ewha.back.domain.category.entity.CategoryType;
import com.ewha.back.domain.category.service.CategoryService;
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
import com.ewha.back.domain.user.repository.UserQueryRepository;
import com.ewha.back.domain.user.repository.UserRepository;
import com.ewha.back.global.exception.BusinessLogicException;
import com.ewha.back.global.exception.ExceptionCode;
import com.ewha.back.global.security.dto.LoginDto;
import com.ewha.back.global.security.refreshToken.repository.RefreshTokenRepository;
import com.ewha.back.global.security.util.CustomAuthorityUtils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final UserQueryRepository userQueryRepository;
	private final UserCategoryRepository userCategoryRepository;
	private final UserCategoryQueryRepository userCategoryQueryRepository;
	private final FeedQueryRepository feedQueryRepository;
	private final CommentQueryRepository commentQueryRepository;
	private final QuestionQueryRepository questionQueryRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final CustomAuthorityUtils customAuthorityUtils;
	private final CategoryService categoryService;

	@Override
	@Transactional
	public User createUser(User user) {

		verifyUserId(user.getUserId());
		verifyNickname(user.getNickname());

		String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
		List<String> roles = customAuthorityUtils.createRoles(user.getUserId());

		User savedUser = User.builder()
			.userId(user.getUserId())
			.password(encryptedPassword)
			.nickname(user.getNickname())
			.role(roles)
			.ariFactor(36.5)
			.centerCode(user.getCenterCode())
			.birthday(user.getBirthday())
			.phoneNumber(user.getPhoneNumber())
			.isFirstLogin(true)
			.build();

		return userRepository.save(savedUser);
	}

	@Override
	public List<List<String>> verifyVerifyDto(UserDto.Verify verifyDto) {

		List<List<String>> fieldErrors = new ArrayList<>();

		fieldErrors.add(verifyUserId(verifyDto.getUserId()));
		fieldErrors.add(verifyNickname(verifyDto.getNickname()));
		fieldErrors.add(verifyPassword(verifyDto.getPassword()));

		return fieldErrors.stream()
			.filter(list -> !list.isEmpty())
			.collect(Collectors.toList());
	}

	@Override
	public List<String> verifyUserId(String userId) {

		List<String> list = new ArrayList<>();

		if (!userRepository.existsByUserId(userId) && userId.matches("[0-9a-z\\s]{6,12}")) {
			return list;
		} else if (!userId.matches("[0-9a-z\\s]{6,12}")) {
			list.add(String.valueOf(ExceptionCode.USER_ID_NOT_VALID.getStatus()));
			list.add(ExceptionCode.USER_ID_NOT_VALID.getMessage());
			list.add("userId");
			list.add(userId);
			list.add("6~12자의 영문, 숫자만 사용 가능합니다.");
		} else if (userRepository.existsByUserId(userId)) {
			list.add(String.valueOf(ExceptionCode.USER_ID_EXISTS.getStatus()));
			list.add(ExceptionCode.USER_ID_EXISTS.getMessage());
			list.add("userId");
			list.add(userId);
			list.add("존재하는 아이디 입니다.");
		}
		return list;
	}

	// public Boolean verifyUserId(String userId) {
	// 	if (!userRepository.existsByUserId(userId))
	// 		return true;
	// 	else
	// 		throw new BusinessLogicException(ExceptionCode.USER_ID_EXISTS);
	// }

	@Override
	public List<String> verifyNickname(String nickname) {

		List<String> list = new ArrayList<>();

		if (!userRepository.existsByNickname(nickname) && nickname.matches("[0-9a-zA-Zㄱ-ㅎ가-힣\\s]{3,20}")) {
			return list;
		} else if (!nickname.matches("[0-9a-zA-Zㄱ-ㅎ가-힣\\s]{3,20}")) {
			list.add(String.valueOf(ExceptionCode.NICKNAME_NOT_VALID.getStatus()));
			list.add(ExceptionCode.NICKNAME_NOT_VALID.getMessage());
			list.add("nickname");
			list.add(nickname);
			list.add("3~20자의 한글, 영문, 숫자만 사용 가능합니다.");
		} else if (userRepository.existsByNickname(nickname)) {
			list.add(String.valueOf(ExceptionCode.NICKNAME_EXISTS.getStatus()));
			list.add(ExceptionCode.NICKNAME_EXISTS.getMessage());
			list.add("nickname");
			list.add(nickname);
			list.add("존재하는 닉네임 입니다.");
		}
		return list;
	}

	// public Boolean verifyNickname(String nickname) {
	// 	if (!userRepository.existsByNickname(nickname))
	// 		return true;
	// 	else
	// 		throw new BusinessLogicException(ExceptionCode.NICKNAME_EXISTS);
	// }

	@Override
	public List<String> verifyPassword(String password) {

		List<String> list = new ArrayList<>();

		if (password.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$")) {
			return list;
		} else {
			list.add(String.valueOf(ExceptionCode.PASSWORD_NOT_VALID.getStatus()));
			list.add(ExceptionCode.PASSWORD_NOT_VALID.getMessage());
			list.add("password");
			list.add(password);
			list.add("8~16자의 영문, 숫자, 특수문자(@$!%*?&)만 사용 가능합니다.");
		}
		return list;
	}

	@Override
	public Boolean verifyUserIdForSms(String userId) {
		if (userRepository.existsByUserId(userId))
			return true;
		else
			throw new BusinessLogicException(ExceptionCode.USER_ID_NOT_FOUND);
	}

	@Override
	public Boolean verifyLoginUserPassword(String password) {
		User findUser = getLoginUser();
		return findUser.verifyPassword(bCryptPasswordEncoder, password);
	}

	@Override
	@Transactional
	public User updateUser(UserDto.UserInfo userInfo) {

		User findUser = getLoginUser();

		userCategoryQueryRepository.deleteByUser(findUser);

		List<UserCategory> userCategories = userInfo.getCategories().stream()
			.map(a -> {
				UserCategory userCategory = new UserCategory();
				Category category = categoryService.findVerifiedCategory(a);
				userCategory.addUser(findUser);
				userCategory.addCategory(category);
				return userCategoryRepository.save(userCategory);
			}).collect(Collectors.toList());
		findUser.setUserCategories(userCategories);

		findUser.updateUserInfo(userInfo);

		return userRepository.save(findUser);
	}

	@Override
	@Transactional
	public void updatePassword(UserDto.Password password) {

		User findUser = getLoginUser();

		if (!verifyLoginUserPassword(password.getOldPassword())) {
			throw new BusinessLogicException(ExceptionCode.PASSWORD_NOT_MATCH);
		}

		if (!password.getNewPassword().equals(password.getNewPasswordRepeat())) {
			throw new BusinessLogicException(ExceptionCode.PASSWORDS_NOT_MATCH);
		}

		findUser.setPassword(bCryptPasswordEncoder.encode(password.getNewPassword()));

		userRepository.save(findUser);

	}

	@Override
	@Transactional(readOnly = true)
	public User getUser(Long userId) {

		User findUser = getLoginUser();

		return findVerifiedUser(userId);
	}

	@Override
	@Transactional(readOnly = true)
	public User getMyInfo() {

		User findUser = getLoginUser();

		return findUser;
	}

	@Override
	@Transactional
	public void deleteUser() {

		User findUser = getLoginUser();

		userRepository.deleteById(findUser.getId());

	}

	@Override
	@Transactional
	public User onFirstLogin(LoginDto.PatchDto patchDto) {

		User findUser = getLoginUser();

		findUser.setGenderType(patchDto.getGenderType());
		findUser.setAgeType(patchDto.getAgeType());

		List<UserCategory> userCategories = patchDto.getCategories().stream()
			.map(a -> {
				UserCategory userCategory = new UserCategory();
				Category category = categoryService.findVerifiedCategory(a);
				userCategory.addUser(findUser);
				userCategory.addCategory(category);
				return userCategoryRepository.save(userCategory);
			}).collect(Collectors.toList());
		log.info("######" + patchDto.getCategories().get(0).toString());
		log.info("######" + userCategories.size());
		findUser.setUserCategories(userCategories);
		findUser.setIsFirstLogin(false);

		return userRepository.save(findUser);
	}

	@Override
	@Transactional(readOnly = true)
	public User findByUserId(String userId) {
		Optional<User> optionalUser = userRepository.findByUserId(userId);
		User user = optionalUser.orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

		return user;
	}

	@Override
	public User findVerifiedUser(Long id) {
		return userRepository.findById(id)
			.orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
	}

	@Override
	@Transactional(readOnly = true)
	public User getLoginUser() { // 로그인된 유저 가져오기

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || authentication.getName() == null || authentication.getName()
			.equals("anonymousUser"))
			throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED);

		Optional<User> optionalUser = userRepository.findByUserId(authentication.getName());
		User user = optionalUser.orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

		return user;
	}

	/*
	 * 마이 페이지에서 표시될 정보. 나의 게시글, 댓글, 북마크, 대답한 질문
	 */

	@Override
	public Page<Feed> findUserFeeds(int page) {

		User findUser = getLoginUser();

		PageRequest pageRequest = PageRequest.of(page - 1, 10);

		return feedQueryRepository.findFeedListByUser(findUser, pageRequest);
	}

	@Override
	public Page<Comment> findUserComments(int page) {

		User findUser = getLoginUser();

		PageRequest pageRequest = PageRequest.of(page - 1, 10);

		return commentQueryRepository.findCommentListByUser(findUser, pageRequest);
	}

	@Override
	public Page<Feed> findUserFeedLikes(int page) {

		User findUser = getLoginUser();

		PageRequest pageRequest = PageRequest.of(page - 1, 10);

		return feedQueryRepository.findFeedLikesListByUser(findUser, pageRequest);
	}

	@Override
	public Page<Question> findUserQuestions(int page) {

		User findUser = getLoginUser();

		PageRequest pageRequest = PageRequest.of(page - 1, 10);

		return questionQueryRepository.findQuestionListByUser(findUser, pageRequest);
	}

	@Override
	public void saveUser(User user) {
		userRepository.save(user);
	}
}
