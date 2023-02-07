package com.ewha.back.domain.feed.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.ewha.back.domain.category.entity.Category;
import com.ewha.back.global.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedCategory extends BaseTimeEntity implements Serializable {

	private static final long serialVersionUID = 6494678977089006639L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "feed_category_id")
	private Long id;

	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "category_id")
	private Category category;

	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "feed_id")
	@NotFound(action = NotFoundAction.IGNORE)
	private Feed feed;

	public void addFeed(Feed feed) {
		this.feed = feed;
		if (!this.feed.getFeedCategories().contains(this)) {
			this.feed.getFeedCategories().add(this);
		}
	}

	public void addCategory(Category category) {
		this.category = category;
		if (!this.category.getFeedCategories().contains(this)) {
			this.category.addFeedCategory(this);
		}
	}

}
