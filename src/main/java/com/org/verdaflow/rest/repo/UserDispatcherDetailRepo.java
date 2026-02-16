package com.org.verdaflow.rest.repo;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.org.verdaflow.rest.common.enums.ApplicationStatus;
import com.org.verdaflow.rest.entity.UserDispatcherDetail;

@Repository
public interface UserDispatcherDetailRepo extends JpaRepository<UserDispatcherDetail, Integer> {

	/**
	 * This query to fetch user dispatcher details by dispatcher id.
	 * 
	 * @param dispatcherId
	 * @return
	 */
	@Query("SELECT d FROM UserDispatcherDetail d WHERE d.id = :dispatcherId AND d.isDeleted = false")
	UserDispatcherDetail findByDispatcherId(@Param("dispatcherId") int dispatcherId);

	/**
	 * This query to fetch the all active Dispatchers list on the basis of
	 * applicationStatus
	 *
	 * @param pageable
	 * @param applicationStatus
	 * @return
	 */
	@Query("SELECT d FROM UserDispatcherDetail d WHERE d.applicationStatus = :status AND d.active = true AND d.isDeleted = false ORDER BY d.createdAt")
	Page<UserDispatcherDetail> findAllActiveByApplicationStatusOrder(Pageable pageable,
			@Param("status") ApplicationStatus applicationStatus);

	/**
	 * This query to fetch the all active Dispatchers list on the basis of
	 * applicationStatus with search on storeName (With Search).
	 *
	 * @param pageable
	 * @param applicationStatus
	 * @param query
	 * @return
	 */
	@Query("SELECT d FROM UserDispatcherDetail d WHERE d.applicationStatus =:status AND d.storeName LIKE :query AND d.active = true AND d.isDeleted = false ORDER BY d.createdAt")
	Page<UserDispatcherDetail> findAllActiveByApplicationStatusSearch(Pageable pageable,
			@Param("status") ApplicationStatus applicationStatus, @Param("query") String query);

	/**
	 * This query to fetch the all active Dispatchers list on the basis of
	 * applicationStatus orderBy nearest location
	 *
	 * @param pageable
	 * @param applicationStatus
	 * @param lat
	 * @param lng
	 * @return
	 */
	@Query("SELECT new com.org.verdaflow.rest.entity.UserDispatcherDetail(d, 69.172*haversine(d.lat,d.lng,:lat,:lng)) FROM UserDispatcherDetail d WHERE d.applicationStatus = :status AND d.active = true AND d.isDeleted = false ORDER BY 69.172*haversine(d.lat,d.lng,:lat,:lng)")
	Page<UserDispatcherDetail> findAllActiveByApplicationStatusOrderByNearestLocation(Pageable pageable,
			@Param("status") ApplicationStatus applicationStatus, @Param("lat") BigDecimal lat,
			@Param("lng") BigDecimal lng);

	/**
	 * This query to fetch the all active Dispatchers list on the basis of
	 * applicationStatus with category filter orderBy nearest location
	 *
	 * @param pageable
	 * @param applicationStatus
	 * @param lat
	 * @param lng
	 * @param categoryIds
	 * @return
	 */
	@Query("SELECT new com.org.verdaflow.rest.entity.UserDispatcherDetail(d, 69.172*haversine(d.lat,d.lng,:lat,:lng)) FROM UserDispatcherDetail d, Product p WHERE d.id = p.userDispatcherDetail.id AND d.applicationStatus = :status AND d.active = true AND p.masterCategory.id IN (:categoryIds) AND d.isDeleted = false AND p.isDeleted = false GROUP BY d.id ORDER BY 69.172*haversine(d.lat,d.lng,:lat,:lng)")
	Page<UserDispatcherDetail> findAllActiveByApplicationStatusWithCategoryFilterOrderByNearestLocation(
			Pageable pageable, @Param("status") ApplicationStatus applicationStatus, @Param("lat") BigDecimal lat,
			@Param("lng") BigDecimal lng, @Param("categoryIds") List<Integer> categoryIds);

