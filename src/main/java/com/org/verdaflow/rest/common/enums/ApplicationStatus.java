package com.org.verdaflow.rest.common.enums;

import com.org.verdaflow.rest.config.common.AppConst;

public enum ApplicationStatus implements IdentifierType<Integer> {
	PENDING(AppConst.NUMBER.ONE, "PENDING"), APPROVED(AppConst.NUMBER.TWO, "APPROVED"), REJECTED(AppConst.NUMBER.THREE,
			"REJECTED");

	private Integer id;
	private String name;

	private ApplicationStatus(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public static ApplicationStatus valueOf(Integer id) {
		return EnumHelper.INSTANCE.valueOf(id, ApplicationStatus.values());
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
