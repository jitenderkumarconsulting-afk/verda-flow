package com.org.verdaflow.rest.error;

public final class RestErrorConstants {
	// not to be looked up in error.properties file
	public static final String SUCCESSFULLSIGNUP_MSG = "Successfully user registered";
	public static final String SENTLINKTOPASSWORD_MSG = "Sent link to reset password"; // NOSONAR
	public static final String PASSWORDCHANGED_MSG = "Password reset"; // NOSONAR
	public static final String PASSWORDCOULDNOTBECHANGED_MSG = "Password could not be changed"; // NOSONAR
	public static final String LOGOUTSUCCESS_MSG = "Log out successfully";
	public static final String LOGOUTFAILURE_MSG = "Log out unsuccessful";
	public static final String PLEASELOGIN_MSG = "Please login to access resource";
	public static final String UNAUTHORIZED_MSG = "You are not authorize access resource";
	public static final String UNAUTHORIZED_INVALID_TOKEN_MSG = "You are not an authorized user.";
	public static final String UNAUTHORIZED_TOKEN_EXPIRED_MSG = "Your session has been expired. Please login again.";
	public static final String UNAUTHORIZED_ACCOUNT_DEACTIVATED_MSG = "Your account has been deactivated.";
	public static final String SUCCESSFULLSIGNIN_MSG = "Sign in successfully";
	public static final String PROFILECREATED_MSG = "Profile updated successfully";
	public static final String ADDRESSDELETEDSUCCESS_MSG = "Address deleted successfully";
	public static final String ADDRESSNOTDELETEDSUCCESS_MSG = "Address could not be deleted";
	public static final String ADDRESSADDEDSUCCESS_MSG = "Address added sucessfully";
	public static final String ADDRESSUPDATESUCCESS_MSG = "Address updated successfully";
	public static final String PROCESSED_MSG = "Processed successfully";
	public static final String USERBLOCKEDSTATUSUPDATED_MSG = "status updated successfully";
	public static final String JOBIDISINVALID_MSG = "Given job id is invalid";
	public static final String USERNOTFOUND = "user.not.found";

	// RESPONSE INTEGER CODE
	public static final Integer BAD_REQUEST_CODE = 400;
	public static final Integer UNAUTHORIZE_CODE = 401;
	public static final Integer UNAUTHORIZE_CODE_LOGOUT = 401;
	public static final Integer FORBIDDEN_CODE = 403;
	public static final Integer OK = 200;

	public static final String AUTHENTICATION_ERROR = "authenticationError";

}
