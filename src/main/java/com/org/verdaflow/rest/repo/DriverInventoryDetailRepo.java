package com.org.verdaflow.rest.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.org.verdaflow.rest.entity.DriverInventoryDetail;

@Repository
public interface DriverInventoryDetailRepo extends JpaRepository<DriverInventoryDetail, Integer> {

	/**
	 * This query is to find the Driver Inventory detail by driverInventoryId and
	 * productPriceDetailId.
	 * 
	 * @param driverInventoryId
	 * @param productPriceDetailId
	 * @return
	 */
	@Query(value = "SELECT d FROM DriverInventoryDetail d WHERE d.driverInventory.id = :driverInventoryId AND d.productPriceDetail.id = :productPriceDetailId AND d.isDeleted = false")
	DriverInventoryDetail findByDriverInventoryIdAndProductPriceDetailId(
			@Param("driverInventoryId") int driverInventoryId, @Param("productPriceDetailId") int productPriceDetailId);

}