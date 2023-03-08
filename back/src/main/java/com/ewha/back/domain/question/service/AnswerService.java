package com.ewha.back.domain.question.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ewha.back.domain.question.entity.Answer;
import com.ewha.back.domain.question.entity.Question;
import com.ewha.back.domain.question.repository.AnswerQueryRepository;
import com.ewha.back.domain.question.repository.AnswerRepository;
import com.ewha.back.domain.user.entity.User;
import com.ewha.back.domain.user.service.UserService;
import com.ewha.back.global.exception.BusinessLogicException;
import com.ewha.back.global.exception.ExceptionCode;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AnswerService {
	private final UserService userService;
	private final QuestionService questionService;
	private final AnswerRepository answerRepository;
	private final AnswerQueryRepository answerQueryRepository;

	@Transactional
	public Answer createAnswer(Answer answer, Long questionId) {

		User findUser = userService.getLoginUser();
		Question findQuestion = questionService.findVerifiedQuestion(questionId);

		Answer savedAnswer = Answer.builder()
			.question(findQuestion)
			.user(findUser)
			.body(answer.getBody())
			.build();

		return answerRepository.save(savedAnswer);
	}

	@Transactional(readOnly = true)
	public Answer findByQuestionIdAndUserId(Long questionId, Long userId) {
		return answerQueryRepository.findByQuestionIdAndUserId(questionId, userId);
	}

	@Transactional(readOnly = true)
	public Page<Answer> findMyAnswers(int page) {

		User findUser = userService.getLoginUser();

		PageRequest pageRequest = PageRequest.of(page - 1, 10);

		return answerQueryRepository.findMyAnswers(findUser, pageRequest);
	}

	@Transactional(readOnly = true)
	public Answer findVerifiedAnswer(Long answerId) {

		Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
		return optionalAnswer.orElseThrow(() ->
			new BusinessLogicException(ExceptionCode.ANSWER_NOT_FOUND));
	}
}
