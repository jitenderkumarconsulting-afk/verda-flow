package com.org.verdaflow.rest.common.model;

import com.org.verdaflow.rest.common.enums.ApplicationStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserRoleModel extends IdModel {

	private String name;
	private ApplicationStatus applicationStatus;

	public UserRoleModel(Integer id, String name, ApplicationStatus applicationStatus) {
		super(id);
		this.name = name;
		this.applicationStatus = applicationStatus;
	}

}
