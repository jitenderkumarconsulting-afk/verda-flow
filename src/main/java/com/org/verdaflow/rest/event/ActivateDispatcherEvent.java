package com.org.verdaflow.rest.event;

import org.springframework.context.ApplicationEvent;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ActivateDispatcherEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;

	private String email;
	private String storeName;
	private String managerName;

	public ActivateDispatcherEvent(Object source) {
		super(source);
	}

	public ActivateDispatcherEvent(Object source, String email, String storeName, String managerName) {
		super(source);
		this.email = email;
		this.storeName = storeName;
		this.managerName = managerName;
	}

}
