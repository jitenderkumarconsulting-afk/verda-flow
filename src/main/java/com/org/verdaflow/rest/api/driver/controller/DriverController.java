package com.org.verdaflow.rest.api.driver.controller;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.org.verdaflow.rest.api.admin.model.UserDetailModel;
import com.org.verdaflow.rest.api.dispatcher.model.DriverInventoryModel;
import com.org.verdaflow.rest.api.driver.form.UpdateOrderEtaForm;
import com.org.verdaflow.rest.api.driver.model.OrdersCountModel;
import com.org.verdaflow.rest.api.driver.service.DriverService;
import com.org.verdaflow.rest.api.user.form.RateOrderForm;
import com.org.verdaflow.rest.api.user.model.OrderModel;
import com.org.verdaflow.rest.common.model.GenericModel;
import com.org.verdaflow.rest.config.common.AppConst;
import com.org.verdaflow.rest.config.common.StringConst;
import com.org.verdaflow.rest.config.security.jwt.JwtUser;
import com.org.verdaflow.rest.dto.PaginatedResponse;
import com.org.verdaflow.rest.dto.ResponseEnvelope;
import com.org.verdaflow.rest.util.AppUtil;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/driver")
public class DriverController {
	public static final Logger log = LoggerFactory.getLogger(DriverController.class);

	@Autowired
	private DriverService driverService;

	@Autowired
	private Environment env;

	@Autowired
	private AppUtil appUtil;

