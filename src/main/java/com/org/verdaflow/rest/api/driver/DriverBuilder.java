package com.org.verdaflow.rest.api.driver;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.org.verdaflow.rest.api.admin.model.UserDetailModel;
import com.org.verdaflow.rest.api.auth.AuthBuilder;
import com.org.verdaflow.rest.api.auth.model.UserDriverDetailModel;
import com.org.verdaflow.rest.api.customer.CustomerBuilder;
import com.org.verdaflow.rest.api.customer.model.CustomerAddressDetailModel;
import com.org.verdaflow.rest.api.dispatcher.DispatcherBuilder;
import com.org.verdaflow.rest.api.dispatcher.model.PromoCodeModel;
import com.org.verdaflow.rest.api.driver.form.UpdateOrderEtaForm;
import com.org.verdaflow.rest.api.driver.model.DriverLocationDetailModel;
import com.org.verdaflow.rest.api.driver.model.OrdersCountModel;
import com.org.verdaflow.rest.api.master.MasterBuilder;
import com.org.verdaflow.rest.api.master.model.MasterModel;
import com.org.verdaflow.rest.api.user.UserBuilder;
import com.org.verdaflow.rest.api.user.form.RateUserForm;
import com.org.verdaflow.rest.api.user.model.AuditOrderStatusModel;
import com.org.verdaflow.rest.api.user.model.OrderItemDetailModel;
import com.org.verdaflow.rest.api.user.model.OrderModel;
import com.org.verdaflow.rest.api.user.model.OrderPriceDetailModel;
import com.org.verdaflow.rest.api.user.model.OrderRatingDetailModel;
import com.org.verdaflow.rest.common.enums.OrderStatus;
import com.org.verdaflow.rest.common.model.UpdateStatusModel;
import com.org.verdaflow.rest.common.model.UserRoleModel;
import com.org.verdaflow.rest.config.common.AppConst;
import com.org.verdaflow.rest.config.common.StringConst;
import com.org.verdaflow.rest.entity.DriverInventory;
import com.org.verdaflow.rest.entity.DriverInventoryDetail;
import com.org.verdaflow.rest.entity.DriverLocationDetail;
import com.org.verdaflow.rest.entity.MasterEta;
import com.org.verdaflow.rest.entity.Order;
import com.org.verdaflow.rest.entity.UserDriverDetail;
import com.org.verdaflow.rest.entity.UserEntity;
import com.org.verdaflow.rest.error.AppException;
import com.org.verdaflow.rest.repo.DriverInventoryDetailRepo;
import com.org.verdaflow.rest.repo.DriverInventoryRepo;
import com.org.verdaflow.rest.repo.DriverOrderMappingRepo;
import com.org.verdaflow.rest.repo.OrderRatingDetailRepo;
import com.org.verdaflow.rest.repo.OrderRepo;
import com.org.verdaflow.rest.repo.UserDriverDetailRepo;

@Component
public class DriverBuilder {
	public static final Logger log = LoggerFactory.getLogger(DriverBuilder.class);

	@Autowired
	private DispatcherBuilder dispatcherBuilder;

	@Autowired
	private CustomerBuilder customerBuilder;

	@Autowired
	private OrderRepo orderRepo;

	@Autowired
	private DriverOrderMappingRepo driverOrderMappingRepo;

	@Autowired
	private MasterBuilder masterBuilder;

	@Autowired
	private AuthBuilder authBuilder;

	@Autowired
	private UserDriverDetailRepo userDriverDetailRepo;

	@Autowired
	private OrderRatingDetailRepo orderRatingDetailRepo;

	@Autowired
	private UserBuilder userBuilder;

	@Autowired
	private DriverInventoryRepo driverInventoryRepo;

	@Autowired
	private DriverInventoryDetailRepo driverInventoryDetailRepo;

	@Transactional
	public UserDetailModel createDriverUserDetailModel(UserEntity userEntity) {
		log.info("createDriverUserDetailModel");
		UserDriverDetailModel userDriverDetailModel = authBuilder
				.createUserDriverDetailModel(userEntity.getUserDriverDetail());

		return new UserDetailModel(authBuilder.createUserModel(userEntity),
				userEntity.getUserRoleMappings().stream()
						.map(mapper -> new UserRoleModel(mapper.getMasterRole().getId(),
								String.valueOf(mapper.getMasterRole().getRole()), mapper.getApplicationStatus()))
						.collect(Collectors.toList()),
				userDriverDetailModel);
	}

	@Transactional
	public DriverLocationDetailModel createDriverLocationDetailModel(DriverLocationDetail driverLocationDetail) {
		Integer orderId = null;
		if (null != driverLocationDetail.getOrder()) {
			orderId = driverLocationDetail.getOrder().getId();
		}

		return new DriverLocationDetailModel(driverLocationDetail.getId(),
				driverLocationDetail.getUserDriverDetail().getId(), driverLocationDetail.getLat(),
				driverLocationDetail.getLng(), driverLocationDetail.getRotation(), driverLocationDetail.getRoute(),
				orderId);
	}

