package com.org.verdaflow.rest.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.org.verdaflow.rest.common.enums.DeviceType;
import com.org.verdaflow.rest.entity.base.BaseEntity;

/**
 * The persistent class for the device_details database table.
 * 
 */
@Entity
@Table(name = "device_details")
@NamedQuery(name = "DeviceDetail.findAll", query = "SELECT d FROM DeviceDetail d")
public class DeviceDetail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "device_token")
	private String deviceToken;

	@Basic
	@Column(name = "device_type")
	@Enumerated(EnumType.STRING)
	private DeviceType deviceType;

	// bi-directional many-to-one association to User
	@ManyToOne
	private UserEntity user;

	public DeviceDetail() {
	}

	public String getDeviceToken() {
		return this.deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}

	public UserEntity getUser() {
		return this.user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

}
