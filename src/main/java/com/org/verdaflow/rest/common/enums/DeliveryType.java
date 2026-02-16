package com.org.verdaflow.rest.common.enums;

import com.org.verdaflow.rest.config.common.AppConst;

public enum DeliveryType implements IdentifierType<Integer> {
	PICKUP(AppConst.NUMBER.ONE, "PICKUP"), DELIVERY(AppConst.NUMBER.TWO, "DELIVERY");

	private Integer id;
	private String name;

	private DeliveryType(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public static DeliveryType valueOf(Integer id) {
		return EnumHelper.INSTANCE.valueOf(id, DeliveryType.values());
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