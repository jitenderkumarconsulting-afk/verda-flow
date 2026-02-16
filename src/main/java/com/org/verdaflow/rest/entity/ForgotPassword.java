package com.org.verdaflow.rest.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.org.verdaflow.rest.common.enums.ForgotPasswordRequest;
import com.org.verdaflow.rest.entity.base.BaseEntity;

@Entity
@Table(name = "forgot_password")
@NamedQuery(name = "ForgotPassword.findAll", query = "SELECT f FROM ForgotPassword f")
public class ForgotPassword extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "expires_at")
	private Date expiresAt;

	@Column(name = "verification_key", length = 100)
	private String verificationKey;

	// bi-directional many-to-one association to UserEntity
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;

	@Column(name = "is_admin")
	private boolean isAdmin = Boolean.FALSE;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private ForgotPasswordRequest status = ForgotPasswordRequest.REQUESTED;

	public ForgotPassword() {
	}

	public Date getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(Date expiresAt) {
		this.expiresAt = expiresAt;
	}

	public String getVerificationKey() {
		return verificationKey;
	}

	public void setVerificationKey(String verificationKey) {
		this.verificationKey = verificationKey;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public ForgotPasswordRequest getStatus() {
		return status;
	}

	public void setStatus(ForgotPasswordRequest status) {
		this.status = status;
	}
}
