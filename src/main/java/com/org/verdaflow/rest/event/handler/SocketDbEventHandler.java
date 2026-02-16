package com.org.verdaflow.rest.event.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.org.verdaflow.rest.config.common.AppConst;
import com.org.verdaflow.rest.entity.AuditDriverLocation;
import com.org.verdaflow.rest.entity.DriverLocationDetail;
import com.org.verdaflow.rest.entity.Order;
import com.org.verdaflow.rest.entity.UserDriverDetail;
import com.org.verdaflow.rest.event.SocketDbEvent;
import com.org.verdaflow.rest.repo.AuditDriverLocationRepo;
import com.org.verdaflow.rest.repo.DriverLocationDetailRepo;
import com.org.verdaflow.rest.repo.OrderRepo;
import com.org.verdaflow.rest.repo.UserDriverDetailRepo;

@Component
public class SocketDbEventHandler implements ApplicationListener<SocketDbEvent> {
	public static final Logger log = LoggerFactory.getLogger(SocketDbEventHandler.class);

	@Autowired
	private UserDriverDetailRepo userDriverDetailRepo;

	@Autowired
	private AuditDriverLocationRepo auditDriverLocationRepo;

	@Autowired
	private DriverLocationDetailRepo driverLocationDetailRepo;

	@Autowired
	private OrderRepo orderRepo;

	@Override
	public void onApplicationEvent(SocketDbEvent event) {
		log.info("onApplicationEvent");
		UserDriverDetail userDriverDetail = null;
		DriverLocationDetail driverLocationDetail = null;

		if (null != event.getLocationModel()) {
			if (AppConst.NUMBER.ZERO == event.getLocationModel().getDispatcherId()
					|| AppConst.NUMBER.ZERO == event.getLocationModel().getDriverId())
				return;

			userDriverDetail = userDriverDetailRepo.findDriverByIdAndDispatcherId(
					event.getLocationModel().getDriverId(), event.getLocationModel().getDispatcherId());
			if (null == userDriverDetail)
				return;

			driverLocationDetail = driverLocationDetailRepo.findByDriverId(userDriverDetail.getId());

			driverLocationDetail.setLat(event.getLocationModel().getLat());
			driverLocationDetail.setLng(event.getLocationModel().getLng());
			driverLocationDetail.setRotation(event.getLocationModel().getRotation());
			driverLocationDetailRepo.save(driverLocationDetail);
			log.info("saveDriverLocation :: driverLocationDetail " + driverLocationDetail);

			AuditDriverLocation auditDriverLocation = new AuditDriverLocation();
			auditDriverLocation.setLat(event.getLocationModel().getLat());
			auditDriverLocation.setLng(event.getLocationModel().getLng());
			auditDriverLocation.setRotation(event.getLocationModel().getRotation());
			auditDriverLocation.setDriverLocationDetail(driverLocationDetail);
			auditDriverLocationRepo.save(auditDriverLocation);
			log.info("saveAuditDriverLocation :: auditDriverLocation " + auditDriverLocation);
		} else if (null != event.getRouteModel()) {
			if (AppConst.NUMBER.ZERO == event.getRouteModel().getDispatcherId()
					|| AppConst.NUMBER.ZERO == event.getRouteModel().getDriverId())
				return;

			userDriverDetail = userDriverDetailRepo.findDriverByIdAndDispatcherId(event.getRouteModel().getDriverId(),
					event.getRouteModel().getDispatcherId());
			if (null == userDriverDetail)
				return;

			driverLocationDetail = driverLocationDetailRepo.findByDriverId(userDriverDetail.getId());

			if (null == driverLocationDetail.getOrder() || (null != driverLocationDetail.getOrder()
					&& event.getRouteModel().getOrderId() != driverLocationDetail.getOrder().getId())) {
				Order order = orderRepo.findByOrderIdAndDriverId(event.getRouteModel().getOrderId(),
						userDriverDetail.getId());
				log.info("saveDriverRoute :: order " + order);
				if (null != order)
					driverLocationDetail.setOrder(order);
			}

			driverLocationDetail.setRoute(event.getRouteModel().getRoute());
			driverLocationDetailRepo.save(driverLocationDetail);
			log.info("saveDriverRoute :: driverLocationDetail " + driverLocationDetail);

			AuditDriverLocation auditDriverLocation = new AuditDriverLocation();
			auditDriverLocation.setOrder(driverLocationDetail.getOrder());
			auditDriverLocation.setRoute(event.getRouteModel().getRoute());
			auditDriverLocation.setDriverLocationDetail(driverLocationDetail);
			auditDriverLocationRepo.save(auditDriverLocation);
			log.info("saveAuditDriverLocation :: auditDriverLocation " + auditDriverLocation);
		}
	}
}