	/**
	 * This query to fetch the all active Dispatchers list on the basis of
	 * applicationStatus orderBy nearest location with search on storeName (With
	 * Search).
	 *
	 * @param pageable
	 * @param applicationStatus
	 * @param lat
	 * @param lng
	 * @param query
	 * @return
	 */
	@Query("SELECT new com.org.verdaflow.rest.entity.UserDispatcherDetail(d, 69.172*haversine(d.lat,d.lng,:lat,:lng)) FROM UserDispatcherDetail d WHERE d.applicationStatus =:status AND (d.storeName LIKE :query OR d.id IN (SELECT d1.id FROM UserDispatcherDetail d1, Product p WHERE p.userDispatcherDetail.id = d1.id AND p.name LIKE :query AND p.isDeleted = false AND d1.isDeleted = false)) AND d.active = true AND d.isDeleted = false ORDER BY 69.172*haversine(d.lat,d.lng,:lat,:lng)")
	Page<UserDispatcherDetail> findAllActiveByApplicationStatusOrderByNearestLocationSearch(Pageable pageable,
			@Param("status") ApplicationStatus applicationStatus, @Param("lat") BigDecimal lat,
			@Param("lng") BigDecimal lng, @Param("query") String query);

	/**
	 * This query to fetch the all active Dispatchers list on the basis of
	 * applicationStatus with category filter orderBy nearest location with search
	 * on storeName (With Search).
	 *
	 * @param pageable
	 * @param applicationStatus
	 * @param lat
	 * @param lng
	 * @param categoryIds
	 * @param query
	 * @return
	 */
	@Query("SELECT DISTINCT new com.org.verdaflow.rest.entity.UserDispatcherDetail(d, 69.172*haversine(d.lat,d.lng,:lat,:lng)) FROM UserDispatcherDetail d, Product p WHERE d.id = p.userDispatcherDetail.id AND d.applicationStatus =:status AND (d.storeName LIKE :query OR p.name LIKE :query) AND p.masterCategory.id IN (:categoryIds) AND d.active = true AND d.isDeleted = false AND p.isDeleted = false GROUP BY d.id ORDER BY 69.172*haversine(d.lat,d.lng,:lat,:lng)")
	Page<UserDispatcherDetail> findAllActiveByApplicationStatusWithCategoryFilterOrderByNearestLocationSearch(
			Pageable pageable, @Param("status") ApplicationStatus applicationStatus, @Param("lat") BigDecimal lat,
			@Param("lng") BigDecimal lng, @Param("categoryIds") List<Integer> categoryIds,
			@Param("query") String query);

	/**
	 * This query to fetch all the favorite Dispatchers list on the basis of user id
	 * of customer.
	 *
	 * @param pageable
	 * @param userId
	 * @return
	 */
	@Query("SELECT d FROM UserDispatcherDetail d, FavoriteDispatcherMapping m WHERE d.id = m.userDispatcher.id AND m.user.id = :userId AND m.isFav = true AND d.isDeleted = false ORDER BY d.storeName")
	Page<UserDispatcherDetail> findAllFavoriteDispatchersByUserIdOrderByName(Pageable pageable,
			@Param("userId") int userId);

	/**
	 * This query to fetch the all active Dispatchers list on the basis of
	 * applicationStatus orderBy nearest location
	 *
	 * @param pageable
	 * @param userId
	 * @param lat
	 * @param lng
	 * @return
	 */
	@Query("SELECT new com.org.verdaflow.rest.entity.UserDispatcherDetail(d, 69.172*haversine(d.lat,d.lng,:lat,:lng)) FROM UserDispatcherDetail d, FavoriteDispatcherMapping m WHERE d.id = m.userDispatcher.id AND m.user.id = :userId AND m.isFav = true AND d.isDeleted = false ORDER BY 69.172*haversine(d.lat,d.lng,:lat,:lng)")
	Page<UserDispatcherDetail> findAllFavoriteDispatchersByUserIdOrderByNearestLocation(Pageable pageable,
			@Param("userId") int userId, @Param("lat") BigDecimal lat, @Param("lng") BigDecimal lng);

}
