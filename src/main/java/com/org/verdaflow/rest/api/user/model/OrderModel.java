package com.org.verdaflow.rest.api.user.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.org.verdaflow.rest.api.admin.model.UserDetailModel;
import com.org.verdaflow.rest.api.customer.model.CustomerAddressDetailModel;
import com.org.verdaflow.rest.api.dispatcher.model.PromoCodeModel;
import com.org.verdaflow.rest.api.master.model.MasterModel;
import com.org.verdaflow.rest.common.model.IdModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(value = Include.NON_NULL)
public class OrderModel extends IdModel {

	private Integer deliveryType;
	private CustomerAddressDetailModel customerAddressDetailModel;
	private PromoCodeModel promoCode;
	private List<OrderItemDetailModel> orderItemDetails;
	private OrderPriceDetailModel orderPriceDetail;
	private OrderRatingDetailModel orderRatingDetail;
	private MasterModel eta;
	private Integer orderStatus;
	private Date cancelExpiryTime;
	private Date createdAt;
	private Date modifiedAt;

	private UserDetailModel dispatcherUser;
	private UserDetailModel customerUser;
	private UserDetailModel driverUser;

	private Boolean isFav;

	private List<AuditOrderStatusModel> auditOrderStatus;

	public OrderModel(Integer id, Integer deliveryType, CustomerAddressDetailModel customerAddressDetailModel,
			PromoCodeModel promoCode, List<OrderItemDetailModel> orderItemDetails,
			OrderPriceDetailModel orderPriceDetail, OrderRatingDetailModel orderRatingDetail, MasterModel eta,
			Integer orderStatus, Date cancelExpiryTime, Date createdAt, Date modifiedAt, UserDetailModel dispatcherUser,
			UserDetailModel driverUser, List<AuditOrderStatusModel> auditOrderStatus) {
		super(id);
		this.deliveryType = deliveryType;
		this.customerAddressDetailModel = customerAddressDetailModel;
		this.promoCode = promoCode;
		this.orderItemDetails = orderItemDetails;
		this.orderPriceDetail = orderPriceDetail;
		this.eta = eta;
		this.orderStatus = orderStatus;
		this.cancelExpiryTime = cancelExpiryTime;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
		this.dispatcherUser = dispatcherUser;
		this.driverUser = driverUser;
		this.auditOrderStatus = auditOrderStatus;
	}

	public OrderModel(int id, int deliveryType, CustomerAddressDetailModel customerAddressDetailModel,
			PromoCodeModel promoCode, List<OrderItemDetailModel> orderItemDetails,
			OrderPriceDetailModel orderPriceDetail, OrderRatingDetailModel orderRatingDetail, MasterModel eta,
			int orderStatus, Date cancelExpiryTime, Date createdAt, Date modifiedAt, UserDetailModel dispatcherUser,
			UserDetailModel driverUser, boolean isFav, List<AuditOrderStatusModel> auditOrderStatus) {
		super(id);
		this.deliveryType = deliveryType;
		this.customerAddressDetailModel = customerAddressDetailModel;
		this.promoCode = promoCode;
		this.orderItemDetails = orderItemDetails;
		this.orderPriceDetail = orderPriceDetail;
		this.orderRatingDetail = orderRatingDetail;
		this.eta = eta;
		this.orderStatus = orderStatus;
		this.cancelExpiryTime = cancelExpiryTime;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
		this.dispatcherUser = dispatcherUser;
		this.driverUser = driverUser;
		this.isFav = isFav;
		this.auditOrderStatus = auditOrderStatus;
	}

	public OrderModel(int id, int deliveryType, CustomerAddressDetailModel customerAddressDetailModel,
			PromoCodeModel promoCode, List<OrderItemDetailModel> orderItemDetails,
			OrderPriceDetailModel orderPriceDetail, OrderRatingDetailModel orderRatingDetail, MasterModel eta,
			int orderStatus, Date createdAt, Date modifiedAt, UserDetailModel customerUser, UserDetailModel driverUser,
			List<AuditOrderStatusModel> auditOrderStatus) {
		super(id);
		this.deliveryType = deliveryType;
		this.customerAddressDetailModel = customerAddressDetailModel;
		this.promoCode = promoCode;
		this.orderItemDetails = orderItemDetails;
		this.orderPriceDetail = orderPriceDetail;
		this.orderRatingDetail = orderRatingDetail;
		this.eta = eta;
		this.orderStatus = orderStatus;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
		this.customerUser = customerUser;
		this.driverUser = driverUser;
		this.auditOrderStatus = auditOrderStatus;
	}

	public OrderModel(int id, int deliveryType, CustomerAddressDetailModel customerAddressDetailModel,
			PromoCodeModel promoCode, List<OrderItemDetailModel> orderItemDetails,
			OrderPriceDetailModel orderPriceDetail, OrderRatingDetailModel orderRatingDetail, MasterModel eta,
			int orderStatus, UserDetailModel customerUser, UserDetailModel dispatcherUser, Date createdAt,
			Date modifiedAt, List<AuditOrderStatusModel> auditOrderStatus) {
		super(id);
		this.deliveryType = deliveryType;
		this.customerAddressDetailModel = customerAddressDetailModel;
		this.promoCode = promoCode;
		this.orderItemDetails = orderItemDetails;
		this.orderPriceDetail = orderPriceDetail;
		this.orderRatingDetail = orderRatingDetail;
		this.eta = eta;
		this.orderStatus = orderStatus;
		this.customerUser = customerUser;
		this.dispatcherUser = dispatcherUser;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
		this.auditOrderStatus = auditOrderStatus;
	}

}
