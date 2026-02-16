package com.org.verdaflow.rest.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.org.verdaflow.rest.common.enums.LoginAttempt;
import com.org.verdaflow.rest.entity.base.BaseEntity;

@Entity
@Table(name = "audit_login")
@NamedQuery(name = "AuditLogin.findAll", query = "SELECT d FROM AuditLogin d")
public class AuditLogin extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private LoginAttempt status = LoginAttempt.FAILED;

	public AuditLogin() {
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public LoginAttempt getStatus() {
		return status;
	}

	public void setStatus(LoginAttempt status) {
		this.status = status;
	}

}
