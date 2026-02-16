package com.org.verdaflow.rest.api.auth.service;

import com.org.verdaflow.rest.api.auth.form.AdminSignInForm;
import com.org.verdaflow.rest.api.auth.form.AppSignInForm;
import com.org.verdaflow.rest.api.auth.form.AppSignUpForm;
import com.org.verdaflow.rest.api.auth.form.ForgotPasswordForm;
import com.org.verdaflow.rest.api.auth.form.ResetPasswordForm;
import com.org.verdaflow.rest.api.auth.form.UpdatePasswordForm;
import com.org.verdaflow.rest.api.auth.form.WebSignInForm;
import com.org.verdaflow.rest.api.auth.model.UserAuthModel;
import com.org.verdaflow.rest.common.model.IdModel;

public interface AuthService {

	/**
	 * LogIn from Web (Admin)
	 * 
	 * @param adminSignInForm
	 * @return
	 */
	UserAuthModel adminLogIn(AdminSignInForm adminSignInForm);

	/**
	 * SignIn from Web (Dispatcher)
	 * 
	 * @param emailSignInForm
	 * @return
	 */
	UserAuthModel webSignIn(WebSignInForm emailSignInForm);

	/**
	 * SignUp from App (Customer)
	 * 
	 * @param appSignUpForm
	 * @return
	 */
	UserAuthModel appSignUp(AppSignUpForm appSignUpForm);

	/**
	 * SignIn from App (Dispatcher/Driver/Customer)
	 * 
	 * @param appSignInForm
	 * @return
	 */
	UserAuthModel appSignIn(AppSignInForm appSignInForm);

	/**
	 * Delete account (Dispatcher/Driver/Customer)
	 * 
	 * @param roleId
	 * @param userId
	 * @return
	 */
	boolean deleteAccount(int roleId, int userId);

	/**
	 * Admin forgot Password
	 * 
	 * @return
	 */
	boolean adminForgotPassword();

	/**
	 * Forgot Password (Dispatcher/Driver/Customer)
	 * 
	 * @param forgotPasswordForm
	 * @return
	 */
	boolean forgotPassword(ForgotPasswordForm forgotPasswordForm);

	/**
	 * Validating the verification code used for reset password.
	 * 
	 * @param resetPasswordForm
	 * @return
	 */
	boolean validateResetCode(ResetPasswordForm resetPasswordForm);

	/**
	 * Reset Password via Email
	 * 
	 * @param resetPasswordForm
	 * @return
	 */
	boolean resetPassword(ResetPasswordForm resetPasswordForm);

	/**
	 * Check user existence
	 * 
	 * @param roleId
	 * @param countryCode
	 * @param mobileNumber
	 * @return
	 */
	int checkUserExistence(int roleId, String countryCode, String mobileNumber);

	/**
	 * Update Password after check user existence (Dispatcher/Driver/Customer)
	 * 
	 * @param updatePasswordForm
	 * @return
	 */
	boolean updatePassword(UpdatePasswordForm updatePasswordForm);

}
