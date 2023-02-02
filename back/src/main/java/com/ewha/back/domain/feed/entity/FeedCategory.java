package com.ewha.back.domain.feed.entity;

import com.ewha.back.domain.category.entity.Category;
import com.ewha.back.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.*;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedCategory {

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
