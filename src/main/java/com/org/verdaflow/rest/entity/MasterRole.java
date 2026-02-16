package com.org.verdaflow.rest.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.org.verdaflow.rest.common.enums.UserRoleEnum;
import com.org.verdaflow.rest.entity.base.BaseEntity;

/**
 * The persistent class for the master_roles database table.
 * 
 */
@Entity
@Table(name = "master_roles")
@NamedQuery(name = "MasterRole.findAll", query = "SELECT m FROM MasterRole m")
public class MasterRole extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "role", nullable = false)
	@Enumerated(EnumType.STRING)
	private UserRoleEnum role;

	// bi-directional many-to-one association to UserRoleMapping
	@OneToMany(mappedBy = "masterRole")
	private List<UserRoleMapping> userRoleMappings;

	public MasterRole() {
	}

	public UserRoleEnum getRole() {
		return role;
	}

	public void setRole(UserRoleEnum role) {
		this.role = role;
	}

	public List<UserRoleMapping> getUserRoleMappings() {
		return this.userRoleMappings;
	}

	public void setUserRoleMappings(List<UserRoleMapping> userRoleMappings) {
		this.userRoleMappings = userRoleMappings;
	}

	public UserRoleMapping addUserRoleMapping(UserRoleMapping userRoleMapping) {
		getUserRoleMappings().add(userRoleMapping);
		userRoleMapping.setMasterRole(this);

		return userRoleMapping;
	}

	public UserRoleMapping removeUserRoleMapping(UserRoleMapping userRoleMapping) {
		getUserRoleMappings().remove(userRoleMapping);
		userRoleMapping.setMasterRole(null);

		return userRoleMapping;
	}

}