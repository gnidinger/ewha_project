package com.ewha.back.global;

import com.ewha.back.domain.category.entity.Category;
import com.ewha.back.domain.category.entity.CategoryType;
import com.ewha.back.domain.category.repository.CategoryRepository;
import com.ewha.back.domain.user.entity.User;
import com.ewha.back.domain.user.entity.UserCategory;
import com.ewha.back.domain.user.entity.enums.AgeType;
import com.ewha.back.domain.user.entity.enums.GenderType;
import com.ewha.back.domain.user.entity.enums.Role;
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
                               CategoryRepository categoryRepository,
                               BCryptPasswordEncoder encoder) {

        for (int i = 0; i < 12; i++) {
            Category category = Category.builder()
                    .categoryType(CategoryType.values()[i])
                    .build();
            categoryRepository.save(category);
        }

//         ------------------------------------------------------------------------------------------
//         USER STUB
//         ------------------------------------------------------------------------------------------

        Category category = new Category();
        Category category2 = new Category();
        category.setId(1L);
        category2.setId(2L);

        UserCategory userCategory = UserCategory.builder()
//                .user(user)
                .build();
        userCategory.addCategory(category);
        userCategory.addCategory(category2);
        User user = User.builder()
                .userId("asdfasdfasdf")
                .ariFactor(36.5)
                .password(encoder.encode("1234"))
                .nickname("qwer")
                .userCategories(List.of(userCategory))
                .role(List.of("USER"))
                .isFirstLogin(false)
                .genderType(GenderType.MALE)
                .ageType(AgeType.THIRTIES)
                .build();
        userCategory.addUser(user);
//        userCategory.addCategory(category);
//        userCategory.addCategory(category2);

        log.info("USER STUB " +
                userRepository.save(user));

        return null;
    }
}