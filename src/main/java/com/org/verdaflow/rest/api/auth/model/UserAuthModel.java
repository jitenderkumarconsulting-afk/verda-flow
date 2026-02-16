package com.org.verdaflow.rest.api.auth.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.org.verdaflow.rest.common.model.IdModel;
import com.org.verdaflow.rest.common.model.UserRoleModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(value = Include.NON_NULL)
public class UserAuthModel extends IdModel {

	private String token;
	private Integer roleId;
	private String logInAs;
	private Integer roleStatus;

	private UserModel userModel;
	private List<UserRoleModel> userRoles;

	private UserDispatcherDetailModel dispatcherDetails;
	private UserDriverDetailModel driverDetails;
	private UserCustomerDetailModel customerDetails;

	public UserAuthModel(Integer id, String token, int roleId, String logInAs, Integer roleStatus, UserModel userModel,
			List<UserRoleModel> userRoles, UserDispatcherDetailModel dispatcherDetails,
			UserDriverDetailModel driverDetails, UserCustomerDetailModel customerDetails) {
		super(id);
		this.token = token;
		this.roleId = roleId;
		this.logInAs = logInAs;
		this.roleStatus = roleStatus;
		this.userModel = userModel;
		this.userRoles = userRoles;
		this.dispatcherDetails = dispatcherDetails;
		this.driverDetails = driverDetails;
		this.customerDetails = customerDetails;
	}

}
