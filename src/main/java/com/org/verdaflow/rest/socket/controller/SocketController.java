package com.org.verdaflow.rest.socket.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import com.org.verdaflow.rest.event.SocketDbEvent;
import com.org.verdaflow.rest.socket.model.LocationModel;
import com.org.verdaflow.rest.socket.model.RouteModel;

@RestController
public class SocketController {
	public static final Logger log = LoggerFactory.getLogger(SocketController.class);

	private SimpMessagingTemplate template;

	@Autowired
	private ApplicationEventMulticaster applicationEventMulticaster;

	@Autowired
	public SocketController(SimpMessagingTemplate template) {
		this.template = template;
	}

	@MessageMapping("/send/location/driver")
	public void sendDriverLocation(LocationModel locationModel) throws Exception {
		log.debug("sendDriverLocation");

		this.template.convertAndSend("/publish/location/dispatcher/" + locationModel.getDispatcherId() + "/driver/"
				+ locationModel.getDriverId(), locationModel);
		this.template.convertAndSend("/publish/location/dispatcher/" + locationModel.getDispatcherId(), locationModel);

		SocketDbEvent socketDbEvent = new SocketDbEvent(this, locationModel);
		applicationEventMulticaster.multicastEvent(socketDbEvent);
	}

	@MessageMapping("/send/route/driver")
	public void sendDriverRoute(RouteModel routeModel) throws Exception {
		log.debug("sendDriverRoute");

		this.template.convertAndSend(
				"/publish/route/dispatcher/" + routeModel.getDispatcherId() + "/driver/" + routeModel.getDriverId(),
				routeModel);

		SocketDbEvent socketDbEvent = new SocketDbEvent(this, routeModel);
		applicationEventMulticaster.multicastEvent(socketDbEvent);
	}

}
