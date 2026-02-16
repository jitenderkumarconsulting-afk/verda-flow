package com.org.verdaflow.rest.api.dispatcher.service;

import org.springframework.data.domain.Pageable;

import com.org.verdaflow.rest.api.admin.model.OrderListWithUserCustomerModel;
import com.org.verdaflow.rest.api.admin.model.UserDetailModel;
import com.org.verdaflow.rest.api.auth.model.UserDriverDetailModel;
import com.org.verdaflow.rest.api.dispatcher.form.AssignOrderDriverForm;
import com.org.verdaflow.rest.api.dispatcher.form.DriverInventoryForm;
//xeemu@103.36.77.34/home/xeemu/Java_Backend/verdaflow-java
import com.org.verdaflow.rest.api.dispatcher.form.ProductForm;
import com.org.verdaflow.rest.api.dispatcher.form.PromoCodeForm;
import com.org.verdaflow.rest.api.dispatcher.form.RegisterDriverForm;
import com.org.verdaflow.rest.api.dispatcher.model.DriverInventoryModel;
import com.org.verdaflow.rest.api.dispatcher.model.GraphCountModel;
import com.org.verdaflow.rest.api.dispatcher.model.OrderListWithUserDriverModel;
import com.org.verdaflow.rest.api.dispatcher.model.ProductModel;
import com.org.verdaflow.rest.api.dispatcher.model.PromoCodeModel;
import com.org.verdaflow.rest.api.user.form.RateOrderForm;
import com.org.verdaflow.rest.api.user.model.OrderModel;
import com.org.verdaflow.rest.config.security.jwt.JwtUser;
import com.org.verdaflow.rest.dto.PaginatedResponse;

public interface DispatcherService {

	/**
	 * Fetch dispatcher profile details.
	 * 
	 * @param jwtUser
	 * @return
	 */
	UserDetailModel getProfile(JwtUser jwtUser);

	/**
	 * Register new Driver.
	 * 
	 * @param registerDriverForm
	 * @param jwtUser
	 * @return
	 */
	boolean registerDriver(RegisterDriverForm registerDriverForm, JwtUser jwtUser);

	/**
	 * Update driver details.
	 * 
	 * @param registerDriverForm
	 * @param jwtUser
	 * @return
	 */
	UserDetailModel updateDriverDetails(RegisterDriverForm registerDriverForm, JwtUser jwtUser);

	/**
	 * List Drivers.
	 * 
	 * @param pageable
	 * @param query
	 * @param filter
	 *                 (0 = All driver users, 1 = All approved driver users, 2 = All
	 *                 pending driver users, 3 = All rejected driver users)
	 * @param jwtUser
	 * @return
	 */
	PaginatedResponse<UserDetailModel> listDriverUsers(Pageable pageable, String query, int filter, JwtUser jwtUser);

	/**
	 * Fetch Driver Details
	 * 
	 * @param driverUserId
	 * @param jwtUser
	 * @return
	 */
	UserDetailModel driverDetails(int driverUserId, JwtUser jwtUser);

	/**
	 * Add new Product.
	 * 
	 * @param productForm
	 * @param jwtUser
	 * @return
	 */
	boolean addProduct(ProductForm productForm, JwtUser jwtUser);

	/**
	 * Update Product.
	 * 
	 * @param productForm
	 * @param jwtUser
	 * @return
	 */
	ProductModel updateProduct(ProductForm productForm, JwtUser jwtUser);

	/**
	 * List Products.
	 * 
	 * @param pageable
	 * @param query
	 * @param filter
	 * @param jwtUser
	 * @return
	 */
	PaginatedResponse<ProductModel> listProducts(Pageable pageable, String query, int filter, JwtUser jwtUser);

	/**
	 * Fetch Product Details
	 * 
	 * @param productId
	 * @param jwtUser
	 * @return
	 */
	ProductModel productDetails(int productId, JwtUser jwtUser);

	/**
	 * Delete Product.
	 * 
	 * @param productId
	 * @param jwtUser
	 * @return
	 */
	boolean deleteProduct(int productId, JwtUser jwtUser);

	/**
	 * Add new Promo Code.
	 * 
	 * @param promoCodeForm
	 * @param jwtUser
	 * @return
	 */
	boolean addPromoCode(PromoCodeForm promoCodeForm, JwtUser jwtUser);

	/**
	 * Update Promo Code.
	 * 
	 * @param promoCodeForm
	 * @param jwtUser
	 * @return
	 */
	PromoCodeModel updatePromoCode(PromoCodeForm promoCodeForm, JwtUser jwtUser);

	/**
	 * Delete Promo Code.
	 * 
	 * @param promoCodeId
	 * @param jwtUser
	 * @return
	 */
	boolean deletePromoCode(int promoCodeId, JwtUser jwtUser);

