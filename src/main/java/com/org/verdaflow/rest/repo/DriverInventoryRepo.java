package com.org.verdaflow.rest.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.org.verdaflow.rest.entity.DriverInventory;

@Repository
public interface DriverInventoryRepo extends JpaRepository<DriverInventory, Integer> {

	/**
	 * This query is to find the Driver Inventory by driverId and productId.
	 * 
	 * @param driverId
	 * @param productId
	 * @return
	 */
	@Query(value = "SELECT di FROM DriverInventory di WHERE di.userDriverDetail.id = :driverId AND di.product.id = :productId AND di.isDeleted = false")
	DriverInventory findByDriverIdAndProductId(@Param("driverId") int driverId, @Param("productId") int productId);

}