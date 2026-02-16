package com.org.verdaflow.rest.common.enums;

import com.org.verdaflow.rest.config.common.AppConst;

public enum LoginAttempt implements IdentifierType<Integer> {
	FAILED(AppConst.NUMBER.ONE, "FAILED"), SUCCESS(AppConst.NUMBER.TWO, "SUCCESS");

	private Integer id;
	private String name;

	private LoginAttempt(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public static LoginAttempt valueOf(Integer id) {
		return EnumHelper.INSTANCE.valueOf(id, LoginAttempt.values());
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
