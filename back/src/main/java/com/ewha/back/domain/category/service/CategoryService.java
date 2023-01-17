package com.ewha.back.domain.category.service;

import com.ewha.back.domain.category.entity.Category;
import com.ewha.back.domain.category.entity.CategoryType;
import com.ewha.back.domain.category.repository.CategoryRepository;
import com.ewha.back.global.exception.BusinessLogicException;
import com.ewha.back.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category findVerifiedCategory(CategoryType categoryType) {
        return categoryRepository.findCategoryByCategoryType(categoryType)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CATEGORY_NOT_FOUND));
    }
}
