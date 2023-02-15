package com.ewha.back.domain.question.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/answer/myanswer")
	public ResponseEntity<PageImpl<AnswerDto.MyAnswerResponse>> getMyAnswers(@RequestParam(name = "page", defaultValue = "1") int page) {

		Page<Answer> answerPage = answerService.findMyAnswers(page);
		PageImpl<AnswerDto.MyAnswerResponse> responses = answerMapper.answerPageToMyAnswerResponse(answerPage);

		return ResponseEntity.status(HttpStatus.OK).body(responses);
	}
}
