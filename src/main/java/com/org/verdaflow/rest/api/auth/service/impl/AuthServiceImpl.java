package com.org.verdaflow.rest.api.auth.service.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.org.verdaflow.rest.api.auth.AuthBuilder;
import com.org.verdaflow.rest.api.auth.form.AdminSignInForm;
import com.org.verdaflow.rest.api.auth.form.AppSignInForm;
import com.org.verdaflow.rest.api.auth.form.AppSignUpForm;
import com.org.verdaflow.rest.api.auth.form.ForgotPasswordForm;
import com.org.verdaflow.rest.api.auth.form.ResetPasswordForm;
import com.org.verdaflow.rest.api.auth.form.UpdatePasswordForm;
import com.org.verdaflow.rest.api.auth.form.WebSignInForm;
import com.org.verdaflow.rest.api.auth.model.UserAuthModel;
import com.org.verdaflow.rest.api.auth.service.AuthService;
import com.org.verdaflow.rest.common.enums.DeviceType;
import com.org.verdaflow.rest.common.enums.UserRoleEnum;
import com.org.verdaflow.rest.config.common.AppConst;
import com.org.verdaflow.rest.config.common.StringConst;
import com.org.verdaflow.rest.entity.ForgotPassword;
import com.org.verdaflow.rest.entity.UserEntity;
import com.org.verdaflow.rest.error.AppException;
import com.org.verdaflow.rest.repo.ForgotPasswordRepo;
import com.org.verdaflow.rest.repo.UserEntityRepo;
import com.org.verdaflow.rest.util.AppUtil;

@Service
public class AuthServiceImpl implements AuthService {
	public static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

	@Autowired
	private AuthBuilder authBuilder;

	@Autowired
	private UserEntityRepo userEntityRepo;

	@Autowired
	private AppUtil appUtil;

	@Autowired
	private Environment env;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ForgotPasswordRepo forgotPasswordRepo;

