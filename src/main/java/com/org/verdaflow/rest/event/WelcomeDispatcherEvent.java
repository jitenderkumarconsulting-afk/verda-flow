package com.org.verdaflow.rest.event;

import org.springframework.context.ApplicationEvent;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class WelcomeDispatcherEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;

	private String email;
	private String mobileNumber;
	private String password;

	private String storeName;
	private String managerName;

	public WelcomeDispatcherEvent(Object source) {
		super(source);
	}

	public WelcomeDispatcherEvent(Object source, String email, String mobileNumber, String password, String storeName,
			String managerName) {
		super(source);
		this.email = email;
		this.mobileNumber = mobileNumber;
		this.password = password;
		this.storeName = storeName;
		this.managerName = managerName;
	}
}
