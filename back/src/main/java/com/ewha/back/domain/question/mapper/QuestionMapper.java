package com.ewha.back.domain.question.mapper;

import com.ewha.back.domain.question.dto.QuestionDto;
import com.ewha.back.domain.question.entity.Answer;
import com.ewha.back.domain.question.entity.Question;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    Question questionPostToQuestion(QuestionDto.Post postQuestion);
    Question questionPatchToQuestion(QuestionDto.Patch patchQuestion);
    QuestionDto.Response questionToQuestionResponse(Question question);
    default QuestionDto.AnsweredResponse questionToAnsweredQuestionResponse(Question question) {

        if (question == null) return null;

        return QuestionDto.AnsweredResponse.builder()
                .questionId(question.getId())
                .title(question.getTitle())
                .body(question.getBody())
                .imagePath(question.getImagePath())
                .thumbnailPath(question.getThumbnailPath())
                .answerBody(question.getAnswerBody())
                .userAnswer(question.getAnswers().stream()
                        .map(Answer::getBody).toString())
                .build();
    }

    default PageImpl<QuestionDto.AnsweredResponse> myQuestionsToPageResponse(Page<Question> questionList) {

        if (questionList == null) return null;

        return new PageImpl<>(questionList.stream()
                .map(question -> {
                    return QuestionDto.AnsweredResponse.builder()
                            .questionId(question.getId())
                            .title(question.getTitle())
                            .body(question.getBody())
                            .answerBody(question.getAnswerBody())
                            .userAnswer(question.getAnswers().stream()
                                    .map(Answer::getBody).toString())
                            .build();
                }).collect(Collectors.toList()));
    }
}
