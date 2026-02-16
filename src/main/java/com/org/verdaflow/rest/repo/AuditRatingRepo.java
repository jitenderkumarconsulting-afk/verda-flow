package com.org.verdaflow.rest.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.org.verdaflow.rest.entity.AuditRating;

@Repository
public interface AuditRatingRepo extends JpaRepository<AuditRating, Integer> {

	/**
	 * This query is to populate the list of AuditRatings by userId
	 * 
	 * @param userId
	 * @param pageable
	 * @return
	 */
	@Query(value = "SELECT r FROM AuditRating r WHERE r.user.id = :userId AND r.isDeleted = false")
	Page<AuditRating> findAllAuditRatingsByUserId(Pageable pageable, @Param("userId") int userId);

	/**
	 * This query is to populate the list of AuditRatings by userId (With Search).
	 * 
	 * @param pageable
	 * @param userId
	 * @param query
	 * @return
	 */
	@Query(value = "SELECT r FROM AuditRating r WHERE r.user.id = :userId AND (r.note LIKE :query OR r.byUser.email LIKE :query OR CONCAT(r.byUser.countryCode,r.byUser.mobileNumber) LIKE :query) AND r.isDeleted = false")
	Page<AuditRating> findAllAuditRatingsByUserIdSearch(Pageable pageable, @Param("userId") int userId,
			@Param("query") String query);

}
