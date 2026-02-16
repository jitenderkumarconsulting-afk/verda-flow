package com.org.verdaflow.rest.event;

import org.springframework.context.ApplicationEvent;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ActivateCustomerEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;

	private String email;
	private String firstName;
	private String lastName;

	public ActivateCustomerEvent(Object source) {
		super(source);
	}

	public ActivateCustomerEvent(Object source, String email, String firstName, String lastName) {
		super(source);
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
	}

}
