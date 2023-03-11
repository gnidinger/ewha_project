package com.ewha.back.domain.follow.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ewha.back.domain.follow.entity.Follow;
import com.ewha.back.domain.follow.repository.FollowQueryRepository;
import com.ewha.back.domain.follow.repository.FollowRepository;
import com.ewha.back.domain.notification.service.NotificationService;
import com.ewha.back.domain.user.entity.User;
import com.ewha.back.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowService {

	private final FollowRepository followRepository;
	private final FollowQueryRepository followQueryRepository;
	private final UserService userService;
	private final NotificationService notificationService;

	public String createOrDeleteFollow(Long followedUserId) {

		User followingUser = userService.getLoginUser();
		Long followingUserId = followingUser.getId();

		User followedUser = userService.findVerifiedUser(followedUserId);

		if (followQueryRepository.findFollowByUserIds(followingUserId, followedUserId) == null) {

			Follow createdFollow = Follow.builder()
				.followingUser(followingUser)
				.followedUser(followedUser)
				.build();

			followRepository.save(createdFollow);

			followingUser.addFollowing();
			followedUser.addFollower();

			return "Create Follow";

		} else {

			Follow findFollow = followQueryRepository.findFollowByUserIds(followingUserId, followedUserId);

			followRepository.delete(findFollow);

			followingUser.removeFollowing();
			followedUser.removeFollower();

			return "Delete Follow";
		}
	}

	public Page<User> findFollowers(Long userId, Integer page) {

		PageRequest pageRequest = PageRequest.of(page - 1, 10);

		return followQueryRepository.findFollowersByUserId(userId, pageRequest);
	}

	public Page<User> findFollowersWithLoginUser(Long loginUserId, Long userId, Integer page) {

		PageRequest pageRequest = PageRequest.of(page - 1, 10);

		return followQueryRepository.findFollowersWithLoginUserByUserId(loginUserId, userId, pageRequest);
	}

	public List<User> findFollowingsList(Long followingUserId, Page<User> userPage) {
		return userPage.stream()
			.filter(user -> followQueryRepository.findFollowByUserIds(followingUserId, user.getId()) != null)
			.collect(Collectors.toList());
	}

	public Page<User> findFollowings(Long userId, Integer page) {

		PageRequest pageRequest = PageRequest.of(page - 1, 10);

		return followQueryRepository.findFollowingsByUserId(userId, pageRequest);
	}

	public Boolean isFollowing(Long followingUserId, Long followedUserId) {
		return followQueryRepository.findFollowByUserIds(followingUserId, followedUserId) != null;
	}
}
