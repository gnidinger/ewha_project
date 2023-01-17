package com.ewha.back.domain.user.entity;

import com.ewha.back.domain.category.entity.Category;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_category_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private User user;

    public void addUser(User user) {
        this.user = user;
        if (!this.user.getUserCategories().contains(this)) {
            this.user.getUserCategories().add(this);
        }
    }

    public void addCategory(Category category) {
        this.category = category;
        if (!this.category.getUserCategories().contains(this)) {
            this.category.addUserCategory(this);
        }
    }
}
