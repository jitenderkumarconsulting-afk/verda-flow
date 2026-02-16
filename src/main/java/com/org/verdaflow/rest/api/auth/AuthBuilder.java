package com.org.verdaflow.rest.api.auth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.org.verdaflow.rest.api.admin.model.UserDetailModel;
import com.org.verdaflow.rest.api.auth.form.AppSignUpForm;
import com.org.verdaflow.rest.api.auth.form.ResetPasswordForm;
import com.org.verdaflow.rest.api.auth.form.UpdatePasswordForm;
import com.org.verdaflow.rest.api.auth.model.UserAuthModel;
import com.org.verdaflow.rest.api.auth.model.UserCustomerDetailModel;
import com.org.verdaflow.rest.api.auth.model.UserDispatcherDetailModel;
import com.org.verdaflow.rest.api.auth.model.UserDriverDetailModel;
import com.org.verdaflow.rest.api.auth.model.UserModel;
import com.org.verdaflow.rest.api.dispatcher.DispatcherBuilder;
import com.org.verdaflow.rest.api.driver.DriverBuilder;
import com.org.verdaflow.rest.api.driver.model.DriverLocationDetailModel;
import com.org.verdaflow.rest.api.master.MasterBuilder;
import com.org.verdaflow.rest.api.master.model.MasterModel;
import com.org.verdaflow.rest.api.user.UserBuilder;
import com.org.verdaflow.rest.api.user.model.UserRatingDetailModel;
import com.org.verdaflow.rest.common.enums.ApplicationStatus;
import com.org.verdaflow.rest.common.enums.DeviceType;
import com.org.verdaflow.rest.common.enums.UserRoleEnum;
import com.org.verdaflow.rest.common.model.UserRoleModel;
import com.org.verdaflow.rest.config.common.AppConst;
import com.org.verdaflow.rest.config.common.StringConst;
import com.org.verdaflow.rest.config.security.jwt.JwtTokenUtil;
import com.org.verdaflow.rest.config.security.jwt.JwtUserFactory;
import com.org.verdaflow.rest.entity.DeviceDetail;
import com.org.verdaflow.rest.entity.ForgotPassword;
import com.org.verdaflow.rest.entity.UserCustomerDetail;
import com.org.verdaflow.rest.entity.UserDispatcherDetail;
import com.org.verdaflow.rest.entity.UserDriverDetail;
import com.org.verdaflow.rest.entity.UserEntity;
import com.org.verdaflow.rest.entity.UserRoleMapping;
import com.org.verdaflow.rest.entity.embeddedId.UserRoleMappingPK;
import com.org.verdaflow.rest.error.AppException;
import com.org.verdaflow.rest.event.ForgotPasswordEvent;
import com.org.verdaflow.rest.event.WelcomeCustomerEvent;
import com.org.verdaflow.rest.repo.DeviceDetailRepo;
import com.org.verdaflow.rest.repo.ForgotPasswordRepo;
import com.org.verdaflow.rest.repo.MasterRoleRepo;
import com.org.verdaflow.rest.repo.OrderRepo;
import com.org.verdaflow.rest.repo.UserCustomerDetailRepo;
import com.org.verdaflow.rest.repo.UserEntityRepo;
import com.org.verdaflow.rest.repo.UserRoleMappingRepo;
import com.org.verdaflow.rest.util.AppUtil;

@Component
public class AuthBuilder {
	public static final Logger log = LoggerFactory.getLogger(AuthBuilder.class);

	@Autowired
	private DeviceDetailRepo deviceDetailRepo;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserRoleMappingRepo userRoleMappingRepo;

	@Autowired
	private MasterRoleRepo masterRoleRepo;

	@Autowired
	private UserEntityRepo userEntityRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserCustomerDetailRepo userCustomerDetailRepo;

	@Autowired
	private ApplicationEventMulticaster applicationEventMulticaster;

	@Autowired
	private AppUtil appUtil;

	@Autowired
	private ForgotPasswordRepo forgotPasswordRepo;

	@Autowired
	private OrderRepo orderRepo;

	@Autowired
	private MasterBuilder masterBuilder;

	@Autowired
	private DispatcherBuilder dispatcherBuilder;

	@Autowired
	private DriverBuilder driverBuilder;

