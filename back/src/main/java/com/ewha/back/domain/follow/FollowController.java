package com.ewha.back.domain.follow;

import java.util.List;

import javax.validation.constraints.Positive;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ewha.back.domain.user.entity.User;
import com.ewha.back.global.dto.MultiResponseDto;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@Transactional
@RequiredArgsConstructor
@RequestMapping("/follows")
public class FollowController {

	private final FollowService followService;
	private final FollowMapper followMapper;

	@PostMapping("/{userId}")
	public ResponseEntity<String> follow(@PathVariable("userId") @Positive long followedUserId) {

		String result = followService.createOrDeleteFollow(followedUserId);

		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/{userId}/followers")
	public ResponseEntity<MultiResponseDto<FollowDto.FollowerResponse>> followersList(
		@PathVariable("userId") @Positive Long followedUserId,
		@RequestParam(name = "page", defaultValue = "1") int page) {

		Page<User> findFollowers = followService.findFollowers(followedUserId, page);

		List<User> findFollowingsList = followService.findFollowingsList(followedUserId, findFollowers);

		PageImpl<FollowDto.FollowerResponse> responsePage =
			followMapper.followersToFollowerResponses(findFollowers, findFollowingsList);

		return ResponseEntity.ok(new MultiResponseDto<>(responsePage.getContent(), findFollowers));
	}

	@GetMapping("/{userId}/followings")
	public ResponseEntity<MultiResponseDto<FollowDto.FollowingResponse>> followingList(
		@PathVariable("userId") @Positive Long followingUserId,
		@RequestParam(name = "page", defaultValue = "1") int page) {

		Page<User> findFollowings = followService.findFollowings(followingUserId, page);

		PageImpl<FollowDto.FollowingResponse> responsePage =
			followMapper.followingsToFollowingResponses(findFollowings);

		return ResponseEntity.ok(new MultiResponseDto<>(responsePage.getContent(), findFollowings));
	}
}
