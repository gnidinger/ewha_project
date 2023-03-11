package com.ewha.back.domain.user.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ewha.back.domain.comment.entity.Comment;
import com.ewha.back.domain.feed.entity.Feed;
import com.ewha.back.domain.image.entity.Image;
import com.ewha.back.domain.like.entity.CommentLike;
import com.ewha.back.domain.like.entity.FeedLike;
import com.ewha.back.domain.like.entity.Like;
import com.ewha.back.domain.notification.entity.Notification;
import com.ewha.back.domain.question.entity.Answer;
import com.ewha.back.domain.user.dto.UserDto;
import com.ewha.back.domain.user.entity.enums.AgeType;
import com.ewha.back.domain.user.entity.enums.GenderType;
import com.ewha.back.global.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@DynamicInsert
@Table(name = "users")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", updatable = false)
	private Long id;
	@Column(name = "string_id", nullable = false, unique = true)
	private String userId;
	@Column
	private String phoneNumber;
	@Column(nullable = false)
	private Double ariFactor;
	@Column(nullable = false)
	private Boolean isFirstLogin;
	@Column(name = "oauth_id", unique = true)
	private String oauthId;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private String nickname;
	@Column
	private String profileImage; // 프로필 이미지 경로
	@Column
	private String thumbnailPath;
	@Column
	@Builder.Default
	private Boolean isVerified = false;
	@Enumerated(EnumType.STRING)
	private GenderType genderType;
	@Enumerated(EnumType.STRING)
	private AgeType ageType;
	@Column
	private String introduction;
	@Column
	private Long centerCode;
	@Column
	private String birthday;

	@ColumnDefault("0")
	private Long followerCount;

	@ColumnDefault("0")
	private Long followingCount;

	@ElementCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<String> role = new ArrayList<>();

	@Nullable
	@JsonManagedReference
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Image> images = new ArrayList<>();

	@Column
	private String provider;    // oauth2를 이용할 경우 어떤 플랫폼을 이용하는지

	@Column
	private String providerId;  // oauth2를 이용할 경우 아이디 값

	@Column
	private String email; // OAuth의 경우 이메일이 존재할 가능성 있음

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<UserCategory> userCategories = new ArrayList<>();

	@JsonManagedReference
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Feed> feeds = new ArrayList<>();

	@JsonManagedReference
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Answer> answers = new ArrayList<>();

	@JsonManagedReference
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Comment> comments = new ArrayList<>();

	@JsonManagedReference
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Like> likes = new ArrayList<>();

	@JsonManagedReference
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<FeedLike> feedLikes = new ArrayList<>();

	@JsonManagedReference
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<CommentLike> commentLikes = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Notification> notifications = new ArrayList<>();

	@Builder(builderClassName = "OAuth2Register", builderMethodName = "oauth2Register")
	public User(String nickname, String password, String email, Double ariFactor, List<String> role, String provider,
		String providerId) {
		this.nickname = nickname;
		this.password = password;
		this.ariFactor = ariFactor;
		//        this.email = email;
		this.role = role;
		this.provider = provider;
		this.providerId = providerId;
	}

	public void addFollower() {
		this.followerCount++;
	}

	public void removeFollower() {
		if (this.followerCount > 0) {
			this.followerCount--;
		}
	}

	public void addFollowing() {
		this.followingCount++;
	}

	public void removeFollowing() {
		if (this.followingCount > 0) {
			this.followingCount--;
		}
	}

	public User oauthUpdate(String nickname, String email) {
		if (!this.nickname.equals(nickname)) {
			this.nickname = nickname;
		}
		if (!this.email.equals(email)) {
			this.email = email;
		}
		return this;
	}

	public User oauthUpdate(String nickname) {
		if (!this.nickname.equals(nickname)) {
			this.nickname = nickname;
		}
		return this;
	}

	public void updateUserInfo(UserDto.UserInfo userInfo) {
		this.nickname = userInfo.getNickname();
		this.profileImage = userInfo.getProfileImage();
		this.genderType = userInfo.getGenderType();
		this.ageType = userInfo.getAgeType();
		this.introduction = userInfo.getIntroduction();
	}

	public Boolean verifyPassword(BCryptPasswordEncoder bCryptPasswordEncoder, String password) {
		return bCryptPasswordEncoder.matches(password, this.password);
	}
}
