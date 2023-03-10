package com.ewha.back.domain.search.controller;

import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ewha.back.domain.feed.dto.FeedDto.ListResponse;
import com.ewha.back.domain.feed.entity.Feed;
import com.ewha.back.domain.feed.mapper.FeedMapper;
import com.ewha.back.domain.search.service.SearchService;
import com.ewha.back.global.dto.MultiResponseDto;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

	private final SearchService searchService;
	private final FeedMapper feedMapper;

	@GetMapping
	public ResponseEntity<MultiResponseDto<ListResponse>> getSearchResult(
		@Nullable @RequestParam("category") String category,
		@RequestParam("query") String queryParam,
		@RequestParam(name = "page", defaultValue = "1") Integer page) {

		Page<Feed> feedPage = new PageImpl<>(new ArrayList<>());

		if (category == null) {
			feedPage = searchService.findAllFeedsPageByQueryParam(queryParam, page);
		} else {
			feedPage = searchService.findCategoryFeedsPageByQueryParam(category, queryParam, page);
		}

		PageImpl<ListResponse> responses = feedMapper.newFeedsToPageResponse(feedPage);

		return ResponseEntity.ok(new MultiResponseDto<>(responses.getContent(), feedPage));
	}
}
