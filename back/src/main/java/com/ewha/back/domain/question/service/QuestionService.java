package com.ewha.back.domain.question.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ewha.back.domain.question.entity.Question;
import com.ewha.back.domain.question.repository.AnswerQueryRepository;
import com.ewha.back.domain.question.repository.AnswerRepository;
import com.ewha.back.domain.question.repository.QuestionRepository;
import com.ewha.back.domain.user.entity.User;
import com.ewha.back.domain.user.entity.enums.Role;
import com.ewha.back.domain.user.service.UserService;
import com.ewha.back.global.exception.BusinessLogicException;
import com.ewha.back.global.exception.ExceptionCode;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionService {
	private final UserService userService;
	private final QuestionRepository questionRepository;
	private final AnswerRepository answerRepository;
	private final AnswerQueryRepository answerQueryRepository;

	@Transactional
	public Question createQuestion(Question question) {

		User findUser = userService.getLoginUser();

		if (findUser.getRole().contains(Role.ROLE_ADMIN)) {

			Question savedQuestion = Question.builder()
				.title(question.getTitle())
				.body(question.getBody())
				.imagePath(question.getImagePath())
				.answerBody(question.getAnswerBody())
				.build();

			return questionRepository.save(savedQuestion);
		} else
			throw new BusinessLogicException(ExceptionCode.FORBIDDEN);
	}

	@Transactional
	public Question updateQuestion(Question question, Long questionId) {

		User findUser = userService.getLoginUser();

		Question findQuestion = findVerifiedQuestion(questionId);

		if (findUser.getRole().contains(Role.ROLE_ADMIN)) {

			findQuestion.updateQuestion(question);

			return questionRepository.save(findQuestion);
		} else
			throw new BusinessLogicException(ExceptionCode.FORBIDDEN);
	}

	@Transactional(readOnly = true)
	public Question getQuestion(Long questionId) {

		Question findQuestion = findVerifiedQuestion(questionId);
		return findVerifiedQuestion(questionId);
	}

	@Transactional(readOnly = true)
	public Question getAnsweredQuestion(Long questionId, Long userId) {

		Question findQuestion = findVerifiedQuestion(questionId);

		Question question = Question.builder()
			.title(findQuestion.getTitle())
			.body(findQuestion.getBody())
			.answerBody(findQuestion.getAnswerBody())
			.answers(answerQueryRepository.findByUserId(userId))
			.build();

		return question;
	}

	public Question findVerifiedQuestion(Long questionId) {

		Optional<Question> optionalPairing = questionRepository.findById(questionId);
		return optionalPairing.orElseThrow(() ->
			new BusinessLogicException(ExceptionCode.QUESTION_NOT_FOUND));
	}

	public void saveQuestion(Question question) {
		questionRepository.save(question);
	}
}
