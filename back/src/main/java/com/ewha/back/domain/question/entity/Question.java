package com.ewha.back.domain.question.entity;

import com.ewha.back.global.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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
    private String answerBody;

    @Column
    private String dummy1;

    @Column
    private String dummy2;

    @Column
    private String dummy3;

    @Column
    private String dummy4;

    @JsonManagedReference
    @OneToMany(mappedBy = "question", cascade = CascadeType.PERSIST)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Answer> answers;

}
