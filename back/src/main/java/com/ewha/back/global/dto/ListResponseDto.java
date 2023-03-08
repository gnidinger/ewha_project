package com.ewha.back.global.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ListResponseDto<T> {
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Long listCount;
	private List<T> data;

	public ListResponseDto(List<T> data) {
		this.data = data;
	}
}
