package com.org.verdaflow.rest.api.user.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.org.verdaflow.rest.common.model.IdModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(value = Include.NON_NULL)
public class UserRatingDetailModel extends IdModel {

	private Integer userId;
	private Float avg;
	private Integer count;

	public UserRatingDetailModel(Integer id, Integer userId, Float avg, int count) {
		super(id);
		this.userId = userId;
		this.avg = avg;
		this.count = count;
	}

}
