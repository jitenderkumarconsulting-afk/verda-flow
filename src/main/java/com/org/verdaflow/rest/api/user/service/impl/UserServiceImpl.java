package com.org.verdaflow.rest.api.user.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.org.verdaflow.rest.api.auth.AuthBuilder;
import com.org.verdaflow.rest.api.auth.model.UserModel;
import com.org.verdaflow.rest.api.user.UserBuilder;
import com.org.verdaflow.rest.api.user.form.ChangePasswordForm;
import com.org.verdaflow.rest.api.user.form.ReportProblemForm;
import com.org.verdaflow.rest.api.user.model.AuditRatingModel;
import com.org.verdaflow.rest.api.user.service.UserService;
import com.org.verdaflow.rest.common.model.GenericModel;
import com.org.verdaflow.rest.config.common.AppConst;
import com.org.verdaflow.rest.config.common.StringConst;
import com.org.verdaflow.rest.config.security.jwt.JwtUser;
import com.org.verdaflow.rest.dto.PaginatedResponse;
import com.org.verdaflow.rest.entity.AuditRating;
import com.org.verdaflow.rest.entity.Notification;
import com.org.verdaflow.rest.error.AppException;
import com.org.verdaflow.rest.notification.NotificationBuilder;
import com.org.verdaflow.rest.notification.model.NotificationListWithUnreadCountModel;
import com.org.verdaflow.rest.notification.model.NotificationModel;
import com.org.verdaflow.rest.repo.AuditRatingRepo;
import com.org.verdaflow.rest.repo.NotificationRepo;
import com.org.verdaflow.rest.util.AppUtil;

@Service
public class UserServiceImpl implements UserService {
	public static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserBuilder userBuilder;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthBuilder authBuilder;

	@Autowired
	private AppUtil appUtil;

	@Autowired
	private AuditRatingRepo auditRatingRepo;

	@Autowired
	private NotificationRepo notificationRepo;

	@Autowired
	private NotificationBuilder notificationBuilder;

