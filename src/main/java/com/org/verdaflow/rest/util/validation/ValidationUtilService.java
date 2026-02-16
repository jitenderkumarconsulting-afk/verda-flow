package com.org.verdaflow.rest.util.validation;

import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.org.verdaflow.rest.config.common.StringConst;

@Service
public class ValidationUtilService {

	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile(StringConst.EMAIL_ADDRESS_REGEX,
			Pattern.CASE_INSENSITIVE);

	public static final Pattern VALID_PASSWORD_REGEX = Pattern.compile(StringConst.PASWORD_REGEX,
			Pattern.CASE_INSENSITIVE);

	public boolean isEmpty(String input) {
		return input != null && !input.isEmpty();
	}

	public boolean isEmail(String email) {
		return VALID_EMAIL_ADDRESS_REGEX.matcher(email).find();
	}
}