	@Autowired
	private UserBuilder userBuilder;

	@Transactional
	public List<UserRoleMapping> saveUserRoleMapping(int roleid, UserEntity userEntity,
			ApplicationStatus applicationStatus) {
		log.info("saveUserRoleMapping");
		List<UserRoleMapping> userRoleMappings = new ArrayList<>();

		UserRoleMapping userRoleMapping = new UserRoleMapping();
		UserRoleMappingPK userRoleMappingPK = new UserRoleMappingPK();
		userRoleMappingPK.setRoleId(roleid);
		userRoleMappingPK.setUserId(userEntity.getId());
		userRoleMapping.setUser(userEntity);

		userRoleMapping.setMasterRole(masterRoleRepo.findOne(roleid));
		userRoleMapping.setApplicationStatus(applicationStatus);
		userRoleMapping.setDeleted(false);
		userRoleMapping.setId(userRoleMappingPK);
		userRoleMappingRepo.save(userRoleMapping);
		userRoleMappings.add(userRoleMapping);
		log.info("saveUserRoleMapping :: userRoleMapping " + userRoleMapping);

		return userRoleMappings;
	}

	@Transactional
	public UserAuthModel createSessionReturnUser(String deviceType, UserEntity userEntity, String deviceToken,
			int roleId) {
		int deviceId = signDetailToDeviceDetail(deviceType, userEntity, deviceToken);
		log.info("createSessionReturnUser :: deviceId " + deviceId);

		String token = jwtTokenUtil.generateToken(JwtUserFactory.create(userEntity, deviceId, roleId));
		log.info("createSessionReturnUser :: token " + token);

		int roleStatus = 0;
		UserRoleMapping mapping = userRoleMappingRepo.findMappingByUserId(userEntity.getId(), roleId);
		if (null != mapping) {
			if (ApplicationStatus.APPROVED == mapping.getApplicationStatus()) {
				roleStatus = AppConst.NUMBER.ONE;
			} else if (ApplicationStatus.REJECTED == mapping.getApplicationStatus()) {
				roleStatus = AppConst.NUMBER.TWO;
			}
		}

		UserDispatcherDetailModel userDispatcherDetailModel = null;
		UserDriverDetailModel userDriverDetailModel = null;
		UserCustomerDetailModel userCustomerDetailModel = null;

		switch (UserRoleEnum.valueOf(roleId)) {
			case DISPATCHER:
				if (null != userEntity.getUserDispatcherDetail())
					userDispatcherDetailModel = createUserDispatcherDetailModel(userEntity.getUserDispatcherDetail());
				break;

			case DRIVER:
				if (null != userEntity.getUserDriverDetail())
					userDriverDetailModel = createUserDriverDetailModelWithDispatcherUserDetailModel(
							userEntity.getUserDriverDetail());
				break;

			case CUSTOMER:
				if (null != userEntity.getUserCustomerDetail())
					userCustomerDetailModel = createUserCustomerDetailModel(userEntity.getUserCustomerDetail());
				break;

			default:
				break;
		}

		return new UserAuthModel(userEntity.getId(), token, roleId, String.valueOf(UserRoleEnum.values()[roleId - 1]),
				roleStatus, createUserModel(userEntity),
				userEntity.getUserRoleMappings().stream()
						.map(mapper -> new UserRoleModel(mapper.getMasterRole().getId(),
								String.valueOf(mapper.getMasterRole().getRole()), mapper.getApplicationStatus()))
						.collect(Collectors.toList()),
				userDispatcherDetailModel, userDriverDetailModel, userCustomerDetailModel);
	}