	@Transactional
	public UserDetailModel createDriverUserDetailModelWithUserDispatcherDetailModel(UserEntity userEntity,
			UserEntity dispatcherUserEntity) {
		log.info("createDriverUserDetailModelWithUserDispatcherDetailModel");
		UserDriverDetailModel userDriverDetailModel = authBuilder
				.createUserDriverDetailModelWithDispatcherUserDetailModel(userEntity.getUserDriverDetail());

		return new UserDetailModel(authBuilder.createUserModel(userEntity),
				userEntity.getUserRoleMappings().stream()
						.map(mapper -> new UserRoleModel(mapper.getMasterRole().getId(),
								String.valueOf(mapper.getMasterRole().getRole()), mapper.getApplicationStatus()))
						.collect(Collectors.toList()),
				userDriverDetailModel);
	}

	@Transactional
	public OrderModel createOrderModel(Order order) {
		log.info("createOrderModel");

		UserDetailModel customerUser = customerBuilder.createCustomerUserDetailModel(order.getUser());
		log.info("createOrderModel :: customerUser " + customerUser);

		UserDetailModel dispatcherUser = dispatcherBuilder.createDispatcherUserDetailModel(
				order.getDriverOrderMapping().getUserDriverDetail().getUserDispatcherDetail().getUser());
		log.info("createOrderModel :: dispatcherUser " + dispatcherUser);

		PromoCodeModel promoCodeModel = null;
		if (null != order.getPromoCode())
			promoCodeModel = dispatcherBuilder.createPromoCodeModel(order.getPromoCode());
		log.info("createOrderModel :: promoCodeModel " + promoCodeModel);

		CustomerAddressDetailModel customerAddressDetailModel = null;
		if (null != order.getAddressDetail()) {
			customerAddressDetailModel = dispatcherBuilder.createCustomerAddressModel(order.getAddressDetail());
			log.info("createOrderModel :: customerAddressDetailModel " + customerAddressDetailModel);
		}

		List<OrderItemDetailModel> orderItemDetailModels = new ArrayList<>();
		if (null != order.getOrderItemDetails() && !order.getOrderItemDetails().isEmpty()) {
			orderItemDetailModels = order.getOrderItemDetails().stream()
					.map(orderItemDetail -> customerBuilder.createOrderItemDetailModel(orderItemDetail))
					.collect(Collectors.toList());
		}
		log.info("createOrderModel ::orderItemDetailModels " + orderItemDetailModels);

		OrderPriceDetailModel orderPriceDetailModel = null;
		if (null != order.getOrderPriceDetail()) {
			orderPriceDetailModel = customerBuilder.createOrderPriceDetailModel(order.getOrderPriceDetail());
			log.info("createOrderModel :: orderPriceDetailModel " + orderPriceDetailModel);
		}

		OrderRatingDetailModel orderRatingDetailModel = null;
		if (null != order.getOrderRatingDetail()) {
			orderRatingDetailModel = customerBuilder.createOrderRatingDetailModel(order.getOrderRatingDetail());
			log.info("createOrderModel :: orderRatingDetailModel " + orderRatingDetailModel);
		}

		MasterModel etaModel = null;
		if (null != order.getEta())
			etaModel = masterBuilder.createMasterModel(order.getEta());
		log.info("createOrderModel :: etaModel " + etaModel);

		List<AuditOrderStatusModel> auditOrderStatusModels = new ArrayList<>();
		if (null != order.getAuditOrderStatus() && !order.getAuditOrderStatus().isEmpty()) {
			auditOrderStatusModels = order.getAuditOrderStatus().stream()
					.filter(auditOrderStatus -> !auditOrderStatus.isDeleted()
							&& (OrderStatus.DRIVER_ASSIGNED == auditOrderStatus.getOrderStatus()
									|| OrderStatus.OUT_FOR_DELIVERY == auditOrderStatus.getOrderStatus()
									|| OrderStatus.DELIVERY_FAILED == auditOrderStatus.getOrderStatus()
									|| OrderStatus.COMPLETED == auditOrderStatus.getOrderStatus()))
					.map(auditOrderStatus -> userBuilder.createAuditOrderStatusModel(auditOrderStatus))
					.collect(Collectors.toList());
			log.info("createAuditOrderStatusModel :: auditOrderStatusModels " + auditOrderStatusModels);
		}

		return new OrderModel(order.getId(), order.getDeliveryType().getId(), customerAddressDetailModel,
				promoCodeModel, orderItemDetailModels, orderPriceDetailModel, orderRatingDetailModel, etaModel,
				order.getOrderStatus().getId(), customerUser, dispatcherUser, order.getCreatedAt(),
				order.getModifiedAt(), auditOrderStatusModels);
	}

