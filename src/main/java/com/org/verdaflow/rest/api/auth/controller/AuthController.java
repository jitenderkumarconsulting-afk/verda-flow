package com.org.verdaflow.rest.api.auth.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.org.verdaflow.rest.api.auth.form.AdminSignInForm;
import com.org.verdaflow.rest.api.auth.form.AppSignInForm;
import com.org.verdaflow.rest.api.auth.form.AppSignUpForm;
import com.org.verdaflow.rest.api.auth.form.ForgotPasswordForm;
import com.org.verdaflow.rest.api.auth.form.ResetPasswordForm;
import com.org.verdaflow.rest.api.auth.form.UpdatePasswordForm;
import com.org.verdaflow.rest.api.auth.form.WebSignInForm;
import com.org.verdaflow.rest.api.auth.model.UserAuthModel;
import com.org.verdaflow.rest.api.auth.service.AuthService;
import com.org.verdaflow.rest.common.model.GenericModel;
import com.org.verdaflow.rest.config.common.AppConst;
import com.org.verdaflow.rest.config.common.StringConst;
import com.org.verdaflow.rest.dto.ResponseEnvelope;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
	public static final Logger log = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private AuthService authService;

	@Autowired
	private Environment env;

	/**
	 * This method is used by ADMIN to sign-in from Web into the application.
	 * 
	 * @param adminSignInForm
	 * @return
	 */
	@PostMapping(value = "/adminLogIn")
	@ApiOperation(value = "This method is used by ADMIN to sign-in from Web into the application.")
	public ResponseEnvelope<UserAuthModel> adminLogIn(@RequestBody @Valid AdminSignInForm adminSignInForm) {
		log.info("adminLogIn");
		return new ResponseEnvelope<>(authService.adminLogIn(adminSignInForm), true,
				env.getProperty(StringConst.SIGNIN_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for signing-In the user (Dispatcher) from Web into the
	 * application.
	 * 
	 * @param emailSignInForm
	 * @return
	 */
	@PostMapping(value = "/webSignIn")
	@ApiOperation(value = "This method is used for signing-In the user (Dispatcher) from Web into the application.")
	public ResponseEnvelope<UserAuthModel> webSignIn(@RequestBody @Valid WebSignInForm emailSignInForm) {
		log.info("webSignIn");
		return new ResponseEnvelope<>(authService.webSignIn(emailSignInForm), true,
				env.getProperty(StringConst.SIGNIN_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for signing-Up the user (Customer) from App into the
	 * application.
	 * 
	 * @param appSignUpForm
	 * @return
	 */
	@PostMapping(value = "/appSignUp")
	@ApiOperation(value = "This method is used for signing-Up the user (Customer) from App into the application.")
	public ResponseEnvelope<UserAuthModel> appSignUp(@RequestBody @Valid AppSignUpForm appSignUpForm) {
		log.info("appSignUp");

		return new ResponseEnvelope<>(authService.appSignUp(appSignUpForm), true,
				appSignUpForm.isOtpVerified() ? env.getProperty(StringConst.SIGNUP_SUCCESS) : StringConst.EMPTY_STRING,
				AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for signing-In the user (Dispatcher/Driver/Customer) from
	 * App into the application.
	 * 
	 * @param appSignInForm
	 * @return
	 */
	@PostMapping(value = "/appSignIn")
	@ApiOperation(value = "This method is used for signing-In the user (Dispatcher/Driver/Customer) from App into the application.")
	public ResponseEnvelope<UserAuthModel> appSignIn(@RequestBody @Valid AppSignInForm appSignInForm) {
		log.info("appSignIn");
		return new ResponseEnvelope<>(authService.appSignIn(appSignInForm), true,
				env.getProperty(StringConst.SIGNIN_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

	// /**
	// * This method is used for deleting the user (Dispatcher/Driver/Customer) from
	// * the application.
	// *
	// * @param roleId
	// * @param userId
	// * @return
	// */
	// @RequestMapping(value = "/account/{roleId}/{userId}", produces =
	// MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.DELETE)
	// @ApiOperation(value = "This method is used for deleting the user
	// (Dispatcher/Driver/Customer) from the application.")
	// public ResponseEnvelope<Boolean> deleteAccount(@PathVariable("roleId") int
	// roleId,
	// @PathVariable("userId") int userId) {
	// log.info("deleteAccount");
	// return new ResponseEnvelope<>(authService.deleteAccount(roleId, userId),
	// true, StringConst.EMPTY_STRING,
	// AppConst.HTTP_STATUS_OK);
	// }

	/**
	 * This method is used for providing the functionality of forgot password to
	 * Admin from Web into the application.
	 * 
	 * @return
	 */
	@PostMapping(value = "/adminForgotPassword")
	@ApiOperation(value = "This method is used to send a link to Admin's mail for reseting password (Only for ADMIN) from Web into the application.")
	public ResponseEnvelope<GenericModel<Boolean>> adminForgotPassword() {
		log.info("adminForgotPassword");
		return new ResponseEnvelope<>(new GenericModel<>(authService.adminForgotPassword()), true,
				env.getProperty(StringConst.LINK_CHANGE_PASWORD_SENT_TO_EMAIL), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for providing the functionality of forgot password via
	 * email to User (Dispatcher/Driver/Customer) from Web into the application.
	 * 
	 * @param forgotPasswordForm
	 * @return
	 */
	@PostMapping(value = "/forgotPassword")
	@ApiOperation(value = "This method is used for providing the functionality of forgot password via email to User (Dispatcher/Driver/Customer) from Web into the application.")
	public ResponseEnvelope<GenericModel<Boolean>> forgotPassword(
			@RequestBody @Valid ForgotPasswordForm forgotPasswordForm) {
		log.info("forgotPassword");
		return new ResponseEnvelope<>(new GenericModel<>(authService.forgotPassword(forgotPasswordForm)), true,
				env.getProperty(StringConst.LINK_CHANGE_PASWORD_SENT_TO_EMAIL), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for providing the functionality of validating the
	 * verification code used for reset password.
	 * 
	 * @param resetPasswordForm
	 * @return
	 */
	@PostMapping(value = "/validateResetPassCode")
	@ApiOperation(value = "This method is used for providing the functionality of validating the verification code used for reset password.")
	public ResponseEnvelope<GenericModel<Boolean>> validateResetCode(
			@RequestBody @Valid ResetPasswordForm resetPasswordForm) {
		log.info("AuthController.validateResetCode");
		return new ResponseEnvelope<>(new GenericModel<>(authService.validateResetCode(resetPasswordForm)), true,
				StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for providing the functionality of reset password.
	 * 
	 * @param resetPasswordForm
	 * @return
	 */
	@PostMapping(value = "/resetPassword")
	@ApiOperation(value = " This method is used by User for providing the functionality of reset password.")
	public ResponseEnvelope<GenericModel<Boolean>> resetPassword(
			@RequestBody @Valid ResetPasswordForm resetPasswordForm) {
		log.info("resetPassword");
		return new ResponseEnvelope<>(new GenericModel<>(authService.resetPassword(resetPasswordForm)), true,
				env.getProperty(StringConst.PASWRD_RESET_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used for checking user existence by mobile number for specific
	 * role from App into the application.
	 * 
	 * @param roleId
	 * @param countryCode
	 * @param mobileNumber
	 * @return
	 */
	@GetMapping(value = "/userExists/{roleId}/{countryCode}/{mobileNumber}")
	@ApiOperation(value = "This method is used for checking user existence by mobile number for specific role from App into the application.")
	public ResponseEnvelope<GenericModel<Integer>> checkUserExistence(
			@PathVariable(value = "roleId", required = true) int roleId,
			@PathVariable(value = "countryCode", required = true) String countryCode,
			@PathVariable(value = "mobileNumber", required = true) String mobileNumber) {
		log.info("checkUserExistence");
		return new ResponseEnvelope<>(
				new GenericModel<>(authService.checkUserExistence(roleId, countryCode, mobileNumber)), true,
				StringConst.EMPTY_STRING, AppConst.HTTP_STATUS_OK);
	}

	/**
	 * This method is used to update password after checking user existence by
	 * mobile number for specific role from App into the application.
	 * 
	 * 
	 * @param updatePasswordForm
	 * @return
	 */
	@PostMapping(value = "/updatePassword")
	@ApiOperation(value = "This method is used to update password after checking user existence by mobile number for specific role from App into the application.")
	public ResponseEnvelope<GenericModel<Boolean>> updatePassword(
			@RequestBody @Valid UpdatePasswordForm updatePasswordForm) {
		log.info("updatePassword");
		return new ResponseEnvelope<>(new GenericModel<>((authService.updatePassword(updatePasswordForm))), true,
				env.getProperty(StringConst.PASWRD_UPDATE_SUCCESS), AppConst.HTTP_STATUS_OK);
	}

	// @RequestMapping(value = "/testing", produces =
	// MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	// public ResponseEnvelope<List<CustomModel>> testing(@RequestParam("page") int
	// page,
	// @RequestParam("size") int size) {
	// log.info("testing");
	// Pageable pageable = new PageRequest(page - 1, size);
	// List<CustomModel> customModels = userDispatcherDetailRepo.testing();
	//
	// /*int nextPage;
	// if (customModels.getContent().isEmpty()
	// || customModels.getTotalPages() == pageable.getPageNumber() +
	// AppConst.NUMBER.ONE
	// || customModels.getTotalPages() == AppConst.NUMBER.ONE) {
	// nextPage = -AppConst.NUMBER.ONE;
	// } else {
	// nextPage = (pageable.getPageNumber() + AppConst.NUMBER.ONE) +
	// AppConst.NUMBER.ONE;
	// }
	// log.info("listDispatchers :: nextPage " + nextPage);
	//
	// List<?> customs=customModels.getContent();*/
	//
	// return new ResponseEnvelope<>(customModels, true, null,
	// AppConst.HTTP_STATUS_OK);
	// }

}
