package com.org.verdaflow.rest.entity.base;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@MappedSuperclass
public class AuditEntity {

	@Column(name = "created_at")
	protected Date createdAt;

	@Column(name = "modified_at")
	protected Date modifiedAt;

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	@PrePersist
	protected void onCreate() {
		createdAt = new Date();
		modifiedAt = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		modifiedAt = new Date();
	}
}
