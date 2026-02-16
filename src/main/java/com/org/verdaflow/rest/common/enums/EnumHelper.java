package com.org.verdaflow.rest.common.enums;

public enum EnumHelper {
	INSTANCE;

	/**
	 * This will return {@link Enum} constant out of provided {@link Enum} values
	 * with the specified id.
	 * 
	 * @param id
	 *               the id of the constant to return.
	 * @param values
	 *               the {@link Enum} constants of specified type.
	 * @return the {@link Enum} constant.
	 */
	public <T extends IdentifierType<S>, S> T valueOf(S id, T[] values) {
		if (!values[0].getClass().isEnum()) {
			throw new IllegalArgumentException("Values provided to scan is not an Enum");
		}

		T type = null;

		for (int i = 0; i < values.length && type == null; i++) {
			if (values[i].getId().equals(id)) {
				type = values[i];
			}
		}

		return type;
	}

}