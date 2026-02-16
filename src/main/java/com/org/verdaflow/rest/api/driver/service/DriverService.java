package com.org.verdaflow.rest.api.driver.service;

import org.springframework.data.domain.Pageable;

import com.org.verdaflow.rest.api.admin.model.UserDetailModel;
import com.org.verdaflow.rest.api.dispatcher.model.DriverInventoryModel;
import com.org.verdaflow.rest.api.driver.form.UpdateOrderEtaForm;
import com.org.verdaflow.rest.api.driver.model.OrdersCountModel;
import com.org.verdaflow.rest.api.user.form.RateOrderForm;
import com.org.verdaflow.rest.api.user.model.OrderModel;
import com.org.verdaflow.rest.config.security.jwt.JwtUser;
import com.org.verdaflow.rest.dto.PaginatedResponse;

public interface DriverService {

	/**
	 * Fetch driver profile details.
	 * 
	 * @param jwtUser
	 * @return
	 */
	UserDetailModel getProfile(JwtUser jwtUser);

	/**
	 * Fetch dispatcher details.
	 * 
	 * @param jwtUser
	 * @return
	 */
	UserDetailModel getDispatcherDetails(JwtUser jwtUser);

	/**
	 * List Orders.
	 *
	 * @param pageable
	 * @param filter
	 * @param jwtUser
	 * @return
	 */
	PaginatedResponse<OrderModel> listOrders(Pageable pageable, int filter, JwtUser jwtUser);

	/**
	 * Accept Order.
	 * 
	 * @param orderId
	 * @param reject
	 * @param jwtUser
	 * @return
	 */
	OrderModel acceptOrder(int orderId, boolean reject, JwtUser jwtUser);

	/**
	 * Update Order ETA.
	 * 
	 * @param updateOrderEtaForm
	 * @param jwtUser
	 * @return
	 */
	OrderModel updateOrderEta(UpdateOrderEtaForm updateOrderEtaForm, JwtUser jwtUser);

	/**
	 * Get Orders Count.
	 * 
	 * @param jwtUser
	 * @return
	 */
	OrdersCountModel ordersCount(JwtUser jwtUser);

	/**
	 * Complete Order.
	 * 
	 * @param orderId
	 * @param jwtUser
	 * @return
	 */
	OrderModel completeOrder(int orderId, JwtUser jwtUser);

	/**
	 * Online or Offline status.
	 * 
	 * @param offline
	 * @param jwtUser
	 * @return
	 */
	boolean onlineStatus(boolean offline, JwtUser jwtUser);

	/**
	 * Rate the customer user for particular order.
	 * 
	 * @param rateOrderForm
	 * @param jwtUser
	 * @return
	 */
	boolean rateOrder(RateOrderForm rateOrderForm, JwtUser jwtUser);

	/**
	 * List Driver Inventories.
	 *
	 * @param pageable
	 * @param filter
	 * @param query
	 * @param jwtUser
	 * @return
	 */
	PaginatedResponse<DriverInventoryModel> listDriverInventories(Pageable pageable, int filter, String query,
			JwtUser jwtUser);

}
