package com.ewha.back.global;

import com.ewha.back.domain.category.entity.Category;
import com.ewha.back.domain.category.entity.CategoryType;
import com.ewha.back.domain.category.repository.CategoryRepository;
import com.ewha.back.domain.comment.entity.Comment;
import com.ewha.back.domain.comment.repository.CommentRepository;
import com.ewha.back.domain.feed.entity.Feed;
import com.ewha.back.domain.feed.entity.FeedCategory;
import com.ewha.back.domain.feed.repository.FeedRepository;
import com.ewha.back.domain.feed.service.FeedService;
import com.ewha.back.domain.user.entity.User;
import com.ewha.back.domain.user.entity.UserCategory;
import com.ewha.back.domain.user.entity.enums.AgeType;
import com.ewha.back.domain.user.entity.enums.GenderType;
import com.ewha.back.domain.user.repository.UserRepository;
import com.ewha.back.domain.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Configuration
public class Stub {

    private static final Logger log = LoggerFactory.getLogger(Stub.class);

    @Bean
    @Transactional
    CommandLineRunner stubInit(UserRepository userRepository, UserService userService,
                               CategoryRepository categoryRepository, FeedRepository feedRepository,
                               FeedService feedService,
                               CommentRepository commentRepository, BCryptPasswordEncoder encoder) {

        for (int i = 0; i < 12; i++) {
            Category category = Category.builder()
                    .categoryType(CategoryType.values()[i])
                    .build();
            categoryRepository.save(category);
        }

//         ------------------------------------------------------------------------------------------
//         USER STUB
//         ------------------------------------------------------------------------------------------
        for (int i = 1; i <= 20; i++) {

            Long rand = (long) ((Math.random() * 12) + 1);

            UserCategory userCategory = UserCategory.builder().build();

            Category category = new Category();
            category.setId(rand);

            userCategory.addCategory(category);

            User user = User.builder()
                    .userId("asdfasdfasdf" + i)
                    .ariFactor(36.5)
                    .password(encoder.encode("1234"))
                    .nickname("qwer" + i)
                    .userCategories(List.of(userCategory))
                    .role(List.of("USER"))
                    .isFirstLogin(false)
                    .genderType(GenderType.MALE)
                    .ageType(AgeType.THIRTIES)
                    .profileImage("profile image")
                    .build();

            userCategory.addUser(user);

            log.info("USER STUB " + userRepository.save(user));
        }
////         ------------------------------------------------------------------------------------------
////         ------------------------------------------------------------------------------------------
////         FEED STUB
////         ------------------------------------------------------------------------------------------
//        for (int i = 1; i <= 40; i++) {
//
//            Long rand = (long) ((Math.random() * 12) + 1);
//
//            Category category = new Category();
//            category.setId(rand);
//
//            FeedCategory feedCategory = new FeedCategory();
//            feedCategory.addCategory(category);
//
//            Feed feed = Feed.builder()
//                    .feedCategories(List.of(feedCategory))
//                    .user(userService.findVerifiedUser((long) ((Math.random() * 20) + 1)))
//                    .title("title" + i)
//                    .body("body" + i)
//                    .likeCount((long) (Math.random() * 50))
//                    .viewCount((long) (Math.random() * 100))
//                    .build();
//
//            feedCategory.addFeed(feed);
//
//            log.info("FEED STUB " + feedRepository.save(feed));
//        }
////         ------------------------------------------------------------------------------------------
////         ------------------------------------------------------------------------------------------
////         COMMENT STUB
////         ------------------------------------------------------------------------------------------
//        for (int i = 1; i <= 80; i++) {
//
//            Comment comment = Comment.builder()
//                    .body("comment body" + i)
//                    .user(userService.findVerifiedUser((long) (Math.random() * 20) + 1))
//                    .feed(feedService.findVerifiedFeed((long) (Math.random() * 40) + 1))
//                    .likeCount((long) (Math.random() * 10) + 1)
//                    .build();
//
//            log.info("COMMENT STUB " + commentRepository.save(comment));
//        }
//
        return null;
    }
}