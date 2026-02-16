package com.org.verdaflow.rest.entity.embeddedId;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the user_role_mapping database table.
 * 
 */
@Embeddable
public class UserRoleMappingPK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "user_id", insertable = false, updatable = false)
	private int userId;

	@Column(name = "role_id", insertable = false, updatable = false)
	private int roleId;

	public UserRoleMappingPK() {
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getRoleId() {
		return this.roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof UserRoleMappingPK)) {
			return false;
		}
		UserRoleMappingPK castOther = (UserRoleMappingPK) other;
		return (this.userId == castOther.userId) && (this.roleId == castOther.roleId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.userId;
		hash = hash * prime + this.roleId;

		return hash;
	}
}