	@Override
	@Transactional
	public boolean changePassword(ChangePasswordForm changePasswordForm, JwtUser jwtUser) {
		log.info("changePassword");

		if (changePasswordForm.getCurrentPass().equals(changePasswordForm.getNewPass()))
			throw new AppException(StringConst.OLD_AND_NEW_MISS_MATCH, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		if (!passwordEncoder.matches(changePasswordForm.getCurrentPass(), jwtUser.getUserEntity().getPassword()))
			throw new AppException(StringConst.INCORRECT_PASWORD, AppConst.EXCEPTION_CAT.BAD_REQUEST_400);

		userBuilder.changePassword(changePasswordForm, jwtUser.getUserEntity());

		return true;
	}

	@Override
	@Transactional
	public boolean logout(JwtUser jwtUser) {
		log.info("logout");

		userBuilder.logout(jwtUser.getUserEntity());

		return true;
	}

	@Override
	@Transactional
	public boolean reportProblem(ReportProblemForm reportProblemForm, JwtUser jwtUser) {
		log.info("reportProblem");

		userBuilder.reportProblemEmail(reportProblemForm, jwtUser);

		return true;
	}

	@Override
	@Transactional
	public UserModel enablePushNotification(boolean disable, JwtUser jwtUser) {
		log.info("enablePushNotification");

		userBuilder.enablePushNotification(disable, jwtUser.getUserEntity());

		return authBuilder.createUserModel(jwtUser.getUserEntity());
	}

	@Override
	@Transactional
	public PaginatedResponse<AuditRatingModel> listRatings(Pageable pageable, String query, JwtUser jwtUser) {
		log.info("listRatings");
		Page<AuditRating> auditRatings = null;

		if (null == query || StringUtils.isBlank(query)) {
			auditRatings = auditRatingRepo.findAllAuditRatingsByUserId(pageable, jwtUser.getUserEntity().getId());
		} else {
			String searchQuery = new StringBuilder(StringConst.PERCENTILE).append(appUtil.sanatizeQuery(query))
					.append(StringConst.PERCENTILE).toString();

			auditRatings = auditRatingRepo.findAllAuditRatingsByUserIdSearch(pageable, jwtUser.getUserEntity().getId(),
					searchQuery);
		}

		List<AuditRatingModel> auditRatingModels = new ArrayList<>();

		if (!auditRatings.getContent().isEmpty()) {
			log.info("listRatings :: auditRatings " + auditRatings);
			auditRatingModels = auditRatings.getContent().stream().filter(predicate -> !predicate.isDeleted())
					.map(auditRating -> userBuilder.createAuditRatingModel(auditRating)).collect(Collectors.toList());
		}
		log.info("listRatings :: auditRatingModels " + auditRatingModels);

		int nextPage;
		if (auditRatings.getContent().isEmpty()
				|| auditRatings.getTotalPages() == pageable.getPageNumber() + AppConst.NUMBER.ONE
				|| auditRatings.getTotalPages() == AppConst.NUMBER.ONE) {
			nextPage = -AppConst.NUMBER.ONE;
		} else {
			nextPage = (pageable.getPageNumber() + AppConst.NUMBER.ONE) + AppConst.NUMBER.ONE;
		}
		return new PaginatedResponse<>(auditRatingModels, (int) auditRatings.getTotalPages(),
				auditRatings.getTotalElements(), nextPage);
	}

	@Override
	@Transactional
	public PaginatedResponse<AuditRatingModel> listRatingsForUser(Pageable pageable, String query, int userId,
			JwtUser jwtUser) {
		log.info("listRatingsForUser");
		Page<AuditRating> auditRatings = null;

		if (null == query || StringUtils.isBlank(query)) {
			auditRatings = auditRatingRepo.findAllAuditRatingsByUserId(pageable, jwtUser.getUserEntity().getId());
		} else {
			String searchQuery = new StringBuilder(StringConst.PERCENTILE).append(appUtil.sanatizeQuery(query))
					.append(StringConst.PERCENTILE).toString();

			auditRatings = auditRatingRepo.findAllAuditRatingsByUserIdSearch(pageable, jwtUser.getUserEntity().getId(),
					searchQuery);
		}

		List<AuditRatingModel> auditRatingModels = new ArrayList<>();

		if (!auditRatings.getContent().isEmpty()) {
			log.info("listRatingsForUser :: auditRatings " + auditRatings);
			auditRatingModels = auditRatings.getContent().stream().filter(predicate -> !predicate.isDeleted())
					.map(auditRating -> userBuilder.createAuditRatingModel(auditRating)).collect(Collectors.toList());
		}
		log.info("listRatingsForUser :: auditRatingModels " + auditRatingModels);

		int nextPage;
		if (auditRatings.getContent().isEmpty()
				|| auditRatings.getTotalPages() == pageable.getPageNumber() + AppConst.NUMBER.ONE
				|| auditRatings.getTotalPages() == AppConst.NUMBER.ONE) {
			nextPage = -AppConst.NUMBER.ONE;
		} else {
			nextPage = (pageable.getPageNumber() + AppConst.NUMBER.ONE) + AppConst.NUMBER.ONE;
		}
		return new PaginatedResponse<>(auditRatingModels, (int) auditRatings.getTotalPages(),
				auditRatings.getTotalElements(), nextPage);
	}

	@Override
	@Transactional
	public NotificationListWithUnreadCountModel listNotifications(Pageable pageable, JwtUser jwtUser) {
		log.info("listNotifications");
		Page<Notification> notifications = notificationRepo.findAllNotificationsByUserId(pageable,
				jwtUser.getUserEntity().getId());
		List<NotificationModel> notificationModels = new ArrayList<>();

		if (!notifications.getContent().isEmpty()) {
			log.info("listNotifications :: notifications " + notifications);
			notificationModels = notifications.getContent().stream().filter(predicate -> !predicate.isDeleted())
					.map(notification -> notificationBuilder.createNotificationModel(notification))
					.collect(Collectors.toList());
		}
		log.info("listNotifications :: notificationModels " + notificationModels);

		int unreadCount = notificationRepo.findAllUnreadNotificationsCountByUserId(jwtUser.getUserEntity().getId());
		log.info("listNotifications :: unreadCount " + unreadCount);

		int nextPage;
		if (notifications.getContent().isEmpty()
				|| notifications.getTotalPages() == pageable.getPageNumber() + AppConst.NUMBER.ONE
				|| notifications.getTotalPages() == AppConst.NUMBER.ONE) {
			nextPage = -AppConst.NUMBER.ONE;
		} else {
			nextPage = (pageable.getPageNumber() + AppConst.NUMBER.ONE) + AppConst.NUMBER.ONE;
		}
		return new NotificationListWithUnreadCountModel(new PaginatedResponse<>(notificationModels,
				(int) notifications.getTotalPages(), notifications.getTotalElements(), nextPage),
				new GenericModel<>(unreadCount));
	}

	@Override
	@Transactional
	public int markNotificationsRead(JwtUser jwtUser) {
		log.info("markNotificationsRead");
		List<Notification> notifications = notificationRepo
				.findAllUnreadNotificationsByUserId(jwtUser.getUserEntity().getId());
		log.info("markNotificationsRead :: notifications " + notifications);

		userBuilder.markNotificationsRead(notifications);

		int unreadCount = notificationRepo.findAllUnreadNotificationsCountByUserId(jwtUser.getUserEntity().getId());
		log.info("markNotificationsRead :: unreadCount " + unreadCount);

		return unreadCount;
	}

	@Override
	@Transactional
	public int getUnreadNotificationsCount(JwtUser jwtUser) {
		log.info("getUnreadNotificationsCount");

		int unreadCount = notificationRepo.findAllUnreadNotificationsCountByUserId(jwtUser.getUserEntity().getId());
		log.info("getUnreadNotificationsCount :: unreadCount " + unreadCount);

		return unreadCount;
	}

	@Override
	@Transactional
	public int markNotificationRead(int notificationId, JwtUser jwtUser) {
		log.info("markNotificationRead");
		Notification notification = notificationRepo.findByNotificationIdAndUserId(notificationId,
				jwtUser.getUserEntity().getId());
		if (null == notification)
			throw new AppException(StringConst.NOTIFICATION_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);

		log.info("markNotificationRead :: notification " + notification);

		if (!notification.isRead())
			userBuilder.markNotificationRead(notification);

		int unreadCount = notificationRepo.findAllUnreadNotificationsCountByUserId(jwtUser.getUserEntity().getId());
		log.info("markNotificationRead :: unreadCount " + unreadCount);

		return unreadCount;
	}

}
