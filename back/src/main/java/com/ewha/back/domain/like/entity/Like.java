package com.ewha.back.domain.like.entity;


import com.ewha.back.domain.comment.entity.Comment;
import com.ewha.back.domain.feed.entity.Feed;
import com.ewha.back.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "likes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LikeType likeType;

    @JsonBackReference
    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;
}
