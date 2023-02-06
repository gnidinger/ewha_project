package com.ewha.back.global.config;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true, value = {"pageable"})
public class CustomPage<T> extends PageImpl<T> {
	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public CustomPage(
		@JsonProperty("content") List<T> content,
		@JsonProperty("page") int page,
		@JsonProperty("size") int size,
		@JsonProperty("totalElements") long total) {
		super(content, PageRequest.of(page, size), total);
	}


	public CustomPage(Page<T> page) {
		super(page.getContent(), page.getPageable(), page.getTotalElements());
	}

	public CustomPage(List<T> content) {
		super(content, Pageable.unpaged(), null == content ? 0 : content.size());
	}

	public CustomPage(List<T> content, Pageable pageable, Long total) {
		super(content, pageable, total);
	}
}
