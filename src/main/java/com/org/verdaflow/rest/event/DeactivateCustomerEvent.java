package com.org.verdaflow.rest.event;

import org.springframework.context.ApplicationEvent;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DeactivateCustomerEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;

	private String email;
	private String firstName;
	private String lastName;

	public DeactivateCustomerEvent(Object source) {
		super(source);
	}

	public DeactivateCustomerEvent(Object source, String email, String firstName, String lastName) {
		super(source);
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
	}
}
