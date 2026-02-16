package com.org.verdaflow.rest.api.dispatcher.controller;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.org.verdaflow.rest.api.admin.model.OrderListWithUserCustomerModel;
import com.org.verdaflow.rest.api.admin.model.UserDetailModel;
import com.org.verdaflow.rest.api.auth.model.UserDriverDetailModel;
import com.org.verdaflow.rest.api.dispatcher.form.AssignOrderDriverForm;
import com.org.verdaflow.rest.api.dispatcher.form.DriverInventoryForm;
import com.org.verdaflow.rest.api.dispatcher.form.ProductForm;
import com.org.verdaflow.rest.api.dispatcher.form.PromoCodeForm;
import com.org.verdaflow.rest.api.dispatcher.form.RegisterDriverForm;
import com.org.verdaflow.rest.api.dispatcher.model.DriverInventoryModel;
import com.org.verdaflow.rest.api.dispatcher.model.GraphCountModel;
import com.org.verdaflow.rest.api.dispatcher.model.OrderListWithUserDriverModel;
import com.org.verdaflow.rest.api.dispatcher.model.ProductModel;
import com.org.verdaflow.rest.api.dispatcher.model.PromoCodeModel;
import com.org.verdaflow.rest.api.dispatcher.service.DispatcherService;
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
@RequestMapping("/api/v1/dispatcher")
public class DispatcherController {
	public static final Logger log = LoggerFactory.getLogger(DispatcherController.class);

	@Autowired
	private DispatcherService dispatcherService;

	@Autowired
	private Environment env;

	@Autowired
	private AppUtil appUtil;

