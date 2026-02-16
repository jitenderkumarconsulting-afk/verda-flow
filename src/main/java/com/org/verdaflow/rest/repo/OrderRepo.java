package com.org.verdaflow.rest.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.org.verdaflow.rest.common.enums.OrderStatus;
import com.org.verdaflow.rest.entity.Order;

@Repository
public interface OrderRepo extends JpaRepository<Order, Integer> {

	/**
	 * This query to fetch order details by orderId.
	 * 
	 * @param orderId
	 * @return
	 */
	@Query("SELECT o FROM Order o WHERE o.id = :orderId AND o.isDeleted = false")
	Order findByOrderId(@Param("orderId") int orderId);

	/**
	 * This query to fetch the Order details on the basis of orderId and userId.
	 *
	 * @param orderId
	 * @param userId
	 * @return
	 */
	@Query("SELECT o FROM Order o WHERE o.id = :orderId AND o.user.id = :userId AND o.isDeleted = false")
	Order findByOrderIdAndUserId(@Param("orderId") int orderId, @Param("userId") int userId);

	/**
	 * This query to find the Order on the basis of dispatcherId.
	 *
	 * @param pageable
	 * @param dispatcherId
	 * @return
	 */
	@Query("SELECT o FROM Order o WHERE o.id = :orderId AND o.userDispatcherDetail.id = :dispatcherId AND o.isDeleted = false")
	Order findByOrderIdAndDispatcherId(@Param("orderId") int orderId, @Param("dispatcherId") int dispatcherId);

	/**
	 * This query to find the Order on the basis of driverId.
	 *
	 * @param pageable
	 * @param driverId
	 * @return
	 */
	@Query("SELECT o FROM Order o, DriverOrderMapping d WHERE o.id = d.order.id AND d.userDriverDetail.id = :driverId AND o.id = :orderId AND d.isRejected = false AND o.isDeleted = false")
	Order findByOrderIdAndDriverId(@Param("orderId") int orderId, @Param("driverId") int driverId);

	/**
	 * This query to fetch the all Orders list on the basis of user id of customer.
	 *
	 * @param pageable
	 * @param userId
	 * @return
	 */
	@Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.isDeleted = false ORDER BY o.createdAt DESC")
	Page<Order> findAllByUserId(Pageable pageable, @Param("userId") int userId);

	/**
	 * This query to fetch the all Orders list on the basis of dispatcher id for
	 * Dispatcher.
	 *
	 * @param pageable
	 * @param dispatcherId
	 * @return
	 */
	@Query("SELECT o FROM Order o WHERE o.userDispatcherDetail.id = :dispatcherId AND o.orderStatus != 'PROCESSING' AND o.isPlaced = true AND o.isDeleted = false ORDER BY o.createdAt DESC")
	Page<Order> findAllByDispatcherId(Pageable pageable, @Param("dispatcherId") int dispatcherId);

	/**
	 * This query to fetch the all Orders list on the basis of dispatcher id for
	 * Dispatcher with Search.
	 *
	 * @param pageable
	 * @param dispatcherId
	 * @return
	 */
	@Query("SELECT o FROM Order o, UserCustomerDetail c WHERE o.user.id = c.user.id AND o.userDispatcherDetail.id = :dispatcherId AND CONCAT(c.firstName,' ',c.lastName) LIKE :query AND o.orderStatus != 'PROCESSING' AND o.isPlaced = true AND o.isDeleted = false ORDER BY o.createdAt DESC")
	Page<Order> findAllByDispatcherIdSearch(Pageable pageable, @Param("dispatcherId") int dispatcherId,
			@Param("query") String query);

	/**
	 * This query to fetch all the Orders for Driver on the basis of driverId.
	 *
	 * @param driverId
	 * @return
	 */
	@Query("SELECT o FROM Order o, DriverOrderMapping d WHERE o.id = d.order.id AND d.userDriverDetail.id= :driverId AND d.isRejected = false AND o.isDeleted = false ORDER BY o.createdAt DESC")
	Page<Order> findAllByDriverId(Pageable pageable, @Param("driverId") int driverId);

