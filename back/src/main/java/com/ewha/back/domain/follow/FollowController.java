package com.ewha.back.domain.follow;

import java.util.List;

import javax.validation.constraints.Positive;

import org.springframework.data.domain.SliceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ewha.back.domain.user.entity.User;
import com.ewha.back.domain.user.service.UserService;
import com.ewha.back.global.dto.SingleResponseDto;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@Transactional
@RequiredArgsConstructor
@RequestMapping("/follow")
public class FollowController {

	private final FollowService followService;
	private final FollowMapper followMapper;
	private final UserService userService;
	private final FollowRepository followRepository;

	@PostMapping("/{userId}/add")
	public ResponseEntity follow(@PathVariable("userId") @Positive long followedUserId) {

		User findUser = userService.getLoginUser();
		Long followingUserId = findUser.getId();

		String result = followService.createFollow(followingUserId, followedUserId);

		return new ResponseEntity<>(
			new FollowDto.CMResponse<>(1, result), HttpStatus.OK
		);
		//        return new FollowDto.CMResponse<>(1, result);
	}

	@DeleteMapping("/{userId}/delete")
	public FollowDto.CMResponse unFollow(@PathVariable("userId") @Positive long followedUserId) {

		User findUser = userService.getLoginUser();
		Long followingUserId = findUser.getId();

		String result = followService.deleteFollow(followingUserId, followedUserId);

		return new FollowDto.CMResponse<>(1, result);
	}

	@GetMapping("/{userId}/followers")
	public ResponseEntity followersList(@PathVariable("userId") @Positive long followedUserId) {

		List<User> findFollowers = followService.findFollowers(followedUserId);
		SliceImpl<FollowDto.Response> response = followMapper.followersToFollowResponses(findFollowers);

		return new ResponseEntity<>(
			new SingleResponseDto<>(response), HttpStatus.OK
		);
	}

	@GetMapping("/{userId}/followings")
	public ResponseEntity followingList(@PathVariable("userId") @Positive long followingUserId) {

		List<User> findFollowings = followService.findFollowings(followingUserId);
		SliceImpl<FollowDto.Response> response = followMapper.followingsToFollowResponses(findFollowings);

		return new ResponseEntity<>(
			new SingleResponseDto<>(response), HttpStatus.OK
		);
	}
}
