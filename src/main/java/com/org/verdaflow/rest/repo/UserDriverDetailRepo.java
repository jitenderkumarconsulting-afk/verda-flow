package com.org.verdaflow.rest.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.org.verdaflow.rest.entity.UserDriverDetail;

@Repository
public interface UserDriverDetailRepo extends JpaRepository<UserDriverDetail, Integer> {

	/**
	 * This query is to find the Driver by Id and dispatcherId
	 * 
	 * @param driverId
	 * @param dispatcherId
	 * @return
	 */
	@Query(value = "SELECT d FROM UserDriverDetail d WHERE d.id = :driverId AND d.userDispatcherDetail.id = :dispatcherId AND d.isDeleted = false")
	UserDriverDetail findDriverByIdAndDispatcherId(@Param("driverId") int driverId,
			@Param("dispatcherId") int dispatcherId);

	/**
	 * This query is to fetch all the active and approved drivers by dispatcher id.
	 * 
	 * @param pageable
	 * @param dispatcherId
	 * @return
	 */
	@Query("SELECT d FROM UserDriverDetail d WHERE d.userDispatcherDetail.id = :dispatcherId AND d.applicationStatus = 'APPROVED' AND d.active = true AND d.isDeleted = false ORDER BY d.createdAt DESC")
	Page<UserDriverDetail> findAllActiveDriversByDispatcherId(Pageable pageable,
			@Param("dispatcherId") int dispatcherId);

	/**
	 * This query is to fetch all the active and approved drivers by dispatcher id.
	 * 
	 * @param pageable
	 * @param dispatcherId
	 * @param query
	 * @return
	 */
	@Query("SELECT d FROM UserDriverDetail d, UserEntity u WHERE u.id = d.user.id AND d.userDispatcherDetail.id = :dispatcherId AND d.applicationStatus = 'APPROVED' AND (u.email LIKE :query OR CONCAT(u.countryCode,u.mobileNumber) LIKE :query OR d.name LIKE :query) AND d.active = true AND d.isDeleted = false ORDER BY d.createdAt DESC")
	Page<UserDriverDetail> findAllActiveDriversByDispatcherIdSearch(Pageable pageable,
			@Param("dispatcherId") int dispatcherId, @Param("query") String query);

	/**
	 * This query is to find the Driver Details by Id
	 * 
	 * @param driverId
	 * @return
	 */
	@Query(value = "SELECT d FROM UserDriverDetail d WHERE d.id = :driverId AND d.isDeleted = false")
	UserDriverDetail findDriverDetailsById(@Param("driverId") int driverId);

}
