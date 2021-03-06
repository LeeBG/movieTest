package com.cos.movie.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UpdateReqDto {
	@NotNull(message = "fail")
	@NotBlank(message = "fail")
	private String title;
	@NotNull(message = "fail")
	private double rating;
}
