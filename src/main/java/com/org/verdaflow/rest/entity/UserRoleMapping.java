package com.org.verdaflow.rest.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.org.verdaflow.rest.common.enums.ApplicationStatus;
import com.org.verdaflow.rest.entity.base.AuditEntity;
import com.org.verdaflow.rest.entity.embeddedId.UserRoleMappingPK;

/**
 * The persistent class for the user_role_mapping database table.
 * 
 */
@Entity
@Table(name = "user_role_mapping")
@NamedQuery(name = "UserRoleMapping.findAll", query = "SELECT u FROM UserRoleMapping u")
public class UserRoleMapping extends AuditEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private UserRoleMappingPK id;

	@Column(name = "is_deleted")
	private boolean isDeleted;

	@Column(name = "application_status")
	@Enumerated(EnumType.STRING)
	private ApplicationStatus applicationStatus = ApplicationStatus.PENDING;

	@ManyToOne
	@JoinColumn(name = "role_id", nullable = false, insertable = false, updatable = false)
	private MasterRole masterRole;

	// bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
	private UserEntity user;

	public UserRoleMapping() {
	}

	public UserRoleMappingPK getId() {
		return this.id;
	}

	public void setId(UserRoleMappingPK id) {
		this.id = id;
	}

	public MasterRole getMasterRole() {
		return this.masterRole;
	}

	public void setMasterRole(MasterRole masterRole) {
		this.masterRole = masterRole;
	}

	public UserEntity getUser() {
		return this.user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public ApplicationStatus getApplicationStatus() {
		return applicationStatus;
	}

	public void setApplicationStatus(ApplicationStatus applicationStatus) {
		this.applicationStatus = applicationStatus;
	}

}