	/**
	 * This query to fetch all the Orders for Driver on the basis of driverId.
	 *
	 * @param driverId
	 * @param query
	 * @return
	 */
	@Query("SELECT o FROM Order o, DriverOrderMapping d, UserCustomerDetail c WHERE o.user.id = c.user.id AND o.id = d.order.id AND d.userDriverDetail.id= :driverId AND d.isRejected = false AND CONCAT(c.firstName,' ',c.lastName) LIKE :query AND o.isDeleted = false ORDER BY o.createdAt DESC")
	Page<Order> findAllByDriverIdSearch(Pageable pageable, @Param("driverId") int driverId,
			@Param("query") String query);

	/**
	 * This query to fetch the Orders list for Driver on the basis of driverId and
	 * orderStatus.
	 *
	 * @param pageable
	 * @param driverId
	 * @param orderStatus
	 * @return
	 */
	@Query("SELECT o FROM Order o, DriverOrderMapping d WHERE o.id = d.order.id AND d.userDriverDetail.id= :driverId AND o.orderStatus = :orderStatus AND d.isRejected = false AND o.isDeleted = false ORDER BY o.createdAt DESC")
	Page<Order> findAllByDriverIdAndOrderStatus(Pageable pageable, @Param("driverId") int driverId,
			@Param("orderStatus") OrderStatus orderStatus);

	/**
	 * This query to fetch all the Orders for Driver on the basis of driverId and
	 * dispatcherId.
	 *
	 * @param driverId
	 * @param dispatcherId
	 * @return
	 */
	@Query("SELECT o FROM Order o, DriverOrderMapping d WHERE o.id = d.order.id AND d.userDriverDetail.id = :driverId AND o.userDispatcherDetail.id = :dispatcherId AND d.isRejected = false AND o.isDeleted = false AND d.isDeleted = false ORDER BY o.createdAt DESC")
	Page<Order> findAllByDriverIdAndDispatcherId(Pageable pageable, @Param("driverId") int driverId,
			@Param("dispatcherId") int dispatcherId);

	/**
	 * This query to fetch the all Orders list on the basis of customer id for
	 * Customer.
	 *
	 * @param pageable
	 * @param customerId
	 * @return
	 */
	@Query("SELECT o FROM Order o WHERE o.user.id = :customerId AND o.orderStatus != 'PROCESSING' AND o.isPlaced = true AND o.isDeleted = false ORDER BY o.createdAt DESC")
	Page<Order> findAllByCustomerId(Pageable pageable, @Param("customerId") int customerId);

	/**
	 * This query to fetch all the favorite Orders list on the basis of user id of
	 * customer.
	 *
	 * @param pageable
	 * @param userId
	 * @return
	 */
	@Query("SELECT o FROM Order o, FavoriteOrderMapping f WHERE o.id = f.order.id AND f.user.id = :userId AND f.isFav = true AND o.isDeleted = false ORDER BY o.createdAt DESC")
	Page<Order> findAllFavoriteOrdersByUserId(Pageable pageable, @Param("userId") int userId);

	/**
	 * This query to fetch all the Orders for Customer with particular Dispatcher on
	 * the basis of userId and dispatcherId.
	 *
	 * @param userId
	 * @param dispatcherId
	 * @return
	 */
	@Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.userDispatcherDetail.id = :dispatcherId AND o.orderStatus != 'PROCESSING' AND o.isPlaced = true AND o.isDeleted = false ORDER BY o.createdAt DESC")
	Page<Order> findAllByUserIdAndDispatcherId(Pageable pageable, @Param("userId") int userId,
			@Param("dispatcherId") int dispatcherId);

