package com.org.verdaflow.rest.event;

import org.springframework.context.ApplicationEvent;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ReportProblemEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;

	private String adminName;
	private String email;
	private String mobileNumber;
	private String role;
	private String name;
	private String title;
	private String message;

	public ReportProblemEvent(Object source) {
		super(source);
	}

	public ReportProblemEvent(Object source, String adminName, String email, String mobileNumber, String role,
			String name, String title, String message) {
		super(source);
		this.adminName = adminName;
		this.email = email;
		this.mobileNumber = mobileNumber;
		this.role = role;
		this.name = name;
		this.title = title;
		this.message = message;
	}
}
