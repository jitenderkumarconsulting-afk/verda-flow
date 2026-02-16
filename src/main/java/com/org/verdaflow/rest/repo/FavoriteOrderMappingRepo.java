package com.org.verdaflow.rest.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.org.verdaflow.rest.entity.FavoriteOrderMapping;

@Repository
public interface FavoriteOrderMappingRepo extends JpaRepository<FavoriteOrderMapping, Integer> {

	/**
	 * This query to fetch the details on the basis of userId and orderId.
	 * 
	 * @param userId
	 * @param orderId
	 * @return
	 */
	@Query("SELECT f FROM FavoriteOrderMapping f WHERE f.user.id = :userId AND f.order.id = :orderId AND f.isDeleted = false")
	FavoriteOrderMapping findByUserIdAndOrderId(@Param("userId") int userId, @Param("orderId") int orderId);

}
