package com.org.verdaflow.rest.common.enums;

import com.org.verdaflow.rest.config.common.AppConst;

public enum ForgotPasswordRequest implements IdentifierType<Integer> {
	REQUESTED(AppConst.NUMBER.ONE, "REQUESTED"), RESET(AppConst.NUMBER.TWO, "RESET");

	private Integer id;
	private String name;

	private ForgotPasswordRequest(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public static ForgotPasswordRequest valueOf(Integer id) {
		return EnumHelper.INSTANCE.valueOf(id, ForgotPasswordRequest.values());
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
