package com.org.verdaflow.rest.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.org.verdaflow.rest.common.enums.ApplicationStatus;
import com.org.verdaflow.rest.config.common.AppConst;
import com.org.verdaflow.rest.entity.UserEntity;

@Repository
public interface UserEntityRepo extends JpaRepository<UserEntity, Integer> {

	/**
	 * This query to fetch the User details on the basis of id.
	 *
	 * @param userId
	 * @return
	 */
	@Query("SELECT u FROM UserEntity u WHERE u.id = :userId AND u.isDeleted = false")
	UserEntity findByUserId(@Param("userId") int userId);

	/**
	 * This query is to check whether the entered email or user name exists in
	 * database or not for specific role.
	 * 
	 * @param userName
	 * @param roleId
	 * @return
	 */
	@Query("SELECT u FROM UserEntity u, UserRoleMapping r WHERE u.id = r.user.id AND (CONCAT(u.countryCode,u.mobileNumber) = :userName OR u.email = :userName) AND r.masterRole.id = :roleId AND u.isDeleted = false")
	UserEntity checkUserExistence(@Param("userName") String userName, @Param("roleId") int roleId);

	/**
	 * This query is to fetch the user details via userId & role
	 * 
	 * @param userId
	 * @param roleId
	 * @return
	 */
	@Query("SELECT u FROM UserEntity u, UserRoleMapping r WHERE u.id = r.user.id AND r.masterRole.id = :roleId AND u.id = :userId AND u.isDeleted = false")
	UserEntity findByUserIdAndRole(@Param("userId") int userId, @Param("roleId") int roleId);

	/**
	 * This query is to fetch the user details via email & role on the basis of
	 * active status
	 * 
	 * @param email
	 * @param roleId
	 * @return
	 */
	@Query("SELECT u FROM UserEntity u, UserRoleMapping r WHERE u.id = r.user.id AND r.masterRole.id = :roleId AND u.email = :email AND u.active = true AND u.isDeleted = false")
	UserEntity checkActiveUserExistenceByEmailAndRole(@Param("email") String email, @Param("roleId") int roleId);

	/**
	 * This query is to fetch the user details via email & role
	 * 
	 * @param email
	 * @param roleId
	 * @return
	 */
	@Query("SELECT u FROM UserEntity u, UserRoleMapping r WHERE u.id = r.user.id AND r.masterRole.id = :roleId AND u.email = :email AND u.isDeleted = false")
	UserEntity checkUserExistenceByEmailAndRole(@Param("email") String email, @Param("roleId") int roleId);

	/**
	 * This query is to check the user existence via email & role except current.
	 * 
	 * @param email
	 * @param roleId
	 * @param userId
	 * @return
	 */
	@Query("SELECT COUNT(u) FROM UserEntity u, UserRoleMapping r WHERE u.id = r.user.id AND r.masterRole.id = :roleId AND u.email = :email AND u.id != :userId AND u.isDeleted = false")
	int checkUserExistenceByEmailAndRoleExceptCurrent(@Param("email") String email, @Param("roleId") int roleId,
			@Param("userId") int userId);

	/**
	 * This query is to fetch the user details via mobile number & role.
	 * 
	 * @param countryCode
	 * @param mobileNumber
	 * @param roleId
	 * @return
	 */
	@Query("SELECT u FROM UserEntity u, UserRoleMapping r WHERE u.id = r.user.id AND r.masterRole.id = :roleId AND u.countryCode = :countryCode AND u.mobileNumber = :mobileNumber AND u.isDeleted = false")
	UserEntity checkUserExistenceByMobileNumberAndRole(@Param("countryCode") String countryCode,
			@Param("mobileNumber") String mobileNumber, @Param("roleId") int roleId);

	/**
	 * This query is to check the user existence via mobile number & role except
	 * current.
	 * 
	 * @param countryCode
	 * @param mobileNumber
	 * @param roleId
	 * @param userId
	 * @return
	 */
	@Query("SELECT COUNT(u) FROM UserEntity u, UserRoleMapping r WHERE u.id = r.user.id AND r.masterRole.id = :roleId AND u.countryCode = :countryCode AND u.mobileNumber = :mobileNumber AND u.id != :userId AND u.isDeleted = false")
	int checkUserExistenceByMobileNumberAndRoleExceptCurrent(@Param("countryCode") String countryCode,
			@Param("mobileNumber") String mobileNumber, @Param("roleId") int roleId, @Param("userId") int userId);

	/**
	 * This query is to fetch the user details of all dispatcher users.
	 * 
	 * @param pageable
	 * @return
	 */
	@Query("SELECT u FROM UserEntity u, UserRoleMapping r WHERE u.id = r.user.id AND r.masterRole.id = "
			+ AppConst.USER_ROLE.DISPATCHER + " AND u.isDeleted = false ORDER BY u.createdAt DESC")
	Page<UserEntity> findAllDispatcherUsers(Pageable pageable);