	/**
	 * This method is used to fetch the driver profile details.
	 * 
	 * @param authtoken
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/profile")
	@ApiOperation(value = "This method is used to fetch the driver profile details.")
	public ResponseEnvelope<UserDetailModel> getProfile(
			@RequestHeader(value = "authtoken", required = true) String authtoken, JwtUser jwtUser) {
		log.info("getProfile");
		return new ResponseEnvelope<>(driverService.getProfile(jwtUser), true, StringConst.EMPTY_STRING,
				AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to fetch the Dispatcher Details.
	 * 
	 * @param authtoken
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/dispatcher")
	@ApiOperation(value = "This method is used to fetch the Dispatcher Details.")
	public ResponseEnvelope<UserDetailModel> getDispatcherDetails(
			@RequestHeader(value = "authtoken", required = true) String authtoken, JwtUser jwtUser) {
		log.info("getDispatcherDetails");
		return new ResponseEnvelope<>(driverService.getDispatcherDetails(jwtUser), true, StringConst.EMPTY_STRING,
				AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to populate the list of orders.
	 * 
	 * @param authtoken
	 * @param page
	 * @param size
	 * @param filter
	 * @param jwtUser
	 * @return
	 */
	@GetMapping("/orders")
	@ApiOperation(value = "This method is used to populate the list of orders.", notes = "Filter Values "
			+ "(Inside 'int' param 'filter') :  "
			+ " 0 = All orders, 1 = All new orders, 2 = All pending orders, 3 = All completed orders")
	public ResponseEnvelope<PaginatedResponse<OrderModel>> listOrders(
			@RequestHeader(value = "authtoken", required = true) String authtoken, @RequestParam("page") int page,
			@RequestParam("size") int size,
			@RequestParam(value = "filter", required = false, defaultValue = "0") int filter, JwtUser jwtUser) {
		log.info("listOrders");

		Pageable pageable = new PageRequest(page - 1, size);
		return new ResponseEnvelope<>(driverService.listOrders(pageable, filter, jwtUser), true,
				StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to accept or reject a Order.
	 * 
	 * @param authtoken
	 * @param orderId
	 * @param reject
	 * @param jwtUser
	 * @return
	 */
	@GetMapping("/acceptOrder")
	@ApiOperation(value = "This method is used to accept or reject a Order.", notes = " PARAM DESCRIPTION  :  "
			+ " reject(boolean) - true (Will reject the Order) ")
	public ResponseEnvelope<OrderModel> acceptOrder(
			@RequestHeader(value = "authtoken", required = true) String authtoken, @RequestParam("orderId") int orderId,
			@RequestParam(value = "reject", required = false) boolean reject, JwtUser jwtUser) {
		log.info("acceptOrder");
		return new ResponseEnvelope<>(driverService.acceptOrder(orderId, reject, jwtUser), true,
				reject ? env.getProperty(StringConst.ORDER_REJECT_DRIVER_SUCCESSFULLY)
						: env.getProperty(StringConst.ORDER_ACCEPT_DRIVER_SUCCESSFULLY),
				AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for updating the ETA(Estimated Time of Arrival) of order.
	 * 
	 * @param authtoken
	 * @param updateOrderEtaForm
	 * @param jwtUser
	 * @return
	 */
	@PatchMapping(value = "/order/eta")
	@ApiOperation(value = "This method is used for updating the ETA(Estimated Time of Arrival) of order.")
	public ResponseEnvelope<OrderModel> updateOrderEta(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestBody @Valid UpdateOrderEtaForm updateOrderEtaForm, JwtUser jwtUser) {
		log.info("updateOrderEta");
		return new ResponseEnvelope<>(driverService.updateOrderEta(updateOrderEtaForm, jwtUser), true,
				env.getProperty(StringConst.UPDATE_ORDER_ETA), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to get the count of New and Pending Orders.
	 * 
	 * @param authtoken
	 * @param jwtUser
	 * @return
	 */
	@GetMapping("/ordersCount")
	@ApiOperation(value = "This method is used to get the count of New and Pending Orders.")
	public ResponseEnvelope<OrdersCountModel> ordersCount(
			@RequestHeader(value = "authtoken", required = true) String authtoken, JwtUser jwtUser) {
		log.info("ordersCount");
		return new ResponseEnvelope<>(driverService.ordersCount(jwtUser), true, StringConst.EMPTY_STRING,
				AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to complete the order when delivered to the Customer.
	 * 
	 * @param authtoken
	 * @param orderId
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/completeOrder/{orderId}")
	@ApiOperation(value = "This method is used to complete the order when delivered to the Customer.")
	public ResponseEnvelope<OrderModel> completeOrder(
			@RequestHeader(value = "authtoken", required = true) String authtoken, @PathVariable("orderId") int orderId,
			JwtUser jwtUser) {
		log.info("completeOrder");
		return new ResponseEnvelope<>(driverService.completeOrder(orderId, jwtUser), true,
				env.getProperty(StringConst.ORDER_COMPLETED_SUCCESSFULLY), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to set online or offline status.
	 * 
	 * @param authtoken
	 * @param offline
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/onlineStatus")
	@ApiOperation(value = "This method is used to set online or offline status.", notes = " PARAM DESCRIPTION  :  "
			+ " offline(boolean) - true (Will set the offline status) ")
	public ResponseEnvelope<GenericModel<Boolean>> onlineStatus(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestParam(value = "offline", required = false) boolean offline, JwtUser jwtUser) {
		log.info("onlineStatus");

		return new ResponseEnvelope<>(new GenericModel<>(driverService.onlineStatus(offline, jwtUser)), true,
				offline ? env.getProperty(StringConst.DRIVER_OFFLINE_SUCCESS)
						: env.getProperty(StringConst.DRIVER_ONLINE_SUCCESS),
				AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for rate the customer user for particular order.
	 * 
	 * @param authtoken
	 * @param rateOrderForm
	 * @param jwtUser
	 * @return
	 */
	@PostMapping(value = "/rateOrder")
	@ApiOperation(value = "This method is used for rate the customer user for particular order.")
	public ResponseEnvelope<GenericModel<Boolean>> rateOrder(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestBody @Valid RateOrderForm rateOrderForm, JwtUser jwtUser) {
		log.info("rateOrder");

		return new ResponseEnvelope<>(new GenericModel<>(driverService.rateOrder(rateOrderForm, jwtUser)), true,
				env.getProperty(StringConst.RATING_SUBMIT_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to populate the list of driver inventories.
	 * 
	 * @param authtoken
	 * @param page
	 * @param size
	 * @param driverUserId
	 * @param filter
	 * @param query
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/driverInventories")
	@ApiOperation(value = "This method is used to populate the list of driver inventories.", notes = "Filter Values "
			+ "(Inside 'int' param 'filter') :  "
			+ " 0 = All driver inventories, 1 = All in stock driver inventories, 2 = All out of stock driver inventories")
	public ResponseEnvelope<PaginatedResponse<DriverInventoryModel>> listDriverInventories(
			@RequestHeader(value = "authtoken", required = true) String authtoken, @RequestParam("page") int page,
			@RequestParam("size") int size,
			@RequestParam(value = "filter", required = false, defaultValue = "0") int filter,
			@RequestParam(value = "query", required = false, defaultValue = StringConst.BLANK_STRING) String query,
			JwtUser jwtUser) {
		log.info("listDriverInventories");
		if (StringUtils.isNotBlank(query))
			appUtil.validateScriptingTagAlert(query);

		Pageable pageable = new PageRequest(page - 1, size);
		return new ResponseEnvelope<>(
				driverService.listDriverInventories(pageable, filter, appUtil.sanatizeQuery(query), jwtUser), true,
				StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

}
