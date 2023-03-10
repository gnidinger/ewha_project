package com.ewha.back.domain.follow;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.DynamicInsert;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.ewha.back.domain.user.entity.User;
import com.ewha.back.global.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@Builder
@ToString
@DynamicInsert
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "FOLLOW",
	uniqueConstraints = {
		@UniqueConstraint(name = "follow_following", columnNames = {"followingUserId", "followedUserId"})})
public class Follow extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long followId;

	@JsonIgnoreProperties
	@ManyToOne
	@JoinColumn(name = "followingUserId") // 팔로우 하는 유저 아이디
	private User followingUser;

	@JsonIgnoreProperties
	@ManyToOne
	@JoinColumn(name = "followedUserId") // 팔로잉 당하는 유저 아이디
	private User followedUser;
}
