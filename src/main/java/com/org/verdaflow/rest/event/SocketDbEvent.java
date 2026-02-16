package com.org.verdaflow.rest.event;

import org.springframework.context.ApplicationEvent;

import com.org.verdaflow.rest.socket.model.LocationModel;
import com.org.verdaflow.rest.socket.model.RouteModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SocketDbEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;

	private LocationModel locationModel;
	private RouteModel routeModel;

	public SocketDbEvent(Object source) {
		super(source);
	}

	public SocketDbEvent(Object source, LocationModel locationModel) {
		super(source);
		this.locationModel = locationModel;
	}

	public SocketDbEvent(Object source, RouteModel routeModel) {
		super(source);
		this.routeModel = routeModel;
	}

}
