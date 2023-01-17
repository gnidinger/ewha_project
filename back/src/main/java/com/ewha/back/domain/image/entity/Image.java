package com.ewha.back.domain.image.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Column(nullable = false)
    private String originalImageName;

    @Column(nullable = false)
    private String storedImageName;

    @Column(nullable = false)
    private String storedPath;
}
