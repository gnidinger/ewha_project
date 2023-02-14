package com.ewha.back.domain.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ewha.back.domain.image.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

	void deleteById(Long imageId);
}
