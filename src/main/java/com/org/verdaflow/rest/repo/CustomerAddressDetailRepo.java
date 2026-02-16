package com.org.verdaflow.rest.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.org.verdaflow.rest.entity.CustomerAddressDetail;

@Repository
public interface CustomerAddressDetailRepo extends JpaRepository<CustomerAddressDetail, Integer> {

	/**
	 * This query is to find the Address details by addressId and customerId.
	 * 
	 * @param addressId
	 * @param customerId
	 * @return
	 */
	@Query(value = "SELECT c FROM CustomerAddressDetail c WHERE c.id = :addressId AND c.userCustomerDetail.id = :customerId AND c.isDeleted = false")
	CustomerAddressDetail findByAddressIdAndCustomerId(@Param("addressId") int addressId,
			@Param("customerId") int customerId);

	/**
	 * This query is to populate the list of Addresses by customerId.
	 * 
	 * @param pageable
	 * @param customerId
	 * @return
	 */
	@Query(value = "SELECT c FROM CustomerAddressDetail c WHERE c.userCustomerDetail.id = :customerId AND c.isDeleted = false ORDER BY c.isDefault DESC")
	Page<CustomerAddressDetail> findAllByCustomerId(Pageable pageable, @Param("customerId") int customerId);

	/**
	 * This query is to populate the list of Addresses by customerId (With Search).
	 * 
	 * @param pageable
	 * @param customerId
	 * @param query
	 * @return
	 */
	@Query(value = "SELECT c FROM CustomerAddressDetail c WHERE c.userCustomerDetail.id = :customerId AND (c.name LIKE :query OR c.phoneNumber LIKE :query OR c.address LIKE :query) AND c.isDeleted = false ORDER BY c.isDefault DESC")
	Page<CustomerAddressDetail> findAllByCustomerIdSearch(Pageable pageable, @Param("customerId") int customerId,
			@Param("query") String query);

	/**
	 * This query is to find default Address details by customerId.
	 * 
	 * @param customerId
	 * @return
	 */
	@Query(value = "SELECT c.* FROM customer_address_details c WHERE c.customer_id = :customerId AND c.is_default = true AND c.is_deleted = false LIMIT 1", nativeQuery = true)
	CustomerAddressDetail findDefaultByCustomerId(@Param("customerId") int customerId);

	/**
	 * This query is to populate the list of default addresses by customerId.
	 * 
	 * @param pageable
	 * @param customerId
	 * @return
	 */
	@Query(value = "SELECT c FROM CustomerAddressDetail c WHERE c.userCustomerDetail.id = :customerId AND c.isDefault = true AND c.isDeleted = false")
	List<CustomerAddressDetail> findAllDefaultAddressesByCustomerId(@Param("customerId") int customerId);

	/**
	 * This query is to fetch the Addresses' count by customerId.
	 * 
	 * @param customerId
	 * @return
	 */
	@Query(value = "SELECT COUNT(c) FROM CustomerAddressDetail c WHERE c.userCustomerDetail.id = :customerId AND c.isDeleted = false")
	int findAddressesCountByCustomerId(@Param("customerId") int customerId);

}
