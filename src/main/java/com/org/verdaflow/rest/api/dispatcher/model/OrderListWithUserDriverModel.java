package com.org.verdaflow.rest.api.dispatcher.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.org.verdaflow.rest.api.admin.model.UserDetailModel;
import com.org.verdaflow.rest.api.user.model.OrderModel;
import com.org.verdaflow.rest.common.model.IdModel;
import com.org.verdaflow.rest.dto.PaginatedResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(value = Include.NON_NULL)
public class OrderListWithUserDriverModel extends IdModel {

	private PaginatedResponse<OrderModel> orders;
	private UserDetailModel userDriver;

}
