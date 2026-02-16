package com.org.verdaflow.rest.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.org.verdaflow.rest.entity.base.BaseEntity;

/**
 * The persistent class for the order_rating_details database table.
 * 
 */
@Entity
@Table(name = "order_rating_details")
@NamedQuery(name = "OrderRatingDetail.findAll", query = "SELECT d FROM OrderRatingDetail d")
public class OrderRatingDetail extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	// bi-directional one-to-one association to Order
	@OneToOne
	@JoinColumn(name = "order_id")
	private Order order;

	@Column(name = "is_dispatcher_pending_by_customer")
	private boolean isDispatcherPendingByCustomer;

	@Column(name = "is_driver_pending_by_customer")
	private boolean isDriverPendingByCustomer;

	@Column(name = "is_customer_pending_by_dispatcher")
	private boolean isCustomerPendingByDispatcher;

	@Column(name = "is_customer_pending_by_driver")
	private boolean isCustomerPendingByDriver;

	@Column(name = "dispatcher_rating_by_customer")
	private float dispatcherRatingByCustomer;

	@Column(name = "driver_rating_by_customer")
	private float driverRatingByCustomer;

	@Column(name = "customer_rating_by_dispatcher")
	private float customerRatingByDispatcher;

	@Column(name = "customer_rating_by_driver")
	private float customerRatingByDriver;

	// bi-directional one-to-many association to AuditRating
	@OneToMany(mappedBy = "orderRatingDetail", fetch = FetchType.LAZY)
	@Where(clause = "is_deleted = 0")
	private List<AuditRating> auditRatings;

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public boolean isDispatcherPendingByCustomer() {
		return isDispatcherPendingByCustomer;
	}

	public void setDispatcherPendingByCustomer(boolean isDispatcherPendingByCustomer) {
		this.isDispatcherPendingByCustomer = isDispatcherPendingByCustomer;
	}

	public boolean isDriverPendingByCustomer() {
		return isDriverPendingByCustomer;
	}

	public void setDriverPendingByCustomer(boolean isDriverPendingByCustomer) {
		this.isDriverPendingByCustomer = isDriverPendingByCustomer;
	}

	public boolean isCustomerPendingByDispatcher() {
		return isCustomerPendingByDispatcher;
	}

	public void setCustomerPendingByDispatcher(boolean isCustomerPendingByDispatcher) {
		this.isCustomerPendingByDispatcher = isCustomerPendingByDispatcher;
	}

	public boolean isCustomerPendingByDriver() {
		return isCustomerPendingByDriver;
	}

	public void setCustomerPendingByDriver(boolean isCustomerPendingByDriver) {
		this.isCustomerPendingByDriver = isCustomerPendingByDriver;
	}

	public float getDispatcherRatingByCustomer() {
		return dispatcherRatingByCustomer;
	}

	public void setDispatcherRatingByCustomer(float dispatcherRatingByCustomer) {
		this.dispatcherRatingByCustomer = dispatcherRatingByCustomer;
	}

	public float getDriverRatingByCustomer() {
		return driverRatingByCustomer;
	}

	public void setDriverRatingByCustomer(float driverRatingByCustomer) {
		this.driverRatingByCustomer = driverRatingByCustomer;
	}

	public float getCustomerRatingByDispatcher() {
		return customerRatingByDispatcher;
	}

	public void setCustomerRatingByDispatcher(float customerRatingByDispatcher) {
		this.customerRatingByDispatcher = customerRatingByDispatcher;
	}

	public float getCustomerRatingByDriver() {
		return customerRatingByDriver;
	}

	public void setCustomerRatingByDriver(float customerRatingByDriver) {
		this.customerRatingByDriver = customerRatingByDriver;
	}

	public List<AuditRating> getAuditRatings() {
		return auditRatings;
	}

	public void setAuditRatings(List<AuditRating> auditRatings) {
		this.auditRatings = auditRatings;
	}

}
