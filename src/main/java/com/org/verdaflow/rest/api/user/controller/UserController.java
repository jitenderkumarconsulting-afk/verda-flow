package com.org.verdaflow.rest.api.user.controller;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.org.verdaflow.rest.api.auth.model.UserModel;
import com.org.verdaflow.rest.api.user.form.ChangePasswordForm;
import com.org.verdaflow.rest.api.user.form.ReportProblemForm;
import com.org.verdaflow.rest.api.user.model.AuditRatingModel;
import com.org.verdaflow.rest.api.user.service.UserService;
import com.org.verdaflow.rest.common.model.GenericModel;
import com.org.verdaflow.rest.config.common.AppConst;
import com.org.verdaflow.rest.config.common.StringConst;
import com.org.verdaflow.rest.config.security.jwt.JwtUser;
import com.org.verdaflow.rest.dto.PaginatedResponse;
import com.org.verdaflow.rest.dto.ResponseEnvelope;
import com.org.verdaflow.rest.notification.model.NotificationListWithUnreadCountModel;
import com.org.verdaflow.rest.util.AppUtil;

//xeemu@103.36.77.34/home/xeemu/Java_Backend/verdaflow-java

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
	public static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private Environment env;

	@Autowired
	private AppUtil appUtil;

	/**
	 * This method is used to change password.
	 * 
	 * @param authtoken
	 * @param changePasswordForm
	 * @param jwtUser
	 * @return
	 */
	@PatchMapping(value = "/changePassword")
	@ApiOperation(value = "This method is used to change password.")
	public ResponseEnvelope<GenericModel<Boolean>> changePassword(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestBody @Valid ChangePasswordForm changePasswordForm, JwtUser jwtUser) {
		log.info("changePassword");
		return new ResponseEnvelope<>(new GenericModel<>(userService.changePassword(changePasswordForm, jwtUser)), true,
				env.getProperty(StringConst.PASWRD_CHANGE_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to logout the user.
	 * 
	 * @param authtoken
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/logout")
	@ApiOperation(value = "This method is used to logout the user.")
	public ResponseEnvelope<GenericModel<Boolean>> logout(
			@RequestHeader(value = "authtoken", required = true) String authtoken, JwtUser jwtUser) {
		log.info("logout");
		return new ResponseEnvelope<>(new GenericModel<>(userService.logout(jwtUser)), true,
				env.getProperty(StringConst.LOGOUT_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to report a problem (email will be sent to Admin).
	 * 
	 * @param authtoken
	 * @param reportProblemForm
	 * @param jwtUser
	 * @return
	 */
	@PostMapping(value = "/reportProblem")
	@ApiOperation(value = "This method is used to report a problem (email will be sent to Admin).")
	public ResponseEnvelope<GenericModel<Boolean>> reportProblem(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestBody @Valid ReportProblemForm reportProblemForm, JwtUser jwtUser) {
		log.info("reportProblem");
		return new ResponseEnvelope<>(new GenericModel<>(userService.reportProblem(reportProblemForm, jwtUser)), true,
				env.getProperty(StringConst.REPORT_PROBLEM_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to enable or disable push notification.
	 * 
	 * @param authtoken
	 * @param dispatcherUserId
	 * @param disable
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/enablePushNotification")
	@ApiOperation(value = "This method is used to enable or disable push notification.", notes = " PARAM DESCRIPTION  :  "
			+ " disable(boolean) - true (Will disable the push notification) ")
	public ResponseEnvelope<UserModel> enablePushNotification(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@RequestParam(value = "disable", required = false) boolean disable, JwtUser jwtUser) {
		log.info("enablePushNotification");

		return new ResponseEnvelope<>(userService.enablePushNotification(disable, jwtUser), true,
				disable ? env.getProperty(StringConst.PUSH_NOTIFICATION_DISABLED_SUCCESS)
						: env.getProperty(StringConst.PUSH_NOTIFICATION_ENABLED_SUCCESS),
				AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to populate the list of ratings.
	 * 
	 * @param authtoken
	 * @param page
	 * @param size
	 * @param query
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/ratings")
	@ApiOperation(value = "This method is used to populate the list of ratings.")
	public ResponseEnvelope<PaginatedResponse<AuditRatingModel>> listRatings(
			@RequestHeader(value = "authtoken", required = true) String authtoken, @RequestParam("page") int page,
			@RequestParam("size") int size,
			@RequestParam(value = "query", required = false, defaultValue = StringConst.BLANK_STRING) String query,
			JwtUser jwtUser) {
		log.info("listRatings");
		if (StringUtils.isNotBlank(query))
			appUtil.validateScriptingTagAlert(query);

		Pageable pageable = new PageRequest(page - 1, size);
		return new ResponseEnvelope<>(userService.listRatings(pageable, appUtil.sanatizeQuery(query), jwtUser), true,
				StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to populate the list of ratings of a user for a
	 * particular user.
	 * 
	 * @param authtoken
	 * @param page
	 * @param size
	 * @param query
	 * @param userId
	 * @param jwtUser
	 * @return
	 */
	@GetMapping(value = "/ratings/{userId}")
	@ApiOperation(value = "This method is used to populate the list of ratings for a particular user.")
	public ResponseEnvelope<PaginatedResponse<AuditRatingModel>> listRatingsForUser(
			@RequestHeader(value = "authtoken", required = true) String authtoken, @RequestParam("page") int page,
			@RequestParam("size") int size,
			@RequestParam(value = "query", required = false, defaultValue = StringConst.BLANK_STRING) String query,
			@PathVariable(value = "userId") int userId, JwtUser jwtUser) {
		log.info("listRatingsForUser");
		if (StringUtils.isNotBlank(query))
			appUtil.validateScriptingTagAlert(query);

		Pageable pageable = new PageRequest(page - 1, size);
		return new ResponseEnvelope<>(
				userService.listRatingsForUser(pageable, appUtil.sanatizeQuery(query), userId, jwtUser), true,
				StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to populate the list of notifications.
	 * 
	 * @param authtoken
	 * @param page
	 * @param size
	 * @param jwtUser
	 * @return
	 */
	@GetMapping("/notifications")
	@ApiOperation(value = "This method is used to populate the list of notifications.")
	public ResponseEnvelope<NotificationListWithUnreadCountModel> listNotifications(
			@RequestHeader(value = "authtoken", required = true) String authtoken, @RequestParam("page") int page,
			@RequestParam("size") int size, JwtUser jwtUser) {
		log.info("listNotifications");

		Pageable pageable = new PageRequest(page - 1, size);
		return new ResponseEnvelope<>(userService.listNotifications(pageable, jwtUser), true, StringConst.EMPTY_STRING,
				AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to mark all the notifications as read.
	 * 
	 * @param authtoken
	 * @param jwtUser
	 * @return
	 */
	@ApiOperation(value = "This method is used to mark all the notifications as read.")
	@GetMapping("/markNotificationsRead")
	public ResponseEnvelope<GenericModel<Integer>> markNotificationsRead(
			@RequestHeader(value = "authtoken", required = true) String authtoken, JwtUser jwtUser) {
		log.info("markNotificationsRead");

		return new ResponseEnvelope<>(new GenericModel<>(userService.markNotificationsRead(jwtUser)), true,
				StringConst.ALL_NOTIFICATIONS_READ_SUCCESSFULLY, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to fetch the count of unread notifications.
	 * 
	 * @param authtoken
	 * @param jwtUser
	 * @return
	 */
	@ApiOperation(value = "This method is used to fetch the count of unread notifications.")
	@GetMapping("/unreadNotificationsCount")
	public ResponseEnvelope<GenericModel<Integer>> unreadNotificationsCount(
			@RequestHeader(value = "authtoken", required = true) String authtoken, JwtUser jwtUser) {
		log.info("unreadNotificationsCount");

		return new ResponseEnvelope<>(new GenericModel<>(userService.getUnreadNotificationsCount(jwtUser)), true,
				StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to mark the notification as read.
	 * 
	 * @param authtoken
	 * @param notificationId
	 * @return
	 */
	@GetMapping(value = "/markNotificationRead/{notificationId}")
	@ApiOperation(value = "This method is used to mark the notification as read.")
	public ResponseEnvelope<GenericModel<Integer>> markNotificationRead(
			@RequestHeader(value = "authtoken", required = true) String authtoken,
			@PathVariable(value = "notificationId", required = true) int notificationId, JwtUser jwtUser) {
		log.info("markNotificationRead");

		return new ResponseEnvelope<>(new GenericModel<>(userService.markNotificationRead(notificationId, jwtUser)),
				true, StringConst.NOTIFICATION_READ_SUCCESSFULLY, AppConst.HTTP_STATUS_OK);
	}

}
