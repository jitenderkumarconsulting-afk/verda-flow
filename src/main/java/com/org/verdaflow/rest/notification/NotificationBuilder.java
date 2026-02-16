package com.org.verdaflow.rest.notification;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.org.verdaflow.rest.common.enums.DeliveryType;
import com.org.verdaflow.rest.common.enums.DeviceType;
import com.org.verdaflow.rest.common.enums.NotificationType;
import com.org.verdaflow.rest.config.common.StringConst;
import com.org.verdaflow.rest.entity.DeviceDetail;
import com.org.verdaflow.rest.entity.Notification;
import com.org.verdaflow.rest.entity.Order;
import com.org.verdaflow.rest.entity.UserDriverDetail;
import com.org.verdaflow.rest.entity.UserEntity;
import com.org.verdaflow.rest.notification.model.NotificationModel;
import com.org.verdaflow.rest.notification.service.NotificationService;
import com.org.verdaflow.rest.repo.DeviceDetailRepo;
import com.org.verdaflow.rest.repo.NotificationRepo;

@Component
public class NotificationBuilder {
	// Initialize the Log4j logger.
	public static final Logger log = LoggerFactory.getLogger(NotificationBuilder.class);

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private NotificationRepo notificationRepo;

	@Autowired
	private DeviceDetailRepo deviceDetailRepo;

	@Autowired
	private Environment env;

	@Transactional
	public void createOrderPlacedNotifications(List<Order> orders) {
		log.info("createOrderPlacedNotifications");

		// send notifications to Dispatcher (Order placed)
		String title = env.getProperty(StringConst.NOTIFICATION_TITLE_ORDER_PLACED);

		List<Notification> notifications = new ArrayList<>();

		orders.forEach(order -> {
			String body = String.format(env.getProperty(StringConst.NOTIFICATION_BODY_ORDER_PLACED), order.getId());
			String bodyFormatted = String.format(env.getProperty(StringConst.NOTIFICATION_BODY_FORMATTED_ORDER_PLACED),
					order.getId());

			Notification notification = createNotification(order, order.getUser(),
					order.getUserDispatcherDetail().getUser(), title, body, bodyFormatted,
					NotificationType.ORDER_PLACED);
			notifications.add(notification);
		});

		notificationRepo.save(notifications);
		log.info("createOrderPlacedNotifications :: notifications " + notifications);

		callSendNotifications(notifications);
	}

	@Transactional
	public void createOrderConfirmedNotification(Order order, UserEntity byUserEntity) {
		log.info("createOrderConfirmedNotification");

		// send notification to Customer (Confirmed by Dispatcher)
		String title = env.getProperty(StringConst.NOTIFICATION_TITLE_ORDER_CONFIRMED);
		String body = String.format(env.getProperty(StringConst.NOTIFICATION_BODY_ORDER_CONFIRMED), order.getId(),
				order.getOrderItemDetails().size(), order.getUserDispatcherDetail().getStoreName());
		String bodyFormatted = String.format(env.getProperty(StringConst.NOTIFICATION_BODY_FORMATTED_ORDER_CONFIRMED),
				order.getId(), order.getOrderItemDetails().size(), order.getUserDispatcherDetail().getStoreName());

		Notification notification = createNotification(order, byUserEntity, order.getUser(), title, body, bodyFormatted,
				NotificationType.ORDER_CONFIRMED);

		notificationRepo.save(notification);
		log.info("createOrderConfirmedNotification :: notification " + notification);

		callSendNotification(notification);
	}

