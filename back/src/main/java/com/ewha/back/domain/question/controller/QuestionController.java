package com.ewha.back.domain.question.controller;

import com.ewha.back.domain.question.dto.QuestionDto;
import com.ewha.back.domain.question.entity.Question;
import com.ewha.back.domain.question.mapper.QuestionMapper;
import com.ewha.back.domain.question.service.AnswerService;
import com.ewha.back.domain.question.service.QuestionService;
import com.ewha.back.domain.user.entity.User;
import com.ewha.back.domain.user.service.UserService;
import com.ewha.back.global.dto.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;
    private final QuestionMapper questionMapper;
    private final AnswerService answerService;
    private final UserService userService;

    @GetMapping("/{question_id}")
    public ResponseEntity getQuestion(@PathVariable("question_id") Long questionId) {

        User findUser = userService.getLoginUser();
        Long userId = findUser.getId();

        if (answerService.findByQuestionIdAndUserId(questionId, userId) == null) {

            Question question = questionService.getQuestion(questionId);
            QuestionDto.Response response = questionMapper.questionToQuestionResponse(question);

            return new ResponseEntity<>(
                    new SingleResponseDto<>(response), HttpStatus.OK);
        } else {

            Question question = questionService.getAnsweredQuestion(questionId, userId);
            QuestionDto.AnsweredResponse response = questionMapper.questionToAnsweredQuestionResponse(question);

            return new ResponseEntity<>(
                    new SingleResponseDto<>(response), HttpStatus.OK);
        }

    }
}
