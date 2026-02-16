package com.org.verdaflow.rest.api.user.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.org.verdaflow.rest.api.admin.model.UserDetailModel;
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
public class AuditRatingModel extends IdModel {

	private OrderRatingDetailModel orderRatingDetail;
	private Integer userId;
	private UserDetailModel byUserDetail;
	private Float rating;
	private String note;

	public AuditRatingModel(Integer id, OrderRatingDetailModel orderRatingDetail, Integer userId,
			UserDetailModel byUserDetail, Float rating, String note) {
		super(id);
		this.orderRatingDetail = orderRatingDetail;
		this.userId = userId;
		this.byUserDetail = byUserDetail;
		this.rating = rating;
		this.note = note;
	}

	public AuditRatingModel(Integer id, Integer userId, UserDetailModel byUserDetail, Float rating, String note) {
		super(id);
		this.userId = userId;
		this.byUserDetail = byUserDetail;
		this.rating = rating;
		this.note = note;
	}

}
