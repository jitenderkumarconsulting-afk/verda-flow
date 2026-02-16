package com.org.verdaflow.rest.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.org.verdaflow.rest.common.enums.DeliveryType;
import com.org.verdaflow.rest.common.enums.OrderStatus;
import com.org.verdaflow.rest.entity.base.BaseEntity;

/**
 * The persistent class for the orders database table.
 * 
 */
@Entity
@Table(name = "orders")
@NamedQuery(name = "Order.findAll", query = "SELECT o FROM Order o")
public class Order extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	// bi-directional many-to-one association to UserEntity
	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;

	// bi-directional many-to-one association to UserDispatcherDetail
	@ManyToOne
	@JoinColumn(name = "dispatcher_id")
	private UserDispatcherDetail userDispatcherDetail;

	// bi-directional many-to-one association to PromoCode
	@ManyToOne
	@JoinColumn(name = "promo_code_id")
	private PromoCode promoCode;

	// bi-directional many-to-one association to CustomerAddressDetail
	@ManyToOne
	@JoinColumn(name = "address_id")
	private CustomerAddressDetail addressDetail;

	@Column(name = "delivery_type")
	@Enumerated(EnumType.STRING)
	private DeliveryType deliveryType = DeliveryType.PICKUP;

	// bi-directional one-to-one association to MasterEta
	@OneToOne
	@JoinColumn(name = "eta_id")
	private MasterEta eta;

	@Column(name = "order_status")
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus = OrderStatus.PROCESSING;

	@Column(name = "cancel_expiry_time")
	private Date cancelExpiryTime;

	@Column(name = "is_placed")
	private boolean isPlaced = false;

	// bi-directional one-to-many association to OrderItemDetail
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
	@Where(clause = "is_deleted = 0")
	private List<OrderItemDetail> orderItemDetails;

	// bi-directional one-to-one association to OrderPriceDetail
	@OneToOne(mappedBy = "order", fetch = FetchType.LAZY)
	@Where(clause = "is_deleted = 0")
	private OrderPriceDetail orderPriceDetail;

	// bi-directional one-to-one association to OrderRatingDetail
	@OneToOne(mappedBy = "order", fetch = FetchType.LAZY)
	@Where(clause = "is_deleted = 0")
	private OrderRatingDetail orderRatingDetail;

	// bi-directional one-to-one association to DriverOrderMapping
	@OneToOne(mappedBy = "order", fetch = FetchType.LAZY)
	@Where(clause = "is_deleted = 0")
	private DriverOrderMapping driverOrderMapping;

	// bi-directional one-to-one association to AuditPromoCode
	@OneToOne(mappedBy = "order", fetch = FetchType.LAZY)
	@Where(clause = "is_deleted = 0")
	private AuditPromoCode auditPromoCode;

	// bi-directional one-to-many association to AuditOrderStatus
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
	@Where(clause = "is_deleted = 0")
	private List<AuditOrderStatus> auditOrderStatus;

	public Order() {
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public UserDispatcherDetail getUserDispatcherDetail() {
		return userDispatcherDetail;
	}

	public void setUserDispatcherDetail(UserDispatcherDetail userDispatcherDetail) {
		this.userDispatcherDetail = userDispatcherDetail;
	}

	public PromoCode getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(PromoCode promoCode) {
		this.promoCode = promoCode;
	}

	public CustomerAddressDetail getAddressDetail() {
		return addressDetail;
	}

	public void setAddressDetail(CustomerAddressDetail addressDetail) {
		this.addressDetail = addressDetail;
	}

	public DeliveryType getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(DeliveryType deliveryType) {
		this.deliveryType = deliveryType;
	}

	public MasterEta getEta() {
		return eta;
	}

	public void setEta(MasterEta eta) {
		this.eta = eta;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Date getCancelExpiryTime() {
		return cancelExpiryTime;
	}

	public void setCancelExpiryTime(Date cancelExpiryTime) {
		this.cancelExpiryTime = cancelExpiryTime;
	}

	public boolean isPlaced() {
		return isPlaced;
	}

	public void setPlaced(boolean isPlaced) {
		this.isPlaced = isPlaced;
	}

	public List<OrderItemDetail> getOrderItemDetails() {
		return orderItemDetails;
	}

	public void setOrderItemDetails(List<OrderItemDetail> orderItemDetails) {
		this.orderItemDetails = orderItemDetails;
	}

	public OrderPriceDetail getOrderPriceDetail() {
		return orderPriceDetail;
	}

	public void setOrderPriceDetail(OrderPriceDetail orderPriceDetail) {
		this.orderPriceDetail = orderPriceDetail;
	}

	public OrderRatingDetail getOrderRatingDetail() {
		return orderRatingDetail;
	}

	public void setOrderRatingDetail(OrderRatingDetail orderRatingDetail) {
		this.orderRatingDetail = orderRatingDetail;
	}

	public DriverOrderMapping getDriverOrderMapping() {
		return driverOrderMapping;
	}

	public void setDriverOrderMapping(DriverOrderMapping driverOrderMapping) {
		this.driverOrderMapping = driverOrderMapping;
	}

	public AuditPromoCode getAuditPromoCode() {
		return auditPromoCode;
	}

	public void setAuditPromoCode(AuditPromoCode auditPromoCode) {
		this.auditPromoCode = auditPromoCode;
	}

	public List<AuditOrderStatus> getAuditOrderStatus() {
		return auditOrderStatus;
	}

	public void setAuditOrderStatus(List<AuditOrderStatus> auditOrderStatus) {
		this.auditOrderStatus = auditOrderStatus;
	}

}
