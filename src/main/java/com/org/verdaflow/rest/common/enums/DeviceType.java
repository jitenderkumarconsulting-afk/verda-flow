package com.org.verdaflow.rest.common.enums;

import com.org.verdaflow.rest.config.common.AppConst;

public enum DeviceType implements IdentifierType<Integer> {
	ANDROID(AppConst.NUMBER.ONE, "ANDROID"), IOS(AppConst.NUMBER.TWO, "IOS"), WEB(AppConst.NUMBER.THREE, "WEB");

	private Integer id;
	private String name;

	DeviceType(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public static DeviceType valueOf(Integer id) {
		return EnumHelper.INSTANCE.valueOf(id, DeviceType.values());
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