	/*
	 * Used for generating a unique deviceId of a loggedIn user.
	 */
	@Transactional
	public int signDetailToDeviceDetail(String deviceType, UserEntity user, String deviceToken) {
		log.info("signDetailToDeviceDetail");
		List<DeviceDetail> deviceDetails = deviceDetailRepo.findByUserIdAndDeviceToken(user.getId(),
				DeviceType.valueOf(deviceType), deviceToken);
		List<DeviceDetail> signDetailToDeviceDetailEntities = new ArrayList<>();
		if (!deviceDetails.isEmpty()) {
			for (DeviceDetail sessionEntity : deviceDetails) {
				sessionEntity.setDeleted(true);
				signDetailToDeviceDetailEntities.add(sessionEntity);
			}
			deviceDetailRepo.save(signDetailToDeviceDetailEntities);
			log.info(
					"signDetailToDeviceDetail :: signDetailToDeviceDetailEntities " + signDetailToDeviceDetailEntities);
		}
		DeviceDetail deviceDetail = new DeviceDetail();
		deviceDetail.setDeviceType(DeviceType.valueOf(deviceType));
		deviceDetail.setDeviceToken(deviceToken);
		deviceDetail.setDeleted(false);
		deviceDetail.setUser(user);
		deviceDetailRepo.save(deviceDetail);
		log.info("signDetailToDeviceDetail :: deviceDetail " + deviceDetail);

		return deviceDetail.getId();
	}

	@Transactional
	public UserModel createUserModel(UserEntity userEntity) {
		log.info("AuthBuilder.createUserModel");

		UserRatingDetailModel userRatingDetail = null;
		if (null != userEntity.getUserRatingDetail())
			userRatingDetail = userBuilder.createUserRatingDetailModel(userEntity.getUserRatingDetail());

		return new UserModel(userEntity.getId(), userEntity.getEmail(), userEntity.getCountryCode(),
				userEntity.getMobileNumber(), userEntity.isPushNotificationStatus(), userEntity.isActive(),
				userEntity.getCreatedAt(), userRatingDetail);
	}

	@Transactional
	public UserDispatcherDetailModel createUserDispatcherDetailModel(UserDispatcherDetail userDispatcherDetail) {
		log.info("createUserDispatcherDetailModel");
		String imageUrl = null;
		if (null != userDispatcherDetail.getImage() && StringUtils.isNotBlank(userDispatcherDetail.getImage()))
			imageUrl = AppUtil.AWS_S3_BASE_URL_AND_BUCKET + userDispatcherDetail.getImage();
		log.info("createUserDispatcherDetailModel :: imageUrl" + imageUrl);

		String imageThumbUrl = null;
		if (null != userDispatcherDetail.getImageThumb()
				&& StringUtils.isNotBlank(userDispatcherDetail.getImageThumb()))
			imageThumbUrl = AppUtil.AWS_S3_BASE_URL_AND_BUCKET + userDispatcherDetail.getImageThumb();
		log.info("createUserDispatcherDetailModel :: imageThumbUrl" + imageThumbUrl);

		MasterModel etaModel = null;
		if (null != userDispatcherDetail.getEta())
			etaModel = masterBuilder.createMasterModel(userDispatcherDetail.getEta());
		log.info("createUserDispatcherDetailModel :: etaModel " + etaModel);

		return new UserDispatcherDetailModel(userDispatcherDetail.getId(), userDispatcherDetail.getStoreName(),
				userDispatcherDetail.getManagerName(), userDispatcherDetail.getAddress(), userDispatcherDetail.getLat(),
				userDispatcherDetail.getLng(), imageUrl, imageThumbUrl, etaModel,
				userDispatcherDetail.getApplicationStatus(), userDispatcherDetail.isActive(),
				userDispatcherDetail.getDeliveryCharges());
	}

