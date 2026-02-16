package com.org.verdaflow.rest.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.org.verdaflow.rest.entity.Notification;

@Repository
public interface NotificationRepo extends JpaRepository<Notification, Integer> {

	/**
	 * This query to fetch the Notification details on the basis of notificationId
	 * and userId.
	 *
	 * @param notificationId
	 * @param userId
	 * @return
	 */
	@Query("SELECT n FROM Notification n WHERE n.id = :notificationId AND n.user.id = :userId AND n.isDeleted = false")
	Notification findByNotificationIdAndUserId(@Param("notificationId") int notificationId,
			@Param("userId") int userId);

	/**
	 * This query to fetch all the notifications list on the basis of userId.
	 *
	 * @param pageable
	 * @param userId
	 * @return
	 */
	@Query("SELECT n FROM Notification n WHERE n.user.id = :userId AND n.isDeleted = false ORDER BY n.createdAt DESC")
	Page<Notification> findAllNotificationsByUserId(Pageable pageable, @Param("userId") int userId);

	/**
	 * This query to fetch the count of unread notifications list on the basis of
	 * userId.
	 * 
	 * @param userId
	 * @return
	 */
	@Query("SELECT COUNT(n) FROM Notification n WHERE n.user.id = :userId AND n.isRead = false AND n.isDeleted = false")
	int findAllUnreadNotificationsCountByUserId(@Param("userId") int userId);

	/**
	 * This query to fetch all the unread notifications list on the basis of userId.
	 * 
	 * @param userId
	 * @return
	 */
	@Query("SELECT n FROM Notification n WHERE n.user.id = :userId AND n.isRead = false AND n.isDeleted = false ORDER BY n.createdAt DESC")
	List<Notification> findAllUnreadNotificationsByUserId(@Param("userId") int userId);

}
