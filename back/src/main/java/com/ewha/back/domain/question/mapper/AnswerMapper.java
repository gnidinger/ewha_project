package com.ewha.back.domain.question.mapper;

import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.ewha.back.domain.question.dto.AnswerDto;
import com.ewha.back.domain.question.entity.Answer;

@Mapper(componentModel = "spring")
public interface AnswerMapper {

	Answer answerPostToAnswer(AnswerDto.Post answerPost);

	AnswerDto.Response answerToAnswerResponse(Answer answer);

	default PageImpl<AnswerDto.MyAnswerResponse> answerPageToMyAnswerResponse(Page<Answer> answerPage) {

		if (answerPage == null)
			return null;

		return new PageImpl<>(answerPage.stream()
			.map(answer -> {
				AnswerDto.MyAnswerResponse.MyAnswerResponseBuilder myAnswerResponseBuilder = AnswerDto.MyAnswerResponse.builder();

				myAnswerResponseBuilder.answerId(answer.getId());
				myAnswerResponseBuilder.user(answer.getUser());
				myAnswerResponseBuilder.question(answer.getQuestion());
				myAnswerResponseBuilder.body(answer.getBody());
				return myAnswerResponseBuilder.build();

			}).collect(Collectors.toList()));
	}
}
