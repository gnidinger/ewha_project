package com.ewha.back.domain.user.entity;

import com.ewha.back.domain.comment.entity.Comment;
import com.ewha.back.domain.feed.entity.Feed;
import com.ewha.back.domain.image.entity.Image;
import com.ewha.back.domain.like.entity.Like;
import com.ewha.back.domain.notification.entity.Notification;
import com.ewha.back.domain.question.entity.Answer;
import com.ewha.back.domain.user.dto.UserDto;
import com.ewha.back.domain.user.entity.enums.AgeType;
import com.ewha.back.domain.user.entity.enums.GenderType;
import com.ewha.back.global.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.*;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@Table(name = "users")
@NoArgsConstructor
public class User extends BaseTimeEntity {
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
	private LocalDate birthday;

	@ElementCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<String> role = new ArrayList<>();

	@Nullable
	@JsonManagedReference
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	private List<Image> images = new ArrayList<>();

	@Column
	private String provider;    // oauth2를 이용할 경우 어떤 플랫폼을 이용하는지

	@Column
	private String providerId;  // oauth2를 이용할 경우 아이디 값

	@Column
	private String email; // OAuth의 경우 이메일이 존재할 가능성 있음

	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	private List<UserCategory> userCategories = new ArrayList<>();

	@JsonManagedReference
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	private List<Feed> feeds = new ArrayList<>();

	@JsonManagedReference
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	private List<Answer> answers = new ArrayList<>();

	@JsonManagedReference
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	private List<Comment> comments = new ArrayList<>();

	@JsonManagedReference
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	private List<Like> likes = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
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

	public User oauthUpdate(String name, String email) {
		this.nickname = name;
		this.userId = email;
		return this;
	}

	public void updateUserInfo(UserDto.UserInfo userInfo) {
		this.nickname = userInfo.getNickname();
		this.genderType = userInfo.getGenderType();
		this.ageType = userInfo.getAgeType();
		this.introduction = userInfo.getIntroduction();
	}

	public Boolean verifyPassword(BCryptPasswordEncoder bCryptPasswordEncoder, String password) {
		return bCryptPasswordEncoder.matches(password, this.password);
	}
}
