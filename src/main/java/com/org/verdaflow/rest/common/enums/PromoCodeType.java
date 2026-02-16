package com.org.verdaflow.rest.common.enums;

import com.org.verdaflow.rest.config.common.AppConst;

public enum PromoCodeType implements IdentifierType<Integer> {
	MULTIPLE_USE(AppConst.NUMBER.ONE, "MULTIPLE_USE"), SINGLE_USE(AppConst.NUMBER.TWO,
			"SINGLE_USE"),
	SINGLE_USE_PER_USER(AppConst.NUMBER.THREE, "SINGLE_USE_PER_USER");

	private Integer id;
	private String name;

	PromoCodeType(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public static PromoCodeType valueOf(Integer id) {
		return EnumHelper.INSTANCE.valueOf(id, PromoCodeType.values());
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
