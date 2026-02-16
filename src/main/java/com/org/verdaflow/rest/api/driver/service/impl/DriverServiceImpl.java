package com.org.verdaflow.rest.api.driver.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.org.verdaflow.rest.api.admin.model.UserDetailModel;
import com.org.verdaflow.rest.api.customer.CustomerBuilder;
import com.org.verdaflow.rest.api.dispatcher.DispatcherBuilder;
import com.org.verdaflow.rest.api.dispatcher.model.DriverInventoryModel;
import com.org.verdaflow.rest.api.driver.DriverBuilder;
import com.org.verdaflow.rest.api.driver.form.UpdateOrderEtaForm;
import com.org.verdaflow.rest.api.driver.model.OrdersCountModel;
import com.org.verdaflow.rest.api.driver.service.DriverService;
import com.org.verdaflow.rest.api.user.form.RateOrderForm;
import com.org.verdaflow.rest.api.user.model.OrderModel;
import com.org.verdaflow.rest.common.enums.DeliveryType;
import com.org.verdaflow.rest.common.enums.OrderStatus;
import com.org.verdaflow.rest.config.common.AppConst;
import com.org.verdaflow.rest.config.common.StringConst;
import com.org.verdaflow.rest.config.security.jwt.JwtUser;
import com.org.verdaflow.rest.dto.PaginatedResponse;
import com.org.verdaflow.rest.entity.DriverOrderMapping;
import com.org.verdaflow.rest.entity.MasterEta;
import com.org.verdaflow.rest.entity.Order;
import com.org.verdaflow.rest.entity.Product;
import com.org.verdaflow.rest.entity.UserEntity;
import com.org.verdaflow.rest.error.AppException;
import com.org.verdaflow.rest.notification.NotificationBuilder;
import com.org.verdaflow.rest.repo.DriverInventoryRepo;
import com.org.verdaflow.rest.repo.DriverOrderMappingRepo;
import com.org.verdaflow.rest.repo.MasterEtaRepo;
import com.org.verdaflow.rest.repo.OrderRepo;
import com.org.verdaflow.rest.repo.ProductRepo;
import com.org.verdaflow.rest.repo.UserEntityRepo;
import com.org.verdaflow.rest.util.AppUtil;

@Service
public class DriverServiceImpl implements DriverService {
	public static final Logger log = LoggerFactory.getLogger(DriverServiceImpl.class);

	@Autowired
	private DriverBuilder driverBuilder;

	@Autowired
	private OrderRepo orderRepo;

	@Autowired
	private DriverOrderMappingRepo driverOrderMappingRepo;

	@Autowired
	private DispatcherBuilder dispatcherBuilder;

	@Autowired
	private MasterEtaRepo masterEtaRepo;

	@Autowired
	private CustomerBuilder customerBuilder;

	@Autowired
	private UserEntityRepo userEntityRepo;

	@Autowired
	private NotificationBuilder notificationBuilder;

	@Autowired
	private DriverInventoryRepo driverInventoryRepo;

	@Autowired
	private AppUtil appUtil;

	@Autowired
	private ProductRepo productRepo;

