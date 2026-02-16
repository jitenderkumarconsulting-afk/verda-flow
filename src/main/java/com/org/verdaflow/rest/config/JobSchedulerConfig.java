package com.org.verdaflow.rest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.org.verdaflow.rest.repo.RegisteredSchedulerRepo;
import com.org.verdaflow.rest.scheduler.config.AnnotationBasedScheduler;
import com.org.verdaflow.rest.scheduler.config.ScheduledVoterTask;
import com.org.verdaflow.rest.scheduler.config.Scheduler;
import com.org.verdaflow.rest.scheduler.config.SchedulerVoter;
import com.org.verdaflow.rest.scheduler.config.SchedulerVoterTask;
import com.org.verdaflow.rest.scheduler.service.RegisteredSchedulerService;
import com.org.verdaflow.rest.scheduler.service.impl.RegisteredSchedulerServiceImpl;

@Configuration
public class JobSchedulerConfig {

	@Autowired
	private Environment env;

	@Bean
	SchedulerVoterTask schedulerVoterTask() {
		return new ScheduledVoterTask(60);
	}

	@Bean
	Scheduler scheduler() {
		return new AnnotationBasedScheduler(env.getProperty("scheduler.name"));
	}

	@Bean
	RegisteredSchedulerService registeredSchedulerService(final RegisteredSchedulerRepo registeredSchedulerRepository) {
		return new RegisteredSchedulerServiceImpl(registeredSchedulerRepository, 90);
	}

	@Bean
	SchedulerVoter schedulerVoter(final SchedulerVoterTask schedulerVoterTask, final Scheduler scheduler,
			final RegisteredSchedulerService registeredSchedulerService) {
		return new SchedulerVoter(schedulerVoterTask, scheduler, registeredSchedulerService);
	}
}
