package com.ewha.back.domain.image.entity;

import com.ewha.back.domain.feed.entity.Feed;
import com.ewha.back.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private ImageType imageType;

    @Column(nullable = false)
    private String originalImageName;

    @Column(nullable = false)
    private String storedImageName;

    @Column(nullable = false)
    private String storedPath;

    @Column(nullable = false)
    private String thumbnailPath;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
