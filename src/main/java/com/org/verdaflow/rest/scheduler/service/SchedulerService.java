package com.org.verdaflow.rest.scheduler.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.org.verdaflow.rest.api.customer.CustomerBuilder;
import com.org.verdaflow.rest.common.enums.OrderStatus;
import com.org.verdaflow.rest.entity.AuditOrderStatus;
import com.org.verdaflow.rest.entity.Order;
import com.org.verdaflow.rest.notification.NotificationBuilder;
import com.org.verdaflow.rest.repo.AuditOrderStatusRepo;
import com.org.verdaflow.rest.repo.OrderRepo;

@Component
public class SchedulerService {
	public static final Logger log = LoggerFactory.getLogger(SchedulerService.class);

	@Autowired
	private OrderRepo orderRepo;

	@Autowired
	private CustomerBuilder customerBuilder;

	@Autowired
	private AuditOrderStatusRepo auditOrderStatusRepo;

	@Autowired
	private NotificationBuilder notificationBuilder;

	@Transactional
	public void updateOrderStatusToPlaced() {
		log.info("updateOrderStatusToPlaced");
		List<Order> orders = orderRepo.findAllOrdersToBePlaced();
		log.info("updateOrderStatusToPlaced :: orders.size() " + orders.size());

		List<AuditOrderStatus> auditOrderStatusList = new ArrayList<>();

		if (!orders.isEmpty()) {
			orders.forEach(order -> {
				log.info("updateOrderStatusToPlaced :: order " + order);
				order.setOrderStatus(OrderStatus.PLACED);
				order.setPlaced(true);

				auditOrderStatusList
						.add(customerBuilder.createAuditOrderStatus(order, order.getUser(), OrderStatus.PLACED));
			});

			orderRepo.save(orders);
			log.info("updateOrderStatusToPlaced :: orders " + orders);

			auditOrderStatusRepo.save(auditOrderStatusList);
			log.info("updateOrderStatusToPlaced :: auditOrderStatusList " + auditOrderStatusList);

			notificationBuilder.createOrderPlacedNotifications(orders);
		}
	}

}
