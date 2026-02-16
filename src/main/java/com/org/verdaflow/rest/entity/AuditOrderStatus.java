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

import com.org.verdaflow.rest.common.enums.OrderStatus;
import com.org.verdaflow.rest.entity.base.BaseEntity;

@Entity
@Table(name = "audit_order_status")
@NamedQuery(name = "AuditOrderStatus.findAll", query = "SELECT a FROM AuditOrderStatus a")
public class AuditOrderStatus extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;

	@ManyToOne
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;

	@Column(name = "order_status")
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus = OrderStatus.PROCESSING;

	public AuditOrderStatus() {

	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

}