	@Transactional
	public UserDispatcherDetailModel createDispatcherUserDetailModelWithDriverAndOrdersCount(
			UserDispatcherDetail userDispatcherDetail) {
		log.info("createDispatcherUserDetailModelWithDriverAndOrdersCount");
		String imageUrl = null;
		if (null != userDispatcherDetail.getImage() && StringUtils.isNotBlank(userDispatcherDetail.getImage()))
			imageUrl = AppUtil.AWS_S3_BASE_URL_AND_BUCKET + userDispatcherDetail.getImage();
		log.info("createDispatcherUserDetailModelWithDriverAndOrdersCount :: imageUrl" + imageUrl);

		String imageThumbUrl = null;
		if (null != userDispatcherDetail.getImageThumb()
				&& StringUtils.isNotBlank(userDispatcherDetail.getImageThumb()))
			imageThumbUrl = AppUtil.AWS_S3_BASE_URL_AND_BUCKET + userDispatcherDetail.getImageThumb();
		log.info("createDispatcherUserDetailModelWithDriverAndOrdersCount :: imageThumbUrl" + imageThumbUrl);

		int orderCount = orderRepo.findOrdersCountByDispatcherId(userDispatcherDetail.getId());
		log.info("createDispatcherUserDetailModelWithDriverAndOrdersCount :: orderCount" + orderCount);

		MasterModel etaModel = null;
		if (null != userDispatcherDetail.getEta())
			etaModel = masterBuilder.createMasterModel(userDispatcherDetail.getEta());
		log.info("createDispatcherUserDetailModelWithDriverAndOrdersCount :: etaModel " + etaModel);

		return new UserDispatcherDetailModel(userDispatcherDetail.getId(), userDispatcherDetail.getStoreName(),
				userDispatcherDetail.getManagerName(), userDispatcherDetail.getAddress(), userDispatcherDetail.getLat(),
				userDispatcherDetail.getLng(), imageUrl, imageThumbUrl, etaModel,
				userDispatcherDetail.getApplicationStatus(), userDispatcherDetail.isActive(),
				userDispatcherDetail.getDeliveryCharges(), userDispatcherDetail.getUserDriverDetails().size(),
				orderCount);
	}

	@Transactional
	public UserDriverDetailModel createUserDriverDetailModel(UserDriverDetail userDriverDetail) {
		log.info("createUserDriverDetailModel");
		String imageUrl = null;
		if (null != userDriverDetail.getImage() && StringUtils.isNotBlank(userDriverDetail.getImage()))
			imageUrl = AppUtil.AWS_S3_BASE_URL_AND_BUCKET + userDriverDetail.getImage();
		log.info("createUserDriverDetailModel :: imageUrl" + imageUrl);

		String imageThumbUrl = null;
		if (null != userDriverDetail.getImageThumb() && StringUtils.isNotBlank(userDriverDetail.getImageThumb()))
			imageThumbUrl = AppUtil.AWS_S3_BASE_URL_AND_BUCKET + userDriverDetail.getImageThumb();
		log.info("createUserDriverDetailModel :: imageThumbUrl" + imageThumbUrl);

		String vehicleRegistrationPhotoUrl = null;
		if (null != userDriverDetail.getVehicleRegistrationPhoto()
				&& StringUtils.isNotBlank(userDriverDetail.getVehicleRegistrationPhoto()))
			vehicleRegistrationPhotoUrl = AppUtil.AWS_S3_BASE_URL_AND_BUCKET
					+ userDriverDetail.getVehicleRegistrationPhoto();
		log.info("createUserDriverDetailModel :: vehicleRegistrationPhotoUrl" + vehicleRegistrationPhotoUrl);

		String driverLicensePhotoUrl = null;
		if (null != userDriverDetail.getDriverLicensePhoto()
				&& StringUtils.isNotBlank(userDriverDetail.getDriverLicensePhoto()))
			driverLicensePhotoUrl = AppUtil.AWS_S3_BASE_URL_AND_BUCKET + userDriverDetail.getDriverLicensePhoto();
		log.info("createUserDriverDetailModel :: driverLicensePhotoUrl" + driverLicensePhotoUrl);

		String carLicensePlatePhotoUrl = null;
		if (null != userDriverDetail.getCarLicensePlatePhoto()
				&& StringUtils.isNotBlank(userDriverDetail.getCarLicensePlatePhoto()))
			carLicensePlatePhotoUrl = AppUtil.AWS_S3_BASE_URL_AND_BUCKET + userDriverDetail.getCarLicensePlatePhoto();
		log.info("createUserDriverDetailModel :: carLicensePlatePhotoUrl" + carLicensePlatePhotoUrl);

		DriverLocationDetailModel driverLocationDetailModel = null;
		if (null != userDriverDetail.getDriverLocationDetail())
			driverLocationDetailModel = driverBuilder
					.createDriverLocationDetailModel(userDriverDetail.getDriverLocationDetail());

		return new UserDriverDetailModel(userDriverDetail.getId(), userDriverDetail.getName(),
				userDriverDetail.getYear(), userDriverDetail.getModel(), userDriverDetail.getMake(),
				vehicleRegistrationPhotoUrl, driverLicensePhotoUrl, carLicensePlatePhotoUrl, imageUrl, imageThumbUrl,
				userDriverDetail.getApplicationStatus(), userDriverDetail.isActive(), userDriverDetail.isOnline(),
				driverLocationDetailModel);
	}

