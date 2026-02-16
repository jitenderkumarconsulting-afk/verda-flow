package com.org.verdaflow.rest.api.customer.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Pageable;

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
import com.org.verdaflow.rest.api.dispatcher.model.ProductModel;
import com.org.verdaflow.rest.api.user.form.RateOrderForm;
import com.org.verdaflow.rest.api.user.model.OrderModel;
import com.org.verdaflow.rest.common.model.CountModel;
//xeemu@103.36.77.34/home/xeemu/Java_Backend/verdaflow-java
import com.org.verdaflow.rest.config.security.jwt.JwtUser;
import com.org.verdaflow.rest.dto.PaginatedResponse;

public interface CustomerService {

	/**
	 * Fetch customer profile details.
	 * 
	 * @param jwtUser
	 * @return
	 */
	UserDetailModel getProfile(JwtUser jwtUser);

	/**
	 * Update customer profile details.
	 * 
	 * @param updateUserCustomerDetailForm
	 * @param jwtUser
	 * @return
	 */
	UserDetailModel updateProfile(UpdateUserCustomerDetailForm updateUserCustomerDetailForm, JwtUser jwtUser);

	/**
	 * List Dispatchers on the basis of distance from user.
	 * 
	 * @param pageable
	 * @param query
	 * @param lat
	 * @param lng
	 * @param categoryIds
	 * @param jwtUser
	 * @return
	 */
	PaginatedResponse<UserDispatcherDetailModel> listDispatchers(Pageable pageable, String query, BigDecimal lat,
			BigDecimal lng, List<Integer> categoryIds, JwtUser jwtUser);

	/**
	 * List Products on the basis of dispatcherId and typeId.
	 * 
	 * @param pageable
	 * @param query
	 * @param dispatcherId
	 * @param typeId
	 * @param jwtUser
	 * @return
	 */
	PaginatedResponse<ProductModel> listProducts(Pageable pageable, String query, int dispatcherId, int typeId,
			JwtUser jwtUser);

	/**
	 * Create Address.
	 * 
	 * @param customerAddressDetailForm
	 * @param jwtUser
	 * @return
	 */
	CustomerAddressDetailModel createAddress(CustomerAddressDetailForm customerAddressDetailForm, JwtUser jwtUser);

	/**
	 * Update Address.
	 * 
	 * @param customerAddressDetailForm
	 * @param jwtUser
	 * @return
	 */
	CustomerAddressDetailModel updateAddress(CustomerAddressDetailForm customerAddressDetailForm, JwtUser jwtUser);

	/**
	 * Delete Address.
	 * 
	 * @param addressId
	 * @param jwtUser
	 * @return
	 */
	boolean deleteAddress(int addressId, JwtUser jwtUser);

	/**
	 * List Addresses.
	 * 
	 * @param pageable
	 * @param query
	 * @param jwtUser
	 * @return
	 */
	PaginatedResponse<CustomerAddressDetailModel> listAddresses(Pageable pageable, String query, JwtUser jwtUser);

	/**
	 * Fetch Address Details
	 * 
	 * @param addressId
	 * @param jwtUser
	 * @return
	 */
	CustomerAddressDetailModel addressDetails(int addressId, JwtUser jwtUser);

	/**
	 * Set Default Address.
	 * 
	 * @param addressId
	 * @param jwtUser
	 * @return
	 */
	CustomerAddressDetailModel setDefaultAddress(int addressId, JwtUser jwtUser);

	/**
	 * Favorite - Unfavorite a Dispatcher
	 * 
	 * @param dispatcherId
	 * @param unfav
	 * @param jwtUser
	 * @return
	 */
	UserDispatcherDetailModel favoriteDispatcher(int dispatcherId, boolean unfav, JwtUser jwtUser);

	/**
	 * List Favorite Dispatchers.
	 * 
	 * @param pageable
	 * @param lat
	 * @param lng
	 * @param jwtUser
	 * @return
	 */
	PaginatedResponse<UserDispatcherDetailModel> listFavoriteDispatchers(Pageable pageable, BigDecimal lat,
			BigDecimal lng, JwtUser jwtUser);

