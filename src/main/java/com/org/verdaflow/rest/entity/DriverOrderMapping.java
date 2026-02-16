package com.org.verdaflow.rest.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.org.verdaflow.rest.entity.base.BaseEntity;

/**
 * The persistent class for the driver_order_mapping database table.
 * 
 */
@Entity
@Table(name = "driver_order_mapping")
@NamedQuery(name = "DriverOrderMapping.findAll", query = "SELECT d FROM DriverOrderMapping d")
public class DriverOrderMapping extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	// bi-directional many-to-one association to UserDriverDetail
	@ManyToOne
	@JoinColumn(name = "driver_id")
	private UserDriverDetail userDriverDetail;

	// bi-directional one-to-one association to Order
	@OneToOne
	@JoinColumn(name = "order_id")
	private Order order;

	@Column(name = "is_rejected")
	private boolean isRejected;

	public UserDriverDetail getUserDriverDetail() {
		return userDriverDetail;
	}

	public void setUserDriverDetail(UserDriverDetail userDriverDetail) {
		this.userDriverDetail = userDriverDetail;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public boolean isRejected() {
		return isRejected;
	}

	public void setRejected(boolean isRejected) {
		this.isRejected = isRejected;
	}

}
