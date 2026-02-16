package com.org.verdaflow.rest.common.enums;

import com.org.verdaflow.rest.config.common.AppConst;

public enum UploadType implements IdentifierType<Integer> {
	DISPATCHER(AppConst.NUMBER.ONE, "DISPATCHER"), DRIVER(AppConst.NUMBER.TWO,
			"DRIVER"),
	CUSTOMER(AppConst.NUMBER.THREE, "CUSTOMER"), PRODUCT(AppConst.NUMBER.FOUR, "PRODUCT");

	private Integer id;
	private String name;

	private UploadType(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public static UploadType valueOf(Integer id) {
		return EnumHelper.INSTANCE.valueOf(id, UploadType.values());
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