package com.ewha.back.domain.follow;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ewha.back.domain.user.entity.User;
import com.ewha.back.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowService {

	private final FollowRepository followRepository;
	private final EntityManager em;
	private final UserService userService;

	public String createFollow(long followingUserId, long followedUserId) {

		long result = followRepository.makeFollow(followingUserId, followedUserId);

		if (result == 1) {
			return "팔로우 성공";
		}
		return "이미 팔로우 한 사용자 입니다.";
	}

	public String deleteFollow(long followingUserId, long followedUserId) {

		Integer result = followRepository.makeUnFollow(followingUserId, followedUserId);

		if (result == 1) {
			return "언팔로우 성공";
		}
		return "이미 언팔로우한 사용자 입니다.";
	}

	public List<User> findFollowers(long userId) {

		// PageRequest pageRequest = PageRequest.of(page - 1, 10);

		List<Long> followerIdList = followRepository.findFollowersByUserId(userId);

		return followerIdList.stream()
			.map(userService::findVerifiedUser)
			.collect(Collectors.toList());
	}

	public List<User> findFollowings(long userId) {

		List<Long> followingIdList = followRepository.findFollowingsByUserId(userId);

		return followingIdList.stream()
			.map(userService::findVerifiedUser)
			.collect(Collectors.toList());
	}

	public long countFollowers(long userId) {

		List<Long> followerIdList = followRepository.findFollowersByUserId(userId);

		return followerIdList.stream().count();
	}

	public long countFollowings(long userId) {

		List<Long> followingIdList = followRepository.findFollowingsByUserId(userId);

		return followingIdList.stream().count();
	}
}