	/**
	 * Favorite - Unfavorite a Product
	 * 
	 * @param productId
	 * @param unfav
	 * @param jwtUser
	 * @return
	 */
	ProductModel favoriteProduct(int productId, boolean unfav, JwtUser jwtUser);

	/**
	 * List Favorite Products.
	 * 
	 * @param pageable
	 * @param jwtUser
	 * @return
	 */
	PaginatedResponse<ProductModel> listFavoriteProducts(Pageable pageable, JwtUser jwtUser);

	/**
	 * Add To Cart.
	 * 
	 * @param cartDetailForm
	 * @param jwtUser
	 * @return
	 */
	CountModel addToCart(CartDetailForm cartDetailForm, JwtUser jwtUser);

	/**
	 * List Carts.
	 * 
	 * @param jwtUser
	 * @return
	 */
	CartModel listCarts(JwtUser jwtUser);

	/**
	 * Update Cart Details
	 * 
	 * @param cartDetailForm
	 * @param jwtUser
	 * @return
	 */
	CartDetailModel updateCartDetails(CartDetailForm cartDetailForm, JwtUser jwtUser);

	/**
	 * Delete From Cart.
	 * 
	 * @param cartId
	 * @param jwtUser
	 * @return
	 */
	CountModel deleteFromCart(int cartId, JwtUser jwtUser);

	/**
	 * Fetch Cart Details.
	 * 
	 * @param cartId
	 * @param jwtUser
	 * @return
	 */
	CartDetailModel getCartDetails(int cartId, JwtUser jwtUser);

	/**
	 * Empty Cart.
	 * 
	 * @param jwtUser
	 * @return
	 */
	CountModel emptyCart(JwtUser jwtUser);

	/**
	 * Cart's Count.
	 * 
	 * @param jwtUser
	 * @return
	 */
	CountModel cartCount(JwtUser jwtUser);

	/**
	 * List Temporary Carts.
	 * 
	 * @param jwtUser
	 * @return
	 */
	CartModel listCartsTemporary(int productId, int productPriceDetailId, JwtUser jwtUser);

	/**
	 * Apply Promo Code.
	 * 
	 * @param dispatcherId
	 * @param promoCodeName
	 * @param productIds
	 * @param jwtUser
	 * @return
	 */
	ApplyPromoCodeModel applyPromoCode(int dispatcherId, String promoCodeName, List<Integer> productIds,
			JwtUser jwtUser);

	/**
	 * Place Order.
	 * 
	 * @param placeOrderForm
	 * @param jwtUser
	 * @return
	 */
	OrderModel placeOrder(PlaceOrderForm placeOrderForm, JwtUser jwtUser);

	/**
	 * List Orders.
	 *
	 * @param pageable
	 * @param filter
	 *                 (0 = All orders, 1 = All new orders, 2 = All pending orders ,
	 *                 3 =
	 *                 All completed orders, 4 = All cancelled orders)
	 * @param jwtUser
	 * @return
	 */
	PaginatedResponse<OrderModel> listOrders(Pageable pageable, int filter, JwtUser jwtUser);

	/**
	 * Favorite - Unfavorite a Order
	 * 
	 * @param orderId
	 * @param unfav
	 * @param jwtUser
	 * @return
	 */
	OrderModel favoriteOrder(int orderId, boolean unfav, JwtUser jwtUser);

	/**
	 * List Favorite Orders.
	 * 
	 * @param pageable
	 * @param jwtUser
	 * @return
	 */
	PaginatedResponse<OrderModel> listFavoriteOrders(Pageable pageable, JwtUser jwtUser);

	/**
	 * Cancel Order.
	 * 
	 * @param orderId
	 * @param jwtUser
	 * @return
	 */
	OrderModel cancelOrder(int orderId, JwtUser jwtUser);

	/**
	 * Last Completed Order.
	 * 
	 * @param jwtUser
	 * @return
	 */
	OrderModel lastCompletedOrder(JwtUser jwtUser);

	/**
	 * Rate the dispatcher and/or driver user(s) for particular order.
	 * 
	 * @param rateOrderForm
	 * @param jwtUser
	 * @return
	 */
	boolean rateOrder(RateOrderForm rateOrderForm, JwtUser jwtUser);

}
