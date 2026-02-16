package com.org.verdaflow.rest.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.org.verdaflow.rest.entity.PromoCode;

@Repository
public interface PromoCodeRepo extends JpaRepository<PromoCode, Integer> {

	/**
	 * This query to fetch promo code details by promo code id.
	 * 
	 * @param promoCodeId
	 * @return
	 */
	@Query("SELECT c FROM PromoCode c WHERE c.id = :promoCodeId AND c.isDeleted = false")
	PromoCode findByPromoCodeId(@Param("promoCodeId") int promoCodeId);

	/**
	 * This query to fetch promo code details by promo code name and dispatcherId.
	 * 
	 * @param name
	 * @param dispatcherId
	 * @return
	 */
	@Query(value = "SELECT c FROM PromoCode c WHERE c.name = :name AND c.userDispatcherDetail.id = :dispatcherId AND c.isDeleted = false")
	PromoCode findPromoCodeByNameAndDispatcherId(@Param("name") String name, @Param("dispatcherId") int dispatcherId);

	/**
	 * This query is to check the existence of PromoCode by promo code name and
	 * dispatcherId.
	 * 
	 * @param name
	 * @param dispatcherId
	 * @return
	 */
	@Query(value = "SELECT COUNT(c) FROM PromoCode c WHERE c.name = :name AND c.userDispatcherDetail.id = :dispatcherId AND c.isDeleted = false")
	int checkPromoCodeExistenceByNameAndDispatcherId(@Param("name") String name,
			@Param("dispatcherId") int dispatcherId);

	/**
	 * This query is to check the existence of PromoCode by promo code name and
	 * dispatcherId except current.
	 * 
	 * @param name
	 * @param dispatcherId
	 * @param promoCodeId
	 * @return
	 */
	@Query(value = "SELECT COUNT(c) FROM PromoCode c WHERE c.name = :name AND c.userDispatcherDetail.id = :dispatcherId AND c.id != :promoCodeId AND c.isDeleted = false")
	int checkPromoCodeExistenceByNameAndDispatcherIdExceptCurrent(@Param("name") String name,
			@Param("dispatcherId") int dispatcherId, @Param("promoCodeId") int promoCodeId);

	/**
	 * This query is to find the PromoCode by promoCodeId and dispatcherId
	 * 
	 * @param promoCodeId
	 * @param dispatcherId
	 * @return
	 */
	@Query(value = "SELECT c FROM PromoCode c WHERE c.id = :promoCodeId AND c.userDispatcherDetail.id = :dispatcherId AND c.isDeleted = false")
	PromoCode findPromoCodeByIdAndDispatcherId(@Param("promoCodeId") int promoCodeId,
			@Param("dispatcherId") int dispatcherId);

	/**
	 * This query is to populate the list of PromoCodes by dispatcherId
	 * 
	 * @param dispatcherId
	 * @param pageable
	 * @return
	 */
	@Query(value = "SELECT c FROM PromoCode c WHERE c.userDispatcherDetail.id = :dispatcherId AND c.isDeleted = false")
	Page<PromoCode> findAllPromoCodesByDispatcherId(Pageable pageable, @Param("dispatcherId") int dispatcherId);

	/**
	 * This query is to populate the list of PromoCodes by dispatcherId (With
	 * Search).
	 * 
	 * @param pageable
	 * @param dispatcherId
	 * @param query
	 * @return
	 */
	@Query(value = "SELECT c FROM PromoCode c WHERE c.userDispatcherDetail.id = :dispatcherId AND c.name LIKE :query AND c.isDeleted = false")
	Page<PromoCode> findAllPromoCodesByDispatcherIdSearch(Pageable pageable, @Param("dispatcherId") int dispatcherId,
			@Param("query") String query);

	/**
	 * This query is to fetch all the active PromoCodes by dispatcherId.
	 * 
	 * @param dispatcherId
	 * @return
	 */
	@Query("SELECT c FROM PromoCode c WHERE c.userDispatcherDetail.id= :dispatcherId AND c.active = true AND c.isDeleted = false ORDER BY c.name")
	List<PromoCode> findAllActivePromoCodesByDispatcherId(@Param("dispatcherId") int dispatcherId);

	/**
	 * This query is to fetch all the active PromoCodes by dispatcherId (With
	 * Search).
	 * 
	 * @param dispatcherId
	 * @param query
	 * @return
	 */
	@Query("SELECT c FROM PromoCode c WHERE c.userDispatcherDetail.id= :dispatcherId AND c.name LIKE :query AND c.active = true AND c.isDeleted = false ORDER BY c.name")
	List<PromoCode> findAllActivePromoCodesByDispatcherIdSearch(@Param("dispatcherId") int dispatcherId,
			@Param("query") String query);

}
