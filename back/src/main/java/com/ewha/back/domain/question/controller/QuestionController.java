package com.ewha.back.domain.question.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ewha.back.domain.image.service.AwsS3Service;
import com.ewha.back.domain.question.dto.QuestionDto;
import com.ewha.back.domain.question.entity.Question;
import com.ewha.back.domain.question.mapper.QuestionMapper;
import com.ewha.back.domain.question.service.AnswerService;
import com.ewha.back.domain.question.service.QuestionService;
import com.ewha.back.domain.user.entity.User;
import com.ewha.back.domain.user.service.UserService;
import com.ewha.back.global.dto.SingleResponseDto;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {
	private final QuestionService questionService;
	private final QuestionMapper questionMapper;
	private final AnswerService answerService;
	private final UserService userService;
	private final AwsS3Service awsS3Service;

	@PostMapping("/add")
	public ResponseEntity postQuestion(@Nullable @RequestParam(value = "image") MultipartFile multipartFile,
		@Valid @RequestPart QuestionDto.Post postQuestion) throws Exception {

		List<String> imagePath = null;

		Question question = questionMapper.questionPostToQuestion(postQuestion);
		Question createdQuestion = questionService.createQuestion(question);
		if (multipartFile != null)
			imagePath = awsS3Service.uploadQuestionImageToS3(multipartFile, createdQuestion.getId());
		createdQuestion.addImagePaths(imagePath.get(0), imagePath.get(1));
		QuestionDto.Response response = questionMapper.questionToQuestionResponse(createdQuestion);

		return new ResponseEntity<>(
			new SingleResponseDto<>(response), HttpStatus.CREATED
		);
	}

	@PatchMapping("/{question_id}/edit")
	public ResponseEntity patchQuestion(@PathVariable("question_id") Long questionId,
		@Nullable @RequestParam(value = "image") MultipartFile multipartFile,
		@Valid @RequestPart QuestionDto.Patch patchQuestion) throws Exception {

		List<String> imagePath = null;

		if (multipartFile != null) {
			imagePath = awsS3Service.updateORDeleteQuestionImageFromS3(questionId, multipartFile);
		}

		patchQuestion.setImagePath(imagePath.get(0));
		patchQuestion.setThumbnailPath(imagePath.get(1));
		Question question = questionMapper.questionPatchToQuestion(patchQuestion);
		Question updatedQuestion = questionService.updateQuestion(question, questionId);
		QuestionDto.Response response = questionMapper.questionToQuestionResponse(updatedQuestion);

		return new ResponseEntity<>(
			new SingleResponseDto<>(response), HttpStatus.CREATED
		);
	}

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
