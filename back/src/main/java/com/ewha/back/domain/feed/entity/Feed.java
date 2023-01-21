package com.ewha.back.domain.feed.entity;

import com.ewha.back.domain.comment.entity.Comment;
import com.ewha.back.domain.image.entity.Image;
import com.ewha.back.domain.like.entity.Like;
import com.ewha.back.domain.user.entity.User;
import com.ewha.back.global.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Feed extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_id")
    private Long id;

    @OneToMany(mappedBy = "feed", cascade = CascadeType.PERSIST)
    private List<FeedCategory> feedCategories = new ArrayList<>();

    @Column
    private String imagePath;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String body;

    @Column
    private Boolean isLiked;

    @Column
    private Long likeCount;

    @Column
    private Long viewCount;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Nullable
    @JsonManagedReference
    @OneToOne(mappedBy = "feed", cascade = CascadeType.REMOVE)
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

    public void addFeedCategories(List<FeedCategory> feedCategoriesList) {
        this.feedCategories = feedCategoriesList;
    }

    public void updateFeed(Feed feed) {
        this.title = feed.getTitle();
        this.body = feed.getBody();
        this.imagePath = feed.getImagePath();
    }

    public void addView() {
        if (viewCount == null) this.viewCount = 1L;
        else this.viewCount = viewCount + 1;
    }

    public void addLike() {
        if (likeCount == null) this.likeCount = 1L;
        else this.likeCount = likeCount + 1;
        this.isLiked = true;
    }

    public void removeLike() {
        if (likeCount > 0) this.likeCount = likeCount - 1;
        this.isLiked = false;
    }

    public void setIsLiked(Boolean isLiked) {
        this.isLiked = isLiked;
    }

    public void setComments(List<Comment> isLikedComments) {
        this.comments = isLikedComments;
    }
}