	@Transactional
	public UserDriverDetailModel createUserDriverDetailModelWithDispatcherUserDetailModel(
			UserDriverDetail userDriverDetail) {
		log.info("createUserDriverDetailModelWithDispatcherUserDetailModel");
		String imageUrl = null;
		if (null != userDriverDetail.getImage() && StringUtils.isNotBlank(userDriverDetail.getImage()))
			imageUrl = AppUtil.AWS_S3_BASE_URL_AND_BUCKET + userDriverDetail.getImage();
		log.info("createUserDriverDetailModelWithDispatcherUserDetailModel :: imageUrl " + imageUrl);

		String imageThumbUrl = null;
		if (null != userDriverDetail.getImageThumb() && StringUtils.isNotBlank(userDriverDetail.getImageThumb()))
			imageThumbUrl = AppUtil.AWS_S3_BASE_URL_AND_BUCKET + userDriverDetail.getImageThumb();
		log.info("createUserDriverDetailModelWithDispatcherUserDetailModel :: imageThumbUrl " + imageThumbUrl);

		String vehicleRegistrationPhotoUrl = null;
		if (null != userDriverDetail.getVehicleRegistrationPhoto()
				&& StringUtils.isNotBlank(userDriverDetail.getVehicleRegistrationPhoto()))
			vehicleRegistrationPhotoUrl = AppUtil.AWS_S3_BASE_URL_AND_BUCKET
					+ userDriverDetail.getVehicleRegistrationPhoto();
		log.info("createUserDriverDetailModelWithDispatcherUserDetailModel :: vehicleRegistrationPhotoUrl "
				+ vehicleRegistrationPhotoUrl);

		String driverLicensePhotoUrl = null;
		if (null != userDriverDetail.getDriverLicensePhoto()
				&& StringUtils.isNotBlank(userDriverDetail.getDriverLicensePhoto()))
			driverLicensePhotoUrl = AppUtil.AWS_S3_BASE_URL_AND_BUCKET + userDriverDetail.getDriverLicensePhoto();
		log.info("createUserDriverDetailModelWithDispatcherUserDetailModel :: driverLicensePhotoUrl "
				+ driverLicensePhotoUrl);

		String carLicensePlatePhotoUrl = null;
		if (null != userDriverDetail.getCarLicensePlatePhoto()
				&& StringUtils.isNotBlank(userDriverDetail.getCarLicensePlatePhoto()))
			carLicensePlatePhotoUrl = AppUtil.AWS_S3_BASE_URL_AND_BUCKET + userDriverDetail.getCarLicensePlatePhoto();
		log.info("createUserDriverDetailModelWithDispatcherUserDetailModel :: carLicensePlatePhotoUrl "
				+ carLicensePlatePhotoUrl);

		UserEntity dipatcherUserEntity = userEntityRepo.findByUserIdAndRole(
				userDriverDetail.getUserDispatcherDetail().getUser().getId(), AppConst.USER_ROLE.DISPATCHER);
		if (null == dipatcherUserEntity)
			throw new AppException(StringConst.DISPATCHER_DETAIL_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("createUserDriverDetailModelWithDispatcherUserDetailModel :: dipatcherUserEntity "
				+ dipatcherUserEntity);

		UserDetailModel userDispatcherDetailModel = dispatcherBuilder
				.createDispatcherUserDetailModel(dipatcherUserEntity);

		DriverLocationDetailModel driverLocationDetailModel = null;
		if (null != userDriverDetail.getDriverLocationDetail())
			driverLocationDetailModel = driverBuilder
					.createDriverLocationDetailModel(userDriverDetail.getDriverLocationDetail());

		return new UserDriverDetailModel(userDriverDetail.getId(), userDriverDetail.getName(),
				userDriverDetail.getYear(), userDriverDetail.getModel(), userDriverDetail.getMake(),
				vehicleRegistrationPhotoUrl, driverLicensePhotoUrl, carLicensePlatePhotoUrl, imageUrl, imageThumbUrl,
				userDriverDetail.getApplicationStatus(), userDriverDetail.isActive(), userDriverDetail.isOnline(),
				userDispatcherDetailModel, driverLocationDetailModel);
	}

	@Transactional
	public UserEntity registerCustomer(AppSignUpForm appSignUpForm) {
		log.info("registerCustomer");
		UserEntity userEntity = saveUserDetail(appSignUpForm);
		log.info("registerCustomer :: userEntity " + userEntity);

		userEntity = saveUserCustomerDetail(appSignUpForm, userEntity);

		return userEntity;
	}

	@Transactional
	public UserEntity saveUserDetail(AppSignUpForm appSignUpForm) {
		log.info("saveUserDetail");
		UserEntity userEntity = new UserEntity();
		userEntity.setEmail(appSignUpForm.getEmail());
		userEntity.setCountryCode(appSignUpForm.getCountryCode());
		userEntity.setMobileNumber(appSignUpForm.getMobileNumber());
		userEntity.setPassword(passwordEncoder.encode(appSignUpForm.getPassword()));
		userEntity.setActive(true);
		userEntityRepo.save(userEntity);

		log.info("saveUserDetail :: userEntity " + userEntity);

		// Set the User Role Mapping
		List<UserRoleMapping> userRoleMappings = saveUserRoleMapping(AppConst.USER_ROLE.CUSTOMER, userEntity,
				ApplicationStatus.APPROVED);
		userEntity.setUserRoleMappings(userRoleMappings);

		return userEntity;
	}

	@Transactional
	public UserEntity saveUserCustomerDetail(AppSignUpForm appSignUpForm, UserEntity userEntity) {
		log.info("saveUserCustomerDetail");
		UserCustomerDetail userCustomerDetail = new UserCustomerDetail();
		userCustomerDetail.setFirstName(appSignUpForm.getFirstName());
		userCustomerDetail.setLastName(appSignUpForm.getLastName());
		userCustomerDetail.setApplicationStatus(ApplicationStatus.APPROVED);
		userCustomerDetail.setActive(true);
		userCustomerDetail.setUser(userEntity);
		userCustomerDetailRepo.save(userCustomerDetail);

		log.info("saveUserCustomerDetail :: userCustomerDetail " + userCustomerDetail);

		userEntity.setUserCustomerDetail(userCustomerDetail);

		return userEntity;
	}

	@Transactional
	public boolean welcomeCustomerEmail(UserEntity userEntity, String password) {
		log.info("welcomeCustomerEmail");
		WelcomeCustomerEvent welcomeCustomerEvent = new WelcomeCustomerEvent(this, userEntity.getEmail(),
				userEntity.getMobileNumber(), password, userEntity.getUserCustomerDetail().getFirstName(),
				userEntity.getUserCustomerDetail().getLastName());

		applicationEventMulticaster.multicastEvent(welcomeCustomerEvent);

		return true;
	}

	@Transactional
	public UserCustomerDetailModel createUserCustomerDetailModel(UserCustomerDetail userCustomerDetail) {
		log.info("createUserCustomerDetailModel");

		String imageUrl = null;
		if (null != userCustomerDetail.getImage() && StringUtils.isNotBlank(userCustomerDetail.getImage()))
			imageUrl = AppUtil.AWS_S3_BASE_URL_AND_BUCKET + userCustomerDetail.getImage();
		log.info("createUserCustomerDetailModel :: imageUrl " + imageUrl);

		String imageThumbUrl = null;
		if (null != userCustomerDetail.getImageThumb() && StringUtils.isNotBlank(userCustomerDetail.getImageThumb()))
			imageThumbUrl = AppUtil.AWS_S3_BASE_URL_AND_BUCKET + userCustomerDetail.getImageThumb();
		log.info("createUserCustomerDetailModel :: imageThumbUrl " + imageThumbUrl);

		return new UserCustomerDetailModel(userCustomerDetail.getId(), userCustomerDetail.getFirstName(),
				userCustomerDetail.getLastName(), imageUrl, imageThumbUrl, userCustomerDetail.getApplicationStatus(),
				userCustomerDetail.isActive());
	}

	@Transactional
	public boolean deleteAccount(UserEntity userEntity) {
		log.info("deleteAccount");

		userEntity.setDeleted(true);
		userEntityRepo.save(userEntity);

		log.info("deleteAccount :: userEntity " + userEntity);

		return true;
	}

	@Transactional
	public boolean forgotPasswordEmail(UserEntity userEntity) {
		log.info("forgotPasswordEmail");
		ForgotPassword forgotPasswordEntity = userEntity.getForgotPassword();
		log.info("forgotPasswordEmail :: forgotPasswordEntity " + forgotPasswordEntity);

		if (forgotPasswordEntity == null) {
			forgotPasswordEntity = new ForgotPassword();
			forgotPasswordEntity.setAdmin(UserRoleEnum.ADMIN == userEntity.getUserRole());
			forgotPasswordEntity.setUser(userEntity);
		}

		Date currentDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		calendar.add(Calendar.DATE, AppConst.NUMBER.ONE);
		Date updatedDt = calendar.getTime();
		String tempCode = appUtil.genTempCode();

		forgotPasswordEntity.setExpiresAt(updatedDt);
		forgotPasswordEntity.setVerificationKey(tempCode);
		forgotPasswordRepo.save(forgotPasswordEntity);

		log.info("forgotPasswordEmail :: forgotPasswordEntity " + forgotPasswordEntity);

		String name = StringConst.BLANK_STRING;
		switch (userEntity.getUserRole()) {
			case ADMIN:
				name = StringConst.ADMIN;
				break;

			case DISPATCHER:
				if (null != userEntity.getUserDispatcherDetail())
					name = userEntity.getUserDispatcherDetail().getManagerName();
				break;

			case DRIVER:
				if (null != userEntity.getUserDriverDetail())
					name = userEntity.getUserDriverDetail().getName();
				break;

			case CUSTOMER:
				if (null != userEntity.getUserCustomerDetail())
					name = userEntity.getUserCustomerDetail().getFirstName();
				break;
		}

		log.info("forgotPasswordEmail :: name " + name);

		ForgotPasswordEvent forgotPasswordEvent = new ForgotPasswordEvent(this, tempCode, name, userEntity.getEmail(),
				userEntity.getId(), userEntity.getUserRole().getId());
		log.info("forgotPasswordEmail :: forgotPasswordEvent " + forgotPasswordEvent);

		applicationEventMulticaster.multicastEvent(forgotPasswordEvent);

		return true;
	}

	@Transactional
	public boolean resetPassword(ForgotPassword forgotPassword, ResetPasswordForm resetPasswordForm) {
		log.info("AuthBuilder.resetPassword");
		UserEntity userEntity = forgotPassword.getUser();
		userEntity.setPassword(passwordEncoder.encode(resetPasswordForm.getPassword()));
		userEntity.getForgotPassword().setVerificationKey(StringConst.EMPTY_STRING);
		userEntityRepo.save(userEntity);
		forgotPasswordRepo.save(userEntity.getForgotPassword());

		log.info("resetPassword :: userEntity " + userEntity);

		return true;
	}

	@Transactional
	public boolean updatePassword(UpdatePasswordForm updatePasswordForm, UserEntity userEntity) {
		log.info("updatePassword");
		userEntity.setPassword(passwordEncoder.encode(updatePasswordForm.getNewPass()));
		userEntityRepo.save(userEntity);

		log.info("updatePassword :: userEntity " + userEntity);

		return true;
	}

	@Transactional
	public boolean isAccountActive(UserEntity user, int roleId) {
		log.info("isAccountActive");
		boolean active = true;

		if (!user.isActive())
			active = false;
		else {
			switch (roleId) {
				case AppConst.USER_ROLE.DISPATCHER:
					if (null == user.getUserDispatcherDetail() || !user.getUserDispatcherDetail().isActive())
						active = false;
					break;

				case AppConst.USER_ROLE.DRIVER:
					if (null == user.getUserDriverDetail() || !user.getUserDriverDetail().isActive()
							|| null == user.getUserDriverDetail().getUserDispatcherDetail()
							|| !user.getUserDriverDetail().getUserDispatcherDetail().isActive())
						active = false;
					break;

				case AppConst.USER_ROLE.CUSTOMER:
					if (null == user.getUserCustomerDetail() || !user.getUserCustomerDetail().isActive())
						active = false;
					break;

				default:
					break;
			}
		}

		return active;
	}
}
