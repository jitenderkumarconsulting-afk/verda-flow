package com.org.verdaflow.rest.api.user;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.org.verdaflow.rest.api.admin.model.UserDetailModel;
import com.org.verdaflow.rest.api.auth.AuthBuilder;
import com.org.verdaflow.rest.api.auth.model.UserCustomerDetailModel;
import com.org.verdaflow.rest.api.auth.model.UserDispatcherDetailModel;
import com.org.verdaflow.rest.api.auth.model.UserDriverDetailModel;
import com.org.verdaflow.rest.api.auth.model.UserModel;
import com.org.verdaflow.rest.api.user.form.ChangePasswordForm;
import com.org.verdaflow.rest.api.user.form.RateUserForm;
import com.org.verdaflow.rest.api.user.form.ReportProblemForm;
import com.org.verdaflow.rest.api.user.model.AuditOrderStatusModel;
import com.org.verdaflow.rest.api.user.model.AuditRatingModel;
import com.org.verdaflow.rest.api.user.model.OrderRatingDetailModel;
import com.org.verdaflow.rest.api.user.model.UserRatingDetailModel;
import com.org.verdaflow.rest.common.enums.UserRoleEnum;
import com.org.verdaflow.rest.config.common.AppConst;
import com.org.verdaflow.rest.config.common.StringConst;
import com.org.verdaflow.rest.config.security.jwt.JwtUser;
import com.org.verdaflow.rest.entity.AuditOrderStatus;
import com.org.verdaflow.rest.entity.AuditRating;
import com.org.verdaflow.rest.entity.DeviceDetail;
import com.org.verdaflow.rest.entity.Notification;
import com.org.verdaflow.rest.entity.Order;
import com.org.verdaflow.rest.entity.OrderRatingDetail;
import com.org.verdaflow.rest.entity.UserEntity;
import com.org.verdaflow.rest.entity.UserRatingDetail;
import com.org.verdaflow.rest.event.ReportProblemEvent;
import com.org.verdaflow.rest.repo.AuditRatingRepo;
import com.org.verdaflow.rest.repo.DeviceDetailRepo;
import com.org.verdaflow.rest.repo.NotificationRepo;
import com.org.verdaflow.rest.repo.UserDriverDetailRepo;
import com.org.verdaflow.rest.repo.UserEntityRepo;
import com.org.verdaflow.rest.repo.UserRatingDetailRepo;

@Component
public class UserBuilder {
	public static final Logger log = LoggerFactory.getLogger(UserBuilder.class);

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserEntityRepo userEntityRepo;

	@Autowired
	private DeviceDetailRepo deviceDetailRepo;

	@Autowired
	private ApplicationEventMulticaster applicationEventMulticaster;

	@Autowired
	private UserDriverDetailRepo userDriverDetailRepo;

	@Autowired
	private AuditRatingRepo auditRatingRepo;

	@Autowired
	private UserRatingDetailRepo userRatingDetailRepo;

	@Autowired
	private AuthBuilder authBuilder;

	@Autowired
	private NotificationRepo notificationRepo;

	@Transactional
	public boolean changePassword(ChangePasswordForm changePasswordForm, UserEntity userEntity) {
		log.info("changePassword");
		userEntity.setPassword(passwordEncoder.encode(changePasswordForm.getNewPass()));
		userEntityRepo.save(userEntity);

		log.info("changePassword :: userEntity " + userEntity);

		return true;
	}

	@Transactional
	public boolean logout(UserEntity userEntity) {
		log.info("logout");
		List<DeviceDetail> deviceDetails = deviceDetailRepo.findByUserIdActive(userEntity.getId());

		log.info("logout :: deviceDetails " + deviceDetails);

		List<DeviceDetail> signDetailToDeviceDetailEntities = new ArrayList<>();

		if (!deviceDetails.isEmpty()) {
			deviceDetails.forEach(sessionEntity -> {
				sessionEntity.setDeleted(true);
				signDetailToDeviceDetailEntities.add(sessionEntity);
			});

			deviceDetailRepo.save(signDetailToDeviceDetailEntities);
		}

		if (UserRoleEnum.DRIVER == userEntity.getUserRole()) {
			userEntity.getUserDriverDetail().setOnline(false);
			userDriverDetailRepo.save(userEntity.getUserDriverDetail());
		}

		return true;
	}

