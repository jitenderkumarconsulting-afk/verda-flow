package com.org.verdaflow.rest.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.org.verdaflow.rest.entity.DriverLocationDetail;

@Repository
public interface DriverLocationDetailRepo extends JpaRepository<DriverLocationDetail, Integer> {

	/**
	 * This query to fetch the DriverLocationDetail details on the basis of
	 * driverId.
	 *
	 * @param driverId
	 * @return
	 */
	@Query("SELECT d FROM DriverLocationDetail d WHERE d.userDriverDetail.id = :driverId AND d.isDeleted = false")
	DriverLocationDetail findByDriverId(@Param("driverId") int driverId);

}
