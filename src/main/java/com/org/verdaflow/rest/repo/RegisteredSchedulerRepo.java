package com.org.verdaflow.rest.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.org.verdaflow.rest.entity.RegisteredScheduler;

public interface RegisteredSchedulerRepo extends JpaRepository<RegisteredScheduler, Integer> {

	@Query("SELECT s FROM RegisteredScheduler s WHERE schedulerType = '" + RegisteredScheduler.MASTER_SCHEDULER_TYPE
			+ "'")
	RegisteredScheduler findMasterScheduler();
}
