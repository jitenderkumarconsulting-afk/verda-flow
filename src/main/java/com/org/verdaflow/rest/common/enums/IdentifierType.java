package com.org.verdaflow.rest.common.enums;

/**
 * Contract that will allow Types with id to have generic implementation.
 */
public interface IdentifierType<T> {

	T getId();

}