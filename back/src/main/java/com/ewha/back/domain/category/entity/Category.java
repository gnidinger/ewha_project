package com.ewha.back.domain.category.entity;

import com.ewha.back.domain.feed.entity.FeedCategory;
import com.ewha.back.domain.user.entity.UserCategory;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.*;

import javax.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category implements Serializable {

	private static final long serialVersionUID = 6494678977089006639L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id")
	private Long id;

	@Enumerated(EnumType.STRING)
	private CategoryType categoryType;

	@JsonManagedReference
	@OneToMany(mappedBy = "category", cascade = CascadeType.PERSIST)
	private List<UserCategory> userCategories = new ArrayList<>();

	@JsonManagedReference
	@OneToMany(mappedBy = "category", cascade = CascadeType.PERSIST)
	private List<FeedCategory> feedCategories = new ArrayList<>();

	public void addUserCategory(UserCategory userCategory) {
		this.userCategories.add(userCategory);
		if (userCategory.getCategory() != this) {
			userCategory.addCategory(this);
		}
	}

	public void addFeedCategory(FeedCategory feedCategory) {
		this.feedCategories.add(feedCategory);
		if (feedCategory.getCategory() != this) {
			feedCategory.addCategory(this);
		}
	}

}
