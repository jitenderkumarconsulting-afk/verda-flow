package com.org.verdaflow.rest.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.org.verdaflow.rest.entity.UserRoleMapping;

@Repository
public interface UserRoleMappingRepo extends JpaRepository<UserRoleMapping, Integer> {

	/**
	 * This query is to fetch the mapping of user by its role.
	 * 
	 * @param userId
	 * @param roleId
	 * @return
	 */
	@Query("SELECT r FROM UserRoleMapping r WHERE r.user.id = :userId AND r.masterRole.id = :roleId AND	r.isDeleted = false")
	UserRoleMapping findMappingByUserId(@Param("userId") int userId, @Param("roleId") int roleId);

}
