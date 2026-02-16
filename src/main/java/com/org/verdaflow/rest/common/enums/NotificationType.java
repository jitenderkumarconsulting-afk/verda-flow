package com.org.verdaflow.rest.common.enums;

import com.org.verdaflow.rest.config.common.AppConst;

public enum NotificationType implements IdentifierType<Integer> {
	CUSTOM(AppConst.NUMBER.ONE, "CUSTOM"),
	TERMS_UPDATED(AppConst.NUMBER.TWO, "TERMS_UPDATED"),
	PRIVACY_UPDATED(AppConst.NUMBER.THREE, "PRIVACY_UPDATED"),
	ORDER_PLACED(AppConst.NUMBER.FOUR, "ORDER_PLACED"),
	ORDER_CONFIRMED(AppConst.NUMBER.FIVE, "ORDER_CONFIRMED"),
	ORDER_PREPARED(AppConst.NUMBER.SIX, "ORDER_PREPARED"),
	ORDER_DRIVER_ASSIGNED(AppConst.NUMBER.SEVEN, "ORDER_DRIVER_ASSIGNED"),
	ORDER_REASSIGN_DRIVER(AppConst.NUMBER.EIGHT, "ORDER_REASSIGN_DRIVER"),
	ORDER_OUT_FOR_DELIVERY(AppConst.NUMBER.NINE, "ORDER_OUT_FOR_DELIVERY"),
	ORDER_DELIVERY_FAILED(AppConst.NUMBER.TEN, "ORDER_DELIVERY_FAILED"),
	ORDER_COMPLETED(AppConst.NUMBER.ELE, "ORDER_COMPLETED"),
	ORDER_CANCELLED_BY_USER(AppConst.NUMBER.TWE, "ORDER_CANCELLED_BY_USER"),
	ORDER_CANCELLED_BY_STORE(AppConst.NUMBER.THIR, "ORDER_CANCELLED_BY_STORE");

	private Integer id;
	private String name;

	private NotificationType(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public static NotificationType valueOf(Integer id) {
		return EnumHelper.INSTANCE.valueOf(id, NotificationType.values());
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