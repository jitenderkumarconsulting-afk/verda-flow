package com.org.verdaflow.rest.api.user.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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
public class OrderRatingDetailModel extends IdModel {

	private Integer orderId;
	private Boolean isDispatcherPendingByCustomer;
	private Boolean isDriverPendingByCustomer;
	private Boolean isCustomerPendingByDispatcher;
	private Boolean isCustomerPendingByDriver;

	private Float dispatcherRatingByCustomer;
	private Float driverRatingByCustomer;
	private Float customerRatingByDispatcher;
	private Float customerRatingByDriver;

	public OrderRatingDetailModel(Integer id, Integer orderId, Boolean isDispatcherPendingByCustomer,
			Boolean isDriverPendingByCustomer, Boolean isCustomerPendingByDispatcher, Boolean isCustomerPendingByDriver,
			Float dispatcherRatingByCustomer, Float driverRatingByCustomer, Float customerRatingByDispatcher,
			Float customerRatingByDriver) {
		super(id);
		this.orderId = orderId;
		this.isDispatcherPendingByCustomer = isDispatcherPendingByCustomer;
		this.isDriverPendingByCustomer = isDriverPendingByCustomer;
		this.isCustomerPendingByDispatcher = isCustomerPendingByDispatcher;
		this.isCustomerPendingByDriver = isCustomerPendingByDriver;
		this.dispatcherRatingByCustomer = dispatcherRatingByCustomer;
		this.driverRatingByCustomer = driverRatingByCustomer;
		this.customerRatingByDispatcher = customerRatingByDispatcher;
		this.customerRatingByDriver = customerRatingByDriver;
	}

	public OrderRatingDetailModel(Integer id, Integer orderId) {
		super(id);
		this.orderId = orderId;
	}

}
