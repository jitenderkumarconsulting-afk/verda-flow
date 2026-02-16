package com.org.verdaflow.rest.event;

import org.springframework.context.ApplicationEvent;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DeactivateDriverEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;

	private String email;
	private String name;

	public DeactivateDriverEvent(Object source) {
		super(source);
	}

	public DeactivateDriverEvent(Object source, String email, String name) {
		super(source);
		this.email = email;
		this.name = name;
	}
}
