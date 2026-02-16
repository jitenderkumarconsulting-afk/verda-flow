package com.org.verdaflow.rest.event;

import org.springframework.context.ApplicationEvent;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ForgotPasswordEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;

	private String verificationCode;
	private String userName;
	private String userMail;
	private int userId;
	private int roleId;

	public ForgotPasswordEvent(Object source) {
		super(source);
	}

	public ForgotPasswordEvent(Object source, String verificationCode, String userName, String userMail, int userId,
			int roleId) {
		super(source);
		this.verificationCode = verificationCode;
		this.userName = userName;
		this.userMail = userMail;
		this.userId = userId;
		this.roleId = roleId;
	}
}
