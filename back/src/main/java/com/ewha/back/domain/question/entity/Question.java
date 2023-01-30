package com.ewha.back.domain.question.entity;

import com.ewha.back.domain.image.entity.Image;
import com.ewha.back.global.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.List;

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

    @Column
    private String dummy1;

    @Column
    private String dummy2;

    @Column
    private String dummy3;

    @Column
    private String dummy4;

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
        this.dummy1 = question.getDummy1();
        this.dummy2 = question.getDummy2();
        this.dummy3 = question.getDummy3();
        this.dummy4 = question.getDummy4();
    }

    public void addImagePaths(String fullPath, String thumbnailPath) {
        this.imagePath = fullPath;
        this.thumbnailPath = thumbnailPath;
    }
}