	/**
	 * This query is to fetch the Orders' count on the basis of dispatcher id for
	 * Dispatcher.
	 *
	 * @param dispatcherId
	 * @return
	 */
	@Query("SELECT COUNT(o) FROM Order o WHERE o.userDispatcherDetail.id = :dispatcherId AND o.orderStatus != 'PROCESSING' AND o.isPlaced = true AND o.isDeleted = false ORDER BY o.createdAt DESC")
	int findOrdersCountByDispatcherId(@Param("dispatcherId") int dispatcherId);

	/**
	 * This query to fetch the list of Orders which need to be placed to Dispatcher.
	 * 
	 * @return
	 */
	@Query("SELECT o FROM Order o WHERE o.orderStatus = 'PROCESSING' AND o.isDeleted = false AND o.cancelExpiryTime <= CURRENT_TIMESTAMP()")
	List<Order> findAllOrdersToBePlaced();

	/**
	 * This query to find the New and Pending Orders count for Driver on the basis
	 * of driverId.
	 *
	 * @param pageable
	 * @param driverId
	 * @return
	 */
	@Query("SELECT COUNT(o) FROM Order o, DriverOrderMapping d WHERE o.id = d.order.id AND d.userDriverDetail.id = :driverId AND o.orderStatus IN ('DRIVER_ASSIGNED','OUT_FOR_DELIVERY','DELIVERY_FAILED') AND d.isRejected = false AND o.isDeleted = false")
	int findNewAndPendingOrdersCountForDriver(@Param("driverId") int driverId);

	/**
	 * This query to get the New Orders count for Driver on the basis of driverId.
	 *
	 * @param driverId
	 * @return
	 */
	@Query("SELECT COUNT(o) FROM Order o, DriverOrderMapping d WHERE o.id = d.order.id AND d.userDriverDetail.id = :driverId AND o.orderStatus = 'DRIVER_ASSIGNED' AND d.isRejected = false AND o.isDeleted = false")
	int findNewOrdersCountForDriver(@Param("driverId") int driverId);

	/**
	 * This query to get the Pending Orders count for Driver on the basis of
	 * driverId.
	 *
	 * @param driverId
	 * @return
	 */
	@Query("SELECT COUNT(o) FROM Order o, DriverOrderMapping d WHERE o.id = d.order.id AND d.userDriverDetail.id= :driverId AND o.orderStatus IN ('OUT_FOR_DELIVERY','DELIVERY_FAILED') AND d.isRejected = false AND o.isDeleted = false")
	int findPendingOrdersCountForDriver(@Param("driverId") int driverId);

	/**
	 * This query to fetch the New Orders list for Driver on the basis of driverId.
	 *
	 * @param pageable
	 * @param driverId
	 * @return
	 */
	@Query("SELECT o FROM Order o, DriverOrderMapping d WHERE o.id = d.order.id AND d.userDriverDetail.id = :driverId AND o.orderStatus = 'DRIVER_ASSIGNED' AND d.isRejected = false AND o.isDeleted = false ORDER BY o.createdAt ASC")
	Page<Order> findAllNewOrdersForDriver(Pageable pageable, @Param("driverId") int driverId);

	/**
	 * This query to fetch the New Orders list for Driver on the basis of driverId
	 * with search.
	 *
	 * @param pageable
	 * @param driverId
	 * @param query
	 * @return
	 */
	@Query("SELECT o FROM Order o, DriverOrderMapping d, UserCustomerDetail c WHERE o.user.id = c.user.id AND o.id = d.order.id AND d.userDriverDetail.id= :driverId AND d.isRejected = false AND CONCAT(c.firstName,' ',c.lastName) LIKE :query AND o.orderStatus = 'DRIVER_ASSIGNED' AND o.isDeleted = false ORDER BY o.createdAt ASC")
	Page<Order> findAllNewOrdersForDriverSearch(Pageable pageable, @Param("driverId") int driverId,
			@Param("query") String query);

