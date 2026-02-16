package com.org.verdaflow.rest.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.org.verdaflow.rest.entity.DriverOrderMapping;

@Repository
public interface DriverOrderMappingRepo extends JpaRepository<DriverOrderMapping, Integer> {

	/**
	 * This query to fetch the DriverOrderMapping details on the basis of orderId.
	 *
	 * @param orderId
	 * @return
	 */
	@Query("SELECT d FROM DriverOrderMapping d WHERE d.order.id = :orderId AND d.isDeleted = false")
	DriverOrderMapping findByOrderId(@Param("orderId") int orderId);

	/**
	 * This query to fetch the DriverOrderMapping details on the basis of orderId
	 * and driverId.
	 *
	 * @param orderId
	 * @param driverId
	 * @return
	 */
	@Query("SELECT d FROM DriverOrderMapping d WHERE d.order.id = :orderId AND d.userDriverDetail.id = :driverId AND d.isDeleted = false")
	DriverOrderMapping findByOrderIdAndDriverId(@Param("orderId") int orderId, @Param("driverId") int driverId);

}