	/**
	 * List Promo Codes.
	 * 
	 * @param pageable
	 * @param query
	 * @param jwtUser
	 * @return
	 */
	PaginatedResponse<PromoCodeModel> listPromoCodes(Pageable pageable, String query, JwtUser jwtUser);

	/**
	 * Activate - Deactivate a Driver
	 * 
	 * @param driverUserId
	 * @param deact
	 * @param jwtUser
	 * @return
	 */
	UserDetailModel activateDriver(int driverUserId, boolean deact, JwtUser jwtUser);

	/**
	 * Fetch Promo Code Details
	 * 
	 * @param promoCodeId
	 * @param jwtUser
	 * @return
	 */
	PromoCodeModel promoCodeDetails(int promoCodeId, JwtUser jwtUser);

	/**
	 * List Orders.
	 *
	 * @param pageable
	 * @param filter
	 * @param query
	 * @param jwtUser
	 * @return
	 */
	PaginatedResponse<OrderModel> listOrders(Pageable pageable, int filter, String query, JwtUser jwtUser);

	/**
	 * List Orders For Driver.
	 *
	 * @param pageable
	 * @param driverUserId
	 * @param filter
	 * @param query
	 * @param jwtUser
	 * @return
	 */
	OrderListWithUserDriverModel listOrdersForDriver(Pageable pageable, int driverUserId, int filter, String query,
			JwtUser jwtUser);

	/**
	 * List Orders For Customer with current dispatcher.
	 *
	 * @param pageable
	 * @param customerUserId
	 * @param filter
	 * @param jwtUser
	 * @return
	 */
	OrderListWithUserCustomerModel listOrdersForCustomer(Pageable pageable, int customerUserId, int filter,
			JwtUser jwtUser);

	/**
	 * Confirm Order.
	 * 
	 * @param orderId
	 * @param jwtUser
	 * @return
	 */
	OrderModel confirmOrder(int orderId, JwtUser jwtUser);

	/**
	 * Prepare Order.
	 * 
	 * @param orderId
	 * @param jwtUser
	 * @return
	 */
	OrderModel prepareOrder(int orderId, JwtUser jwtUser);

	/**
	 * List activated Driver details for assigning order.
	 * 
	 * @param pageable
	 * @param orderId
	 * @param query
	 * @param jwtUser
	 * @return
	 */
	PaginatedResponse<UserDriverDetailModel> listDriversForAssigningOrder(Pageable pageable, int orderId, String query,
			JwtUser jwtUser);

	/**
	 * Assign Order to Driver from Web.
	 * 
	 * @param assignOrderDriverForm
	 * @param jwtUser
	 * @return
	 */
	UserDriverDetailModel assignDriverToOrderFromWeb(AssignOrderDriverForm assignOrderDriverForm, JwtUser jwtUser);

	/**
	 * Assign Driver to Order from App.
	 * 
	 * @param assignOrderDriverForm
	 * @param jwtUser
	 * @return
	 */
	OrderModel assignDriverToOrderFromApp(AssignOrderDriverForm assignOrderDriverForm, JwtUser jwtUser);

	/**
	 * Complete Order.
	 * 
	 * @param orderId
	 * @param jwtUser
	 * @return
	 */
	OrderModel completeOrder(int orderId, JwtUser jwtUser);

	/**
	 * Cancel Order.
	 * 
	 * @param orderId
	 * @param jwtUser
	 * @return
	 */
	OrderModel cancelOrder(int orderId, JwtUser jwtUser);

	/**
	 * Product Sold Graph.
	 * 
	 * @param startDate
	 * @param endDate
	 * @param filter
	 * @param jwtUser
	 * @return
	 */
	GraphCountModel graphProductSold(long startDate, long endDate, int filter, JwtUser jwtUser);

	/**
	 * Rate the customer user for particular order.
	 * 
	 * @param rateOrderForm
	 * @param jwtUser
	 * @return
	 */
	boolean rateOrder(RateOrderForm rateOrderForm, JwtUser jwtUser);

	/**
	 * Add or update the driver inventory for particular product.
	 * 
	 * @param rateOrderForm
	 * @param jwtUser
	 * @return
	 */
	DriverInventoryModel addOrUpdateDriverInventory(DriverInventoryForm driverInventoryForm, JwtUser jwtUser);

	/**
	 * List Driver Inventories.
	 *
	 * @param pageable
	 * @param driverUserId
	 * @param filter
	 * @param query
	 * @param jwtUser
	 * @return
	 */
	PaginatedResponse<DriverInventoryModel> listDriverInventories(Pageable pageable, int driverUserId, int filter,
			String query, JwtUser jwtUser);

	/**
	 * Driver Inventory.
	 *
	 * @param driverUserId
	 * @param productId
	 * @param jwtUser
	 * @return
	 */
	DriverInventoryModel driverInventory(int driverUserId, int productId, JwtUser jwtUser);

}