	@Transactional
	public Order acceptOrder(Order order, boolean reject) {
		log.info("acceptOrder");
		if (reject) {
			order.setOrderStatus(OrderStatus.REASSIGN_DRIVER);

			order.getDriverOrderMapping().setRejected(true);
			driverOrderMappingRepo.save(order.getDriverOrderMapping());
			log.info("acceptOrder :: order.getDriverOrderMapping() " + order.getDriverOrderMapping());
		} else {
			order.setOrderStatus(OrderStatus.OUT_FOR_DELIVERY);
		}

		orderRepo.save(order);
		log.info("acceptOrder :: order " + order);

		return order;
	}

	@Transactional
	public Order updateOrderEta(UpdateOrderEtaForm updateOrderEtaForm, Order order, MasterEta masterEta) {
		log.info("updateOrderEta");
		UpdateStatusModel updateStatusModel = new UpdateStatusModel(false, false);
		if (order.getEta().getId() != masterEta.getId()) {
			order.setEta(masterEta);

			updateStatusModel.setUpdateStatus(true);
			updateStatusModel.setApprovalReq(false);
		}

		if (!updateStatusModel.isUpdateStatus())
			throw new AppException(StringConst.UPDATE_NOTHING, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		orderRepo.save(order);
		log.info("acceptOrder :: order " + order);

		return order;
	}

	@Transactional
	public OrdersCountModel createOrdersCountModel(int newOrdersCount, int pendingOrdersCount) {
		log.info("ordersCount");

		return new OrdersCountModel(customerBuilder.createCountModel(newOrdersCount),
				customerBuilder.createCountModel(pendingOrdersCount));
	}

	@Transactional
	public Order completeOrder(Order order) {
		log.info("completeOrder");

		order.setOrderStatus(OrderStatus.COMPLETED);
		orderRepo.save(order);
		log.info("completeOrder :: order " + order);

		order.getOrderRatingDetail().setDispatcherPendingByCustomer(true);
		order.getOrderRatingDetail().setDriverPendingByCustomer(true);
		order.getOrderRatingDetail().setCustomerPendingByDispatcher(true);
		order.getOrderRatingDetail().setCustomerPendingByDriver(true);
		orderRatingDetailRepo.save(order.getOrderRatingDetail());
		log.info("completeOrder :: order.getOrderRatingDetail() " + order.getOrderRatingDetail());

		return order;
	}

	@Transactional
	public boolean updateDriverInventoryWithDetailsAfterCompleteOrder(Order order, UserEntity userEntity) {
		log.info("updateDriverInventoryWithDetailsAfterCompleteOrder");
		order.getOrderItemDetails().forEach(orderItemDetail -> {
			DriverInventory driverInventory = driverInventoryRepo.findByDriverIdAndProductId(
					userEntity.getUserDriverDetail().getId(), orderItemDetail.getProduct().getId());
			log.info("updateDriverInventoryWithDetailsAfterCompleteOrder :: driverInventory " + driverInventory);

			if (null != driverInventory) {
				driverInventory
						.getDriverInventoryDetails().stream().filter(driverInventoryDetail -> driverInventoryDetail
								.getProductPriceDetail().getPriceType() == orderItemDetail.getPriceType())
						.forEach(driverInventoryDetail -> {
							int quantity = driverInventoryDetail.getQuantity();
							if (quantity > AppConst.NUMBER.ZERO)
								quantity = quantity - 1;

							driverInventoryDetail.setQuantity(quantity);
						});

				driverInventoryDetailRepo.save(driverInventory.getDriverInventoryDetails());

				Optional<DriverInventoryDetail> driverInventoryDetail = driverInventory.getDriverInventoryDetails()
						.stream().filter(detail -> detail.getQuantity() > AppConst.NUMBER.ZERO).findAny();

				driverInventory.setInStock(driverInventoryDetail.isPresent());
				driverInventoryRepo.save(driverInventory);
			}
		});

		return true;
	}

	@Transactional
	public boolean onlineStatus(boolean offline, UserDriverDetail userDriverDetail) {
		log.info("onlineStatus");

		if (offline)
			userDriverDetail.setOnline(false);
		else
			userDriverDetail.setOnline(true);

		userDriverDetailRepo.save(userDriverDetail);
		log.info("onlineStatus :: userDriverDetail " + userDriverDetail);

		return true;
	}

	@Transactional
	public boolean rateCustomer(RateUserForm rateUserForm, Order order, UserEntity userEntity) {
		log.info("rateCustomer");
		if (AppConst.FLOAT_ZERO == rateUserForm.getRating())
			throw new AppException(StringConst.RATING_INVALID, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		userBuilder.rateUser(rateUserForm, order, order.getUser(), userEntity);

		order.getOrderRatingDetail().setCustomerPendingByDriver(false);
		order.getOrderRatingDetail().setCustomerRatingByDriver(rateUserForm.getRating());
		orderRatingDetailRepo.save(order.getOrderRatingDetail());
		log.info("rateCustomer :: order.getOrderRatingDetail() " + order.getOrderRatingDetail());

		return true;
	}

}
