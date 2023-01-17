package com.ewha.back.domain.category.dto;

import com.ewha.back.domain.category.entity.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

public class CategoryDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        @NotBlank
        private CategoryType categoryType;
    }
}