	@Transactional
	public void createOrderPreparedNotification(Order order, UserEntity byUserEntity) {
		log.info("createOrderConfirmedNotification");

		String body;
		String bodyFormatted;

		// send notification to Customer (Prepared by Dispatcher)
		String title = env.getProperty(StringConst.NOTIFICATION_TITLE_ORDER_PREPARED);

		if (DeliveryType.PICKUP == order.getDeliveryType()) {
			body = String.format(env.getProperty(StringConst.NOTIFICATION_BODY_ORDER_PREPARED_PICKUP), order.getId(),
					order.getOrderItemDetails().size(), order.getUserDispatcherDetail().getStoreName());
			bodyFormatted = String.format(
					env.getProperty(StringConst.NOTIFICATION_BODY_FORMATTED_ORDER_PREPARED_PICKUP), order.getId(),
					order.getOrderItemDetails().size(), order.getUserDispatcherDetail().getStoreName());
		} else {
			body = String.format(env.getProperty(StringConst.NOTIFICATION_BODY_ORDER_PREPARED_DELIVERY), order.getId(),
					order.getOrderItemDetails().size(), order.getUserDispatcherDetail().getStoreName());
			bodyFormatted = String.format(
					env.getProperty(StringConst.NOTIFICATION_BODY_FORMATTED_ORDER_PREPARED_DELIVERY), order.getId(),
					order.getOrderItemDetails().size(), order.getUserDispatcherDetail().getStoreName());
		}

		Notification notification = createNotification(order, byUserEntity, order.getUser(), title, body, bodyFormatted,
				NotificationType.ORDER_PREPARED);

		notificationRepo.save(notification);
		log.info("createOrderConfirmedNotification :: notification " + notification);

		callSendNotification(notification);
	}

	@Transactional
	public void createOrderDriverAssignedNotification(Order order, UserEntity byUserEntity,
			UserDriverDetail userDriverDetail) {
		log.info("createOrderDriverAssignedNotification");

		// send notification to Driver (Driver assigned by Dispatcher)
		String title = env.getProperty(StringConst.NOTIFICATION_TITLE_ORDER_DRIVER_ASSIGNED);
		String body = String.format(env.getProperty(StringConst.NOTIFICATION_BODY_ORDER_DRIVER_ASSIGNED),
				order.getId());
		String bodyFormatted = String.format(env.getProperty(StringConst.NOTIFICATION_BODY_FORMATTED_DRIVER_ASSIGNED),
				order.getId());

		Notification notification = createNotification(order, byUserEntity, userDriverDetail.getUser(), title, body,
				bodyFormatted, NotificationType.ORDER_DRIVER_ASSIGNED);
		notificationRepo.save(notification);
		log.info("createOrderDriverAssignedNotification :: notification " + notification);

		callSendNotification(notification);
	}

	@Transactional
	public void createOrderReassignDriverNotification(Order order, UserEntity byUserEntity) {
		log.info("createOrderReassignDriverNotification");

		// send notification to Dispatcher (Order Request rejected by Driver)
		String title = env.getProperty(StringConst.NOTIFICATION_TITLE_ORDER_REASSIGN_DRIVER);
		String body = String.format(env.getProperty(StringConst.NOTIFICATION_BODY_ORDER_REASSIGN_DRIVER), order.getId(),
				order.getDriverOrderMapping().getUserDriverDetail().getName());
		String bodyFormatted = String.format(
				env.getProperty(StringConst.NOTIFICATION_BODY_FORMATTED_ORDER_REASSIGN_DRIVER), order.getId(),
				order.getDriverOrderMapping().getUserDriverDetail().getName());

		Notification notification = createNotification(order, byUserEntity, order.getUserDispatcherDetail().getUser(),
				title, body, bodyFormatted, NotificationType.ORDER_REASSIGN_DRIVER);
		notificationRepo.save(notification);
		log.info("createOrderReassignDriverNotification :: notification " + notification);

		callSendNotification(notification);
	}

	@Transactional
	public void createOrderOutForDeliveryNotification(Order order, UserEntity byUserEntity) {
		log.info("createOrderOutForDeliveryNotification");

		// send notification to Dispatcher (Order Request accepted by Driver)
		createOrderOutForDeliveryNotificationForDispatcher(order, byUserEntity);

		// send notification to Customer (Out for Delivery)
		createOrderOutForDeliveryNotificationForCustomer(order, byUserEntity);
	}

