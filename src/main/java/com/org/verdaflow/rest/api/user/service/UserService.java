package com.org.verdaflow.rest.api.user.service;

import org.springframework.data.domain.Pageable;

import com.org.verdaflow.rest.api.auth.model.UserModel;
import com.org.verdaflow.rest.api.user.form.ChangePasswordForm;
import com.org.verdaflow.rest.api.user.form.ReportProblemForm;
import com.org.verdaflow.rest.api.user.model.AuditRatingModel;
import com.org.verdaflow.rest.config.security.jwt.JwtUser;
import com.org.verdaflow.rest.dto.PaginatedResponse;
import com.org.verdaflow.rest.notification.model.NotificationListWithUnreadCountModel;

public interface UserService {

	/**
	 * Change password.
	 * 
	 * @param changePasswordForm
	 * @param jwtUser
	 * @return
	 */
	boolean changePassword(ChangePasswordForm changePasswordForm, JwtUser jwtUser);

	/**
	 * Logout.
	 * 
	 * @param jwtUser
	 * @return
	 */
	boolean logout(JwtUser jwtUser);

	/**
	 * Report a problem.
	 * 
	 * @param reportProblemForm
	 * @param jwtUser
	 * @return
	 */
	boolean reportProblem(ReportProblemForm reportProblemForm, JwtUser jwtUser);

	/**
	 * Enable - Disable Push Notification
	 * 
	 * @param disable
	 * @param jwtUser
	 * @return
	 */
	UserModel enablePushNotification(boolean disable, JwtUser jwtUser);

	/**
	 * List Ratings.
	 *
	 * @param pageable
	 * @param query
	 * @param jwtUser
	 * @return
	 */
	PaginatedResponse<AuditRatingModel> listRatings(Pageable pageable, String query, JwtUser jwtUser);

	/**
	 * List Ratings for a particular user.
	 * 
	 * @param pageable
	 * @param query
	 * @param userId
	 * @param jwtUser
	 * @return
	 */
	PaginatedResponse<AuditRatingModel> listRatingsForUser(Pageable pageable, String query, int userId,
			JwtUser jwtUser);

	/**
	 * List Notifications.
	 *
	 * @param pageable
	 * @param jwtUser
	 * @return
	 */
	NotificationListWithUnreadCountModel listNotifications(Pageable pageable, JwtUser jwtUser);

	/**
	 * Mark All Notifications as Read.
	 *
	 * @param jwtUser
	 * @return
	 */
	int markNotificationsRead(JwtUser jwtUser);

	/**
	 * Unread Notifications count.
	 * 
	 * @param jwtUser
	 * @return
	 */
	int getUnreadNotificationsCount(JwtUser jwtUser);

	/**
	 * Mark Notification as Read.
	 * 
	 * @param notificationId
	 * @param jwtUser
	 * @return
	 */
	int markNotificationRead(int notificationId, JwtUser jwtUser);

}