	/**
	 * This query to fetch the Pending Orders list for Driver on the basis of
	 * driverId.
	 *
	 * @param pageable
	 * @param driverId
	 * @return
	 */
	@Query("SELECT o FROM Order o, DriverOrderMapping d WHERE o.id = d.order.id AND d.userDriverDetail.id = :driverId AND o.orderStatus IN ('OUT_FOR_DELIVERY','DELIVERY_FAILED') AND d.isRejected = false AND o.isDeleted = false ORDER BY o.createdAt ASC")
	Page<Order> findAllPendingOrdersForDriver(Pageable pageable, @Param("driverId") int driverId);

	/**
	 * This query to fetch the Pending Orders list for Driver on the basis of
	 * driverId.
	 *
	 * @param pageable
	 * @param driverId
	 * @param query
	 * @return
	 */
	@Query("SELECT o FROM Order o, DriverOrderMapping d , UserCustomerDetail c WHERE o.user.id = c.user.id AND o.id = d.order.id AND d.userDriverDetail.id= :driverId AND d.isRejected = false AND CONCAT(c.firstName,' ',c.lastName) LIKE :query AND o.orderStatus IN ('OUT_FOR_DELIVERY','DELIVERY_FAILED') AND o.isDeleted = false ORDER BY o.createdAt ASC")
	Page<Order> findAllPendingOrdersForDriverSearch(Pageable pageable, @Param("driverId") int driverId,
			@Param("query") String query);

	/**
	 * This query to fetch the Completed Orders list for Driver on the basis of
	 * driverId.
	 *
	 * @param pageable
	 * @param driverId
	 * @return
	 */
	@Query("SELECT o FROM Order o, DriverOrderMapping d WHERE o.id = d.order.id AND d.userDriverDetail.id = :driverId AND o.orderStatus = 'COMPLETED' AND d.isRejected = false AND o.isDeleted = false ORDER BY o.createdAt DESC")
	Page<Order> findAllCompletedOrdersForDriver(Pageable pageable, @Param("driverId") int driverId);

	/**
	 * This query to fetch the Completed Orders list for Driver on the basis of
	 * driverId.
	 *
	 * @param pageable
	 * @param driverId
	 * @param query
	 * @return
	 */
	@Query("SELECT o FROM Order o, DriverOrderMapping d , UserCustomerDetail c WHERE o.user.id=c.user.id AND o.id = d.order.id AND d.userDriverDetail.id = :driverId AND d.isRejected = false AND CONCAT(c.firstName,' ',c.lastName) LIKE :query AND o.orderStatus = 'COMPLETED' AND o.isDeleted = false ORDER BY o.createdAt DESC")
	Page<Order> findAllCompletedOrdersForDriverSearch(Pageable pageable, @Param("driverId") int driverId,
			@Param("query") String query);

	/**
	 * This query to fetch the New and Pending Orders list for Driver on the basis
	 * of
	 * driverId.
	 *
	 * @param pageable
	 * @param driverId
	 * @return
	 */
	@Query("SELECT o FROM Order o, DriverOrderMapping d WHERE o.id = d.order.id AND d.userDriverDetail.id = :driverId AND o.orderStatus IN ('DRIVER_ASSIGNED','OUT_FOR_DELIVERY','DELIVERY_FAILED') AND d.isRejected = false AND o.isDeleted = false ORDER BY o.createdAt DESC")
	Page<Order> findAllNewAndPendingOrdersForDriver(Pageable pageable, @Param("driverId") int driverId);

	/**
	 * This query to fetch the Completed Orders list for Driver on the basis of
	 * driverId.
	 *
	 * @param pageable
	 * @param driverId
	 * @param query
	 * @return
	 */
	@Query("SELECT o FROM Order o, DriverOrderMapping d , UserCustomerDetail c WHERE o.user.id=c.user.id AND o.id = d.order.id AND d.userDriverDetail.id = :driverId AND d.isRejected = false AND CONCAT(c.firstName,' ',c.lastName) LIKE :query AND o.orderStatus IN ('DRIVER_ASSIGNED','OUT_FOR_DELIVERY','DELIVERY_FAILED') AND o.isDeleted = false ORDER BY o.createdAt DESC")
	Page<Order> findAllNewAndPendingOrdersForDriverSearch(Pageable pageable, @Param("driverId") int driverId,
			@Param("query") String query);