	@Transactional
	public boolean reportProblemEmail(ReportProblemForm reportProblemForm, JwtUser jwtUser) {
		log.info("reportProblemEmail");
		String name = "";
		String role = "";

		switch (jwtUser.getUserEntity().getUserRole()) {
			case DISPATCHER:
				name = jwtUser.getUserEntity().getUserDispatcherDetail().getStoreName();
				role = UserRoleEnum.DISPATCHER.name();
				break;

			case DRIVER:
				name = jwtUser.getUserEntity().getUserDriverDetail().getName();
				role = UserRoleEnum.DRIVER.name();
				break;

			case CUSTOMER:
				name = new StringBuilder(jwtUser.getUserEntity().getUserCustomerDetail().getFirstName())
						.append(StringConst.SPACE).append(jwtUser.getUserEntity().getUserCustomerDetail().getLastName())
						.toString();
				role = UserRoleEnum.CUSTOMER.name();
				break;

			default:
				break;
		}

		ReportProblemEvent reportProblemEvent = new ReportProblemEvent(this, StringConst.ADMIN,
				jwtUser.getUserEntity().getEmail(), jwtUser.getUserEntity().getMobileNumber(), role, name,
				reportProblemForm.getTitle(), reportProblemForm.getMessage());

		applicationEventMulticaster.multicastEvent(reportProblemEvent);

		return true;
	}

	@Transactional
	public boolean enablePushNotification(boolean disable, UserEntity userEntity) {
		log.info("enablePushNotification");

		if (disable)
			userEntity.setPushNotificationStatus(false);
		else
			userEntity.setPushNotificationStatus(true);

		userEntityRepo.save(userEntity);
		log.info("enablePushNotification :: userEntity " + userEntity);

		return true;
	}

	@Transactional
	public UserRatingDetailModel createUserRatingDetailModel(UserRatingDetail userRatingDetail) {
		log.info("createUserRatingDetailModel");

		return new UserRatingDetailModel(userRatingDetail.getId(), userRatingDetail.getUser().getId(),
				userRatingDetail.getAvg(), userRatingDetail.getCount());
	}

	@Transactional
	public boolean rateUser(RateUserForm rateUserForm, Order order, UserEntity toUserEntity, UserEntity userEntity) {
		log.info("rateUser");

		saveAuditRating(rateUserForm, order.getOrderRatingDetail(), toUserEntity, userEntity);
		saveOrUpdateUserRatingDetail(rateUserForm, toUserEntity);

		return true;
	}

	@Transactional
	private boolean saveAuditRating(RateUserForm rateUserForm, OrderRatingDetail orderRatingDetail,
			UserEntity toUserEntity, UserEntity userEntity) {
		log.info("saveAuditRating");
		AuditRating auditRating = new AuditRating();
		auditRating.setOrderRatingDetail(orderRatingDetail);
		auditRating.setUser(toUserEntity);
		auditRating.setByUser(userEntity);
		auditRating.setRating(rateUserForm.getRating());
		auditRating.setNote(rateUserForm.getNote());

		auditRatingRepo.save(auditRating);
		log.info("saveAuditRating :: auditRating " + auditRating);

		return true;
	}

	@Transactional
	private boolean saveOrUpdateUserRatingDetail(RateUserForm rateUserForm, UserEntity toUserEntity) {
		log.info("saveOrUpdateUserRatingDetail");
		UserRatingDetail userRatingDetail = userRatingDetailRepo.findByUserId(toUserEntity.getId());

		float avg = rateUserForm.getRating();
		float total = rateUserForm.getRating();
		int count = AppConst.NUMBER.ONE;

		if (null == userRatingDetail) {
			userRatingDetail = new UserRatingDetail();
			userRatingDetail.setUser(toUserEntity);
		} else {
			total += userRatingDetail.getTotal();
			count += userRatingDetail.getCount();
		}

		avg = total / count;

		log.info("saveOrUpdateUserRatingDetail :: avg " + avg);
		log.info("saveOrUpdateUserRatingDetail :: total " + total);
		log.info("saveOrUpdateUserRatingDetail :: count " + count);

		userRatingDetail.setAvg(avg);
		userRatingDetail.setTotal(total);
		userRatingDetail.setCount(count);

		userRatingDetailRepo.save(userRatingDetail);
		log.info("saveOrUpdateUserRatingDetail :: userRatingDetail " + userRatingDetail);

		return true;
	}

