package com.ewha.back.domain.question.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.lang.Nullable;

import com.ewha.back.domain.image.entity.Image;
import com.ewha.back.global.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Question extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "question_id")
	private Long id;
	@Column(nullable = false)
	private String title;
	@Column(nullable = false, columnDefinition = "LONGTEXT")
	private String body;
	@Column
	private String imagePath;
	@Column
	private String thumbnailPath;
	@Column
	private String answerBody;

	@Nullable
	@JsonManagedReference
	@OneToOne(mappedBy = "question", cascade = CascadeType.REMOVE)
	private Image image;

	@JsonManagedReference
	@OneToMany(mappedBy = "question", cascade = CascadeType.PERSIST)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Answer> answers;

	public void updateQuestion(Question question) {
		this.title = question.getTitle();
		this.body = question.getBody();
		this.imagePath = question.getImagePath();
		this.thumbnailPath = question.getThumbnailPath();
		this.answerBody = question.getAnswerBody();
	}

	public void addImagePaths(String fullPath, String thumbnailPath) {
		this.imagePath = fullPath;
		this.thumbnailPath = thumbnailPath;
	}
}
