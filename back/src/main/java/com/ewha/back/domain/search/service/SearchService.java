package com.ewha.back.domain.search.service;

import com.ewha.back.domain.feed.entity.Feed;
import com.ewha.back.domain.feed.repository.FeedQueryRepository;
import com.ewha.back.domain.feed.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SearchService {

    private final FeedQueryRepository feedQueryRepository;

    public Page<Feed> findAllFeedsPageByQueryParam(String queryParam, Integer page) {

        PageRequest pageRequest = PageRequest.of(page - 1, 10);

        return feedQueryRepository.findAllSearchResultPage(queryParam, pageRequest);
    }

    public Page<Feed> findCategoryFeedsPageByQueryParam(String category, String queryParam, Integer page) {

        PageRequest pageRequest = PageRequest.of(page - 1, 10);

        return feedQueryRepository.findCategorySearchResultPage(category, queryParam, pageRequest);
    }
}
