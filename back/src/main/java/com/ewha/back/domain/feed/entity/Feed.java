package com.ewha.back.domain.feed.entity;

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
import javax.persistence.OneToOne;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.lang.Nullable;

import com.ewha.back.domain.comment.entity.Comment;
import com.ewha.back.domain.image.entity.Image;
import com.ewha.back.domain.like.entity.FeedLike;
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
public class Feed extends BaseTimeEntity implements Serializable {

	private static final long serialVersionUID = 6494678977089006639L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "feed_id")
	private Long id;

	@JsonManagedReference
	@OneToMany(mappedBy = "feed", cascade = CascadeType.PERSIST, orphanRemoval = true)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<FeedCategory> feedCategories = new ArrayList<>();

	@Column
	private String imagePath;

	@Column
	private String thumbnailPath;

	@Column(nullable = false)
	private String title;

	@Column(columnDefinition = "LONGTEXT")
	private String body;

	@Column
	private Long likeCount;

	@Column
	private Long viewCount;

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@JsonManagedReference
	@OneToOne(fetch = FetchType.EAGER, mappedBy = "feed", cascade = CascadeType.ALL)
	private Image image;

	@JsonManagedReference
	@OneToMany(mappedBy = "feed", cascade = CascadeType.PERSIST)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Comment> comments;

	@JsonManagedReference
	@OneToMany(mappedBy = "feed", cascade = CascadeType.REMOVE)
	@LazyCollection(LazyCollectionOption.FALSE)
	@NotFound(action = NotFoundAction.IGNORE)
	private List<Like> likes = new ArrayList<>();

	@JsonManagedReference
	@OneToMany(mappedBy = "feed", cascade = CascadeType.REMOVE)
	@LazyCollection(LazyCollectionOption.FALSE)
	@NotFound(action = NotFoundAction.IGNORE)
	private List<FeedLike> feedLikes = new ArrayList<>();

	public void addFeedCategories(List<FeedCategory> feedCategoriesList) {
		this.feedCategories = feedCategoriesList;
	}

	public void updateFeed(Feed feed) {
		this.title = feed.getTitle();
		this.body = feed.getBody();
		// this.imagePath = feed.getImagePath();
		// this.thumbnailPath = feed.getThumbnailPath();
	}

	public void addImagePaths(String fullPath, String thumbnailPath) {
		this.imagePath = fullPath;
		this.thumbnailPath = thumbnailPath;
	}

	public void addView() {
		if (viewCount == null) {
			this.viewCount = 1L;
		} else {
			this.viewCount = viewCount + 1;
		}
	}

	public void addLike() {
		if (likeCount == null) {
			this.likeCount = 1L;
		} else {
			this.likeCount = likeCount + 1;
		}
	}

	public void removeLike() {
		if (likeCount > 0) {
			this.likeCount = likeCount - 1;
		}
	}

	public void setComments(List<Comment> isLikedComments) {
		this.comments = isLikedComments;
	}
}
