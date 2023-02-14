package com.ewha.back.domain.follow;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;

import com.ewha.back.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "FOLLOW",
	uniqueConstraints = {
		@UniqueConstraint(name = "follow_following", columnNames = {"followingUserId", "followedUserId"})})
public class Follow {

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

	@CreationTimestamp // 자동으로 현재시간 담김
	private Timestamp createDate;
}