	@Override
	@Transactional
	public UserDetailModel getProfile(JwtUser jwtUser) {
		log.info("getProfile");
		if (null == jwtUser.getUserEntity().getUserDriverDetail())
			throw new AppException(StringConst.DRIVER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		UserEntity dipatcherUserEntity = userEntityRepo.findByUserIdAndRole(
				jwtUser.getUserEntity().getUserDriverDetail().getUserDispatcherDetail().getUser().getId(),
				AppConst.USER_ROLE.DISPATCHER);
		if (null == dipatcherUserEntity)
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("getProfile :: dipatcherUserEntity " + dipatcherUserEntity);

		return driverBuilder.createDriverUserDetailModelWithUserDispatcherDetailModel(jwtUser.getUserEntity(),
				dipatcherUserEntity);
	}

	@Override
	@Transactional
	public UserDetailModel getDispatcherDetails(JwtUser jwtUser) {
		log.info("getDispatcherDetails");
		if (null == jwtUser.getUserEntity().getUserDriverDetail())
			throw new AppException(StringConst.DRIVER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		UserEntity dipatcherUserEntity = userEntityRepo.findByUserIdAndRole(
				jwtUser.getUserEntity().getUserDriverDetail().getUserDispatcherDetail().getUser().getId(),
				AppConst.USER_ROLE.DISPATCHER);
		if (null == dipatcherUserEntity)
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("updateDispatcherDetails :: dipatcherUserEntity " + dipatcherUserEntity);

		return dispatcherBuilder.createDispatcherUserDetailModel(dipatcherUserEntity);
	}

	@Override
	@Transactional
	public PaginatedResponse<OrderModel> listOrders(Pageable pageable, int filter, JwtUser jwtUser) {
		log.info("listOrders");
		if (null == jwtUser.getUserEntity().getUserDriverDetail())
			throw new AppException(StringConst.DRIVER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		Page<Order> orders = null;
		if (AppConst.NUMBER.ZERO == filter) {
			// All orders
			orders = orderRepo.findAllByDriverId(pageable, jwtUser.getUserEntity().getUserDriverDetail().getId());
		} else if (AppConst.NUMBER.ONE == filter) {
			// All new orders
			orders = orderRepo.findAllNewOrdersForDriver(pageable,
					jwtUser.getUserEntity().getUserDriverDetail().getId());
		} else if (AppConst.NUMBER.TWO == filter) {
			// All pending orders
			orders = orderRepo.findAllPendingOrdersForDriver(pageable,
					jwtUser.getUserEntity().getUserDriverDetail().getId());
		} else if (AppConst.NUMBER.THREE == filter) {
			// All completed orders
			orders = orderRepo.findAllCompletedOrdersForDriver(pageable,
					jwtUser.getUserEntity().getUserDriverDetail().getId());
		} else {
			throw new AppException(StringConst.INVALID_FILTER, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
		}

		List<OrderModel> orderModels = new ArrayList<>();

		if (!orders.getContent().isEmpty()) {
			log.info("listOrders :: orders " + orders);
			orderModels = orders.getContent().stream().filter(predicate -> !predicate.isDeleted())
					.map(order -> driverBuilder.createOrderModel(order)).collect(Collectors.toList());
		}
		log.info("listOrders :: orderModels " + orderModels);

		int nextPage;
		if (orders.getContent().isEmpty() || orders.getTotalPages() == pageable.getPageNumber() + AppConst.NUMBER.ONE
				|| orders.getTotalPages() == AppConst.NUMBER.ONE) {
			nextPage = -AppConst.NUMBER.ONE;
		} else {
			nextPage = (pageable.getPageNumber() + AppConst.NUMBER.ONE) + AppConst.NUMBER.ONE;
		}
		return new PaginatedResponse<>(orderModels, (int) orders.getTotalPages(), orders.getTotalElements(), nextPage);
	}

	@Override
	@Transactional
	public OrderModel acceptOrder(int orderId, boolean reject, JwtUser jwtUser) {
		log.info("acceptOrder");
		if (null == jwtUser.getUserEntity().getUserDriverDetail())
			throw new AppException(StringConst.DRIVER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		DriverOrderMapping driverOrderMapping = driverOrderMappingRepo.findByOrderIdAndDriverId(orderId,
				jwtUser.getUserEntity().getUserDriverDetail().getId());
		if (driverOrderMapping == null || driverOrderMapping.isRejected())
			throw new AppException(StringConst.ORDER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("acceptOrder :: driverOrderMapping " + driverOrderMapping);

		if (DeliveryType.PICKUP == driverOrderMapping.getOrder().getDeliveryType()
				|| OrderStatus.DRIVER_ASSIGNED != driverOrderMapping.getOrder().getOrderStatus()) {
			if (reject) {
				throw new AppException(StringConst.ORDER_REJECT_DRIVER_REJECT,
						AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
			} else {
				throw new AppException(StringConst.ORDER_ACCEPT_DRIVER_REJECT,
						AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
			}
		}

		Order order = driverBuilder.acceptOrder(driverOrderMapping.getOrder(), reject);
		log.info("acceptOrder :: order " + order);

		if (reject) {
			customerBuilder.saveAuditOrderStatus(order, jwtUser.getUserEntity(), OrderStatus.REASSIGN_DRIVER);
			notificationBuilder.createOrderReassignDriverNotification(order, jwtUser.getUserEntity());
		} else {
			customerBuilder.saveAuditOrderStatus(order, jwtUser.getUserEntity(), OrderStatus.OUT_FOR_DELIVERY);
			notificationBuilder.createOrderOutForDeliveryNotification(order, jwtUser.getUserEntity());
		}
		return driverBuilder.createOrderModel(order);
	}

	@Override
	@Transactional
	public OrderModel updateOrderEta(UpdateOrderEtaForm updateOrderEtaForm, JwtUser jwtUser) {
		log.info("updateOrderEta");
		if (null == jwtUser.getUserEntity().getUserDriverDetail())
			throw new AppException(StringConst.DRIVER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		DriverOrderMapping driverOrderMapping = driverOrderMappingRepo.findByOrderIdAndDriverId(
				updateOrderEtaForm.getOrderId(), jwtUser.getUserEntity().getUserDriverDetail().getId());
		if (driverOrderMapping == null || driverOrderMapping.isRejected())
			throw new AppException(StringConst.ORDER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("updateOrderEta :: driverOrderMapping " + driverOrderMapping);

		MasterEta masterEta = masterEtaRepo.findByEtaId(updateOrderEtaForm.getEtaId());
		if (masterEta == null)
			throw new AppException(StringConst.ETA_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("updateOrderEta :: masterEta " + masterEta);

		Order order = driverBuilder.updateOrderEta(updateOrderEtaForm, driverOrderMapping.getOrder(), masterEta);
		log.info("updateOrderEta :: order " + order);

		return driverBuilder.createOrderModel(order);
	}

	@Override
	@Transactional
	public OrdersCountModel ordersCount(JwtUser jwtUser) {
		log.info("ordersCount");
		if (null == jwtUser.getUserEntity().getUserDriverDetail())
			throw new AppException(StringConst.DRIVER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		int newOrdersCount = orderRepo
				.findNewOrdersCountForDriver(jwtUser.getUserEntity().getUserDriverDetail().getId());
		log.info("ordersCount :: newOrdersCount " + newOrdersCount);

		int pendingOrdersCount = orderRepo
				.findPendingOrdersCountForDriver(jwtUser.getUserEntity().getUserDriverDetail().getId());
		log.info("ordersCount :: pendingOrdersCount " + pendingOrdersCount);

		return driverBuilder.createOrdersCountModel(newOrdersCount, pendingOrdersCount);
	}

	@Override
	@Transactional
	public OrderModel completeOrder(int orderId, JwtUser jwtUser) {
		log.info("completeOrder");
		if (null == jwtUser.getUserEntity().getUserDriverDetail())
			throw new AppException(StringConst.DRIVER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		DriverOrderMapping driverOrderMapping = driverOrderMappingRepo.findByOrderIdAndDriverId(orderId,
				jwtUser.getUserEntity().getUserDriverDetail().getId());
		if (driverOrderMapping == null || driverOrderMapping.isRejected())
			throw new AppException(StringConst.ORDER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("completeOrder :: driverOrderMapping " + driverOrderMapping);

		if (DeliveryType.PICKUP == driverOrderMapping.getOrder().getDeliveryType()
				|| OrderStatus.OUT_FOR_DELIVERY != driverOrderMapping.getOrder().getOrderStatus())
			throw new AppException(StringConst.ORDER_COMPLETED_REJECT, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		Order order = driverBuilder.completeOrder(driverOrderMapping.getOrder());
		log.info("completeOrder :: order " + order);

		driverBuilder.updateDriverInventoryWithDetailsAfterCompleteOrder(order, jwtUser.getUserEntity());

		dispatcherBuilder.saveProductAggregatesForProductSold(order, jwtUser.getUserEntity());

		customerBuilder.saveAuditOrderStatus(order, jwtUser.getUserEntity(), OrderStatus.COMPLETED);

		notificationBuilder.createOrderCompletedDeliveryNotification(order, jwtUser.getUserEntity());

		return driverBuilder.createOrderModel(order);
	}

	@Override
	@Transactional
	public boolean onlineStatus(boolean offline, JwtUser jwtUser) {
		log.info("onlineStatus");
		if (null == jwtUser.getUserEntity().getUserDriverDetail())
			throw new AppException(StringConst.DRIVER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		driverBuilder.onlineStatus(offline, jwtUser.getUserEntity().getUserDriverDetail());

		return true;
	}

	@Override
	@Transactional
	public boolean rateOrder(RateOrderForm rateOrderForm, JwtUser jwtUser) {
		log.info("rateOrder");
		if (null == jwtUser.getUserEntity().getUserDriverDetail())
			throw new AppException(StringConst.DRIVER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		Order order = orderRepo.findByOrderIdAndDriverId(rateOrderForm.getOrderId(),
				jwtUser.getUserEntity().getUserDriverDetail().getId());
		if (order == null)
			throw new AppException(StringConst.ORDER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("rateOrder :: order " + order);

		if (OrderStatus.COMPLETED != order.getOrderStatus() || null == order.getOrderRatingDetail())
			throw new AppException(StringConst.RATING_SUBMIT_REJECT, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		if (!order.getOrderRatingDetail().isCustomerPendingByDriver())
			throw new AppException(StringConst.RATING_SUBMIT_REJECT, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		if (order.getOrderRatingDetail().isCustomerPendingByDriver() && null == rateOrderForm.getRateCustomerForm())
			throw new AppException(StringConst.RATING_INVALID, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		if (order.getOrderRatingDetail().isCustomerPendingByDriver())
			driverBuilder.rateCustomer(rateOrderForm.getRateCustomerForm(), order, jwtUser.getUserEntity());

		return true;
	}

	@Override
	@Transactional
	public PaginatedResponse<DriverInventoryModel> listDriverInventories(Pageable pageable, int filter, String query,
			JwtUser jwtUser) {
		log.info("listDriverInventories");
		if (null == jwtUser.getUserEntity().getUserDriverDetail())
			throw new AppException(StringConst.DRIVER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		Page<Product> products = null;
		if (null == query || StringUtils.isBlank(query)) {
			if (AppConst.NUMBER.ZERO == filter) {
				// All driver inventories
				products = productRepo.findAllProductsByDispatcherId(pageable,
						jwtUser.getUserEntity().getUserDriverDetail().getUserDispatcherDetail().getId());
			} else if (AppConst.NUMBER.ONE == filter) {
				// All in stock driver inventories
				products = productRepo.findAllInStockDriverInventoryProductsByDispatcherIdAndDriverId(pageable,
						jwtUser.getUserEntity().getUserDriverDetail().getUserDispatcherDetail().getId(),
						jwtUser.getUserEntity().getUserDriverDetail().getId());
			} else if (AppConst.NUMBER.TWO == filter) {
				// All out of stock driver inventories
				products = productRepo.findAllOutOfStockDriverInventoryProductsByDispatcherIdAndDriverId(pageable,
						jwtUser.getUserEntity().getUserDriverDetail().getUserDispatcherDetail().getId(),
						jwtUser.getUserEntity().getUserDriverDetail().getId());
			} else {
				throw new AppException(StringConst.INVALID_FILTER, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
			}
		} else {
			String searchQuery = new StringBuilder(StringConst.PERCENTILE).append(appUtil.sanatizeQuery(query))
					.append(StringConst.PERCENTILE).toString();

			if (AppConst.NUMBER.ZERO == filter) {
				// All driver inventories
				products = productRepo.findAllProductsByDispatcherIdSearch(pageable,
						jwtUser.getUserEntity().getUserDriverDetail().getUserDispatcherDetail().getId(), searchQuery);
			} else if (AppConst.NUMBER.ONE == filter) {
				// All in stock driver inventories
				products = productRepo.findAllInStockDriverInventoryProductsByDispatcherIdAndDriverIdSearch(pageable,
						jwtUser.getUserEntity().getUserDriverDetail().getUserDispatcherDetail().getId(),
						jwtUser.getUserEntity().getUserDriverDetail().getId(), searchQuery);
			} else if (AppConst.NUMBER.TWO == filter) {
				// All out of stock driver inventories
				products = productRepo.findAllOutOfStockDriverInventoryProductsByDispatcherIdAndDriverIdSearch(pageable,
						jwtUser.getUserEntity().getUserDriverDetail().getUserDispatcherDetail().getId(),
						jwtUser.getUserEntity().getUserDriverDetail().getId(), searchQuery);
			} else {
				throw new AppException(StringConst.INVALID_FILTER, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
			}
		}

		List<DriverInventoryModel> driverInventoryModels = new ArrayList<>();

		if (!products.getContent().isEmpty()) {
			log.info("listDriverInventories :: products " + products);
			driverInventoryModels = products.getContent().stream().filter(predicate -> !predicate.isDeleted())
					.map(product -> dispatcherBuilder.createDriverInventoryModelFromProduct(product,
							jwtUser.getUserEntity().getUserDriverDetail().getId()))
					.collect(Collectors.toList());
		}
		log.info("listDriverInventories :: driverInventoryModels " + driverInventoryModels);

		int nextPage;
		if (products.getContent().isEmpty()
				|| products.getTotalPages() == pageable.getPageNumber() + AppConst.NUMBER.ONE
				|| products.getTotalPages() == AppConst.NUMBER.ONE) {
			nextPage = -AppConst.NUMBER.ONE;
		} else {
			nextPage = (pageable.getPageNumber() + AppConst.NUMBER.ONE) + AppConst.NUMBER.ONE;
		}
		return new PaginatedResponse<>(driverInventoryModels, (int) products.getTotalPages(),
				products.getTotalElements(), nextPage);
	}

}
