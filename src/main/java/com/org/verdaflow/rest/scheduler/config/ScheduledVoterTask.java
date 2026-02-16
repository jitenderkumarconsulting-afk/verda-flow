package com.org.verdaflow.rest.scheduler.config;

import org.springframework.scheduling.config.ScheduledTaskRegistrar;

public class ScheduledVoterTask extends ScheduledTaskRegistrar implements SchedulerVoterTask {

    private final int fixedDelayInSeconds;

    public ScheduledVoterTask(final int fixedDelayInSeconds) {
        this.fixedDelayInSeconds = fixedDelayInSeconds;
    }

    @Override
    public void start(Runnable runnable) {
        addFixedDelayTask(runnable, fixedDelayInSeconds * 1000);
        scheduleTasks();
    }
}
