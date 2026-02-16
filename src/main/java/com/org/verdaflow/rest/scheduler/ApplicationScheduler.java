package com.org.verdaflow.rest.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.org.verdaflow.rest.config.common.StringConst;
import com.org.verdaflow.rest.scheduler.service.SchedulerService;

@Component
public class ApplicationScheduler {
	public static final Logger log = LoggerFactory.getLogger(ApplicationScheduler.class);

	@Autowired
	private SchedulerService schedulerService;

	/**
	 * Scheduler to run every minute to update the order status from Processing to
	 * Placed and place it to Dispatcher.
	 */
	@Scheduled(cron = "0 0/1 * 1/1 * ?")
	public void updateOrderStatusToPlaced() {
		log.info("updateOrderStatusToPlaced");

		try {
			schedulerService.updateOrderStatusToPlaced();
		} catch (Exception e) {
			log.error(StringConst.EXCEPTION_HYPHEN, e);
		}
	}

}