	@Transactional
	public void createOrderOutForDeliveryNotificationForDispatcher(Order order, UserEntity byUserEntity) {
		log.info("createOrderOutForDeliveryNotificationForDispatcher");

		// send notification to Dispatcher (Accepted by Driver)
		String title = env.getProperty(StringConst.NOTIFICATION_TITLE_ORDER_OUT_FOR_DELIVERY_DISPATCHER);
		String body = String.format(env.getProperty(StringConst.NOTIFICATION_BODY_ORDER_OUT_FOR_DELIVERY_DISPATCHER),
				order.getId(), order.getDriverOrderMapping().getUserDriverDetail().getName());
		String bodyFormatted = String.format(
				env.getProperty(StringConst.NOTIFICATION_BODY_ORDER_OUT_FOR_DELIVERY_DISPATCHER), order.getId(),
				order.getDriverOrderMapping().getUserDriverDetail().getName());

		Notification notification = createNotification(order, byUserEntity, order.getUserDispatcherDetail().getUser(),
				title, body, bodyFormatted, NotificationType.ORDER_OUT_FOR_DELIVERY);
		notificationRepo.save(notification);
		log.info("createOrderOutForDeliveryNotificationForDispatcher :: notification " + notification);

		callSendNotification(notification);
	}

	@Transactional
	public void createOrderOutForDeliveryNotificationForCustomer(Order order, UserEntity byUserEntity) {
		log.info("createOrderOutForDeliveryNotificationForCustomer");

		// send notification to Customer (Out for Delivery)
		String title = env.getProperty(StringConst.NOTIFICATION_TITLE_ORDER_OUT_FOR_DELIVERY_CUSTOMER);
		String body = String.format(env.getProperty(StringConst.NOTIFICATION_BODY_ORDER_OUT_FOR_DELIVERY_CUSTOMER),
				order.getId(), order.getOrderItemDetails().size(),
				order.getDriverOrderMapping().getUserDriverDetail().getName());
		String bodyFormatted = String.format(
				env.getProperty(StringConst.NOTIFICATION_BODY_FORMATTED_ORDER_OUT_FOR_DELIVERY_CUSTOMER), order.getId(),
				order.getOrderItemDetails().size(), order.getDriverOrderMapping().getUserDriverDetail().getName());

		Notification notification = createNotification(order, byUserEntity, order.getUser(), title, body, bodyFormatted,
				NotificationType.ORDER_OUT_FOR_DELIVERY);
		notificationRepo.save(notification);
		log.info("createOrderOutForDeliveryNotificationForCustomer :: notification " + notification);

		callSendNotification(notification);
	}

	@Transactional
	public void createOrderDeliveryFailedNotification(Order order, UserEntity byUserEntity) {
		log.info("createOrderDeliveryFailedNotification");

		// send notification to Dispatcher

		// send notification to Customer
	}

	@Transactional
	public void createOrderCompletedPickupNotification(Order order, UserEntity byUserEntity) {
		log.info("createOrderCompletedPickupNotification");

		// Send notification to Customer (Picked by Customer)
		String title = env.getProperty(StringConst.NOTIFICATION_TITLE_ORDER_COMPLETED_PICKUP);
		String body = String.format(env.getProperty(StringConst.NOTIFICATION_BODY_ORDER_COMPLETED_PICKUP),
				order.getId(), order.getOrderItemDetails().size());
		String bodyFormatted = String.format(
				env.getProperty(StringConst.NOTIFICATION_BODY_FORMATTED_ORDER_COMPLETED_PICKUP), order.getId(),
				order.getOrderItemDetails().size());

		Notification notification = createNotification(order, byUserEntity, order.getUser(), title, body, bodyFormatted,
				NotificationType.ORDER_COMPLETED);
		notificationRepo.save(notification);
		log.info("createOrderCompletedPickupNotification :: notification " + notification);

		callSendNotification(notification);
	}

	@Transactional
	public void createOrderCompletedDeliveryNotification(Order order, UserEntity byUserEntity) {
		log.info("createOrderCompletedDeliveryNotification");

		// Send notification to Dispatcher (Delivered by Driver)
		createOrderCompletedDeliveryNotificationForDispatcher(order, byUserEntity);

		// Send notification to Customer (Delivered by Driver)
		createOrderCompletedDeliveryNotificationForCustomer(order, byUserEntity);
	}

