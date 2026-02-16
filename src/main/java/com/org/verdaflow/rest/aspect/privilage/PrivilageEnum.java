package com.org.verdaflow.rest.aspect.privilage;

public enum PrivilageEnum {
	USER("USER"), ADMIN("ADMIN");

	final String role;

	PrivilageEnum(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return this.role;
	}
}
