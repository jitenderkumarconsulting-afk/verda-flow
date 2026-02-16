package com.org.verdaflow.rest.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.org.verdaflow.rest.entity.UserCustomerDetail;
import com.org.verdaflow.rest.entity.UserDispatcherDetail;

@Repository
public interface UserCustomerDetailRepo extends JpaRepository<UserCustomerDetail, Integer> {

	/**
	 * This query to fetch user customer details by customer id.
	 * 
	 * @param customerId
	 * @return
	 */
	@Query("SELECT c FROM UserCustomerDetail c WHERE c.id = :customerId AND c.isDeleted = false")
	UserCustomerDetail findByCustomerId(@Param("customerId") int customerId);

}
