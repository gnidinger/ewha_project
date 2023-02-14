package com.ewha.back.domain.follow;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.springframework.data.domain.SliceImpl;

import com.ewha.back.domain.user.entity.User;

@Mapper(componentModel = "spring")
public interface FollowMapper {

	default SliceImpl<FollowDto.Response> followersToFollowResponses(List<User> followersList) {

		if (followersList == null) {
			return null;
		}

		return new SliceImpl<>(
			followersList.stream()
				.map(user -> FollowDto.Response.builder()
					.nickName(user.getNickname())
					.email(user.getEmail())
					.build())
				.collect(Collectors.toList())
		);
	}

	default SliceImpl<FollowDto.Response> followingsToFollowResponses(List<User> followingsList) {

		if (followingsList == null) {
			return null;
		}

		return new SliceImpl<>(
			followingsList.stream()
				.map(user -> FollowDto.Response.builder()
					.nickName(user.getNickname())
					.email(user.getEmail())
					.build())
				.collect(Collectors.toList())
		);
	}
}