	@Transactional
	public void createOrderCompletedDeliveryNotificationForDispatcher(Order order, UserEntity byUserEntity) {
		log.info("createOrderCompletedDeliveryNotificationForDispatcher");

		// Send notification to Dispatcher (Delivered by Driver)
		String title = env.getProperty(StringConst.NOTIFICATION_TITLE_ORDER_COMPLETED_DELIVERY_DISPATCHER);
		String body = String.format(env.getProperty(StringConst.NOTIFICATION_BODY_ORDER_COMPLETED_DELIVERY_DISPATCHER),
				order.getId());
		String bodyFormatted = String.format(
				env.getProperty(StringConst.NOTIFICATION_BODY_FORMATTED_ORDER_COMPLETED_DELIVERY_DISPATCHER),
				order.getId());

		Notification notification = createNotification(order, byUserEntity, order.getUserDispatcherDetail().getUser(),
				title, body, bodyFormatted, NotificationType.ORDER_COMPLETED);

		notificationRepo.save(notification);
		log.info("createOrderCompletedDeliveryNotificationForDispatcher :: notification " + notification);

		callSendNotification(notification);
	}

	@Transactional
	public void createOrderCompletedDeliveryNotificationForCustomer(Order order, UserEntity byUserEntity) {
		log.info("createOrderCompletedDeliveryNotificationForCustomer");

		// Send notification to Customer (Delivered by Driver)
		String title = env.getProperty(StringConst.NOTIFICATION_TITLE_ORDER_COMPLETED_DELIVERY_CUSTOMER);
		String body = String.format(env.getProperty(StringConst.NOTIFICATION_BODY_ORDER_COMPLETED_DELIVERY_CUSTOMER),
				order.getId());
		String bodyFormatted = String.format(
				env.getProperty(StringConst.NOTIFICATION_BODY_FORMATTED_ORDER_COMPLETED_DELIVERY_CUSTOMER),
				order.getId());

		Notification notification = createNotification(order, byUserEntity, order.getUser(), title, body, bodyFormatted,
				NotificationType.ORDER_COMPLETED);

		notificationRepo.save(notification);
		log.info("createOrderCompletedDeliveryNotificationForCustomer :: notification " + notification);

		callSendNotification(notification);
	}

	@Transactional
	public void createOrderCancelledByStoreNotification(Order order, UserEntity byUserEntity) {
		log.info("createOrderCancelledByStoreNotification");

		// send notification to Customer (Cancelled by Store)
		String title = env.getProperty(StringConst.NOTIFICATION_TITLE_ORDER_CANCELLED_BY_STORE);
		String body = String.format(env.getProperty(StringConst.NOTIFICATION_BODY_ORDER_CANCELLED_BY_STORE),
				order.getId(), order.getOrderItemDetails().size(), order.getUserDispatcherDetail().getStoreName());
		String bodyFormatted = String.format(
				env.getProperty(StringConst.NOTIFICATION_BODY_FORMATTED_ORDER_CANCELLED_BY_STORE), order.getId(),
				order.getOrderItemDetails().size(), order.getUserDispatcherDetail().getStoreName());

		Notification notification = createNotification(order, byUserEntity, order.getUser(), title, body, bodyFormatted,
				NotificationType.ORDER_CANCELLED_BY_STORE);

		notificationRepo.save(notification);
		log.info("createOrderCancelledByStoreNotification :: notification " + notification);

		callSendNotification(notification);
	}

	@Transactional
	public void createOrderCancelledByUserNotification(Order order, UserEntity byUserEntity) {
		log.info("createOrderCancelledByUserNotification");

		// send notification to Dispatcher

		// send notification to Driver if driver was assigned
	}

	@Transactional
	public Notification createNotification(Order order, UserEntity byUserEntity, UserEntity toUserEntity, String title,
			String body, String bodyFormatted, NotificationType notificationType) {
		log.info("createNotification");
		Notification notification = new Notification();
		notification.setTitle(title);
		notification.setBody(body);
		notification.setBodyFormatted(bodyFormatted);
		notification.setUser(toUserEntity);
		notification.setCreatedBy(byUserEntity);
		notification.setRead(false);
		notification.setOrder(order);
		notification.setNotificationType(notificationType);

		return notification;
	}

