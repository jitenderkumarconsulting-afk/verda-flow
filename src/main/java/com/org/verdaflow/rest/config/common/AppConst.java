package com.org.verdaflow.rest.config.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public interface AppConst {

	int EXPIRATION = 60400800;

	int MAX_SEARCH_THREAD = 10;
	int PASSWORD_LENGTH = 8;
	int HTTP_STATUS_OK = 200;

	double DOUBLE_ZERO = 0.0;
	float FLOAT_ZERO = 0.0f;
	Object OBJECT_NULL = null;
	public static ObjectNode nullObj = new ObjectMapper().createObjectNode();

	short OTP_LENGTH = 6;
	public static final int MIN_30 = 1800;
	public static final int DEFAULT_CREDITS = 25;
	public static final int TIME_TO_EDIT_EVENT = 24;

	class NUMBER { // NOSONAR
		public static final int ZERO = 0;
		public static final int ONE = 1;
		public static final int TWO = 2;
		public static final int THREE = 3;
		public static final int FOUR = 4;
		public static final int FIVE = 5;
		public static final int SIX = 6;
		public static final int SEVEN = 7;
		public static final int EIGHT = 8;
		public static final int NINE = 9;
		public static final int TEN = 10;
		public static final int ELE = 11;
		public static final int TWE = 12;
		public static final int THIR = 13;
		public static final int FOURTEEN = 14;
		public static final int FIFTEEN = 15;
		public static final int SIXTEEN = 16;
		public static final int SEVENTEEN = 17;
		public static final int NINTEEN = 19;
		public static final int TWENTYFOUR = 24;
		public static final int HUNDRED = 100;

	}

	class ERROR { // NOSONAR
		public static final int SUCCESS = 200;
		public static final int PLEASE_ENTER_VALID_USERNAME_AND_PASSWORD = 108;
		public static final int EMAIL_ALREADY_EXIST = 109;
		public static final int EMAIL_DOES_NOT_EXIST = 110;
		public static final int BAD_REQUEST = 400;
	}

	class EXCEPTION_CAT { // NOSONAR
		public static final String UNAUTHORIZED_401 = "UNAUTHORIZED";
		public static final String PAYMENT_REQUIRED_402 = "PAYMENT_REQUIRED";
		public static final String NOT_FOUND_404 = "NOT_FOUND";
		public static final String METHOD_NOT_ALLOWED_405 = "METHOD_NOT_ALLOWED";
		public static final String NOT_ACCEPTABLE_406 = "NOT_ACCEPTABLE";
		public static final String PRECONDITION_FAILED_412 = "PRECONDITION_FAILED";
		public static final String PAYLOAD_TOO_LARGE_413 = "PAYLOAD_TOO_LARGE";
		public static final String UNSUPPORTED_MEDIA_TYPE_415 = "UNSUPPORTED_MEDIA_TYPE";
		public static final String BAD_REQUEST_400 = "BAD_REQUEST";

	}

	// URLs for validating access-tokens.
	class ACCESS_TOKEN_URL { // NOSONAR
		public static final String FACEBOOK = "https://graph.facebook.com/me";
		public static final String INSTAGRAM = "https://api.instagram.com/v1/users/self";
	}

	class USER_ROLE { // NOSONAR
		public static final int ADMIN = NUMBER.ONE;
		public static final int DISPATCHER = NUMBER.TWO;
		public static final int DRIVER = NUMBER.THREE;
		public static final int CUSTOMER = NUMBER.FOUR;
	}

	enum UPLOAD_TYPE { // NOSONAR
		DISPATCHER, DRIVER, CUSTOMER, PRODUCT
	}

	enum IMAGE_TYPE { // NOSONAR
		ORIGINAL, THUMB
	}

	class PRODUCT_GROUP { // NOSONAR
		public static final int VAPE_OIL_OR_CARTRIDGES = NUMBER.ONE;
		public static final int EDIBLES = NUMBER.TWO;
	}

}