	@Transactional
	public AuditRatingModel createAuditRatingModel(AuditRating auditRating) {
		log.info("createAuditRatingModel");
		OrderRatingDetailModel orderRatingDetailModel = null;
		if (null != auditRating.getOrderRatingDetail())
			orderRatingDetailModel = createOrderRatingDetailModel(auditRating.getOrderRatingDetail());

		UserDetailModel byUserDetailModel = null;
		if (null != auditRating.getByUser())
			byUserDetailModel = createUserDetailModel(auditRating.getByUser());

		return new AuditRatingModel(auditRating.getId(), orderRatingDetailModel, auditRating.getUser().getId(),
				byUserDetailModel, auditRating.getRating(), auditRating.getNote());
	}

	@Transactional
	public OrderRatingDetailModel createOrderRatingDetailModel(OrderRatingDetail orderRatingDetail) {
		log.info("createOrderRatingDetailModel");

		return new OrderRatingDetailModel(orderRatingDetail.getId(), orderRatingDetail.getOrder().getId());
	}

	@Transactional
	public UserDetailModel createUserDetailModel(UserEntity userEntity) {
		log.info("createUserDetailModel");
		UserDetailModel byUserDetailModel = null;
		UserModel userModel = authBuilder.createUserModel(userEntity);

		switch (UserRoleEnum.valueOf(userEntity.getUserRoleMappings().get(0).getMasterRole().getId())) {
			case DISPATCHER:
				UserDispatcherDetailModel userDispatcherDetailModel = null;
				if (null != userEntity.getUserDispatcherDetail())
					userDispatcherDetailModel = authBuilder
							.createUserDispatcherDetailModel(userEntity.getUserDispatcherDetail());

				byUserDetailModel = new UserDetailModel(userModel, userDispatcherDetailModel);
				break;

			case DRIVER:
				UserDriverDetailModel userDriverDetailModel = null;
				if (null != userEntity.getUserDriverDetail())
					userDriverDetailModel = authBuilder
							.createUserDriverDetailModelWithDispatcherUserDetailModel(userEntity.getUserDriverDetail());

				byUserDetailModel = new UserDetailModel(userModel, userDriverDetailModel);
				break;

			case CUSTOMER:
				UserCustomerDetailModel userCustomerDetailModel = null;
				if (null != userEntity.getUserCustomerDetail())
					userCustomerDetailModel = authBuilder
							.createUserCustomerDetailModel(userEntity.getUserCustomerDetail());

				byUserDetailModel = new UserDetailModel(userModel, userCustomerDetailModel);
				break;

			default:
				break;
		}

		return byUserDetailModel;
	}

	@Transactional
	public boolean markNotificationsRead(List<Notification> notifications) {
		log.info("markNotificationsRead");
		if (null != notifications && !notifications.isEmpty()) {
			notifications.forEach(notification -> {
				notification.setRead(true);
			});

			notificationRepo.save(notifications);
			log.info("markNotificationsRead :: notifications " + notifications);
		}

		return true;
	}

	@Transactional
	public Notification markNotificationRead(Notification notification) {
		log.info("markNotificationRead");
		notification.setRead(true);
		notificationRepo.save(notification);

		log.info("markNotificationRead :: notification " + notification);

		return notification;
	}

	@Transactional
	public AuditOrderStatusModel createAuditOrderStatusModel(AuditOrderStatus auditOrderStatus) {
		log.info("createAuditOrderStatusModel");

		return new AuditOrderStatusModel(auditOrderStatus.getId(), auditOrderStatus.getUser().getId(),
				auditOrderStatus.getOrder().getId(), auditOrderStatus.getOrderStatus().getId(),
				auditOrderStatus.getCreatedAt());
	}

}