	@Override
	@Transactional
	public UserAuthModel adminLogIn(AdminSignInForm adminSignInForm) {
		log.info("adminLogIn");
		UserEntity userEntity = userEntityRepo
				.checkUserExistenceByEmailAndRole(adminSignInForm.getEmail().toLowerCase(), AppConst.USER_ROLE.ADMIN);
		if (null == userEntity)
			throw new AppException(StringConst.USER_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("adminLogIn :: userEntity " + userEntity);

		if (!passwordEncoder.matches(adminSignInForm.getPassword(), userEntity.getPassword()))
			throw new AppException(StringConst.SIGN_IN_FAIL_EMAIL, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		// try {
		// Authentication authentication = authenticationManager
		// .authenticate(new
		// UsernamePasswordAuthenticationToken(adminSignInForm.getEmail().toLowerCase(),
		// adminSignInForm.getPassword()));
		// SecurityContextHolder.getContext().setAuthentication(authentication);
		// } catch (BadCredentialsException e) {
		// log.error(new StringBuilder(StringConst.INVALID_CREDENTIAL_FOR_EMAIL)
		// .append(adminSignInForm.getEmail().toLowerCase()).toString(), e);
		// throw new AppException(StringConst.SIGN_IN_FAIL,
		// AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
		// }

		return authBuilder.createSessionReturnUser(String.valueOf(DeviceType.WEB), userEntity,
				appUtil.generateRandomAlphaNumericCode(AppConst.NUMBER.TWENTYFOUR), AppConst.USER_ROLE.ADMIN);
	}

	@Override
	@Transactional
	public UserAuthModel webSignIn(WebSignInForm emailSignInForm) {
		log.info("webSignIn");
		if (AppConst.USER_ROLE.DISPATCHER != emailSignInForm.getRoleId())
			throw new AppException(StringConst.ENTER_VALID_ROLE, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		UserEntity userEntity = userEntityRepo.checkUserExistenceByEmailAndRole(
				emailSignInForm.getEmail().toLowerCase(), AppConst.USER_ROLE.DISPATCHER);
		if (null == userEntity)
			throw new AppException(StringConst.SIGN_IN_FAIL_EMAIL, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("webSignIn :: userEntity " + userEntity);

		if (!passwordEncoder.matches(emailSignInForm.getPassword(), userEntity.getPassword()))
			throw new AppException(StringConst.SIGN_IN_FAIL_EMAIL, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		// try {
		// Authentication authentication = authenticationManager.authenticate(new
		// UsernamePasswordAuthenticationToken(
		// emailSignInForm.getEmail().toLowerCase(), emailSignInForm.getPassword()));
		// SecurityContextHolder.getContext().setAuthentication(authentication);
		// } catch (BadCredentialsException e) {
		// log.error(new StringBuilder(StringConst.INVALID_CREDENTIAL_FOR_EMAIL)
		// .append(emailSignInForm.getEmail().toLowerCase()).toString(), e);
		// throw new AppException(StringConst.SIGN_IN_FAIL,
		// AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
		// }

		if (!authBuilder.isAccountActive(userEntity, emailSignInForm.getRoleId()))
			throw new AppException(StringConst.ACCOUNT_DEACTIVATED, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		return authBuilder.createSessionReturnUser(String.valueOf(DeviceType.WEB), userEntity,
				appUtil.generateRandomAlphaNumericCode(AppConst.NUMBER.TWENTYFOUR), emailSignInForm.getRoleId());
	}

	@Override
	@Transactional
	public UserAuthModel appSignUp(AppSignUpForm appSignUpForm) {
		log.info("appSignUp");
		if (AppConst.USER_ROLE.CUSTOMER != appSignUpForm.getRoleId())
			throw new AppException(StringConst.ENTER_VALID_ROLE, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		if (!DeviceType.ANDROID.name().equals(appSignUpForm.getDeviceType())
				&& !DeviceType.IOS.name().equals(appSignUpForm.getDeviceType()))
			throw new AppException(StringConst.ENTER_VALID_DEVICE_TYPE, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		UserEntity userEntity = userEntityRepo.checkUserExistenceByEmailAndRole(appSignUpForm.getEmail(),
				AppConst.USER_ROLE.CUSTOMER);
		if (null != userEntity)
			throw new AppException(StringConst.EMAIL_ALREADY_EXIST, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
		log.info("appSignUp :: userEntity " + userEntity);

		userEntity = userEntityRepo.checkUserExistenceByMobileNumberAndRole(appSignUpForm.getCountryCode(),
				appSignUpForm.getMobileNumber(), AppConst.USER_ROLE.CUSTOMER);
		if (null != userEntity)
			throw new AppException(StringConst.MOBILE_ALREADY_EXIST, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
		log.info("appSignUp :: userEntity " + userEntity);

		if (appSignUpForm.isOtpVerified()) {
			userEntity = authBuilder.registerCustomer(appSignUpForm);
			authBuilder.welcomeCustomerEmail(userEntity, appSignUpForm.getPassword());

			return authBuilder.createSessionReturnUser(appSignUpForm.getDeviceType(), userEntity,
					appSignUpForm.getDeviceToken(), appSignUpForm.getRoleId());
		} else {
			return null;
		}
	}

	@Override
	@Transactional
	public UserAuthModel appSignIn(AppSignInForm appSignInForm) {
		log.info("appSignIn");
		if (AppConst.USER_ROLE.ADMIN == appSignInForm.getRoleId())
			throw new AppException(StringConst.ENTER_VALID_ROLE, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		if (!DeviceType.ANDROID.name().equals(appSignInForm.getDeviceType())
				&& !DeviceType.IOS.name().equals(appSignInForm.getDeviceType()))
			throw new AppException(StringConst.ENTER_VALID_DEVICE_TYPE, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		UserEntity userEntity = userEntityRepo.checkUserExistenceByMobileNumberAndRole(appSignInForm.getCountryCode(),
				appSignInForm.getMobileNumber(), appSignInForm.getRoleId());
		if (null == userEntity)
			throw new AppException(StringConst.SIGN_IN_FAIL_MOBILE_NUMBER, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("appSignIn :: userEntity " + userEntity);

		if (!passwordEncoder.matches(appSignInForm.getPassword(), userEntity.getPassword()))
			throw new AppException(StringConst.SIGN_IN_FAIL_MOBILE_NUMBER, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		// try {
		// Authentication authentication = authenticationManager.authenticate(new
		// UsernamePasswordAuthenticationToken(
		// appSignInForm.getMobileNumber(), appSignInForm.getPassword()));
		// SecurityContextHolder.getContext().setAuthentication(authentication);
		// } catch (BadCredentialsException e) {
		// log.error(new StringBuilder(StringConst.INVALID_CREDENTIAL_FOR_EMAIL)
		// .append(appSignInForm.getMobileNumber()).toString(), e);
		// throw new AppException(StringConst.SIGN_IN_FAIL,
		// AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
		// }

		if (!authBuilder.isAccountActive(userEntity, appSignInForm.getRoleId()))
			throw new AppException(StringConst.ACCOUNT_DEACTIVATED, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		return authBuilder.createSessionReturnUser(appSignInForm.getDeviceType(), userEntity,
				appSignInForm.getDeviceToken(), appSignInForm.getRoleId());
	}

	@Override
	@Transactional
	public boolean deleteAccount(int roleId, int userId) {
		log.info("deleteAccount");
		if (AppConst.USER_ROLE.ADMIN == roleId)
			throw new AppException(StringConst.ENTER_VALID_ROLE, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		UserEntity userEntity = userEntityRepo.findByUserIdAndRole(userId, roleId);
		if (null == userEntity)
			throw new AppException(StringConst.USER_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("deleteAccount :: userEntity " + userEntity);

		authBuilder.deleteAccount(userEntity);

		return true;
	}

	@Override
	@Transactional
	public boolean adminForgotPassword() {
		log.info("adminForgotPassword");
		String email = env.getProperty(StringConst.ADMIN_MAIL);
		log.info("adminForgotPassword :: email " + email);

		UserEntity userEntity = userEntityRepo.checkUserExistenceByEmailAndRole(email, AppConst.USER_ROLE.ADMIN);
		if (userEntity == null)
			throw new AppException(StringConst.USER_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("adminForgotPassword :: userEntity " + userEntity);

		userEntity.setUserRole(UserRoleEnum.ADMIN);

		authBuilder.forgotPasswordEmail(userEntity);

		return true;
	}

	@Override
	@Transactional
	public boolean forgotPassword(ForgotPasswordForm forgotPasswordForm) {
		log.info("forgotPassword");
		if (AppConst.USER_ROLE.ADMIN == forgotPasswordForm.getRoleId())
			throw new AppException(StringConst.ENTER_VALID_ROLE, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		UserEntity userEntity = userEntityRepo.checkUserExistenceByEmailAndRole(forgotPasswordForm.getEmail(),
				forgotPasswordForm.getRoleId());
		if (userEntity == null)
			throw new AppException(StringConst.USER_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("forgotPassword :: userEntity " + userEntity);

		if (!authBuilder.isAccountActive(userEntity, forgotPasswordForm.getRoleId()))
			throw new AppException(StringConst.ACCOUNT_DEACTIVATED, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		userEntity.setUserRole(UserRoleEnum.valueOf(forgotPasswordForm.getRoleId()));

		authBuilder.forgotPasswordEmail(userEntity);

		return true;
	}

	@Override
	@Transactional
	public boolean validateResetCode(ResetPasswordForm resetPasswordForm) {
		log.info("validateResetCode");
		ForgotPassword forgotPassword = forgotPasswordRepo.findByUserIdAndCode(resetPasswordForm.getUserId(),
				resetPasswordForm.getVerificationCode());
		if (null == forgotPassword) {
			log.info("validateResetCode :: SessionExpired");
			throw new AppException(StringConst.SESSION_EXPIRED, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
		}

		log.info("validateResetCode :: forgotPassword " + forgotPassword);

		if (!authBuilder.isAccountActive(forgotPassword.getUser(), resetPasswordForm.getRoleId()))
			throw new AppException(StringConst.ACCOUNT_DEACTIVATED, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		return true;
	}

	@Override
	@Transactional
	public boolean resetPassword(ResetPasswordForm resetPasswordForm) {
		log.info("resetPassword");
		if (null == resetPasswordForm.getPassword() || StringUtils.isEmpty(resetPasswordForm.getPassword()))
			throw new AppException(StringConst.ENTER_VALID_PASWRD, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		ForgotPassword forgotPassword = forgotPasswordRepo.findByUserIdAndCode(resetPasswordForm.getUserId(),
				resetPasswordForm.getVerificationCode());
		if (null == forgotPassword) {
			log.info("resetPassword :: SessionExpired");
			throw new AppException(StringConst.SESSION_EXPIRED, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);
		}
		log.info("resetPassword :: forgotPassword " + forgotPassword);

		if (!authBuilder.isAccountActive(forgotPassword.getUser(), resetPasswordForm.getRoleId()))
			throw new AppException(StringConst.ACCOUNT_DEACTIVATED, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		authBuilder.resetPassword(forgotPassword, resetPasswordForm);

		return true;
	}

	@Override
	@Transactional
	public int checkUserExistence(int roleId, String countryCode, String mobileNumber) {
		log.info("checkUserExistence");
		UserEntity userEntity = userEntityRepo.checkUserExistenceByMobileNumberAndRole(countryCode, mobileNumber,
				roleId);
		if (userEntity == null)
			throw new AppException(StringConst.USER_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("checkUserExistence :: userEntity " + userEntity);

		if (!authBuilder.isAccountActive(userEntity, roleId))
			throw new AppException(StringConst.ACCOUNT_DEACTIVATED, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		return userEntity.getId();
	}

	@Override
	@Transactional
	public boolean updatePassword(UpdatePasswordForm updatePasswordForm) {
		log.info("updatePassword");
		if (AppConst.USER_ROLE.ADMIN == updatePasswordForm.getRoleId())
			throw new AppException(StringConst.ENTER_VALID_ROLE, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		UserEntity userEntity = userEntityRepo.findByUserIdAndRole(updatePasswordForm.getUserId(),
				updatePasswordForm.getRoleId());
		if (userEntity == null)
			throw new AppException(StringConst.USER_NOT_FOUND, AppConst.EXCEPTION_CAT.NOT_FOUND_404);
		log.info("updatePassword :: userEntity " + userEntity);

		if (!authBuilder.isAccountActive(userEntity, updatePasswordForm.getRoleId()))
			throw new AppException(StringConst.ACCOUNT_DEACTIVATED, AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406);

		authBuilder.updatePassword(updatePasswordForm, userEntity);

		return true;
	}
}
