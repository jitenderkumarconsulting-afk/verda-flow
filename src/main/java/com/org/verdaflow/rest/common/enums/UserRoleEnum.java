package com.org.verdaflow.rest.common.enums;

import com.org.verdaflow.rest.config.common.AppConst;

public enum UserRoleEnum implements IdentifierType<Integer> {
	ADMIN(AppConst.NUMBER.ONE, "ADMIN"), DISPATCHER(AppConst.NUMBER.TWO, "DISPATCHER"), DRIVER(AppConst.NUMBER.THREE,
			"DRIVER"),
	CUSTOMER(AppConst.NUMBER.FOUR, "CUSTOMER");

	private Integer id;
	private String name;

	UserRoleEnum(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public static UserRoleEnum valueOf(Integer id) {
		return EnumHelper.INSTANCE.valueOf(id, UserRoleEnum.values());
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
