package com.org.verdaflow.rest.api.customer.controller;

import java.math.BigDecimal;
import java.util.List;

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

import com.org.verdaflow.rest.api.admin.model.UserDetailModel;
import com.org.verdaflow.rest.api.auth.model.UserDispatcherDetailModel;
import com.org.verdaflow.rest.api.customer.form.CartDetailForm;
import com.org.verdaflow.rest.api.customer.form.CustomerAddressDetailForm;
import com.org.verdaflow.rest.api.customer.form.PlaceOrderForm;
import com.org.verdaflow.rest.api.customer.form.UpdateUserCustomerDetailForm;
import com.org.verdaflow.rest.api.customer.model.ApplyPromoCodeModel;
import com.org.verdaflow.rest.api.customer.model.CartDetailModel;
import com.org.verdaflow.rest.api.customer.model.CartModel;
import com.org.verdaflow.rest.api.customer.model.CustomerAddressDetailModel;
import com.org.verdaflow.rest.api.customer.service.CustomerService;
import com.org.verdaflow.rest.api.dispatcher.model.ProductModel;
import com.org.verdaflow.rest.api.user.form.RateOrderForm;
import com.org.verdaflow.rest.api.user.model.OrderModel;
import com.org.verdaflow.rest.common.model.CountModel;
import com.org.verdaflow.rest.common.model.GenericModel;
import com.org.verdaflow.rest.config.common.AppConst;
import com.org.verdaflow.rest.config.common.StringConst;
import com.org.verdaflow.rest.config.security.jwt.JwtUser;
import com.org.verdaflow.rest.dto.PaginatedResponse;
import com.org.verdaflow.rest.dto.ResponseEnvelope;
import com.org.verdaflow.rest.util.AppUtil;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {
	public static final Logger log = LoggerFactory.getLogger(CustomerController.class);

	@Autowired
	private CustomerService customerService;

	@Autowired
	private Environment env;

	@Autowired
	private AppUtil appUtil;

	/**
	 * This method is used to fetch the customer profile details.
	 * 
	 * @param authtoken
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/profile")
	@ApiOperation(value = "This method is used to fetch the customer profile details.")
	public ResponseEnvelope<UserDetailModel> getProfile(
			@RequestHeader(value = "authtoken", required = true) String authtoken, JwtUser jwtUser) {
		log.info("getProfile");
		return new ResponseEnvelope<>(customerService.getProfile(jwtUser), true, StringConst.EMPTY_STRING,
				AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for updating the customer profile details.
	 * 
	 * @param authtoken
	 * @param updateUserCustomerDetailForm
	 * @param jwtUser
	 * @return
	 */
	@PatchMapping(value = "/profile")
	@ApiOperation(value = "This method is used for updating the customer profile details.")
	public ResponseEnvelope<UserDetailModel> updateProfile(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestBody @Valid UpdateUserCustomerDetailForm updateUserCustomerDetailForm, JwtUser jwtUser) {
		log.info("updateProfile");
		return new ResponseEnvelope<>(customerService.updateProfile(updateUserCustomerDetailForm, jwtUser), true,
				env.getProperty(StringConst.PROFILE_UPDATED_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to populate the list of dispatchers in application on the
	 * basis of distance from user.
	 *
	 * @param authtoken
	 * @param page
	 * @param size
	 * @param query
	 * @param lat
	 * @param lng
	 * @param categoryIds
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/dispatchers")
	@ApiOperation(value = "This method is used to populate the list of dispatchers in application on the basis of distance from user.")
	public ResponseEnvelope<PaginatedResponse<UserDispatcherDetailModel>> listDispatchers(
			@RequestHeader(value = "authtoken", required = true) String authtoken, @RequestParam("page") int page,
			@RequestParam("size") int size,
			@RequestParam(value = "query", required = false, defaultValue = StringConst.BLANK_STRING) String query,
			@RequestParam("lat") BigDecimal lat, @RequestParam("lng") BigDecimal lng,
			@RequestParam(value = "categoryIds", required = false) List<Integer> categoryIds, JwtUser jwtUser) {
		log.info("listDispatchers");
		if (StringUtils.isNotBlank(query))
			appUtil.validateScriptingTagAlert(query);

		Pageable pageable = new PageRequest(page - 1, size);
		return new ResponseEnvelope<>(
				customerService.listDispatchers(pageable, appUtil.sanatizeQuery(query), lat, lng, categoryIds, jwtUser),
				true, StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to fetch the product details on the basis of dispatcherId
	 * and masterTypeId.
	 * 
	 * @param authtoken
	 * @param page
	 * @param size
	 * @param query
	 * @param dispatcherId
	 * @param typeId
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/products")
	@ApiOperation(value = "This method is used to fetch the product details on the basis of dispatcherId and typeId.")
	public ResponseEnvelope<PaginatedResponse<ProductModel>> listProducts(
			@RequestHeader(value = "authtoken", required = true) String authtoken, @RequestParam("page") int page,
			@RequestParam("size") int size,
			@RequestParam(value = "query", required = false, defaultValue = StringConst.BLANK_STRING) String query,
			@RequestParam("dispatcherId") int dispatcherId, @RequestParam("typeId") int typeId, JwtUser jwtUser) {
		log.info("listProducts");
		if (StringUtils.isNotBlank(query))
			appUtil.validateScriptingTagAlert(query);

		Pageable pageable = new PageRequest(page - 1, size);
		return new ResponseEnvelope<>(
				customerService.listProducts(pageable, appUtil.sanatizeQuery(query), dispatcherId, typeId, jwtUser),
				true, StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for creating new address.
	 * 
	 * @param authtoken
	 * @param customerAddressDetailForm
	 * @param jwtUser
	 * @return
	 */

	@PostMapping(value = "/address")
	@ApiOperation(value = "This method is used for creating new address.")
	public ResponseEnvelope<CustomerAddressDetailModel> createAddress(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestBody @Valid CustomerAddressDetailForm customerAddressDetailForm, JwtUser jwtUser) {
		log.info("createAddress");
		return new ResponseEnvelope<>(customerService.createAddress(customerAddressDetailForm, jwtUser), true,
				env.getProperty(StringConst.CREATE_ADDRESS_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for updating the address details.
	 * 
	 * @param authtoken
	 * @param customerAddressDetailForm
	 * @param jwtUser
	 * @return
	 */

	@PatchMapping(value = "/address")
	@ApiOperation(value = "This method is used for updating the address details.")
	public ResponseEnvelope<CustomerAddressDetailModel> updateAddress(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestBody @Valid CustomerAddressDetailForm customerAddressDetailForm, JwtUser jwtUser) {
		log.info("updateAddress");
		return new ResponseEnvelope<>(customerService.updateAddress(customerAddressDetailForm, jwtUser), true,
				env.getProperty(StringConst.UPDATE_ADDRESS_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for deleting the address.
	 *
	 * @param authtoken
	 * @param customerAddressId
	 * @param jwtUser
	 * @return
	 */
	@DeleteMapping(value = "/address/{addressId}")
	@ApiOperation(value = "This method is used for deleting the address.")
	public ResponseEnvelope<GenericModel<Boolean>> deleteAddress(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@PathVariable("addressId") int addressId, JwtUser jwtUser) {
		log.info("deleteAddress");

		return new ResponseEnvelope<>(new GenericModel<>(customerService.deleteAddress(addressId, jwtUser)), true,
				env.getProperty(StringConst.DELETE_ADDRESS_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to populate the list of addresses.
	 *
	 * @param authtoken
	 * @param page
	 * @param size
	 * @param query
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/addresses")
	@ApiOperation(value = "This method is used to populate the list of addresses.")
	public ResponseEnvelope<PaginatedResponse<CustomerAddressDetailModel>> listAddresses(
			@RequestHeader(value = "authtoken", required = true) String authtoken, @RequestParam("page") int page,
			@RequestParam("size") int size,
			@RequestParam(value = "query", required = false, defaultValue = StringConst.BLANK_STRING) String query,
			JwtUser jwtUser) {
		log.info("listAddresses");
		if (StringUtils.isNotBlank(query))
			appUtil.validateScriptingTagAlert(query);

		Pageable pageable = new PageRequest(page - 1, size);
		return new ResponseEnvelope<>(customerService.listAddresses(pageable, appUtil.sanatizeQuery(query), jwtUser),
				true, StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to fetch the details of a address.
	 *
	 * @param authtoken
	 * @param addressId
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/address/{addressId}")
	@ApiOperation(value = "This method is used to fetch the details of a address.")
	public ResponseEnvelope<CustomerAddressDetailModel> addressDetails(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@PathVariable(value = "addressId") int addressId, JwtUser jwtUser) {
		log.info("addressDetails");
		return new ResponseEnvelope<>(customerService.addressDetails(addressId, jwtUser), true,
				StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to set the address as default.
	 * 
	 * @param authtoken
	 * @param addressId
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/defaultAddress/{addressId}")
	@ApiOperation(value = "This method is used set the address as default.")
	public ResponseEnvelope<CustomerAddressDetailModel> setDefaultAddress(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@PathVariable(value = "addressId", required = true) int addressId, JwtUser jwtUser) {
		log.info("setDefaultAddress");

		return new ResponseEnvelope<>(customerService.setDefaultAddress(addressId, jwtUser), true,
				env.getProperty(StringConst.ADDRESS_SET_DEFAULT_SUCCESSFULLY), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to favorite/unfavorite Dispatcher.
	 *
	 * @param authtoken
	 * @param dispatcherId
	 * @param unfav
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/favoriteDispatcher")
	@ApiOperation(value = "This method is used to favorite/unfavorite Dispatcher.", notes = " PARAM DESCRIPTION  :  unfav(boolean) - true (Will unfavorite the Dispatcher)")
	public ResponseEnvelope<UserDispatcherDetailModel> favoriteDispatcher(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestParam(value = "dispatcherId", required = true) int dispatcherId,
			@RequestParam(value = "unfav", required = false) boolean unfav, JwtUser jwtUser) {
		log.info("favoriteDispatcher");

		return new ResponseEnvelope<>(customerService.favoriteDispatcher(dispatcherId, unfav, jwtUser), true,
				unfav ? env.getProperty(StringConst.DISPATCHER_UNFAVORITE_SUCCESSFULLY)
						: env.getProperty(StringConst.DISPATCHER_FAVORITE_SUCCESSFULLY),
				AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to populate the list of favorite Dispatchers.
	 *
	 * @param authtoken
	 * @param page
	 * @param size
	 * @param query
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/favorite/dispatchers")
	@ApiOperation(value = "This method is used to populate the list of favorite Dispatchers.")
	public ResponseEnvelope<PaginatedResponse<UserDispatcherDetailModel>> listFavoriteDispatchers(
			@RequestHeader(value = "authtoken", required = true) String authtoken, @RequestParam("page") int page,
			@RequestParam("size") int size,
			@RequestParam(value = "query", required = false, defaultValue = StringConst.BLANK_STRING) String query,
			@RequestParam("lat") BigDecimal lat, @RequestParam("lng") BigDecimal lng, JwtUser jwtUser) {
		log.info("listFavoriteDispatchers");
		if (StringUtils.isNotBlank(query))
			appUtil.validateScriptingTagAlert(query);

		Pageable pageable = new PageRequest(page - 1, size);
		return new ResponseEnvelope<>(customerService.listFavoriteDispatchers(pageable, lat, lng, jwtUser), true,
				StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to favorite/unfavorite Product.
	 *
	 * @param authtoken
	 * @param productId
	 * @param unfav
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/favoriteProduct")
	@ApiOperation(value = "This method is used to favorite/unfavorite Product.", notes = " PARAM DESCRIPTION  :  unfav(boolean) - true (Will unfavorite the Product)")
	public ResponseEnvelope<ProductModel> favoriteProduct(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestParam(value = "productId", required = true) int productId,
			@RequestParam(value = "unfav", required = false) boolean unfav, JwtUser jwtUser) {
		log.info("favoriteProduct");

		return new ResponseEnvelope<>(customerService.favoriteProduct(productId, unfav, jwtUser), true,
				unfav ? env.getProperty(StringConst.PRODUCT_UNFAVORITE_SUCCESSFULLY)
						: env.getProperty(StringConst.PRODUCT_FAVORITE_SUCCESSFULLY),
				AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to populate the list of favorite Products.
	 *
	 * @param authtoken
	 * @param page
	 * @param size
	 * @param query
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/favorite/products")
	@ApiOperation(value = "This method is used to populate the list of favorite Products.")
	public ResponseEnvelope<PaginatedResponse<ProductModel>> listFavoriteProducts(
			@RequestHeader(value = "authtoken", required = true) String authtoken, @RequestParam("page") int page,
			@RequestParam("size") int size,
			@RequestParam(value = "query", required = false, defaultValue = StringConst.BLANK_STRING) String query,
			JwtUser jwtUser) {
		log.info("listFavoriteProducts");
		if (StringUtils.isNotBlank(query))
			appUtil.validateScriptingTagAlert(query);

		Pageable pageable = new PageRequest(page - 1, size);
		return new ResponseEnvelope<>(customerService.listFavoriteProducts(pageable, jwtUser), true,
				StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for adding product to the cart.
	 * 
	 * @param authtoken
	 * @param cartDetailForm
	 * @param jwtUser
	 * @return
	 */

	@PostMapping(value = "/cart")
	@ApiOperation(value = " This method is used for adding product to the cart.")
	public ResponseEnvelope<CountModel> addToCart(@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestBody @Valid CartDetailForm cartDetailForm, JwtUser jwtUser) {
		log.info("addCartDetails");
		return new ResponseEnvelope<>(customerService.addToCart(cartDetailForm, jwtUser), true,
				env.getProperty(StringConst.ADD_TO_CART_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to populate the list of products added in Cart.
	 *
	 * @param authtoken
	 * @param query
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/carts")
	@ApiOperation(value = "This method is used to populate the list of products added in Cart.")
	public ResponseEnvelope<CartModel> listCarts(@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestParam(value = "query", required = false, defaultValue = StringConst.BLANK_STRING) String query,
			JwtUser jwtUser) {
		log.info("listCart");
		if (StringUtils.isNotBlank(query))
			appUtil.validateScriptingTagAlert(query);

		return new ResponseEnvelope<>(customerService.listCarts(jwtUser), true, StringConst.EMPTY_STRING,
				AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for updating the cart details.
	 * 
	 * @param authtoken
	 * @param cartDetailForm
	 * @param jwtUser
	 * @return
	 */
	@PatchMapping(value = "/cart")
	@ApiOperation(value = "This method is used for updating the cart details.")
	public ResponseEnvelope<CartDetailModel> updateCart(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestBody @Valid CartDetailForm cartDetailForm, JwtUser jwtUser) {
		log.info("updateCart");
		return new ResponseEnvelope<>(customerService.updateCartDetails(cartDetailForm, jwtUser), true,
				env.getProperty(StringConst.UPDATE_CART_DETAIL_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for deleting product from the cart.
	 * 
	 * @param authtoken
	 * @param cartId
	 * @param jwtUser
	 * @return
	 */
	@DeleteMapping(value = "/cart/{cartId}")
	@ApiOperation(value = " This method is used for deleting product from the cart.")
	public ResponseEnvelope<CountModel> deleteFromCart(
			@RequestHeader(value = "authtoken", required = true) String authtoken, @PathVariable("cartId") int cartId,
			JwtUser jwtUser) {
		log.info("deleteFromCart");
		return new ResponseEnvelope<>(customerService.deleteFromCart(cartId, jwtUser), true,
				env.getProperty(StringConst.DELETE_FROM_CART_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to fetch the details of a cart.
	 *
	 * @param authtoken
	 * @param cartId
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/cart/{cartId}")
	@ApiOperation(value = "This method is used to fetch the details of a cart.")
	public ResponseEnvelope<CartDetailModel> getCartDetails(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@PathVariable(value = "cartId") int cartId, JwtUser jwtUser) {
		log.info("getCartDetails");

		return new ResponseEnvelope<>(customerService.getCartDetails(cartId, jwtUser), true, StringConst.EMPTY_STRING,
				AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for empty the cart details.
	 * 
	 * @param authtoken
	 * @param jwtUser
	 * @return
	 */
	@DeleteMapping(value = "/emptyCart")
	@ApiOperation(value = " This method is used for the empty cart details.")
	public ResponseEnvelope<CountModel> emptyCartDetails(
			@RequestHeader(value = "authtoken", required = true) String authtoken, JwtUser jwtUser) {
		log.info("emptyCartDetails");
		return new ResponseEnvelope<>(customerService.emptyCart(jwtUser), true,
				env.getProperty(StringConst.EMPTY_CART_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for fetch the cart's count.
	 * 
	 * @param authtoken
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/cartCount")
	@ApiOperation(value = "This method is used for fetch the cart's count.")
	public ResponseEnvelope<CountModel> cartCount(@RequestHeader(value = "authtoken", required = true) String authtoken,
			JwtUser jwtUser) {
		log.info("cartCount");
		return new ResponseEnvelope<>(customerService.cartCount(jwtUser), true, StringConst.EMPTY_STRING,
				AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to populate the product added temporarily in Cart.
	 *
	 * @param authtoken
	 * @param query
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/carts/temporary")
	@ApiOperation(value = "This method is used to populate the product added temporarily in Cart.")
	public ResponseEnvelope<CartModel> listCartsTemporary(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestParam(value = "productId", required = true) int productId,
			@RequestParam(value = "productPriceDetailId", required = true) int productPriceDetailId, JwtUser jwtUser) {
		log.info("listCartsTemporary");
		return new ResponseEnvelope<>(customerService.listCartsTemporary(productId, productPriceDetailId, jwtUser),
				true, StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to fetch the details of those products who eligible for
	 * promoCode.
	 *
	 * @param authtoken
	 * @param dispatcherId
	 * @param promoCodeName
	 * @param productIds
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/applyPromoCode")
	@ApiOperation(value = "This method is used to fetch the details of those products who eligible for promoCode.")
	public ResponseEnvelope<ApplyPromoCodeModel> applyPromoCode(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestParam(value = "dispatcherId") int dispatcherId,
			@RequestParam(value = "promoCodeName") String promoCodeName,
			@RequestParam(value = "productIds") List<Integer> productIds, JwtUser jwtUser) {
		log.info("applyPromoCode");

		return new ResponseEnvelope<>(customerService.applyPromoCode(dispatcherId, promoCodeName, productIds, jwtUser),
				true, env.getProperty(StringConst.PROMOCODE_APPLIED_SUCCESSFULLY), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for placing order.
	 * 
	 * @param authtoken
	 * @param placeOrderForm
	 * @param jwtUser
	 * @return
	 */
	@PostMapping(value = "/placeOrder")
	@ApiOperation(value = "This method is used for placing order.")
	public ResponseEnvelope<OrderModel> placeOrder(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestBody @Valid PlaceOrderForm placeOrderForm, JwtUser jwtUser) {
		log.info("placeOrder");

		return new ResponseEnvelope<>(customerService.placeOrder(placeOrderForm, jwtUser), true,
				env.getProperty(StringConst.PLACE_ORDER_SUCCESSFULLY), AppConst.HTTP_STATUS_OK);
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
			+ " 0 = All orders, 1 = All new orders, 2 = All pending orders , 3 = All completed orders")
	public ResponseEnvelope<PaginatedResponse<OrderModel>> listOrders(
			@RequestHeader(value = "authtoken", required = true) String authtoken, @RequestParam("page") int page,
			@RequestParam("size") int size,
			@RequestParam(value = "filter", required = false, defaultValue = "0") int filter, JwtUser jwtUser) {
		log.info("listOrders");

		Pageable pageable = new PageRequest(page - 1, size);
		return new ResponseEnvelope<>(customerService.listOrders(pageable, filter, jwtUser), true,
				StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to favorite/unfavorite Order.
	 *
	 * @param authtoken
	 * @param orderId
	 * @param unfav
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/favoriteOrder")
	@ApiOperation(value = "This method is used to favorite/unfavorite Order.", notes = " PARAM DESCRIPTION  :  unfav(boolean) - true (Will unfavorite the Order)")
	public ResponseEnvelope<OrderModel> favoriteOrder(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestParam(value = "orderId", required = true) int orderId,
			@RequestParam(value = "unfav", required = false) boolean unfav, JwtUser jwtUser) {
		log.info("favoriteOrder");

		return new ResponseEnvelope<>(customerService.favoriteOrder(orderId, unfav, jwtUser), true,
				unfav ? env.getProperty(StringConst.ORDER_UNFAVORITE_SUCCESSFULLY)
						: env.getProperty(StringConst.ORDER_FAVORITE_SUCCESSFULLY),
				AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to populate the list of favorite Orders.
	 *
	 * @param authtoken
	 * @param page
	 * @param size
	 * @param query
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/favorite/orders")
	@ApiOperation(value = "This method is used to populate the list of favorite Orders.")
	public ResponseEnvelope<PaginatedResponse<OrderModel>> listFavoriteOrders(
			@RequestHeader(value = "authtoken", required = true) String authtoken, @RequestParam("page") int page,
			@RequestParam("size") int size,
			@RequestParam(value = "query", required = false, defaultValue = StringConst.BLANK_STRING) String query,
			JwtUser jwtUser) {
		log.info("listFavoriteOrders");
		if (StringUtils.isNotBlank(query))
			appUtil.validateScriptingTagAlert(query);

		Pageable pageable = new PageRequest(page - 1, size);
		return new ResponseEnvelope<>(customerService.listFavoriteOrders(pageable, jwtUser), true,
				StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for canceling the order.
	 * 
	 * @param authtoken
	 * @param orderId
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/cancelOrder/{orderId}")
	@ApiOperation(value = "This method is used for canceling the order.")
	public ResponseEnvelope<OrderModel> cancelOrder(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@PathVariable(value = "orderId", required = true) int orderId, JwtUser jwtUser) {
		log.info("cancelOrder");

		return new ResponseEnvelope<>(customerService.cancelOrder(orderId, jwtUser), true,
				env.getProperty(StringConst.ORDER_CANCELLED_SUCCESSFULLY), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to fetch the details of last completed order.
	 * 
	 * @param authtoken
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/lastCompletedOrder")
	@ApiOperation(value = "This method is used to fetch the details of last completed order.")
	public ResponseEnvelope<OrderModel> lastCompletedOrder(
			@RequestHeader(value = "authtoken", required = true) String authtoken, JwtUser jwtUser) {
		log.info("lastCompletedOrder");

		return new ResponseEnvelope<>(customerService.lastCompletedOrder(jwtUser), true, StringConst.EMPTY_STRING,
				AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for rate the dispatcher and/or driver user(s) for
	 * particular order.
	 * 
	 * @param authtoken
	 * @param rateUserForm
	 * @param jwtUser
	 * @return
	 */
	@PostMapping(value = "/rateOrder")
	@ApiOperation(value = "This method is used for rate the dispatcher and/or driver user(s) for particular order.")
	public ResponseEnvelope<GenericModel<Boolean>> rateOrder(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestBody @Valid RateOrderForm rateOrderForm, JwtUser jwtUser) {
		log.info("rateOrder");

		return new ResponseEnvelope<>(new GenericModel<>(customerService.rateOrder(rateOrderForm, jwtUser)), true,
				env.getProperty(StringConst.RATING_SUBMIT_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

}
