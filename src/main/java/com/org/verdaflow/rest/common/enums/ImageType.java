package com.org.verdaflow.rest.common.enums;

import com.org.verdaflow.rest.config.common.AppConst;

public enum ImageType implements IdentifierType<Integer> {
	ORIGINAL(AppConst.NUMBER.ONE, "ORIGINAL"), THUMB(AppConst.NUMBER.TWO, "THUMB");

	private Integer id;
	private String name;

	private ImageType(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public static ImageType valueOf(Integer id) {
		return EnumHelper.INSTANCE.valueOf(id, ImageType.values());
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