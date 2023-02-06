package com.ewha.back.domain.question.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ewha.back.domain.question.dto.AnswerDto;
import com.ewha.back.domain.question.entity.Answer;
import com.ewha.back.domain.question.mapper.AnswerMapper;
import com.ewha.back.domain.question.service.AnswerService;
import com.ewha.back.global.dto.SingleResponseDto;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping
@RequiredArgsConstructor
public class AnswerController {
	private final AnswerMapper answerMapper;
	private final AnswerService answerService;

	@PostMapping("/question/{question_id}/answer")
	public ResponseEntity postAnswer(@PathVariable("question_id") Long questionId,
		AnswerDto.Post postAnswer) {

		Answer answer = answerMapper.answerPostToAnswer(postAnswer);
		Answer createdAnswer = answerService.createAnswer(answer, questionId);
		AnswerDto.Response response = answerMapper.answerToAnswerResponse(createdAnswer);

		return new ResponseEntity<>(
			new SingleResponseDto<>(response), HttpStatus.CREATED);
	}
}
