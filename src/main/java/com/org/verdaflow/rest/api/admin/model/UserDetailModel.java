package com.org.verdaflow.rest.api.admin.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.org.verdaflow.rest.api.auth.model.UserCustomerDetailModel;
import com.org.verdaflow.rest.api.auth.model.UserDispatcherDetailModel;
import com.org.verdaflow.rest.api.auth.model.UserDriverDetailModel;
import com.org.verdaflow.rest.api.auth.model.UserModel;
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
public class UserDetailModel {

	private UserModel userModel;
	private List<UserRoleModel> userRoles;

	private UserDispatcherDetailModel dispatcherDetails;
	private UserDriverDetailModel driverDetails;
	private UserCustomerDetailModel customerDetails;

	private UserDetailModel dispatcherUserDetails;

	public UserDetailModel(UserModel userModel, List<UserRoleModel> userRoles,
			UserDispatcherDetailModel dispatcherDetails) {
		this.userModel = userModel;
		this.userRoles = userRoles;
		this.dispatcherDetails = dispatcherDetails;
	}

	public UserDetailModel(UserModel userModel, UserDispatcherDetailModel dispatcherDetails) {
		this.userModel = userModel;
		this.dispatcherDetails = dispatcherDetails;
	}

	public UserDetailModel(UserModel userModel, List<UserRoleModel> userRoles, UserDriverDetailModel driverDetails) {
		this.userModel = userModel;
		this.userRoles = userRoles;
		this.driverDetails = driverDetails;
	}

	public UserDetailModel(UserModel userModel, UserDriverDetailModel driverDetails) {
		this.userModel = userModel;
		this.driverDetails = driverDetails;
	}

	public UserDetailModel(UserModel userModel, List<UserRoleModel> userRoles, UserDriverDetailModel driverDetails,
			UserDetailModel dispatcherUserDetails) {
		this.userModel = userModel;
		this.userRoles = userRoles;
		this.driverDetails = driverDetails;
		this.dispatcherUserDetails = dispatcherUserDetails;
	}

	public UserDetailModel(UserModel userModel, List<UserRoleModel> userRoles,
			UserCustomerDetailModel customerDetails) {
		this.userModel = userModel;
		this.userRoles = userRoles;
		this.customerDetails = customerDetails;
	}

	public UserDetailModel(UserModel userModel, UserCustomerDetailModel customerDetails) {
		this.userModel = userModel;
		this.customerDetails = customerDetails;
	}

}