	/**
	 * This query is to fetch the user details of all dispatcher users (With
	 * Search).
	 * 
	 * @param pageable
	 * @param query
	 * @return
	 */
	@Query("SELECT u FROM UserEntity u, UserRoleMapping r WHERE u.id = r.user.id AND r.masterRole.id = "
			+ AppConst.USER_ROLE.DISPATCHER
			+ " AND (u.email LIKE :query OR CONCAT(u.countryCode,u.mobileNumber) LIKE :query OR u.userDispatcherDetail.storeName LIKE :query OR u.userDispatcherDetail.managerName LIKE :query) AND u.isDeleted = false ORDER BY u.createdAt DESC")
	Page<UserEntity> findAllDispatcherUsersSearch(Pageable pageable, @Param("query") String query);

	/**
	 * This query is to fetch the user details of all dispatcher users by
	 * application status.
	 * 
	 * @param pageable
	 * @param applicationStatus
	 * @return
	 */
	@Query("SELECT u FROM UserEntity u, UserRoleMapping r WHERE u.id = r.user.id AND r.masterRole.id = "
			+ AppConst.USER_ROLE.DISPATCHER
			+ " AND r.applicationStatus = :applicationStatus AND u.isDeleted = false ORDER BY u.createdAt DESC")
	Page<UserEntity> findAllDispatcherUsersByApplicationStatus(Pageable pageable,
			@Param("applicationStatus") ApplicationStatus applicationStatus);

	/**
	 * This query is to fetch the user details of all dispatcher users (With
	 * Search).
	 * 
	 * @param pageable
	 * @param applicationStatus
	 * @param query
	 * @return
	 */
	@Query("SELECT u FROM UserEntity u, UserRoleMapping r WHERE u.id = r.user.id AND r.masterRole.id = "
			+ AppConst.USER_ROLE.DISPATCHER
			+ " AND r.applicationStatus = :applicationStatus AND (u.email LIKE :query OR CONCAT(u.countryCode,u.mobileNumber) LIKE :query OR u.userDispatcherDetail.storeName LIKE :query OR  u.userDispatcherDetail.managerName LIKE :query) AND u.isDeleted = false ORDER BY u.createdAt DESC")
	Page<UserEntity> findAllDispatcherUsersByApplicationStatusSearch(Pageable pageable,
			@Param("applicationStatus") ApplicationStatus applicationStatus, @Param("query") String query);

	/**
	 * This query is to fetch the user details of all driver users by dispatcher
	 * user id.
	 * 
	 * @param pageable
	 * @param dispatcherId
	 * @return
	 */
	@Query("SELECT u FROM UserEntity u, UserRoleMapping r, UserDriverDetail d WHERE u.id = r.user.id AND r.masterRole.id = "
			+ AppConst.USER_ROLE.DRIVER
			+ " AND d.user.id = u.id AND d.userDispatcherDetail.id = :dispatcherId AND u.isDeleted = false ORDER BY u.createdAt DESC")
	Page<UserEntity> findAllDriverUsersByDispatcherId(Pageable pageable, @Param("dispatcherId") int dispatcherId);

	/**
	 * This query is to fetch the user details of all driver users by dispatcher
	 * user id (With Search).
	 * 
	 * @param pageable
	 * @param dispatcherId
	 * @param query
	 * @return
	 */
	@Query("SELECT u FROM UserEntity u, UserRoleMapping r, UserDriverDetail d WHERE u.id = r.user.id AND r.masterRole.id = "
			+ AppConst.USER_ROLE.DRIVER
			+ " AND d.user.id = u.id AND d.userDispatcherDetail.id = :dispatcherId AND (u.email LIKE :query OR CONCAT(u.countryCode,u.mobileNumber) LIKE :query OR d.name LIKE :query) AND u.isDeleted = false ORDER BY u.createdAt DESC")
	Page<UserEntity> findAllDriverUsersByDispatcherIdSearch(Pageable pageable, @Param("dispatcherId") int dispatcherId,
			@Param("query") String query);

	/**
	 * This query is to fetch the user details of all driver users by dispatcher
	 * user id and application status.
	 * 
	 * @param pageable
	 * @param dispatcherId
	 * @param applicationStatus
	 * @return
	 */
	@Query("SELECT u FROM UserEntity u, UserRoleMapping r, UserDriverDetail d WHERE u.id = r.user.id AND r.masterRole.id = "
			+ AppConst.USER_ROLE.DRIVER
			+ " AND d.user.id = u.id AND d.userDispatcherDetail.id = :dispatcherId AND r.applicationStatus = :applicationStatus AND u.isDeleted = false ORDER BY u.createdAt DESC")
	Page<UserEntity> findAllDriverUsersByDispatcherIdAndApplicationStatus(Pageable pageable,
			@Param("dispatcherId") int dispatcherId, @Param("applicationStatus") ApplicationStatus applicationStatus);

