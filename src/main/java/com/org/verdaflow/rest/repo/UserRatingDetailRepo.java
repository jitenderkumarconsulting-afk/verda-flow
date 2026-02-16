package com.org.verdaflow.rest.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.org.verdaflow.rest.entity.UserRatingDetail;

@Repository
public interface UserRatingDetailRepo extends JpaRepository<UserRatingDetail, Integer> {

	/**
	 * This query to fetch the user rating details by userId.
	 * 
	 * @param userId
	 * @return
	 */
	@Query(value = "SELECT d FROM UserRatingDetail d WHERE d.user.id = :userId AND d.isDeleted = false")
	UserRatingDetail findByUserId(@Param("userId") int userId);

}
