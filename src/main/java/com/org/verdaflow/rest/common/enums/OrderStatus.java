package com.org.verdaflow.rest.common.enums;

import com.org.verdaflow.rest.config.common.AppConst;

public enum OrderStatus implements IdentifierType<Integer> {
	PROCESSING(AppConst.NUMBER.ONE, "PROCESSING"), PLACED(AppConst.NUMBER.TWO, "PLACED"), CONFIRMED(
			AppConst.NUMBER.THREE,
			"CONFIRMED"),
	PREPARED(AppConst.NUMBER.FOUR, "PREPARED"), DRIVER_ASSIGNED(AppConst.NUMBER.FIVE,
			"DRIVER_ASSIGNED"),
	REASSIGN_DRIVER(AppConst.NUMBER.SIX, "REASSIGN_DRIVER"), OUT_FOR_DELIVERY(
			AppConst.NUMBER.SEVEN, "OUT_FOR_DELIVERY"),
	DELIVERY_FAILED(AppConst.NUMBER.EIGHT,
			"DELIVERY_FAILED"),
	COMPLETED(AppConst.NUMBER.NINE, "COMPLETED"), CANCELLED_BY_USER(
			AppConst.NUMBER.TEN, "CANCELLED_BY_USER"),
	CANCELLED_BY_STORE(
			AppConst.NUMBER.ELE, "CANCELLED_BY_STORE");

	private Integer id;
	private String name;

	private OrderStatus(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public static OrderStatus valueOf(Integer id) {
		return EnumHelper.INSTANCE.valueOf(id, OrderStatus.values());
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
