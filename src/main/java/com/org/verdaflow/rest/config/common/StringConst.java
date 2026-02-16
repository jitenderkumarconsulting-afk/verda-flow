package com.org.verdaflow.rest.config.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringConst {
	public static final Logger logger = LoggerFactory.getLogger(StringConst.class);

	private StringConst() {
	}

	public static final String BASE_URL = "BASE_URL";

	public static final String AWS_S3_BASE_URL = "AWS_S3_BASE_URL";
	public static final String AWS_S3_BUCKET_NAME = "AWS_S3_BUCKET_NAME";
	public static final String AWS_S3_ACCESS_KEY = "AWS_S3_ACCESS_KEY";
	public static final String AWS_S3_SECRET_KEY = "AWS_S3_SECRET_KEY";
	public static final String AWS_S3_REGION = "AWS_S3_REGION";

	public static final String FCM_SERVER_KEY = "FCM_SERVER_KEY";
	public static final String NOTIFICATION_PRIORITY = "NOTIFICATION_PRIORITY";
	public static final String AUTHORIZATION = "Authorization";
	public static final String KEY = "key=";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String APPLICATION_JSON = "APPLICATION/JSON";
	public static final String DEFAULT = "default";

	public static final String WEB_URL_RESET_PASWRD = "WEB_URL_RESET_PASWRD";
	public static final String WEB_URL_RESET_PASWRD_ADMIN = "WEB_URL_RESET_PASWRD_ADMIN";

	public static final String OTP_BLOCKED = "otp.blocked";
	public static final String OTP_FAILED = "otp.failed";
	public static final String OTP_NOT_EXISTS = "otp.not_exists";
	public static final String OTP_EXPIRED = "otp.expired";
	public static final String OTP_INVALID = "otp.invalid";

	public static final String SIGNIN_SUCCESS = "signin.success";
	public static final String SIGNUP_SUCCESS = "signup.success";
	public static final String LOGOUT_SUCCESS = "logout.success";
	public static final String PROFILE_UPDATED_SUCCESS = "profile.update.success";
	public static final String TRUCK_FITNESSEXPIRYDATE_INVALID = "truck.fitnessexpirydate.invalid";
	public static final String INVALID_FILTER = "INVALID.FILTER";
	public static final String USER_NOT_FOUND = "USER.NOT_FOUND";
	public static final String MESSAGE_FAILED = "message.failed";
	public static final String INVALID_REQUEST = "invalid.request";
	public static final String SESSION_EXPIRED = "SESSION.EXPIRED";
	public static final String TRUCK_NOT_FOUND = "truck.not.found";
	public static final String NOTIFICATION_LIST_EMPTY = "NOTIFICATION.LIST.EMPTY";
	public static final String USER_ROLE_APPROVED_ENTERPRISE_PENDING_NOT_FOUND = "user.role.approved.enterprise.pending.not_found";
	public static final String USER_ROLE_APPROVED_ENTERPRISE_PENDING = "user.role.approved.enterprise.pending";
	public static final String USER_DOB_INVALID = "user.dob.invalid";
	public static final String LINK_EXPIRED = "LINK.EXPIRED";
	public static final String USER_DEACTIVE = "USER.DEACTIVE";
	public static final String BLOCKED_FOR_THIRTY_MINUTES = "BLOCKED.FOR.THIRTY.MINUTES";
	public static final String BLOCKED_FOR_EXCEEDING_ATTEMPTS = "BLOCKED.FOR.EXCEEDING.ATTEMPTS";
	public static final String EMAIL_ALREADY_EXIST = "EMAIL.ALREADY.EXIST";
	public static final String MOBILE_ALREADY_EXIST = "MOBILE.ALREADY.EXIST";
	public static final String SIGN_IN_FAIL_EMAIL = "sign.in.fail.email";
	public static final String SIGN_IN_FAIL_MOBILE_NUMBER = "sign.in.fail.mobile_number";
	public static final String USER_ALREADY_REGISTERED = "USER.ALREADY.REGISTERED";
	public static final String REGISTER_DISPATCHER = "register.dispatcher";
	public static final String UPDATE_DISPATCHER = "update.dispatcher";
	public static final String REGISTER_DRIVER = "register.driver";
	public static final String UPDATE_DRIVER = "update.driver";
	public static final String ENTER_VALID_DEVICE_TYPE = "ENTER.VALID.DEVICE_TYPE";
	public static final String PASWRD_CHANGE_SUCCESS = "paswrd.change.success";
	public static final String PASWRD_RESET_SUCCESS = "paswrd.reset.success";
	public static final String PASWRD_UPDATE_SUCCESS = "paswrd.update.success";
	public static final String UPDATE_NOTHING = "update.nothing";
	public static final String DISPATCHER_DETAIL_NOT_FOUND = "dispatcher_detail.not_found";
	public static final String UPDATE_DISPATCHER_ID_INVALID = "UPDATE.DISPATCHER_ID.INVALID";
	public static final String DRIVER_DETAIL_NOT_FOUND = "driver_detail.not_found";
	public static final String UPDATE_DRIVER_ID_INVALID = "UPDATE.DRIVER_ID.INVALID";
	public static final String ACCOUNT_DEACTIVATED = "account.deactivated";
	public static final String ENTER_VALID_ROLE = "ENTER.VALID.ROLE";
	public static final String ENTER_VALID_PASWRD = "ENTER_VALID_PASWRD";
	public static final String USER_NATIONAL_ID_ALREADY_EXISTS = "user.national_id.already_exists";
	public static final String DRIVER_LICENSE_NUMBER_EMPTY = "driver.license_number.empty";
	public static final String DRIVER_LICENSE_EXPIRY_DATE_EMPTY = "driver.license_expiry_date.empty";
	public static final String DRIVER_BACK_PHOTO_EMPTY = "driver.back_photo.empty";
	public static final String DRIVER_FRONT_PHOTO_EMPTY = "driver.front_photo.empty";
	public static final String DRIVER_LICENSE_NUMBER_ALREADY_EXISTS = "driver.license_number.already_exists";
	public static final String ALREADY_REQUESTED_PASWORD = "ALREADY.REQUESTED.PASSWORD";
	public static final String INCORRECT_PASWORD = "incorrect.password";
	public static final String OLD_AND_NEW_MISS_MATCH = "old.and.new.miss.match";
	public static final String DRIVER_LICENSE_EXPIRY_DATE_INVALID = "driver.license_expiry_date.invalid";
	public static final String USER_ALREADY_REGISTERED_DRIVER = "USER.ALREADY.REGISTERED.DRIVER";
	public static final String DRIVER_LICENSE_NUMBER_ALREADY_EXISTS_OTHER_USER = "driver.license_number.already_exists.other_user";
	public static final String USER_REGISTERED_DRIVER_NOT_FOUND = "user.registered.driver.not_found";
	public static final String USER_REGISTERED_FLEET_OWNER_NOT_FOUND = "user.registered.fleet_owner.not_found";
	public static final String USER_UPDATE_NOTHING = "user.update.nothing";
	public static final String MASTER_LIST_EMPTY = "master.list.empty";

	public static final String TRUCK_INSURANCEEXPIRYDATE_INVALID = "truck.insuranceexpirydate.invalid";
	public static final String TRUCK_REGISTRATIONDATE_INVALID = "truck.registrationdate.invalid";
	public static final String TRUCK_ROUTEPERMITEXPIRYDATE_INVALID = "truck.routepermitexpirydate.invalid";
	public static final String TRUCK_TAXTOKENEXPIRYDATE_INVALID = "truck.taxtokenexpirydate.invalid";
	public static final String TRUCK_MASTER_BRAND_INVALID = "truck.master.brand.invalid";
	public static final String TRUCK_MASTER_SIZE_INVALID = "truck.master.size.invalid";
	public static final String TRUCK_MASTER_TYPE_INVALID = "truck.master.type.invalid";
	public static final String TRUCK_MASTER_TYRE_INVALID = "truck.master.tyre.invalid";

	public static final String FILE_NOT_FOUND = "file.not.found";
	public static final String FILE_SAVE_FAILURE = "file.save.failure";
	public static final String FILE_DATA_EMPTY = "file.data.empty";
	public static final String CSV_TRANSFORM_FAILURE = "csv.transform.failure";
	public static final String CSV_RETRIEVED_EMPTY = "csv.retrieved.empty";
	public static final String CSV_TRUCK_SAVE_EMPTY = "csv.truck.save.empty";
	public static final String FILE_UNZIP_FAILURE = "file.unzip.failure";
	public static final String ZIP_RETRIEVED_EMPTY = "zip.retrieved.empty";
	public static final String ZIP_TRUCK_SAVE_EMPTY = "zip.truck.save.empty";

	public static final String USER_ROLE_FLEET_OWNER_NOT_APPROVED = "user.role.fleet_owner.not_approved";
	public static final String USER_ROLE_FLEET_OWNER_NOT_APPROVED_DISASSOCIATE_DRIVER = "user.role.fleet_owner.not_approved.disassociate_driver";
	public static final String USER_ROLE_FLEET_OWNER_NOT_APPROVED_APPROVE_DRIVER = "user.role.fleet_owner.not_approved.approve_driver";
	public static final String USER_ROLE_FLEET_OWNER_NOT_APPROVED_REJECT_DRIVER = "user.role.fleet_owner.not_approved.reject_driver";
	public static final String TRUCK_REGISTRATION_NUMBER_ALREADY_EXISTS = "truck.registration_number.already_exists";
	public static final String TRUCK_UPDATE_NOTHING = "truck.update.nothing";
	public static final String INVALID_ROLE = "invalid.role";
	public static final String NOT_AUTHORIZE = "not.authorize";
	public static final String TRADE_ALREADY_EXIST = "trade.already.exist";
	public static final String TIN_ALREADY_EXIST = "tin.already.exist";
	public static final String VAT_ALREADY_EXIST = "vat.already.exist";
	public static final String COI_ALREADY_EXIST = "coi.already.exist";
	public static final String COMPANY_ALREADY_EXIST = "company.already.exist";
	public static final String ARTICLE_CANNOT_EMPTY = "article.cannot.empty";
	public static final String TRADE_LICENSE_CANNOT_EMPTY = "trade.cannot.empty";
	public static final String MEMORANDUM_CANNOT_EMPTY = "memorandum.cannot.empty";
	public static final String TIN_CANNOT_EMPTY = "tin.cannot.empty";
	public static final String VAT_CANNOT_EMPTY = "vat.cannot.empty";
	public static final String CERTIFICATES_ALREADY_EXIST = "certificates.already.exist";
	public static final String GATEWAY_ALREADY_EXIST = "gateway.already.exist";
	public static final String GATEWAY_NUMBER_EXIST = "gateway.number.exist";

	public static final String BANK_ALREADY_EXIST = "bank.already.exist";
	public static final String BANK_NUMBER_EXIST = "bank.number.exist";

	public static final String OTP_GENERATE_FAILURE = "otp.generate.failure";
	public static final String OTP_SEND_FAILURE = "otp.send.failure";
	public static final String USER_SMARTPHONE_INFO_SAVE_FAILURE = "user.smartphone_info.save.failure";

	public static final String EMPTY_STRING_NOT_ALLOWED = "EMPTY_STRING_NOT_ALLOWED";
	public static final String INVALID_SCRIPTING_TAGS_FOUND = "INVALID_SCRIPTING_TAGS_FOUND";
	public static final String FILE_SIZE_MAX_LIMIT = "FILE_SIZE_MAX_LIMIT";
	public static final String IMAGE_SIZE_MAX_LIMIT = "IMAGE_SIZE_MAX_LIMIT";

	public static final String PDF_FILE_INVALID = "PDF.FILE.INVALID";
	public static final String IMAGE_INVALID = "IMAGE.INVALID";
	public static final String INVALID_IMAGE_EXTENSION = "INVALID_IMAGE_EXTENSION";
	public static final String INVALID_DATE_FORMAT = "INVALID_DATE_FORMAT";
	public static final String CSV_READ_FAILURE = "csv.read.failure";
	public static final String FILE_ZIP_FAILURE = "file.zip.failure";
	public static final String FILE_SAVE_CSV_ERROR_FAILURE = "file.save.csv.error.failure";

	public static final String VALIDATION_TRUCK_EMPTY = "validation.truck.empty";
	public static final String VALIDATION_TRUCK_EXISTS = "validation.truck.exists";
	public static final String VALIDATION_TRUCK_DATE_PAST_INVALID = "validation.truck.date.past.invalid";
	public static final String VALIDATION_TRUCK_FORMAT_INVALID = "validation.truck.format.invalid";
	public static final String VALIDATION_TRUCK_DATE_FUTURE_INVALID = "validation.truck.date.future.invalid";

	public static final String CSV_TRUCK_REGISTRATION_NUMBER_ALREADY_EXISTS = "csv.truck.registration_number.already_exists";
	public static final String SMS_ENTERPRISE_APPROVED = "sms.enterprise.approved";
	public static final String PRIVACY = "PRIVACY";
	public static final String TERMS = "TERMS";
	public static final String ADMIN_MAIL = "ADMIN_MAIL";

	public static final String OTP_VALIDITY_MINUTES = "OTP_VALIDITY_MINUTES";
	public static final String OTP_GENERATE_PERIOD_MINUTES = "OTP_GENERATE_PERIOD_MINUTES";
	public static final String OTP_GENERATE_LIMIT = "OTP_GENERATE_LIMIT";
	public static final String OTP_BLOCK_MINUTES = "OTP_BLOCK_MINUTES";
	public static final String DEFAULT_LANDING_IMAGE = "DEFAULT_LANDING_IMAGE";

	public static final String DATASOURCE_DRIVERCLASSNAME = "datasource.driverClassName";
	public static final String DATASOURCE_URL = "datasource.url";
	public static final String DATASOURCE_USERNAME = "datasource.username";
	public static final String DATASOURCE_PASWORD = "datasource.password";

	public static final String HIBERNATE_C3P0_MAX_SIZE = "hibernate.c3p0.max_size";
	public static final String HIBERNATE_C3P0_MIN_SIZE = "hibernate.c3p0.min_size";
	public static final String HIBERNATE_C3P0_MAX_STATEMENTS = "hibernate.c3p0.max_statements";
	public static final String HIBERNATE_C3P0_CHECKOUT_TIMEOUT = "hibernate.c3p0.checkout_timeout";
	public static final String HIBERNATE_C3P0_ACQUIRE_RETRY_ATTEMPTS = "hibernate.c3p0.acquire_retry_attempts";

	public static final String MAIL_HOST = "mail.host";
	public static final String MAIL_PORT = "mail.port";
	public static final String MAIL_USERNAME = "mail.username";
	public static final String MAIL_PASWORD = "mail.password";
	public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
	public static final String MAIL_SMTP_SSL_ENABLE = "mail.smtp.ssl.enable";

	public static final String CSV_BEAN_ID_TRUCK = "CSV_BEAN_ID_TRUCK";
	public static final String CSV_FIELDS_TRUCK = "CSV_FIELDS_TRUCK";
	public static final String CSV_FIELD_ERROR = "CSV_FIELD_ERROR";

	public static final String TRUCK_REMINDER_NOTIFICATION_DAYS = "TRUCK_REMINDER_NOTIFICATION_DAYS";

	public static final String SMS_API_BASE_URL = "SMS_API_BASE_URL";
	public static final String SMS_API_USER = "SMS_API_USER";
	public static final String SMS_API_PASWORD = "SMS_API_PASSWORD";
	public static final String SMS_API_SID = "SMS_API_SID";
	public static final String SMS_COUNTRY_CODE = "SMS_COUNTRY_CODE";

	public static final String CONTEXT = "context";
	public static final String SEPERATOR = "seperator";
	public static final String ENV = "env";
	public static final String USER_IMAGE_FILE_PATH = "USER_IMAGE_FILE_PATH";
	public static final String DRIVER_IMAGE_FILE_PATH = "DRIVER_IMAGE_FILE_PATH";
	public static final String TRUCK_IMAGE_FILE_PATH = "TRUCK_IMAGE_FILE_PATH";
	public static final String PDF_FILE_PATH = "PDF_FILE_PATH";
	public static final String IMAGE_EXTENSION = "IMAGE_EXTENSION";
	public static final String FILE_EXTENSION = "FILE_EXTENSION";
	public static final String S3LARGEIMAGE = "s3LargeImage";
	public static final String S3SMALLIMAGE = "s3SmallImage";
	public static final String S3FILEPATH = "s3FilePath";
	public static final String MAX_IMAGE_SIZE = "MAX_IMAGE_SIZE";
	public static final String MAX_PDF_FILE_SIZE = "MAX_PDF_FILE_SIZE";
	public static final String FIREBASE_API_URL = "FIREBASE_API_URL";
	public static final String FIREBASE_SERVER_KEY = "FIREBASE_SERVER_KEY";
	public static final String OTP_SIZE = "OTP_SIZE";

	public static final String MASTER_TRUCK_BRAND_ID = "Master truck brand id ";
	public static final String MASTER_TRUCK_SIZE_ID = "Master truck size id ";
	public static final String MASTER_TRUCK_TYPE_ID = "Master truck type id ";
	public static final String MASTER_TRUCK_TYRE_ID = "Master truck tyre id ";
	public static final String TRUCK_REGISTRATION_DATE = "Truck registration date ";
	public static final String TRUCK_REGISTRATION_NUMBER = "Truck registration number ";
	public static final String TRUCK_MODEL_YEAR = "Truck model year ";
	public static final String TRUCK_REGISTERED_OWNER_ADDRESS = "Truck registered owner address ";
	public static final String TRUCK_REGISTERED_OWNER_NAME = "Truck registered owner name ";
	public static final String TRUCK_REGISTRATION_PHOTO = "Truck registration photo ";
	public static final String TRUCK_FITNESS_CERTIFICATE_PHOTO = "Truck fitness certificate photo ";
	public static final String TRUCK_FITNESS_EXPIRY_DATE = "Truck fitness expiry date ";
	public static final String TRUCK_TAX_TOKEN_EXPIRY_DATE = "Truck tax token expiry date ";
	public static final String TRUCK_TAX_TOKEN_PHOTO = "Truck tax token photo ";
	public static final String TRUCK_INSURANCE_EXPIRY_DATE = "Truck insurance expiry date ";
	public static final String TRUCK_INSURANCE_PHOTO = "Truck insurance photo ";
	public static final String TRUCK_ROUTE_PERMIT_EXPIRY_DATE = "Truck route permit expiry date ";
	public static final String TRUCK_ROUTE_PERMIT_DATE = "Truck route permit date ";
	public static final String TRUCK_ROUTE_PERMIT_PHOTO = "Truck route permit photo ";
	public static final String TRUCK_BACK_WITH_NUMBER_PLATE = "Truck back with number plate ";
	public static final String TRUCK_FRONT_WITH_NUMBER_PLATE = "Truck front with number plate ";
	public static final String TRUCK_FULL = "Truck full ";
	public static final String TRUCK_INNER_DASHBOARD = "Truck inner dashboard ";
	public static final String YOU_HAVE_SUCCESSFULLY_UPLOADED = "You have successfully uploaded ";
	public static final String TRUCKS_INFORMATION_FROM_CSV = " truck(s) information from csv";
	public static final String TRUCKS_INFORMATION_FROM_CSV_BUT = " truck(s) information from csv but ";
	public static final String TRUCKS_INFORMATION_FAILED = " truck(s) information failed.";
	public static final String TRUCKS_IMAGES_FROM_ZIP = " truck(s) images from zip";
	public static final String TRUCKS_IMAGES_FROM_ZIP_BUT = " truck(s) images from zip but ";
	public static final String TRUCKS_IMAGES_FAILED = " truck(s) images failed.";

	public static final String EXPIRING_SOON = "Expiring soon";
	public static final String TRUCK_FITNESS_EXPIRY_DATE_COLON = "Truck Fitness Expiry Date:";
	public static final String TRUCK_ROUTE_PERMIT_EXPIRY_DATE_COLON = "Truck Route Permit Expiry Date:";
	public static final String TRUCK_TAX_TOKEN_EXPIRY_DATE_COLON = "Truck Tax Token Expiry Date:";
	public static final String TRUCK_INSURANCE_EXPIRY_DATE_COLON = "Truck Insurance Expiry Date:";

	public static final String INFO_ADDED_SUCCESSFULLY_PLEASE_FILL_REST_OF_INFO = "Information added successfully. Please fill the rest of the information.";
	public static final String INFO_UPDATED_SUCCESSFULLY_PLEASE_FILL_REST_OF_INFO = "Information updated successfully. Please fill the rest of the information.";

	public static final String USERNAME_NOT_FOUND_HYPHEN = "UserName Not Found -";
	public static final String DATE_PARSING_PROBLEM_HYPHEN = "Date parsing problem -";
	public static final String AUDIENCE_PARSING_PROBLEM_HYPHEN = "Audience parsing problem -";
	public static final String CLAIMS_PARSING_PROBLEM_HYPHEN = "Claims parsing problem -";
	public static final String PASWORD_PARSING_PROBLEM_HYPHEN = "Password parsing problem -";
	public static final String INVALID_DEVICE = "INVALID_DEVICE";
	public static final String EXCEPTION_HYPHEN = "Exception - ";

	public static final String PASWORD_COLON = "password: ";
	public static final String FORMATDATE_TO_HYPHEN = "formatDate to - ";
	public static final String INPUT_DATE_FORMAT_EXCEPTION_COLON_BRACES = " input date format Exception:{} ";

	public static final String IN_RESET_PG_COLON_TEMPCODE_COLON = "in resetPg : tempCode : ";
	public static final String GREATER_THAN_USERID_COLON = " > userId : ";
	public static final String GREATER_THAN_PASS_COLON = " > pass : ";

	public static final String IN_UPDATE_PASWORD_COLON_TEMPCODE_COLON = "in updatePassword : tempCode : ";
	public static final String IN_GETOTP_COLON_MOBILENUMBER_COLON = "in getOtp : mobileNumber : ";
	public static final String INVALID_CREDENTIAL_FOR_EMAIL = "Invalid credential for email ";
	public static final String ERROR_WHILE_CONFIG_AUTH_MANAGER_BUILDER = "Error while configuring aAuthenticationManagerBuilderuthe";
	public static final String EXCEPTION = "Exception";
	public static final String UNABLE_TO_TRANSFORM_CSV_TO_JAVA_COLON = "Unable to transform csv to java: ";
	public static final String UNABLE_TO_TRANSFORM_JAVA_TO_CSV_COLON = "Unable to transform java to csv: ";
	public static final String CSV_FILE_PATH_COLON = "csvFilePath: ";
	public static final String ERROR_SENDING_MAIL_TO = "Errror sending mail to ";
	public static final String ERROR_SENDING_WELCOME_DISPACTHER_MAIL_TO = "Errror sending welcome dispatcher mail to ";
	public static final String ERROR_SENDING_WELCOME_DRIVER_MAIL_TO = "Errror sending welcome driver mail to ";
	public static final String ERROR_SENDING_WELCOME_CUSTOMER_MAIL_TO = "Errror sending welcome customer mail to ";
	public static final String ERROR_SENDING_ACTIVATE_DISPATCHER_MAIL_TO = "Errror sending activate dispatcher mail to ";
	public static final String ERROR_SENDING_ACTIVATE_DRIVER_MAIL_TO = "Errror sending activate driver mail to ";
	public static final String ERROR_SENDING_ACTIVATE_CUSTOMER_MAIL_TO = "Errror sending activate customer mail to ";
	public static final String ERROR_SENDING_DEACTIVATE_CUSTOMER_MAIL_TO = "Errror sending de-activate customer mail to ";
	public static final String ERROR_SENDING_DEACTIVATE_DRIVER_MAIL_TO = "Errror sending de-activate dispatcher mail to ";
	public static final String ERROR_SENDING_DEACTIVATE_DISPATCHER_MAIL_TO = "Errror sending de-activate dispatcher mail to ";
	public static final String ERROR_SENDING_REPORT_PROBLEM_MAIL_TO = "Errror sending report problem mail to ";
	public static final String ERROR_MESSAGE_COLON = " error message : ";
	public static final String SERVER_ERROR_HYPHEN = "Server Error - ";
	public static final String SQL_ERROR_HYPHEN = "SQL Error - ";
	public static final String JSON_ERROR_HYPHEN = "Json Error - ";
	public static final String HTTP_MEDIATYPE_NOT_SUPPORTED_EXCEP_ERROR_HYPHEN = "HttpMediaTypeNotSupportedException Error - ";
	public static final String HTTP_MESSAGE_NOT_READ_EXCEP_HYPHEN = "HttpMessageNotReadableException - ";
	public static final String METHOD_ARGUMENT_TYPE_MISMATCH_EXCEPTION_HYPHEN = "MethodArgumentTypeMismatchException - ";
	public static final String ILLEGAL_ARGUMENT_EXCEPTION_HYPHEN = "IllegalArgumentException - ";
	public static final String INVALID_DATA_ACCESS_RESOURCES_USAGE_EXCEPTION = "InvalidDataAccessResourceUsageException - ";
	public static final String MISSING_SERVLET_REQUEST_PARAM_EXCEPTION_HYPHEN = "MissingServletRequestParameterException - ";
	public static final String HTTP_REQUEST_METHOD_NOT_SUPPORTED_EXCEPTION_HYPHEN = "HttpRequestMethodNotSupportedException - ";
	public static final String NO_HANDLER_FOUND_HYPHEN = "No handler found- ";
	public static final String VALIDATION_FAILED_EXCEPTION_HYPHEN = "Validation Failed Exception - ";
	public static final String PARAM_BINDING_EXCEPTION_HYPHEN = "Param Binding Exception - ";
	public static final String APP_EXCEPTION_BRACES = "AppException {}";
	public static final String UPDATE_BLOCKED_USER_STATUS_LIST_SIZE_COLON = "updateBlockedUserStatus LIST_SIZE: ";
	public static final String UPDATE_EXPIRED_TRUCK_STATUS_LIST_SIZE_COLON = "updateExpiredTruckStatus LIST_SIZE: ";
	public static final String SEND_TRUCK_EXPIRY_REMINDER_NOTIFICATIONS_LIST_SIZE_COLON = "sendTruckExpiryReminderNotifications LIST_SIZE: ";
	public static final String VALUE_SHOULD_NOT_BE_NULL_OR_EMPTY_COLON = "value should not be null or empty : ";
	public static final String VALUE_CONSIST_THE_SCRIPTING_TAGS_COLON = "value consist the scripting tags : ";
	public static final String ERROR_DECODING_QUERY = "Error decoding query ";
	public static final String APPROXIMATE_FILE_SIZE_COLON = "Approximate file size : ";
	public static final String ILLEGAL_STATE_EXCEPTION_HYPHEN = "IllegalStateException - ";
	public static final String IO_EXCEPTION_HYPHEN = "IOException - ";
	public static final String PARSE_DATE_INPUT_FORMAT_EXCEPTION_BRACES = "parseddMMyyyyTHHmmssToDate input date format Exception:{} ";
	public static final String FORMAT_DATE_TO_HYPHEN = "formatDate to - ";
	public static final String FORMAT_OFFSET_COLON = "formatOffset: ";
	public static final String PARSEDATE_COLON = "parsedDate: ";
	public static final String MIN_DATE_COLON = "minDate: ";
	public static final String MAX_DATE_COLON = "maxDate: ";

	public static final String UNABLE_TO_GENERATE_OTP_COLON = "Unable to generate OTP :";
	public static final String UNABLE_TO_SEND_MESSAGE_COLON = "Unable to send Message :";
	public static final String UNABLE_TO_UPLOAD_FILE_COLON = "Unable to upload file: ";
	public static final String UNABLE_TO_UNZIP_FILE_COLON = "Unable to unzip file: ";
	public static final String UNABLE_TO_READ_INPUT_MESSAGE_FROM_FILE_COLON = "Unable to readInputMessage from file: ";
	public static final String UNABLE_TO_ZIP_FILE_COLON = "Unable to zip file: ";
	public static final String UNABLE_TO_SAVE_CSV_ERROR_FILE_COLON = "Unable to save csv error file: ";
	public static final String PASWORD_FOR_USER_HAS_BEEN_SUCCESSFULLY_UPDATED = "Password for user has been successfully updated.";
	public static final String USER_DEACTIVATED_SUCCESSFULLY = "User deactivated successfully.";
	public static final String USER_ACTIVATED_SUCCESSFULLY = "User activated successfully.";
	public static final String USER_APPLICATION_HAS_BEEN_REJECTED = "User application has been rejected";
	public static final String USER_APPLICATION_HAS_BEEN_APPROVED = "User application has been approved";
	public static final String TRUCK_HAS_BEEN_REJECTED = "Truck has been rejected";
	public static final String TRUCK_HAS_BEEN_APPROVED = "Truck has been approved";
	public static final String USERS_ENTERPRISE_APPLICATION_HAS_BEEN_REJECTED = "User's enterprise application has been rejected";
	public static final String USERS_ENTERPRISE_APPLICATION_HAS_BEEN_APPROVED = "User's enterprise application has been approved";
	public static final String SUCCESSFULLY_SENT_SMS_TO_THE_USERS = "Successfully sent SMS to the user(s).";
	public static final String SUCCESSFULLY_SENT_EMAIL_TO_THE_USERS = "Successfully sent Email to the user(s).";
	public static final String IMAGE_UPLOADED_SUCCESSFULLY = "Image uploaded successfully";
	public static final String LINK_CHANGE_PASWORD_SENT_TO_EMAIL = "LINK.CHANGE_PASWORD.SENT_TO_EMAIL";
	public static final String REQUEST_FOR_RESETING_THE_PASWORD_HAS_BEEN_SEND = "Request for reseting the password has been send.";
	public static final String PASWORD_CHANGED_SUCCESSFULLY = "Password changed successfully";
	public static final String USER_LOGGED_OUT_SUCCESSFULLY = "User logged-out successfully";
	public static final String TRUCK_DELETED_SUCCESSFULLY = "Truck deleted successfully";
	public static final String DRIVER_APPLICATION_FOR_ASSOCIATION_HAS_BEEN_REJECTED = "Driver application for association has been rejected";
	public static final String DRIVER_APPLICATION_FOR_ASSOCIATION_HAS_BEEN_APPROVED = "Driver application for association has been approved";
	public static final String TRUCK_ADDED_SUCCESSFULLY_AND_PENDING_FOR_ADMIN_APPROVAL = "Truck added successfully & pending for admin approval.";
	public static final String TRUCK_INFORMATION_UPDATED_SUCCESSFULLY_AND_PENDING_FOR_ADMIN_APPROVAL = "Truck information updated successfully & pending for admin approval.";
	public static final String YOU_HAVE_SUCCESSFULLY_VALIDATED_THE_TRUCK_INFORMATION = "You have successfully validated the Truck information";

	public static final String YOUR_NEW_PASWORD_IS_COLON = "Your new password is : ";
	public static final String YOUR_APPLICATION_AS_A = "Your application as a ";
	public static final String HAS_BEEN_APPROVED = " has been approved";
	public static final String YOU_HAVE_SUCCESSFULLY_SIGNED_IN = "You have successfully signed-in";
	public static final String THANK_YOU_FOR_LOGGING_INTO_THE_PLATFORM = "Thank you for logging into the platform";
	public static final String YOUR_MOBILE_NUMBER_HAS_BEEN_VERIFIED_SUCCESSFULLY_DOT = "Your mobile number has been verified successfully.";
	public static final String YOUR_REQUEST_SENT_TO_ADMIN_48_HOURS = "Your request has been sent to the admin. An SMS regarding your approval will be sent to your registered mobile number within 48 hours.";
	public static final String NOT_AUTHORIZED_TO_PERFORM_THIS_OPERATION_DOT = "Not Authroized to perform this operation";
	public static final String ENCODING = "encoding";
	public static final String ENCODING_FILTER = "encoding-filter";
	public static final String FORCE_ENCODING = "forceEncoding";
	public static final String THROW_EXCEPTION_IF_NO_HANDLER_FOUND = "throwExceptionIfNoHandlerFound";
	public static final String UTF_8 = "UTF-8";
	public static final String UTF_8_SMALL = "utf-8";
	public static final String DISPATCHER = "dispatcher";
	public static final String TRUE = "true";
	public static final String USER_DETAILS = "userDetails";
	public static final String INVALID_USER = "Invalid_user";
	public static final String INVALID_EMAIL = "Invalid_email";
	public static final String APPLICATION_FORWARD_SLASH_JSON = "application/json";
	public static final String INVALID_PASWORD = "Invalid_password";
	public static final String DOWNLOAD_CSV_RESOURCE = "downloadCsvResource: ";

	public static final String DRIVER_ID_INVALID = "DRIVER.ID.INVALID";

	public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
	public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
	public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";

	public static final String WEB = "WEB";
	public static final String SOMETHING_WENT_WRONG = "Something went wrong";
	public static final String CLIENT_INPUT_ERROR = "CLIENT_INPUT_ERROR";
	public static final String FILENAME = "filename";
	public static final String UTC = "UTC";
	public static final String DATA_COLON = "data:";
	public static final String AP_SOUTH_EAST_1 = "ap-southeast-1";
	public static final String USER = "user";
	public static final String PASS = "pass";
	public static final String SID = "sid";
	public static final String JAVA_IO_TMPDIR = "java.io.tmpdir";
	public static final String FILE_SEPRATOR_UPLOADS_FILE_SEPRATOR = "/uploads/";
	public static final String DOT_ZIP = ".zip";
	public static final String DOT_CSV = ".csv";
	public static final String A_NEW_USER_HAS_USED_YOUR_REFERRAL_CODE_DOT = "A new user has used your referral code.";
	public static final String ERROR_IN_FIELD_S = "Error in field(s)";
	public static final String ERROR_UNDERSCORE = "error_";

	public static final String YOUR_INFORMATION_HAS_BEEN_UPDATED_SUCCESSFULLY_DOT = "Your information has been updated successfully.";
	public static final String INFORMATION_HAS_BEEN_SENT_TO_ADMIN_FOR_APPROVAL = "Information has been sent to admin for approval";
	public static final String YOUR_APPLICATION_HAS_BEEN_SENT_TO_ADMIN_FOR_APPROVAL_DOT = "Your application has been sent to admin for approval.";
	public static final String YOUR_RESPONSE_HAS_BEEN_RECORDED_SUCCESSFULLY = "Your response has been recorded successfully.";

	public static final String TRUCK_FITNESS_CERTIFICATE_PHOTO_CAMEL_FORMAT = "truckFitnessCertificatePhoto";
	public static final String TRUCK_INSURANCE_PHOTO_CAMEL_FORMAT = "truckInsurancePhoto";
	public static final String TRUCK_REGISTRATION_PHOTO_CAMEL_FORMAT = "truckRegistrationPhoto";
	public static final String TRUCK_ROUTE_PERMIT_PHOTO_CAMEL_FORMAT = "truckRoutePermitPhoto";
	public static final String TRUCK_TAX_TOKEN_PHOTO_CAMEL_FORMAT = "truckTaxTokenPhoto";
	public static final String TRUCK_BACK_WITH_NUMBER_PLATE_CAMEL_FORMAT = "truckBackWithNumberPlate";
	public static final String TRUCK_FRONT_WITH_NUMBER_PLATE_CAMEL_FORMAT = "truckFrontWithNumberPlate";
	public static final String TRUCK_FULL_CAMEL_FORMAT = "truckFull";
	public static final String TRUCK_INNER_DASHBOARD_CAMEL_FORMAT = "truckInnerDashboard";
	public static final String ADMIN = "Admin";

	public static final String YOU_HAVE_SUCCESSFULLY_DISASSOCIATED_DRIVER = "You have successfully disassociated driver";
	public static final String FLEETOWNER_DRIVER_ASSOCIATION_NOT_FOUND = "fleetowner.driver.association.not_found";
	public static final String FLEETOWNER_DRIVER_ASSOCIATION_NOT_APPROVED = "fleetowner.driver.association.not_approved";
	public static final String FLEETOWNER_DRIVER_ASSOCIATION_NOT_PENDING = "fleetowner.driver.association.not_pending";

	public static final String SEMI_COLON_BASE64_COMMA = ";base64,";
	public static final String TOKEN_HEADER = "authtoken";
	public static final String SECRET = "~sbA{SK^z(~7X%A";

	public static final String DAY_TH = "th";
	public static final String DAY_ST = "st";
	public static final String DAY_ND = "nd";
	public static final String DATE_D = "d";
	public static final String MONTH_MMM = "MMM";
	public static final String YEAR_YYYY_SMALL = "yyyy";
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String DATE_FORMAT_DD_MM_YYYY = "dd-MM-yyyy";
	public static final String DATE_FORMAT_SLASH = "dd/MM/yyyy";
	public static final String DATE_FORMAT_MONTH = "MM/dd/yyyy";
	public static final String FORMATTED_DATE = "yyyy-MM-dd hh:mm a";
	public static final String FORMATTED_DATE_24 = "yyyy-MM-dd HH:mm:ss";
	public static final String COMPARE_DATE_FORMAT = "dd/MM/yyyy HH:mm";
	public static final String MMDDYYYY_DATE_FORMAT = "MM/dd/yyyy HH:mm";
	public static final String TIME_FORMAT = "hh:mm:ss";
	public static final String TIME_ZONE = "timezone";

	public static final String EMPTY_STRING = null;
	public static final String BLANK_STRING = "";
	public static final String SPACE = " ";
	public static final String COMMA = ",";
	public static final String COMMA_SPACE = ", ";
	public static final String COLON = ":";
	public static final String SEMI_COLON = ";";
	public static final String DOT = ".";
	public static final String HYPHEN = "-";
	public static final String PERCENTILE = "%";
	public static final String FILE_SEPARATOR = "/";
	public static final String FILE_SEPARATOR_MULTIPLICATION = "/*";
	public static final String FILE_SEPARATOR_DOUBLE_MULTIPLICATION = "/**";
	public static final String MULTIPLICATION = "*";
	public static final String NEXT_LINE = "\n";
	public static final String TAB_KEY = "\t";

	public static final String EMAIL_ADDRESS_REGEX = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
	public static final String PASWORD_REGEX = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
	public static final String SPECIAL_CHARACTERS_REGEX = "[^\\w\\s]";
	public static final String SPECIAL_CHARACTERS_REGEX_2 = "[+\\-><\\(\\)~*\\\"@#]+";
	public static final String SCRIPTING_TAG_ALERT_REGEX = "(?!.*(<\\s*script|<\\s*alert)).*$";

	public static final String SMS_ARRAY_ZERO_ZERO = "sms[0][0]";
	public static final String SMS_ARRAY_ZERO_ONE = "sms[0][1]";
	public static final String SMS_ARRAY_ZERO_TWO = "sms[0][2]";
	public static final String ONE_TO_NINE = "123456789";
	public static final String ONE_TO_NINE_A_TO_Z_SMALL = "123456789abcdefghijklmnopqrstuvwxyz";
	public static final String SMS_BRACKET_OPEN = "sms[";
	public static final String BRACKET_CLOSE = "]";
	public static final String ARRAY_ZERO = "[0]";
	public static final String ARRAY_ONE = "[1]";
	public static final String ARRAY_TWO = "[2]";

	public static final String SUBJECT_ORG_WELCOME = "Welcome To Verda-Flow";
	public static final String SUBJECT_ORG_WELCOME_DISPATCHER = "Dispatcher Login Credentials - Verda-Flow";
	public static final String SUBJECT_ORG_WELCOME_DRIVER = "Driver Login Credentials - Verda-Flow";
	public static final String SUBJECT_ORG_ACTIVATE_DISPATCHER = "Dispatcher Activation - Verda-Flow";
	public static final String SUBJECT_ORG_ACTIVATE_DRIVER = "Driver Activation - Verda-Flow";
	public static final String SUBJECT_ORG_DEACTIVATE_DRIVER = "Driver De-activation - Verda-Flow";
	public static final String SUBJECT_ORG_DEACTIVATE_DISPATCHER = "Dispatcher De-activation - Verda-Flow";
	public static final String SUBJECT_ORG_WELCOME_CUSTOMER = "Customer Login Credentials - Verda-Flow";
	public static final String SUBJECT_ORG_ACTIVATE_CUSTOMER = "Customer Activation - Verda-Flow";
	public static final String SUBJECT_ORG_DEACTIVATE_CUSTOMER = "Customer De-activation - Verda-Flow";
	public static final String SUBJECT_ORG_RESET_PASWRD = "Reset Password Link - Verda-Flow";
	public static final String SUBJECT_ORG_REPORT_PROBLEM = "Report A Problem - Verda-Flow";
	public static final String EMAIL = "email";
	public static final String MOBILE_NUMBER = "mobileNumber";
	public static final String PASWRD = "paswrd";
	public static final String STORE_NAME = "storeName";
	public static final String MANAGER_NAME = "managerName";
	public static final String ADMIN_NAME = "adminName";
	public static final String NAME = "name";
	public static final String FIRST_NAME = "firstName";
	public static final String LAST_NAME = "lastName";
	public static final String MESSAGE = "message";
	public static final String TITLE = "title";
	public static final String ROLE = "role";
	public static final String RESET_PASS_LINK = "resetPassLink";
	public static final String BUSINESS_NAME = "businessName";
	public static final String BUSINESS_LOCATION = "businessLocation";
	public static final String APPROVAL_STATUS = "approvalStatus";
	public static final String SIGN_IN_LINK = "signInLink";
	public static final String MANAGER = "Manager";
	public static final String EMPLOYEE = "Employee";

	public static final String INFO_EMAIL_ID = "INFO_EMAIL_ID";

	public static final String CREATE_CATEGORY_SUCCESS = "CREATE.CATEGORY.SUCCESS";
	public static final String UPDATE_CATEGORY_SUCCESS = "UPDATE.CATEGORY.SUCCESS";
	public static final String DELETE_CATEGORY_SUCCESS = "DELETE.CATEGORY.SUCCESS";
	public static final String CATEGORY_ALREADY_EXIST = "CATEGORY.ALREADY.EXIST";
	public static final String CATEGORY_NOT_FOUND = "CATEGORY.NOT.FOUND";
	public static final String UPDATE_CATEGORY_ID_INVALID = "UPDATE.CATEGORY.ID.INVALID";

	public static final String CREATE_EFFECT_SUCCESS = "CREATE.EFFECT.SUCCESS";
	public static final String UPDATE_EFFECT_SUCCESS = "UPDATE.EFFECT.SUCCESS";
	public static final String DELETE_EFFECT_SUCCESS = "DELETE.EFFECT.SUCCESS";
	public static final String EFFECT_ALREADY_EXIST = "EFFECT.ALREADY.EXIST";
	public static final String EFFECT_NOT_FOUND = "EFFECT.NOT.FOUND";
	public static final String UPDATE_EFFECT_ID_INVALID = "UPDATE.EFFECT.ID.INVALID";

	public static final String ADD_PRODUCT_SUCCESS = "ADD.PRODUCT.SUCCESS";
	public static final String UPDATE_PRODUCT_SUCCESS = "UPDATE.PRODUCT.SUCCESS";
	public static final String DELETE_PRODUCT_SUCCESS = "DELETE.PRODUCT.SUCCESS";
	public static final String PRODUCT_ALREADY_EXIST = "PRODUCT.ALREADY.EXIST";
	public static final String PRODUCT_NOT_FOUND = "PRODUCT.NOT.FOUND";
	public static final String UPDATE_PRODUCT_ID_INVALID = "UPDATE.PRODUCT_ID.INVALID";
	public static final String GROUP_TYPE_INVALID = "GROUP.TYPE.INVALID";
	public static final String PRICE_TYPE_INVALID = "PRICE.TYPE.INVALID";
	public static final String CATEGORY_ID_INVALID = "CATEGORY.ID.INVALID";
	public static final String TYPE_ID_INVALID = "TYPE.ID.INVALID";
	public static final String PROMOCODE_INVALID = "PROMOCODE.INVALID";
	public static final String PRODUCT_ID_INVALID = "PRODUCT.ID.INVALID";

	public static final String CREATE_TYPE_SUCCESS = "CREATE.TYPE.SUCCESS";
	public static final String UPDATE_TYPE_SUCCESS = "UPDATE.TYPE.SUCCESS";
	public static final String DELETE_TYPE_SUCCESS = "DELETE.TYPE.SUCCESS";
	public static final String TYPE_ALREADY_EXIST = "TYPE.ALREADY.EXIST";
	public static final String TYPE_NOT_FOUND = "TYPE.NOT.FOUND";
	public static final String UPDATE_TYPE_ID_INVALID = "UPDATE.TYPE.ID.INVALID";

	public static final String CREATE_ETA_SUCCESS = "CREATE.ETA.SUCCESS";
	public static final String UPDATE_ETA_SUCCESS = "UPDATE.ETA.SUCCESS";
	public static final String DELETE_ETA_SUCCESS = "DELETE.ETA.SUCCESS";
	public static final String ETA_ALREADY_EXIST = "ETA.ALREADY.EXIST";
	public static final String ETA_NOT_FOUND = "ETA.NOT.FOUND";
	public static final String UPDATE_ETA_ID_INVALID = "UPDATE.ETA.ID.INVALID";

	public static final String CREATE_PROMOCODE_SUCCESS = "CREATE.PROMOCODE.SUCCESS";
	public static final String UPDATE_PROMOCODE_SUCCESS = "UPDATE.PROMOCODE.SUCCESS";
	public static final String DELETE_PROMOCODE_SUCCESS = "DELETE.PROMOCODE.SUCCESS";
	public static final String PROMOCODE_ALREADY_EXIST = "PROMOCODE.ALREADY.EXIST";
	public static final String PROMOCODE_NOT_FOUND = "PROMOCODE.NOT.FOUND";
	public static final String PROMOCODE_TYPE_INVALID = "PROMOCODE.TYPE.INVALID";
	public static final String UPDATE_PROMOCODE_ID_INVALID = "UPDATE.PROMOCODE_ID.INVALID";
	public static final String PROMOCODE_DATE_CURRENT_OR_FUTURE_INVALID = "PROMOCODE.DATE.CURRENT_OR_FUTURE.INAVLID";
	public static final String PROMOCODE_END_DATE_NOT_BEFORE_START_DATE = "PROMOCODE.END_DATE.NOT_BEFORE.START_DATE";
	public static final String PROMOCODE_EXPIRED = "PROMOCODE.EXPIRED";

	public static final String CREATE_ADDRESS_SUCCESS = "CREATE.ADDRESS.SUCCESS";
	public static final String UPDATE_ADDRESS_SUCCESS = "UPDATE.ADDRESS.SUCCESS";
	public static final String DELETE_ADDRESS_SUCCESS = "DELETE.ADDRESS.SUCCESS";
	public static final String ADDRESS_ALREADY_EXIST = "ADDRESS.ALREADY.EXIST";
	public static final String ADDRESS_NOT_FOUND = "ADDRESS.NOT.FOUND";
	public static final String UPDATE_ADDRESS_ID_INVALID = "UPDATE.ADDRESS.ID.INVALID";

	public static final String DISPATCHER_DEACTIVATED_SUCCESS = "DISPATCHER.DEACTIVATED.SUCCESS";
	public static final String DISPATCHER_ACTIVATED_SUCCESS = "DISPATCHER.ACTIVATED.SUCCESS";
	public static final String DRIVER_DEACTIVATED_SUCCESS = "DRIVER.DEACTIVATED.SUCCESS";
	public static final String DRIVER_ACTIVATED_SUCCESS = "DRIVER.ACTIVATED.SUCCESS";
	public static final String DRIVER_DEACTIVATE_REJECT = "DRIVER.DEACTIVATE.REJECT";
	public static final String CUSTOMER_DEACTIVATED_SUCCESS = "CUSTOMER.DEACTIVATED.SUCCESS";
	public static final String CUSTOMER_ACTIVATED_SUCCESS = "CUSTOMER.ACTIVATED.SUCCESS";

	public static final String DRIVER_ONLINE_SUCCESS = "DRIVER.ONLINE.SUCCESS";
	public static final String DRIVER_OFFLINE_SUCCESS = "DRIVER.OFFLINE.SUCCESS";

	public static final String REPORT_PROBLEM_SUCCESS = "REPORT.PROBLEM.SUCCESS";
	public static final String PUSH_NOTIFICATION_DISABLED_SUCCESS = "PUSH_NOTIFICATION.DISABLED.SUCCESS";
	public static final String PUSH_NOTIFICATION_ENABLED_SUCCESS = "PUSH_NOTIFICATION.ENABLED.SUCCESS";

	public static final String CUSTOMER_DETAIL_NOT_FOUND = "customer_detail.not_found";
	public static final String ADDRESS_NOT_BELONGS_TO_YOU = "ADDRESS.NOT.BELONGS.TO.YOU";

	public static final String DISPATCHER_FAVORITE_SUCCESSFULLY = "DISPATCHER.FAVORITE.SUCCESSFULLY";
	public static final String DISPATCHER_UNFAVORITE_SUCCESSFULLY = "DISPATCHER.UNFAVORITE.SUCCESSFULLY";
	public static final String DISPATCHER_UNFAVORITE_REJECT = "DISPATCHER.UNFAVORITE.REJECT";
	public static final String DISPATCHER_FAVORITE_REJECT = "DISPATCHER.FAVORITE.REJECT";
	public static final String PRODUCT_FAVORITE_SUCCESSFULLY = "PRODUCT.FAVORITE.SUCCESSFULLY";
	public static final String PRODUCT_UNFAVORITE_SUCCESSFULLY = "PRODUCT.UNFAVORITE.SUCCESSFULLY";
	public static final String PRODUCT_FAVORITE_REJECT = "PRODUCT.FAVORITE.REJECT";
	public static final String PRODUCT_UNFAVORITE_REJECT = "PRODUCT.UNFAVORITE.REJECT";

	public static final String PRODUCT_PRICE_DETAIL_NOT_FOUND = "PRODUCT.PRICE.DETAIL.NOT.FOUND";

	public static final String ADD_TO_CART_SUCCESS = "ADD.TO.CART.SUCCESS";
	public static final String DELETE_FROM_CART_SUCCESS = "DELETE.FROM.CART.SUCCESS";
	public static final String CART_DETAIL_ALREADY_EXIST = "CART.DETAIL.ALREADY.EXIST";
	public static final String CART_DETAIL_NOT_FOUND = "CART.DETAIL.NOT.FOUND";
	public static final String CART_ALREADY_EMPTY = "CART.ALREADY.EMPTY";
	public static final String UPDATE_CART_DETAIL_SUCCESS = "UPDATE.CART.DETAIL.SUCCESS";
	public static final String ADD_TO_CART_REJECT = "ADD.TO.CART.REJECT";
	public static final String EMPTY_CART_SUCCESS = "EMPTY.CART.SUCCESS";
	public static final String UPDATE_CART_ID_INVALID = "UPDATE.CART.ID.INVALID";

	public static final String ASSIGN_ORDER_SUCCESS = "ASSIGN.ORDER.SUCCESS";
	public static final String ORDER_DETAIL_NOT_FOUND = "ORDER.DETAIL.NOT.FOUND";
	public static final String DELIVERY_TYPE_INVALID = "DELIVERY.TYPE.INVALID";
	public static final String DELIVERY_CHARGES_NOT_APPLICABLE_ON_PICKUP = "DELIVERY_CHARGES.NOT_APPLICABLE.ON.PICKUP";
	public static final String PLACE_ORDER_SUCCESSFULLY = "PLACE.ORDER.SUCCESSFULLY";
	public static final String PLACE_ORDER_DISPATCHER_DEACTIVATED = "PLACE.ORDER.DISPATCHER.DEACTIVATED";
	public static final String NO_PRODUCT_FOUND_IN_ORDER = "NO.PRODUCT.FOUND.IN.ORDER";
	public static final String NO_ORDER_PRICE_DETAIL_FOUND_IN_ORDER = "NO.ORDER.PRICE.DETAIL.PRODUCT.FOUND.IN.ORDER";
	public static final String ORDER_CANCEL_EXPIRY_MINUTES = "ORDER_CANCEL_EXPIRY_MINUTES";
	public static final String ORDER_CANCELLED_SUCCESSFULLY = "CANCELLED.ORDER.SUCCESSFULLY";
	public static final String ORDER_CANCELLED_REJECT = "ORDER.CANCELLED.REJECT";
	public static final String ORDER_CONFIRMED_SUCCESSFULLY = "ORDER.CONFIRMED.SUCCESSFULLY";
	public static final String ORDER_CONFIRMED_REJECT = "ORDER.CONFIRMED.REJECT";
	public static final String ORDER_PREPARED_SUCCESSFULLY = "ORDER.PREPARED.SUCCESSFULLY";
	public static final String ORDER_PREPARED_REJECT = "ORDER.PREPARED.REJECT";
	public static final String ORDER_ALREADY_ASSIGNED = "ORDER.ALREADY.ASSIGNED";
	public static final String ORDER_ASSIGNED_DELIVERY_TYPE_REJECT = "ORDER.ASSIGNED.DELIVERY_TYPE.REJECT";
	public static final String ORDER_ASSIGNED_STATUS_REJECT = "ORDER.ASSIGNED.STATUS.REJECT";
	public static final String ORDER_ASSIGNED_SAME_DRIVER_REJECT = "ORDER.ASSIGNED.SAME_DRIVER.REJECT";
	public static final String ORDER_COMPLETED_SUCCESSFULLY = "ORDER.COMPLETED.SUCCESSFULLY";
	public static final String ORDER_COMPLETED_REJECT = "ORDER.COMPLETED.REJECT";
	public static final String ORDER_ACCEPT_DRIVER_SUCCESSFULLY = "ORDER.ACCEPT.DRIVER.SUCCESSFULLY";
	public static final String ORDER_REJECT_DRIVER_SUCCESSFULLY = "ORDER.REJECT.DRIVER.SUCCESSFULLY";
	public static final String ORDER_ACCEPT_DRIVER_REJECT = "ORDER.ACCEPT.DRIVER.REJECT";
	public static final String ORDER_REJECT_DRIVER_REJECT = "ORDER.REJECT.DRIVER.REJECT";
	public static final String ORDER_REASSIGNED_SUCCESSFULLY = "ORDER.REASSIGNED.SUCCESSFULLY";
	public static final String RATING_INVALID = "RATING.INVALID";
	public static final String RATING_SUBMIT_SUCCESS = "RATING.SUBMIT.SUCCESS";
	public static final String RATING_SUBMIT_REJECT = "RATING.SUBMIT.REJECT";
	public static final String ALL_NOTIFICATIONS_READ_SUCCESSFULLY = "ALL_NOTIFICATIONS.READ.SUCCESSFULLY";
	public static final String NOTIFICATION_READ_SUCCESSFULLY = "NOTIFICATION.READ.SUCCESSFULLY";
	public static final String NOTIFICATION_NOT_FOUND = "NOTIFICATION.NOT.FOUND";

	public static final String TAX = "TAX";
	public static final String ORDER_FAVORITE_SUCCESSFULLY = "ORDER.FAVORITE.SUCCESSFULLY";
	public static final String ORDER_UNFAVORITE_SUCCESSFULLY = "ORDER.UNFAVORITE.SUCCESSFULLY";
	public static final String ORDER_UNFAVORITE_REJECT = "ORDER.UNFAVORITE.REJECT";
	public static final String ORDER_FAVORITE_REJECT = "ORDER.FAVORITE.REJECT";
	public static final String NO_PRODUCT_FOUND_FOR_PROMOCODE = "NO.PRODUCT.FOUND.FOR.PROMOCODE";
	public static final String PROMOCODE_APPLIED_SUCCESSFULLY = "PROMOCODE.APPLIED.SUCCESSFULLY";
	public static final String ADDRESS_SET_DEFAULT_SUCCESSFULLY = "ADDRESS.SET.DEFAULT.SUCCESSFULLY";
	public static final String UPDATE_ORDER_ETA = "UPDATE.ORDER.ETA";
	public static final String DISPATCHER_DEACTIVATE_REJECT = "DISPATCHER.DEACTIVATE.REJECT";
	public static final String CART_COUNT_MAX = "CART_COUNT_MAX";
	public static final String ADD_TO_CART_LIMIT_EXCEED = "ADD.TO.CART.LIMIT.EXCEED";

	public static final String ADD_OR_UPDATE_DRIVER_INVENTORY_SUCCESS = "ADD.OR.UPDATE.DRIVER.INVENTORY.SUCCESS";

	public static final String YEAR_CANNOT_BE_FUTURE = "YEAR.CANNOT.BE.FUTURE";
	public static final String START_DATEYEAR_OR_END_DATEYEAR_CANNOT_BE_DIFFERENT = "START.DATEYEAR.OR.END.DATEYEAR.CANNOT.BE.DIFFERENT";

	public static final String NOTIFICATION_TITLE_ORDER_PLACED = "NOTIFICATION.TITLE.ORDER.PLACED";
	public static final String NOTIFICATION_BODY_ORDER_PLACED = "NOTIFICATION.BODY.ORDER.PLACED";
	public static final String NOTIFICATION_BODY_FORMATTED_ORDER_PLACED = "NOTIFICATION.BODY.FORMATTED.ORDER.PLACED";
	public static final String NOTIFICATION_TITLE_ORDER_CONFIRMED = "NOTIFICATION.TITLE.ORDER.CONFIRMED";
	public static final String NOTIFICATION_BODY_ORDER_CONFIRMED = "NOTIFICATION.BODY.ORDER.CONFIRMED";
	public static final String NOTIFICATION_BODY_FORMATTED_ORDER_CONFIRMED = "NOTIFICATION.BODY.FORMATTED.ORDER.CONFIRMED";
	public static final String NOTIFICATION_TITLE_ORDER_PREPARED = "NOTIFICATION.TITLE.ORDER.PREPARED";
	public static final String NOTIFICATION_BODY_ORDER_PREPARED_PICKUP = "NOTIFICATION.BODY.ORDER.PREPARED.PICKUP";
	public static final String NOTIFICATION_BODY_FORMATTED_ORDER_PREPARED_PICKUP = "NOTIFICATION.BODY.FORMATTED.ORDER.PREPARED.PICKUP";
	public static final String NOTIFICATION_BODY_ORDER_PREPARED_DELIVERY = "NOTIFICATION.BODY.ORDER.PREPARED.DELIVERY";
	public static final String NOTIFICATION_BODY_FORMATTED_ORDER_PREPARED_DELIVERY = "NOTIFICATION.BODY.FORMATTED.ORDER.PREPARED.DELIVERY";
	public static final String NOTIFICATION_TITLE_ORDER_DRIVER_ASSIGNED = "NOTIFICATION.TITLE.ORDER.DRIVER.ASSIGNED";
	public static final String NOTIFICATION_BODY_ORDER_DRIVER_ASSIGNED = "NOTIFICATION.BODY.ORDER.DRIVER.ASSIGNED";
	public static final String NOTIFICATION_BODY_FORMATTED_DRIVER_ASSIGNED = "NOTIFICATION.BODY.FORMATTED.DRIVER.ASSIGNED";
	public static final String NOTIFICATION_TITLE_ORDER_REASSIGN_DRIVER = "NOTIFICATION.TITLE.ORDER.REASSIGN.DRIVER";
	public static final String NOTIFICATION_BODY_ORDER_REASSIGN_DRIVER = "NOTIFICATION.BODY.ORDER.REASSIGN.DRIVER";
	public static final String NOTIFICATION_BODY_FORMATTED_ORDER_REASSIGN_DRIVER = "NOTIFICATION.BODY.FORMATTED.ORDER.REASSIGN.DRIVER";
	public static final String NOTIFICATION_TITLE_ORDER_OUT_FOR_DELIVERY_DISPATCHER = "NOTIFICATION.TITLE.ORDER.OUT.FOR.DELIVERY.DISPATCHER";
	public static final String NOTIFICATION_BODY_ORDER_OUT_FOR_DELIVERY_DISPATCHER = "NOTIFICATION.BODY.ORDER.OUT.FOR.DELIVERY.DISPATCHER";
	public static final String NOTIFICATION_BODY_FORMATTED_ORDER_OUT_FOR_DELIVERY_DISPATCHER = "NOTIFICATION.BODY.FORMATTED.ORDER.OUT.FOR.DELIVERY.DISPATCHER";
	public static final String NOTIFICATION_TITLE_ORDER_OUT_FOR_DELIVERY_CUSTOMER = "NOTIFICATION.TITLE.ORDER.OUT.FOR.DELIVERY.CUSTOMER";
	public static final String NOTIFICATION_BODY_ORDER_OUT_FOR_DELIVERY_CUSTOMER = "NOTIFICATION.BODY.ORDER.OUT.FOR.DELIVERY.CUSTOMER";
	public static final String NOTIFICATION_BODY_FORMATTED_ORDER_OUT_FOR_DELIVERY_CUSTOMER = "NOTIFICATION.BODY.FORMATTED.ORDER.OUT.FOR.DELIVERY.CUSTOMER";
	public static final String NOTIFICATION_TITLE_ORDER_COMPLETED_PICKUP = "NOTIFICATION.TITLE.ORDER.COMPLETED.PICKUP";
	public static final String NOTIFICATION_BODY_ORDER_COMPLETED_PICKUP = "NOTIFICATION.BODY.ORDER.COMPLETED.PICKUP";
	public static final String NOTIFICATION_BODY_FORMATTED_ORDER_COMPLETED_PICKUP = "NOTIFICATION.BODY.FORMATTED.ORDER.COMPLETED.PICKUP";
	public static final String NOTIFICATION_TITLE_ORDER_COMPLETED_DELIVERY_DISPATCHER = "NOTIFICATION.TITLE.ORDER.COMPLETED.DELIVERY.DISPATCHER";
	public static final String NOTIFICATION_BODY_ORDER_COMPLETED_DELIVERY_DISPATCHER = "NOTIFICATION.BODY.ORDER.COMPLETED.DELIVERY.DISPATCHER";
	public static final String NOTIFICATION_BODY_FORMATTED_ORDER_COMPLETED_DELIVERY_DISPATCHER = "NOTIFICATION.BODY.FORMATTED.ORDER.COMPLETED.DELIVERY.DISPATCHER";
	public static final String NOTIFICATION_TITLE_ORDER_COMPLETED_DELIVERY_CUSTOMER = "NOTIFICATION.TITLE.ORDER.COMPLETED.DELIVERY.CUSTOMER";
	public static final String NOTIFICATION_BODY_ORDER_COMPLETED_DELIVERY_CUSTOMER = "NOTIFICATION.BODY.ORDER.COMPLETED.DELIVERY.CUSTOMER";
	public static final String NOTIFICATION_BODY_FORMATTED_ORDER_COMPLETED_DELIVERY_CUSTOMER = "NOTIFICATION.BODY.FORMATTED.ORDER.COMPLETED.DELIVERY.CUSTOMER";
	public static final String NOTIFICATION_TITLE_ORDER_CANCELLED_BY_STORE = "NOTIFICATION.TITLE.ORDER.CANCELLED.BY.STORE";
	public static final String NOTIFICATION_BODY_ORDER_CANCELLED_BY_STORE = "NOTIFICATION.BODY.ORDER.CANCELLED.BY.STORE";
	public static final String NOTIFICATION_BODY_FORMATTED_ORDER_CANCELLED_BY_STORE = "NOTIFICATION.BODY.FORMATTED.ORDER.CANCELLED.BY.STORE";

}