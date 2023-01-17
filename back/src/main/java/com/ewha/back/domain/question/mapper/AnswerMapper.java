package com.ewha.back.domain.question.mapper;

import com.ewha.back.domain.question.dto.AnswerDto;
import com.ewha.back.domain.question.entity.Answer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AnswerMapper {

    Answer answerPostToAnswer(AnswerDto.Post answerPost);
    AnswerDto.Response answerToAnswerResponse(Answer answer);
}
