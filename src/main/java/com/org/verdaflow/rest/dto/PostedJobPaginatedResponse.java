package com.org.verdaflow.rest.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostedJobPaginatedResponse<T> {

	@JsonProperty("contentList")
	private List<T> feedModelList;
	private int totalPages;
	private long numberOfResults;
	private boolean feedbackStatus;
}
