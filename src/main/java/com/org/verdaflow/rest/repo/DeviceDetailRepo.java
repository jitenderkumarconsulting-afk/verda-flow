package com.org.verdaflow.rest.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.org.verdaflow.rest.common.enums.DeviceType;
import com.org.verdaflow.rest.entity.DeviceDetail;

@Repository
public interface DeviceDetailRepo extends JpaRepository<DeviceDetail, Integer> {

	/**
	 * This query is to fetch the login details of a user.
	 * 
	 * @param userId
	 * @return
	 */
	@Query(value = "SELECT * FROM device_details AS d WHERE d.user_id = :userId AND d.is_deleted = false ORDER BY d.created_at DESC LIMIT 1", nativeQuery = true)
	DeviceDetail findByUser(@Param("userId") int userId);

	/**
	 * This query is to fetch the details of previous sessions of a user.
	 * 
	 * @param userId
	 * @param deviceType
	 * @param deviceToken
	 * @return
	 */
	@Query("SELECT d FROM DeviceDetail d WHERE (d.user.id = :userId OR (d.deviceType = :deviceType AND d.deviceToken = :deviceToken)) AND d.isDeleted = false")
	List<DeviceDetail> findByUserIdAndDeviceToken(@Param("userId") int userId,
			@Param("deviceType") DeviceType deviceType, @Param("deviceToken") String deviceToken);

	/**
	 * This query is to fetch the active sessions of a user.
	 * 
	 * @param userId
	 * @return
	 */
	@Query("SELECT d FROM DeviceDetail d where d.user.id = :userId and d.isDeleted = false")
	List<DeviceDetail> findByUserIdActive(@Param("userId") int userId);

}