	/**
	 * This query to find the New and Pending Orders count for Dispatcher on the
	 * basis of dispatcherId.
	 *
	 * @param pageable
	 * @param dispatcherId
	 * @return
	 */
	@Query("SELECT COUNT(o) FROM Order o WHERE o.userDispatcherDetail.id = :dispatcherId AND o.orderStatus NOT IN ('PROCESSING','COMPLETED','CANCELLED_BY_USER','CANCELLED_BY_STORE') AND o.isPlaced = true AND o.isDeleted = false")
	int findNewAndPendingOrdersCountForDispatcher(@Param("dispatcherId") int dispatcherId);

	/**
	 * This query to fetch the New Orders list for Dispatcher on the basis of
	 * dispatcherId.
	 *
	 * @param pageable
	 * @param dispatcherId
	 * @return
	 */
	@Query("SELECT o FROM Order o WHERE o.userDispatcherDetail.id = :dispatcherId AND o.orderStatus NOT IN ('PROCESSING','COMPLETED','CANCELLED_BY_USER','CANCELLED_BY_STORE') AND o.isDeleted = false ORDER BY o.createdAt DESC")
	Page<Order> findAllNewAndPendingOrdersForDispatcher(Pageable pageable, @Param("dispatcherId") int dispatcherId);

	/**
	 * This query to fetch the New Orders list for Dispatcher on the basis of
	 * dispatcherId Search.
	 *
	 * @param pageable
	 * @param dispatcherId
	 * @return
	 */
	@Query("SELECT o FROM Order o,UserCustomerDetail c WHERE o.user.id = c.user.id AND o.userDispatcherDetail.id = :dispatcherId AND CONCAT(c.firstName,' ',c.lastName) LIKE :query AND o.orderStatus NOT IN ('PROCESSING','COMPLETED','CANCELLED_BY_USER','CANCELLED_BY_STORE') AND o.isDeleted = false ORDER BY o.createdAt DESC")
	Page<Order> findAllNewAndPendingOrdersForDispatcherSearch(Pageable pageable,
			@Param("dispatcherId") int dispatcherId, @Param("query") String query);

	/**
	 * This query to fetch the Completed Orders list for Dispatcher on the basis of
	 * dispatcherId.
	 *
	 * @param pageable
	 * @param dispatcherId
	 * @return
	 */
	@Query("SELECT o FROM Order o WHERE o.userDispatcherDetail.id = :dispatcherId AND o.orderStatus IN ('COMPLETED','CANCELLED_BY_USER','CANCELLED_BY_STORE') AND o.isPlaced = true AND o.isDeleted = false ORDER BY o.createdAt DESC")
	Page<Order> findAllCompletedOrdersForDispatcher(Pageable pageable, @Param("dispatcherId") int dispatcherId);

	/**
	 * This query to fetch the Completed Orders list for Dispatcher on the basis of
	 * dispatcherId.
	 *
	 * @param pageable
	 * @param dispatcherId
	 * @return
	 */
	@Query("SELECT o FROM Order o,UserCustomerDetail c WHERE o.user.id = c.user.id AND o.userDispatcherDetail.id = :dispatcherId AND CONCAT(c.firstName,' ',c.lastName) LIKE :query AND o.orderStatus IN ('COMPLETED','CANCELLED_BY_USER','CANCELLED_BY_STORE') AND o.isPlaced = true AND o.isDeleted = false ORDER BY o.createdAt DESC")
	Page<Order> findAllCompletedOrdersForDispatcherSearch(Pageable pageable, @Param("dispatcherId") int dispatcherId,
			@Param("query") String query);