	public void callSendNotification(Notification notification) {
		log.info("callSendNotification");
		DeviceDetail deviceDetail = deviceDetailRepo.findByUser(notification.getUser().getId());
		log.info("callSendNotification :: deviceDetail " + deviceDetail);

		if (null != deviceDetail && null != deviceDetail.getDeviceToken()
				&& StringUtils.isNotBlank(deviceDetail.getDeviceToken())
				&& notification.getUser().isPushNotificationStatus()) {
			log.info("callSendNotification :: deviceDetail.getDeviceType() " + deviceDetail.getDeviceType());

			NotificationModel notificationModel = createNotificationModel(notification);
			ExecutorService threadExecutor = Executors.newSingleThreadExecutor();

			try {
				if (DeviceType.WEB == deviceDetail.getDeviceType()) {
					try {
						threadExecutor.execute(new Runnable() {
							@Override
							public void run() {
								notificationService.sendOneSignalNotification(notificationModel, deviceDetail);
							}
						});
					} catch (Exception e) {
						log.info("callSendNotification :: ERROR " + e.getMessage());
					} finally {
						threadExecutor.shutdownNow();
					}
				} else if (DeviceType.ANDROID == deviceDetail.getDeviceType()
						|| DeviceType.IOS == deviceDetail.getDeviceType()) {
					try {
						threadExecutor.execute(new Runnable() {
							@Override
							public void run() {
								notificationService.sendFcmNotification(notificationModel, deviceDetail);
							}
						});
					} catch (Exception e) {
						log.info("callSendNotification :: ERROR " + e.getMessage());
					} finally {
						threadExecutor.shutdownNow();
					}
				}
			} catch (Exception e) {
				log.info("callSendNotification :: ERROR " + e.getMessage());
			}
		}

	}

	@Transactional
	public void callSendNotifications(List<Notification> notifications) {
		log.info("callSendNotifications");

		notifications.forEach(notification -> {
			log.info("callSendNotifications :: userId " + notification.getUser().getId());
			DeviceDetail deviceDetail = deviceDetailRepo.findByUser(notification.getUser().getId());
			log.info("callSendNotifications :: deviceDetail " + deviceDetail);

			if (null != deviceDetail && null != deviceDetail.getDeviceToken()
					&& StringUtils.isNotBlank(deviceDetail.getDeviceToken())) {
				log.info("callSendNotifications :: deviceDetail.getDeviceType() " + deviceDetail.getDeviceType());

				ExecutorService threadExecutor = Executors.newSingleThreadExecutor();

				NotificationModel notificationModel = createNotificationModel(notification);

				try {
					if (DeviceType.WEB == deviceDetail.getDeviceType()) {

						// log.info("callSendNotifications.sendOneSignalNotification");
						// notificationService.sendOneSignalNotification(notificationModel,
						// deviceDetail);
						try {
							threadExecutor.execute(new Runnable() {
								@Override
								public void run() {
									log.info("callSendNotifications.sendOneSignalNotification");
									notificationService.sendOneSignalNotification(notificationModel, deviceDetail);
								}
							});
						} catch (Exception e) {
							log.info("callSendNotification :: ERROR " + e.getMessage());
						} finally {
							threadExecutor.shutdownNow();
						}
					} else if (DeviceType.ANDROID == deviceDetail.getDeviceType()
							|| DeviceType.IOS == deviceDetail.getDeviceType()) {
						try {
							threadExecutor.execute(new Runnable() {
								@Override
								public void run() {
									log.info("callSendNotifications.sendFcmNotification");
									notificationService.sendFcmNotification(notificationModel, deviceDetail);
								}
							});
						} catch (Exception e) {
							log.info("callSendNotification :: ERROR " + e.getMessage());
						} finally {
							threadExecutor.shutdownNow();
						}
						// log.info("callSendNotifications.sendFcmNotification");
						// notificationService.sendFcmNotification(notificationModel, deviceDetail);
					}
				} catch (Exception e) {
					log.info("callSendNotifications :: ERROR " + e.getMessage());
				}
			}
		});

	}

	@Transactional
	public NotificationModel createNotificationModel(Notification notification) {
		log.info("ManagerBuilder.createNotificationModel");
		if (null != notification.getOrder())
			return new NotificationModel(notification.getId(), notification.getTitle(), notification.getBody(),
					notification.getBodyFormatted(), notification.isRead(), notification.getOrder().getId(),
					notification.getNotificationType().getId(), notification.getCreatedAt());
		else
			return new NotificationModel(notification.getId(), notification.getTitle(), notification.getBody(),
					notification.getBodyFormatted(), notification.isRead(), notification.getNotificationType().getId(),
					notification.getCreatedAt());
	}

}
