package com.org.verdaflow.rest.scheduler.config;

public interface Scheduler {

	void enable();

	void disable();

	String getName();

	boolean isDisabled();

}
