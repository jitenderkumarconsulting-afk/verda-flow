package com.org.verdaflow.rest.common.enums;

import com.org.verdaflow.rest.config.common.AppConst;

public enum ProductAggregateType implements IdentifierType<Integer> {
	SOLD(AppConst.NUMBER.ONE, "SOLD"), LIKED(AppConst.NUMBER.TWO, "LIKED"), VIEWED(AppConst.NUMBER.THREE, "VIEWED");

	private Integer id;
	private String name;

	private ProductAggregateType(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public static ProductAggregateType valueOf(Integer id) {
		return EnumHelper.INSTANCE.valueOf(id, ProductAggregateType.values());
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