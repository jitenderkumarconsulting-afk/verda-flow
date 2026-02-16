package com.org.verdaflow.rest.api.user.model;

import java.util.Date;

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
public class AuditOrderStatusModel extends IdModel {

	private Integer userId;
	private Integer orderId;
	private Integer orderStatus;
	private Date createdAt;

	public AuditOrderStatusModel(Integer id, Integer userId, Integer orderId, Integer orderStatus, Date createdAt) {
		super(id);
		this.userId = userId;
		this.orderId = orderId;
		this.orderStatus = orderStatus;
		this.createdAt = createdAt;
	}

}