	/**
	 * This query to fetch the New Orders list for Customer on the basis of
	 * customer's userId.
	 *
	 * @param pageable
	 * @param userId
	 * @return
	 */
	@Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.orderStatus IN ('PROCESSING','PLACED') AND o.isDeleted = false ORDER BY o.createdAt DESC")
	Page<Order> findAllNewOrdersForCustomer(Pageable pageable, @Param("userId") int userId);

	/**
	 * This query to fetch the New Orders list for Customer on the basis of
	 * customer's userId and dispatcher id.
	 *
	 * @param pageable
	 * @param userId
	 * @param dispatcherId
	 * @return
	 */
	@Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.userDispatcherDetail.id = :dispatcherId AND o.orderStatus IN ('PROCESSING','PLACED') AND o.isDeleted = false ORDER BY o.createdAt DESC")
	Page<Order> findAllNewOrdersForCustomerByDispatcherId(Pageable pageable, @Param("userId") int userId,
			@Param("dispatcherId") int dispatcherId);

	/**
	 * This query to fetch the Pending Orders list for Customer on the basis of
	 * customer's userId.
	 *
	 * @param pageable
	 * @param userId
	 * @return
	 */
	@Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.orderStatus NOT IN ('PROCESSING','PLACED','COMPLETED','CANCELLED_BY_USER','CANCELLED_BY_STORE') AND o.isDeleted = false ORDER BY o.createdAt DESC")
	Page<Order> findAllPendingOrdersForCustomer(Pageable pageable, @Param("userId") int userId);

	/**
	 * This query to fetch the Pending Orders list for Customer on the basis of
	 * customer's userId and dispatcher id.
	 *
	 * @param pageable
	 * @param userId
	 * @param dispatcherId
	 * @return
	 */
	@Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.userDispatcherDetail.id = :dispatcherId AND o.orderStatus NOT IN ('PROCESSING','PLACED','COMPLETED','CANCELLED_BY_USER','CANCELLED_BY_STORE') AND o.isDeleted = false ORDER BY o.createdAt DESC")
	Page<Order> findAllPendingOrdersForCustomerByDispatcherId(Pageable pageable, @Param("userId") int userId,
			@Param("dispatcherId") int dispatcherId);

	/**
	 * This query to fetch the Completed Orders list for Customer on the basis of
	 * customer's userId.
	 *
	 * @param pageable
	 * @param userId
	 * @return
	 */
	@Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.orderStatus IN ('COMPLETED','CANCELLED_BY_USER','CANCELLED_BY_STORE') AND o.isDeleted = false ORDER BY o.createdAt DESC")
	Page<Order> findAllCompletedOrdersForCustomer(Pageable pageable, @Param("userId") int userId);

	/**
	 * This query to fetch the Completed Orders list for Customer on the basis of
	 * customer's userId and dispatcher id.
	 *
	 * @param pageable
	 * @param userId
	 * @param dispatcherId
	 * @return
	 */
	@Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.userDispatcherDetail.id = :dispatcherId AND o.orderStatus IN ('COMPLETED','CANCELLED_BY_USER','CANCELLED_BY_STORE') AND o.isDeleted = false ORDER BY o.createdAt DESC")
	Page<Order> findAllCompletedOrdersForCustomerByDispatcherId(Pageable pageable, @Param("userId") int userId,
			@Param("dispatcherId") int dispatcherId);

	/**
	 * This query to fetch the last completed Order details on the basis of user id
	 * of customer.
	 *
	 * @param userId
	 * @return
	 */
	@Query(value = "SELECT o.* FROM orders o WHERE o.user_id = :userId AND o.order_status = 'COMPLETED' AND o.is_deleted = false ORDER BY o.modified_at DESC LIMIT 1", nativeQuery = true)
	Order findLastCompletedOrderByUserId(@Param("userId") int userId);

}
