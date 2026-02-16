package com.org.verdaflow.rest.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.org.verdaflow.rest.entity.ForgotPassword;

public interface ForgotPasswordRepo extends JpaRepository<ForgotPassword, Integer> {

	/**
	 * Getting user by its user id.
	 *
	 * @param id
	 * @return
	 */
	@Query("SELECT f FROM ForgotPassword f WHERE f.user.id = :userId AND f.isDeleted = false")
	ForgotPassword findByUserId(@Param("userId") int userId);

	/**
	 * Getting user by its user id and verification key.
	 *
	 * @param userId
	 * @param verificationKey
	 * @return
	 */
	@Query("SELECT f FROM ForgotPassword f WHERE f.user.id = :userId AND f.verificationKey = :verificationKey AND f.isDeleted = false")
	ForgotPassword findByUserIdAndCode(@Param("userId") int userId, @Param("verificationKey") String verificationKey);
}
