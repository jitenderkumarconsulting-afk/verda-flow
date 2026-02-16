package com.org.verdaflow.rest.scheduler.service;

public interface RegisteredSchedulerService {

	boolean ifNoMasterSchedulerRegisterThisScheduler(String schedulerName);

	boolean ifThisSchedulerIsRegisteredAsMasterIncreaseHeartbeat(String schedulerName);
}