	/**
	 * This method is used to fetch the dispatcher profile details.
	 * 
	 * @param authtoken
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/profile")
	@ApiOperation(value = "This method is used to fetch the dispatcher profile details.")
	public ResponseEnvelope<UserDetailModel> getProfile(
			@RequestHeader(value = "authtoken", required = true) String authtoken, JwtUser jwtUser) {
		log.info("getProfile");
		return new ResponseEnvelope<>(dispatcherService.getProfile(jwtUser), true, StringConst.EMPTY_STRING,
				AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for registering the driver into the application.
	 * 
	 * @param authtoken
	 * @param registerDriverForm
	 * @param jwtUser
	 * @return
	 */
	@PostMapping(value = "/driver")
	@ApiOperation(value = "This method is used for registering the driver into the application.")
	public ResponseEnvelope<GenericModel<Boolean>> registerDriver(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestBody @Valid RegisterDriverForm registerDriverForm, JwtUser jwtUser) {
		log.info("registerDriver");
		return new ResponseEnvelope<>(new GenericModel<>(dispatcherService.registerDriver(registerDriverForm, jwtUser)),
				true, env.getProperty(StringConst.REGISTER_DRIVER), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for updating the driver details into the application.
	 * 
	 * @param authtoken
	 * @param registerDriverForm
	 * @param jwtUser
	 * @return
	 */
	@PatchMapping(value = "/driver")
	@ApiOperation(value = "This method is used for updating the driver details into the application.")
	public ResponseEnvelope<UserDetailModel> updateDriverDetails(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestBody @Valid RegisterDriverForm registerDriverForm, JwtUser jwtUser) {
		log.info("updateDriverDetails");
		return new ResponseEnvelope<>(dispatcherService.updateDriverDetails(registerDriverForm, jwtUser), true,
				env.getProperty(StringConst.UPDATE_DRIVER), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to populate the list of driver users application received
	 * on the platform.
	 *
	 * @param authtoken
	 * @param page
	 * @param size
	 * @param query
	 * @param filter
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/users/driver")
	@ApiOperation(value = "This method is used to populate the list of driver users application received on the platform.", notes = "Filter Values "
			+ "(Inside 'int' param 'filter') :  "
			+ " 0 = All driver users, 1 = All approved driver users, 2 = All pending driver users, 3 = All rejected driver users")
	public ResponseEnvelope<PaginatedResponse<UserDetailModel>> listDriverUsers(
			@RequestHeader(value = "authtoken", required = true) String authtoken, @RequestParam("page") int page,
			@RequestParam("size") int size,
			@RequestParam(value = "query", required = false, defaultValue = StringConst.BLANK_STRING) String query,
			@RequestParam(value = "filter", required = false, defaultValue = "0") int filter, JwtUser jwtUser) {
		log.info("listDriverUsers");
		if (StringUtils.isNotBlank(query))
			appUtil.validateScriptingTagAlert(query);

		Pageable pageable = new PageRequest(page - 1, size);
		return new ResponseEnvelope<>(
				dispatcherService.listDriverUsers(pageable, appUtil.sanatizeQuery(query), filter, jwtUser), true,
				StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to fetch the details of a driver.
	 *
	 * @param authtoken
	 * @param driverUserId
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/driver/{driverUserId}")
	@ApiOperation(value = " This method is used to fetch the details of a driver.")
	public ResponseEnvelope<UserDetailModel> driverDetails(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@PathVariable(value = "driverUserId") int driverUserId, JwtUser jwtUser) {
		log.info("driverDetails");
		return new ResponseEnvelope<>(dispatcherService.driverDetails(driverUserId, jwtUser), true,
				StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for adding the new product.
	 * 
	 * @param authtoken
	 * @param productForm
	 * @param jwtUser
	 * @return
	 */
	@PostMapping(value = "/product")
	@ApiOperation(value = "This method is used for adding the new product.")
	public ResponseEnvelope<GenericModel<Boolean>> addProduct(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestBody @Valid ProductForm productForm, JwtUser jwtUser) {
		log.info("addProduct");
		return new ResponseEnvelope<>(new GenericModel<>(dispatcherService.addProduct(productForm, jwtUser)), true,
				env.getProperty(StringConst.ADD_PRODUCT_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for updating the product.
	 * 
	 * @param authtoken
	 * @param productForm
	 * @param jwtUser
	 * @return
	 */
	@PatchMapping(value = "/product")
	@ApiOperation(value = "This method is used for updating the product.")
	public ResponseEnvelope<ProductModel> updateProduct(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestBody @Valid ProductForm productForm, JwtUser jwtUser) {
		log.info("updateProduct");
		return new ResponseEnvelope<>(dispatcherService.updateProduct(productForm, jwtUser), true,
				env.getProperty(StringConst.UPDATE_PRODUCT_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to populate the list of Products.
	 *
	 * @param authtoken
	 * @param page
	 * @param size
	 * @param query
	 * @param filter
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/products")
	@ApiOperation(value = "This method is used to populate the list of Products.", notes = "Filter Values "
			+ "(Inside 'int' param 'filter') :  "
			+ " 0 = All products, 1 = All vape oil/cartridges products, 2 = All edibles products")
	public ResponseEnvelope<PaginatedResponse<ProductModel>> listProducts(
			@RequestHeader(value = "authtoken", required = true) String authtoken, @RequestParam("page") int page,
			@RequestParam("size") int size,
			@RequestParam(value = "query", required = false, defaultValue = StringConst.BLANK_STRING) String query,
			@RequestParam(value = "filter", required = false, defaultValue = "0") int filter, JwtUser jwtUser) {
		log.info("listProducts");
		if (StringUtils.isNotBlank(query))
			appUtil.validateScriptingTagAlert(query);

		Pageable pageable = new PageRequest(page - 1, size);
		return new ResponseEnvelope<>(
				dispatcherService.listProducts(pageable, appUtil.sanatizeQuery(query), filter, jwtUser), true,
				StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to fetch the details of a product.
	 *
	 * @param authtoken
	 * @param productId
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/product/{productId}")
	@ApiOperation(value = "This method is used to fetch the details of a product.")
	public ResponseEnvelope<ProductModel> productDetails(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@PathVariable(value = "productId") int productId, JwtUser jwtUser) {
		log.info("productDetails");
		return new ResponseEnvelope<>(dispatcherService.productDetails(productId, jwtUser), true,
				StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to delete the product.
	 *
	 * @param authtoken
	 * @param productId
	 * @param jwtUser
	 * @return
	 */
	@DeleteMapping(value = "/product/{productId}")
	@ApiOperation(value = "This method is used to delete the product.")
	public ResponseEnvelope<GenericModel<Boolean>> deleteProduct(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@PathVariable("productId") int productId, JwtUser jwtUser) {
		log.info("deleteProduct");

		return new ResponseEnvelope<>(new GenericModel<>(dispatcherService.deleteProduct(productId, jwtUser)), true,
				env.getProperty(StringConst.DELETE_PRODUCT_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for adding the new promo code.
	 * 
	 * @param authtoken
	 * @param promoCodeForm
	 * @param jwtUser
	 * @return
	 */
	@PostMapping(value = "/promoCode")
	@ApiOperation(value = "This method is used for adding the new promo code.")
	public ResponseEnvelope<GenericModel<Boolean>> addPromoCode(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestBody @Valid PromoCodeForm promoCodeForm, JwtUser jwtUser) {
		log.info("addPromoCode");
		return new ResponseEnvelope<>(new GenericModel<>(dispatcherService.addPromoCode(promoCodeForm, jwtUser)), true,
				env.getProperty(StringConst.CREATE_PROMOCODE_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for updating the promo code.
	 * 
	 * @param authtoken
	 * @param promoCodeForm
	 * @param jwtUser
	 * @return
	 */
	@PatchMapping(value = "/promoCode")
	@ApiOperation(value = "This method is used for updating the promo code.")
	public ResponseEnvelope<PromoCodeModel> updatePromoCode(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestBody @Valid PromoCodeForm promoCodeForm, JwtUser jwtUser) {
		log.info("updatePromoCode");
		return new ResponseEnvelope<>(dispatcherService.updatePromoCode(promoCodeForm, jwtUser), true,
				env.getProperty(StringConst.UPDATE_PROMOCODE_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to delete the promo code.
	 *
	 * @param authtoken
	 * @param promoCodeId
	 * @param jwtUser
	 * @return
	 */
	@DeleteMapping(value = "/promoCode/{promoCodeId}")
	@ApiOperation(value = "This method is used to delete the promoCode.")
	public ResponseEnvelope<GenericModel<Boolean>> deletePromoCode(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@PathVariable("promoCodeId") int promoCodeId, JwtUser jwtUser) {
		log.info("deletePromoCode");

		return new ResponseEnvelope<>(new GenericModel<>(dispatcherService.deletePromoCode(promoCodeId, jwtUser)), true,
				env.getProperty(StringConst.DELETE_PROMOCODE_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to populate the list of promo codes.
	 *
	 * @param authtoken
	 * @param page
	 * @param size
	 * @param query
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/promoCodes")
	@ApiOperation(value = "This method is used to populate the list of promo codes.")
	public ResponseEnvelope<PaginatedResponse<PromoCodeModel>> listPromoCodes(
			@RequestHeader(value = "authtoken", required = true) String authtoken, @RequestParam("page") int page,
			@RequestParam("size") int size,
			@RequestParam(value = "query", required = false, defaultValue = StringConst.BLANK_STRING) String query,
			JwtUser jwtUser) {
		log.info("listPromoCodes");
		if (StringUtils.isNotBlank(query))
			appUtil.validateScriptingTagAlert(query);

		Pageable pageable = new PageRequest(page - 1, size);
		return new ResponseEnvelope<>(dispatcherService.listPromoCodes(pageable, appUtil.sanatizeQuery(query), jwtUser),
				true, StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to fetch the details of a promo code.
	 *
	 * @param authtoken
	 * @param promoCodeId
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/promoCode/{promoCodeId}")
	@ApiOperation(value = "This method is used to fetch the details of a promo code.")
	public ResponseEnvelope<PromoCodeModel> promoCodeDetails(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@PathVariable(value = "promoCodeId") int promoCodeId, JwtUser jwtUser) {
		log.info("promoCodeDetails");
		return new ResponseEnvelope<>(dispatcherService.promoCodeDetails(promoCodeId, jwtUser), true,
				StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to activate or deactivate a Driver.
	 * 
	 * @param authtoken
	 * @param driverUserId
	 * @param deact
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/activateDriver/{driverUserId}")
	@ApiOperation(value = "This method is used to activate or deactivate a Driver", notes = " PARAM DESCRIPTION  :  "
			+ " deact(boolean) - true (Will deactivate the Driver) ")
	public ResponseEnvelope<UserDetailModel> activateDriver(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@PathVariable("driverUserId") int driverUserId,
			@RequestParam(value = "deact", required = false) boolean deact, JwtUser jwtUser) {
		log.info("activateDriver");

		return new ResponseEnvelope<>(dispatcherService.activateDriver(driverUserId, deact, jwtUser), true,
				deact ? env.getProperty(StringConst.DRIVER_DEACTIVATED_SUCCESS)
						: env.getProperty(StringConst.DRIVER_ACTIVATED_SUCCESS),
				AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to populate the list of orders.
	 * 
	 * @param authtoken
	 * @param page
	 * @param size
	 * @param filter
	 * @param query
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/orders")
	@ApiOperation(value = "This method is used to populate the list of orders.", notes = "Filter Values "
			+ "(Inside 'int' param 'filter') :  "
			+ " 0 = All orders, 1 = All new and pending orders, 2 = All completed orders")
	public ResponseEnvelope<PaginatedResponse<OrderModel>> listOrders(
			@RequestHeader(value = "authtoken", required = true) String authtoken, @RequestParam("page") int page,
			@RequestParam(value = "filter", required = false, defaultValue = "0") int filter,
			@RequestParam(value = "query", required = false, defaultValue = StringConst.BLANK_STRING) String query,
			@RequestParam("size") int size, JwtUser jwtUser) {
		log.info("listOrders");
		if (StringUtils.isNotBlank(query))
			appUtil.validateScriptingTagAlert(query);

		Pageable pageable = new PageRequest(page - 1, size);
		return new ResponseEnvelope<>(
				dispatcherService.listOrders(pageable, filter, appUtil.sanatizeQuery(query), jwtUser), true,
				StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to populate the list of orders for Driver.
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
	@GetMapping(value = "/orders/driver/{driverUserId}")
	@ApiOperation(value = "This method is used to populate the list of orders for Driver.", notes = "Filter Values "
			+ "(Inside 'int' param 'filter') :  "
			+ " 0 = All orders, 1 = All new orders, 2 = All pending orders, 3 = All completed orders, 4 = All new and pending orders")
	public ResponseEnvelope<OrderListWithUserDriverModel> listOrdersForDriver(
			@RequestHeader(value = "authtoken", required = true) String authtoken, @RequestParam("page") int page,
			@RequestParam("size") int size, @PathVariable(value = "driverUserId") int driverUserId,
			@RequestParam(value = "filter", required = false, defaultValue = "0") int filter,
			@RequestParam(value = "query", required = false, defaultValue = StringConst.BLANK_STRING) String query,
			JwtUser jwtUser) {
		log.info("listOrdersForDriver");
		if (StringUtils.isNotBlank(query))
			appUtil.validateScriptingTagAlert(query);

		Pageable pageable = new PageRequest(page - 1, size);
		return new ResponseEnvelope<>(dispatcherService.listOrdersForDriver(pageable, driverUserId, filter,
				appUtil.sanatizeQuery(query), jwtUser), true, StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to populate the list of orders for Customer with current
	 * dispatcher.
	 * 
	 * @param authtoken
	 * @param page
	 * @param size
	 * @param customerUserId
	 * @param filter
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/orders/customer/{customerUserId}")
	@ApiOperation(value = "This method is used to populate the list of orders for Customer with current dispatcher.", notes = "Filter Values "
			+ "(Inside 'int' param 'filter') :  "
			+ " 0 = All orders, 1 = All new orders, 2 = All pending orders , 3 = All completed orders")
	public ResponseEnvelope<OrderListWithUserCustomerModel> listOrdersForCustomer(
			@RequestHeader(value = "authtoken", required = true) String authtoken, @RequestParam("page") int page,
			@RequestParam("size") int size, @PathVariable(value = "customerUserId") int customerUserId,
			@RequestParam(value = "filter", required = false, defaultValue = "0") int filter, JwtUser jwtUser) {
		log.info("listOrdersForCustomer");

		Pageable pageable = new PageRequest(page - 1, size);
		return new ResponseEnvelope<>(
				dispatcherService.listOrdersForCustomer(pageable, customerUserId, filter, jwtUser), true,
				StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to confirm the order.
	 * 
	 * @param authtoken
	 * @param orderId
	 * @param jwtUser
	 * @return
	 */

	@GetMapping("/confirmOrder/{orderId}")
	@ApiOperation(value = "This method is used to confirm the order.")
	public ResponseEnvelope<OrderModel> confirmOrder(
			@RequestHeader(value = "authtoken", required = true) String authtoken, @PathVariable("orderId") int orderId,
			JwtUser jwtUser) {
		log.info("confirmOrder");
		return new ResponseEnvelope<>(dispatcherService.confirmOrder(orderId, jwtUser), true,
				env.getProperty(StringConst.ORDER_CONFIRMED_SUCCESSFULLY), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to prepare the order.
	 * 
	 * @param authtoken
	 * @param orderId
	 * @param jwtUser
	 * @return
	 */

	@GetMapping("/prepareOrder/{orderId}")
	@ApiOperation(value = "This method is used to prepare the order.")
	public ResponseEnvelope<OrderModel> prepareOrder(
			@RequestHeader(value = "authtoken", required = true) String authtoken, @PathVariable("orderId") int orderId,
			JwtUser jwtUser) {
		log.info("prepareOrder");
		return new ResponseEnvelope<>(dispatcherService.prepareOrder(orderId, jwtUser), true,
				env.getProperty(StringConst.ORDER_PREPARED_SUCCESSFULLY), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to populate the list of activated drivers of a dispatcher
	 * which will be used while assigning the order.
	 * 
	 * @param authtoken
	 * @param page
	 * @param size
	 * @param orderId
	 * @param query
	 * @param jwtUser
	 * 
	 * @return
	 */
	@GetMapping(value = "assignOrder/{orderId}/drivers")
	@ApiOperation(value = "This method is used to populate the list of activated drivers of a dispatcher which will be used while assigning the order.")
	public ResponseEnvelope<PaginatedResponse<UserDriverDetailModel>> listDriversForAssigningOrder(
			@RequestHeader(value = "authtoken", required = true) String authtoken, @RequestParam("page") int page,
			@RequestParam(value = "query", required = false, defaultValue = StringConst.BLANK_STRING) String query,
			@RequestParam("size") int size, @PathVariable("orderId") int orderId, JwtUser jwtUser) {
		log.info("listAssignedDrivers");
		if (StringUtils.isNotBlank(query))
			appUtil.validateScriptingTagAlert(query);

		Pageable pageable = new PageRequest(page - 1, size);
		return new ResponseEnvelope<>(dispatcherService.listDriversForAssigningOrder(pageable, orderId,
				appUtil.sanatizeQuery(query), jwtUser), true, StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for assigning order to driver from Web.
	 * 
	 * @param authtoken
	 * @param assignOrderDriverForm
	 * @param jwtUser
	 * @return
	 */
	@PostMapping(value = "order/assignDriver")
	@ApiOperation(value = "This method is used for assigning order to driver from Web.")
	public ResponseEnvelope<UserDriverDetailModel> assignDriverToOrderFromWeb(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestBody @Valid AssignOrderDriverForm assignOrderDriverForm, JwtUser jwtUser) {
		log.info("assignDriverToOrderFromWeb");
		return new ResponseEnvelope<>(dispatcherService.assignDriverToOrderFromWeb(assignOrderDriverForm, jwtUser),
				true, env.getProperty(StringConst.ASSIGN_ORDER_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for assigning driver to order from App.
	 * 
	 * @param authtoken
	 * @param assignOrderDriverForm
	 * @param jwtUser
	 * @return
	 */
	@PostMapping(value = "assignOrder/driver")
	@ApiOperation(value = "This method is used for assigning driver to order from App.")
	public ResponseEnvelope<OrderModel> assignDriverToOrderFromApp(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestBody @Valid AssignOrderDriverForm assignOrderDriverForm, JwtUser jwtUser) {
		log.info("assignDriverToOrderFromApp");
		return new ResponseEnvelope<>(dispatcherService.assignDriverToOrderFromApp(assignOrderDriverForm, jwtUser),
				true, env.getProperty(StringConst.ASSIGN_ORDER_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to complete the order when picked by the Customer.
	 * 
	 * @param authtoken
	 * @param orderId
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "completeOrder/{orderId}")
	@ApiOperation(value = "This method is used to complete the order when picked by the Customer.")
	public ResponseEnvelope<OrderModel> completeOrder(
			@RequestHeader(value = "authtoken", required = true) String authtoken, @PathVariable("orderId") int orderId,
			JwtUser jwtUser) {
		log.info("completeOrder");
		return new ResponseEnvelope<>(dispatcherService.completeOrder(orderId, jwtUser), true,
				env.getProperty(StringConst.ORDER_COMPLETED_SUCCESSFULLY), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to cancel the order.
	 * 
	 * @param authtoken
	 * @param orderId
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "cancelOrder/{orderId}")
	@ApiOperation(value = "This method is used to cancel the order.")
	public ResponseEnvelope<OrderModel> cancelOrder(
			@RequestHeader(value = "authtoken", required = true) String authtoken, @PathVariable("orderId") int orderId,
			JwtUser jwtUser) {
		log.info("cancelOrder");
		return new ResponseEnvelope<>(dispatcherService.cancelOrder(orderId, jwtUser), true,
				env.getProperty(StringConst.ORDER_CANCELLED_SUCCESSFULLY), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to populate the graph for products sold.
	 * 
	 * @param authtoken
	 * @param jwtUser
	 * @param startDate
	 * @param endDate
	 * @param filter
	 * @return
	 */
	@GetMapping(value = "graph/productSold")
	@ApiOperation(value = "This method is used to populate the graph for products sold.", notes = "Filter Values "
			+ "(Inside 'int' param 'filter') :  " + " 0 = Date wise, 1 = Week wise, 2 = Month wise, 3 = Year wise")
	public ResponseEnvelope<GraphCountModel> graphProductSold(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestParam("startDate") long startDate, @RequestParam("endDate") long endDate,
			@RequestParam(value = "filter", required = false, defaultValue = "0") int filter, JwtUser jwtUser) {
		log.info("graphProductSold");
		return new ResponseEnvelope<>(dispatcherService.graphProductSold(startDate, endDate, filter, jwtUser), true,
				StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
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

		return new ResponseEnvelope<>(new GenericModel<>(dispatcherService.rateOrder(rateOrderForm, jwtUser)), true,
				env.getProperty(StringConst.RATING_SUBMIT_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for adding or updating the driver inventory for
	 * particular product.
	 * 
	 * @param authtoken
	 * @param driverInventoryForm
	 * @param jwtUser
	 * @return
	 */
	@PostMapping(value = "/driverInventory")
	@ApiOperation(value = "This method is used for adding or updating the driver inventory for particular product.")
	public ResponseEnvelope<DriverInventoryModel> addOrUpdateDriverInventory(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestBody @Valid DriverInventoryForm driverInventoryForm, JwtUser jwtUser) {
		log.info("addOrUpdateDriverInventory");
		return new ResponseEnvelope<>(dispatcherService.addOrUpdateDriverInventory(driverInventoryForm, jwtUser), true,
				env.getProperty(StringConst.ADD_OR_UPDATE_DRIVER_INVENTORY_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to populate the list of driver inventories for a
	 * particular driver.
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
	@GetMapping(value = "/driverInventories/{driverUserId}")
	@ApiOperation(value = "This method is used to populate the list of driver inventories for a particular driver.", notes = "Filter Values "
			+ "(Inside 'int' param 'filter') :  "
			+ " 0 = All driver inventories, 1 = All in stock driver inventories, 2 = All out of stock driver inventories")
	public ResponseEnvelope<PaginatedResponse<DriverInventoryModel>> listDriverInventories(
			@RequestHeader(value = "authtoken", required = true) String authtoken, @RequestParam("page") int page,
			@RequestParam("size") int size, @PathVariable("driverUserId") int driverUserId,
			@RequestParam(value = "filter", required = false, defaultValue = "0") int filter,
			@RequestParam(value = "query", required = false, defaultValue = StringConst.BLANK_STRING) String query,
			JwtUser jwtUser) {
		log.info("listDriverInventories");
		if (StringUtils.isNotBlank(query))
			appUtil.validateScriptingTagAlert(query);

		Pageable pageable = new PageRequest(page - 1, size);
		return new ResponseEnvelope<>(dispatcherService.listDriverInventories(pageable, driverUserId, filter,
				appUtil.sanatizeQuery(query), jwtUser), true, StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to fetch the driver inventory by driver user id and
	 * product id.
	 * 
	 * @param authtoken
	 * @param driverUserId
	 * @param productId
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/driverInventories/{driverUserId}/{productId}")
	@ApiOperation(value = "This method is used to fetch the driver inventory by driver id and product id.")
	public ResponseEnvelope<DriverInventoryModel> driverInventory(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@PathVariable("driverUserId") int driverUserId, @PathVariable("productId") int productId, JwtUser jwtUser) {
		log.info("driverInventory");

		return new ResponseEnvelope<>(dispatcherService.driverInventory(driverUserId, productId, jwtUser), true,
				StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

}
