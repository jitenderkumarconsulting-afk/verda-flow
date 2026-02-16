package com.org.verdaflow.rest.common.enums;

import com.org.verdaflow.rest.config.common.AppConst;

public enum PriceType implements IdentifierType<Integer> {
	EIGHTH_OZ(AppConst.NUMBER.ONE, "EIGHTH_OZ"), QUARTER_OZ(AppConst.NUMBER.TWO, "QUARTER_OZ"), HALF_OZ(
			AppConst.NUMBER.THREE, "HALF_OZ"),
	ONE_OZ(AppConst.NUMBER.FOUR, "ONE_OZ");

	private Integer id;
	private String name;

	private PriceType(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public static PriceType valueOf(Integer id) {
		return EnumHelper.INSTANCE.valueOf(id, PriceType.values());
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