package com.org.verdaflow.rest.api.admin.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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
public class UserDriverListWithUserDispatcherModel extends IdModel {

	private PaginatedResponse<UserDetailModel> userDrivers;
	private UserDetailModel userDispatcher;

}
