package com.ewha.back.domain.comment.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.ewha.back.domain.feed.entity.Feed;
import com.ewha.back.domain.like.entity.Like;
import com.ewha.back.domain.user.entity.User;
import com.ewha.back.global.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends BaseTimeEntity implements Serializable {

	private static final long serialVersionUID = -4038131162139964754L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_id")
	private Long id;

	@Column
	private String body;

	@Column
	private Boolean isLiked;

	@Column(nullable = false)
	private Long likeCount;


	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "feed_id")
	private Feed feed;

	@JsonManagedReference
	@OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
	@LazyCollection(LazyCollectionOption.FALSE)
	@NotFound(action = NotFoundAction.IGNORE)
	private final List<Like> likes = new ArrayList<>();

	public void updateComment(Comment comment) {
		this.body = comment.getBody();
	}

	public void addLike() {
		if (likeCount == null)
			this.likeCount = 1L;
		else
			this.likeCount = likeCount + 1;
		this.isLiked = true;
	}

	public void removeLike() {
		if (likeCount > 0)
			this.likeCount = likeCount - 1;
		this.isLiked = false;
	}

	public void setIsLiked(Boolean isLiked) {
		this.isLiked = isLiked;
	}
}