	/**
	 * This query is to fetch the user details of all dispatcher users (With
	 * Search).
	 * 
	 * @param pageable
	 * @param dispatcherId
	 * @param applicationStatus
	 * @param query
	 * @return
	 */
	@Query("SELECT u FROM UserEntity u, UserRoleMapping r, UserDriverDetail d WHERE u.id = r.user.id AND r.masterRole.id = "
			+ AppConst.USER_ROLE.DRIVER
			+ " AND d.user.id = u.id AND d.userDispatcherDetail.id = :dispatcherId AND r.applicationStatus = :applicationStatus AND (u.email LIKE :query OR CONCAT(u.countryCode,u.mobileNumber) LIKE :query OR d.name LIKE :query) AND u.isDeleted = false ORDER BY u.createdAt DESC")
	Page<UserEntity> findAllDriverUsersByDispatcherIdAndApplicationStatusSearch(Pageable pageable,
			@Param("dispatcherId") int dispatcherId, @Param("applicationStatus") ApplicationStatus applicationStatus,
			@Param("query") String query);

	/**
	 * This query is to fetch the user details of all customer users.
	 * 
	 * @param pageable
	 * @return
	 */
	@Query("SELECT u FROM UserEntity u, UserRoleMapping r WHERE u.id = r.user.id AND r.masterRole.id = "
			+ AppConst.USER_ROLE.CUSTOMER + " AND u.isDeleted = false ORDER BY u.createdAt DESC")
	Page<UserEntity> findAllCustomerUsers(Pageable pageable);

	/**
	 * This query is to fetch the user details of all customer users (With Search).
	 * 
	 * @param pageable
	 * @param query
	 * @return
	 */
	@Query("SELECT u FROM UserEntity u, UserRoleMapping r WHERE u.id = r.user.id AND r.masterRole.id = "
			+ AppConst.USER_ROLE.CUSTOMER
			+ " AND (u.email LIKE :query OR CONCAT(u.countryCode,u.mobileNumber) LIKE :query OR CONCAT(u.userCustomerDetail.firstName,' ',u.userCustomerDetail.lastName) LIKE :query) AND u.isDeleted = false ORDER BY u.createdAt DESC")
	Page<UserEntity> findAllCustomerUsersSearch(Pageable pageable, @Param("query") String query);

	/**
	 * This query is to fetch the user details of all customer users by application
	 * status.
	 * 
	 * @param pageable
	 * @param applicationStatus
	 * @return
	 */
	@Query("SELECT u FROM UserEntity u, UserRoleMapping r WHERE u.id = r.user.id AND r.masterRole.id = "
			+ AppConst.USER_ROLE.CUSTOMER
			+ " AND r.applicationStatus = :applicationStatus AND u.isDeleted = false ORDER BY u.createdAt DESC")
	Page<UserEntity> findAllCustomerUsersByApplicationStatus(Pageable pageable,
			@Param("applicationStatus") ApplicationStatus applicationStatus);

	/**
	 * This query is to fetch the user details of all customer users (With Search).
	 * 
	 * @param pageable
	 * @param applicationStatus
	 * @param query
	 * @return
	 */
	@Query("SELECT u FROM UserEntity u, UserRoleMapping r WHERE u.id = r.user.id AND r.masterRole.id = "
			+ AppConst.USER_ROLE.CUSTOMER
			+ " AND r.applicationStatus = :applicationStatus AND (u.email LIKE :query OR CONCAT(u.countryCode,u.mobileNumber) LIKE :query OR CONCAT(u.userCustomerDetail.firstName,' ',u.userCustomerDetail.lastName) LIKE :query) AND u.isDeleted = false ORDER BY u.createdAt DESC")
	Page<UserEntity> findAllCustomerUsersByApplicationStatusSearch(Pageable pageable,
			@Param("applicationStatus") ApplicationStatus applicationStatus, @Param("query") String query);

	/**
	 * This query is to fetch the driver user details via userId & dispatcherId
	 * 
	 * @param userId
	 * @param dispatcherId
	 * @return
	 */
	@Query("SELECT u FROM UserEntity u, UserRoleMapping r, UserDriverDetail d WHERE u.id = r.user.id AND r.masterRole.id = "
			+ AppConst.USER_ROLE.DRIVER
			+ " AND u.id = :userId AND d.userDispatcherDetail.id = :dispatcherId AND u.isDeleted = false")
	UserEntity findDriverByUserIdAndDispatcherId(@Param("userId") int userId, @Param("dispatcherId") int dispatcherId);

	/**
	 * This query is to fetch the dispatcher user details via userId
	 * 
	 * @param userId
	 * @return
	 */
	@Query("SELECT u FROM UserEntity u, UserRoleMapping r WHERE u.id = r.user.id AND u.id = :userId AND r.masterRole.id = "
			+ AppConst.USER_ROLE.DISPATCHER + " AND u.isDeleted = false")
	UserEntity findDispatcherByUserId(@Param("userId") int userId);

	/**
	 * This query is to fetch the customer user details via userId
	 * 
	 * @param userId
	 * @return
	 */
	@Query("SELECT u FROM UserEntity u, UserRoleMapping r WHERE u.id = r.user.id AND u.id = :userId AND r.masterRole.id = "
			+ AppConst.USER_ROLE.CUSTOMER + " AND u.isDeleted = false")
	UserEntity findCustomerByUserId(@Param("userId") int userId);

}
