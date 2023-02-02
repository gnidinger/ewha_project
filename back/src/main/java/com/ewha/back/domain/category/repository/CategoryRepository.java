package com.ewha.back.domain.category.repository;

import com.ewha.back.domain.category.entity.Category;
import com.ewha.back.domain.category.entity.CategoryType;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	//    @Query(nativeQuery = true, value = "SELECT * " +
	//            "FROM CATEGORY " + "WHERE CATEGORY_TYPE = :categoryType")
	Optional<Category> findCategoryByCategoryType(CategoryType categoryType);
}
