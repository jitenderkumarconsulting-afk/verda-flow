package com.org.verdaflow.rest.exception.handler;

import java.sql.SQLException;
import java.util.List;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.org.verdaflow.rest.config.common.AppConst;
import com.org.verdaflow.rest.config.common.StringConst;
import com.org.verdaflow.rest.dto.ResponseEnvelope;
import com.org.verdaflow.rest.error.AppException;
import com.org.verdaflow.rest.error.RestErrorConstants;
import com.org.verdaflow.rest.util.AppUtil;

@ControllerAdvice
@PropertySource({ "classpath:messages_en.properties" })
public class GlobalExceptionHandler {

	@Autowired
	private Environment env;

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler
	@ResponseBody
	public ResponseEnvelope<ObjectNode> handleServerError(Exception e) {
		logger.error(StringConst.SERVER_ERROR_HYPHEN, e);
		return new ResponseEnvelope<>(new ObjectMapper().createObjectNode(), false, StringConst.SOMETHING_WENT_WRONG,
				HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	@SuppressWarnings("rawtypes")
	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler
	@ResponseBody
	public ResponseEntity<ResponseEnvelope> handleSQLServerError(SQLException e) {
		logger.error(StringConst.SQL_ERROR_HYPHEN, e.getMessage());
		String message = e.getLocalizedMessage();
		ResponseEnvelope<ObjectNode> responseEnvelope = new ResponseEnvelope<>(new ObjectMapper().createObjectNode(),
				false, message, 600);
		return new ResponseEntity<>(responseEnvelope, HttpStatus.OK);
	}

	@SuppressWarnings("rawtypes")
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(JSONException.class)
	public ResponseEntity handleJsonServerError(JSONException e) {
		logger.error(StringConst.JSON_ERROR_HYPHEN, e);
		ResponseEnvelope responseEnvelope = new ResponseEnvelope<>(AppUtil.nullObj, false,
				StringConst.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<>(responseEnvelope, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@SuppressWarnings("rawtypes")
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity HttpMediaTypeNotSupportError(HttpMediaTypeNotSupportedException e) {// NOSONAR
		logger.error(StringConst.HTTP_MEDIATYPE_NOT_SUPPORTED_EXCEP_ERROR_HYPHEN, e);
		ResponseEnvelope responseEnvelope = new ResponseEnvelope<>(AppUtil.nullObj, false,
				StringConst.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<>(responseEnvelope, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@SuppressWarnings("rawtypes")
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity handleJsonJmxServerError(HttpMessageNotReadableException e) {
		logger.error(StringConst.HTTP_MESSAGE_NOT_READ_EXCEP_HYPHEN, e);
		ResponseEnvelope responseEnvelope = new ResponseEnvelope<>(AppUtil.nullObj, false,
				StringConst.CLIENT_INPUT_ERROR, HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(responseEnvelope, HttpStatus.BAD_REQUEST);
	}

	@SuppressWarnings("rawtypes")
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity handleJsonJmxServerError(MethodArgumentTypeMismatchException e) {
		logger.error(StringConst.METHOD_ARGUMENT_TYPE_MISMATCH_EXCEPTION_HYPHEN, e);
		ResponseEnvelope responseEnvelope = new ResponseEnvelope<>(AppUtil.nullObj, false, "Required '"
				+ e.getRequiredType().getName() + "' parameter '" + e.getName() + "' is in 'INCORRECT' format",
				HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(responseEnvelope, HttpStatus.BAD_REQUEST);
	}

	@SuppressWarnings("rawtypes")
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity handleJsonJmxServerError(IllegalArgumentException e) {
		logger.error(StringConst.ILLEGAL_ARGUMENT_EXCEPTION_HYPHEN, e);
		ResponseEnvelope responseEnvelope = new ResponseEnvelope<>(AppUtil.nullObj, false, e.getMessage(),
				HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(responseEnvelope, HttpStatus.BAD_REQUEST);
	}

	@SuppressWarnings("rawtypes")
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidDataAccessResourceUsageException.class)
	public ResponseEntity handleJsonJmxServerError(InvalidDataAccessResourceUsageException e) {
		logger.error(StringConst.INVALID_DATA_ACCESS_RESOURCES_USAGE_EXCEPTION, e);
		ResponseEnvelope responseEnvelope = new ResponseEnvelope<>(AppUtil.nullObj, false,
				StringConst.CLIENT_INPUT_ERROR, HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(responseEnvelope, HttpStatus.BAD_REQUEST);
	}

	@SuppressWarnings("rawtypes")
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity handleJsonJmxServerError(MissingServletRequestParameterException e) {
		logger.error(StringConst.MISSING_SERVLET_REQUEST_PARAM_EXCEPTION_HYPHEN, e);
		ResponseEnvelope responseEnvelope = new ResponseEnvelope<>(AppUtil.nullObj, false,
				"Required '" + e.getParameterType() + "' parameter '" + e.getParameterName() + "' is not 'PRESENT'",
				HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(responseEnvelope, HttpStatus.BAD_REQUEST);
	}

	@SuppressWarnings("rawtypes")
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity handleJsonJmxServerError(HttpRequestMethodNotSupportedException e) {
		logger.error(StringConst.HTTP_REQUEST_METHOD_NOT_SUPPORTED_EXCEPTION_HYPHEN, e);
		ResponseEnvelope responseEnvelope = new ResponseEnvelope<>(AppUtil.nullObj, false,
				"Required method '" + e.getMethod() + "' not supported.", HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(responseEnvelope, HttpStatus.BAD_REQUEST);
	}

	@SuppressWarnings("rawtypes")
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler
	public ResponseEntity<ResponseEnvelope> handle404Error(NoHandlerFoundException e) {
		logger.error(StringConst.NO_HANDLER_FOUND_HYPHEN, e.getMessage());
		String message = e.getLocalizedMessage();
		ResponseEnvelope<ObjectNode> responseEnvelope = new ResponseEnvelope<>(new ObjectMapper().createObjectNode(),
				false, message, HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(responseEnvelope, HttpStatus.NOT_FOUND);
	}

	@SuppressWarnings("rawtypes")
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler
	public ResponseEntity<ResponseEnvelope> handleBindError(MethodArgumentNotValidException e) {
		logger.error(StringConst.VALIDATION_FAILED_EXCEPTION_HYPHEN, e.getMessage());
		List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
		StringBuilder errorMessageBuilder = new StringBuilder();
		for (FieldError error : fieldErrors) {
			String field = error.getField();
			String defaultMessage = error.getDefaultMessage();
			errorMessageBuilder.append(field).append(StringConst.COLON).append(defaultMessage);
			errorMessageBuilder.append(StringConst.COMMA);
		}
		String message = errorMessageBuilder.toString().replaceAll(",$", "");

		ResponseEnvelope<ObjectNode> responseEnvelope = new ResponseEnvelope<>(new ObjectMapper().createObjectNode(),
				false, message, HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(responseEnvelope, HttpStatus.BAD_REQUEST);
	}

	@SuppressWarnings("rawtypes")
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler
	public ResponseEntity<ResponseEnvelope> handleBindError(BindException e) {
		logger.error(StringConst.PARAM_BINDING_EXCEPTION_HYPHEN, e.getMessage());
		List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
		StringBuilder errorMessageBuilder = new StringBuilder();
		for (FieldError error : fieldErrors) {
			errorMessageBuilder.append(error.getField()).append(StringConst.COLON).append(error.getDefaultMessage());
			errorMessageBuilder.append(StringConst.COMMA);
		}
		String message = errorMessageBuilder.toString().replaceAll(",$", "");
		return new ResponseEntity<>(
				new ResponseEnvelope<ObjectNode>(AppUtil.nullObj, false, message, HttpStatus.BAD_REQUEST.value()),
				HttpStatus.BAD_REQUEST);
	}

	@SuppressWarnings("rawtypes")
	@ExceptionHandler(AppException.class)
	@ResponseBody
	public ResponseEntity<ResponseEnvelope> handleServerError(AppException e) {
		logger.error(StringConst.APP_EXCEPTION_BRACES, e.getErrorCode());
		String message = env.getProperty(e.getErrorCode());
		if (message == null) {
			message = e.getErrorCode();
		}

		if (e.getExceptionCat().equals(AppConst.EXCEPTION_CAT.BAD_REQUEST_400)) {
			return new ResponseEntity<>(
					new ResponseEnvelope<ObjectNode>(AppUtil.nullObj, false, message, HttpStatus.BAD_REQUEST.value()),
					HttpStatus.BAD_REQUEST);
		}

		if (e.getExceptionCat().equals(AppConst.EXCEPTION_CAT.UNAUTHORIZED_401)) {
			return new ResponseEntity<>(
					new ResponseEnvelope<ObjectNode>(AppUtil.nullObj, false, message, HttpStatus.UNAUTHORIZED.value()),
					HttpStatus.UNAUTHORIZED);
		}

		if (e.getExceptionCat().equals(AppConst.EXCEPTION_CAT.METHOD_NOT_ALLOWED_405)) {
			return new ResponseEntity<>(new ResponseEnvelope<ObjectNode>(AppUtil.nullObj, false, message,
					HttpStatus.METHOD_NOT_ALLOWED.value()), HttpStatus.METHOD_NOT_ALLOWED);
		}
		if (e.getExceptionCat().equals(AppConst.EXCEPTION_CAT.NOT_ACCEPTABLE_406)) {
			return new ResponseEntity<>(new ResponseEnvelope<ObjectNode>(AppUtil.nullObj, false, message,
					HttpStatus.NOT_ACCEPTABLE.value()), HttpStatus.NOT_ACCEPTABLE);
		}

		if (e.getExceptionCat().equals(AppConst.EXCEPTION_CAT.NOT_FOUND_404)) {
			return new ResponseEntity<>(
					new ResponseEnvelope<ObjectNode>(AppUtil.nullObj, false, message, HttpStatus.NOT_FOUND.value()),
					HttpStatus.NOT_FOUND);
		}

		if (e.getExceptionCat().equals(AppConst.EXCEPTION_CAT.PAYLOAD_TOO_LARGE_413)) {
			return new ResponseEntity<>(new ResponseEnvelope<ObjectNode>(AppUtil.nullObj, false, message,
					HttpStatus.PAYLOAD_TOO_LARGE.value()), HttpStatus.PAYLOAD_TOO_LARGE);
		}

		if (e.getExceptionCat().equals(AppConst.EXCEPTION_CAT.PAYMENT_REQUIRED_402)) {
			return new ResponseEntity<>(new ResponseEnvelope<ObjectNode>(AppUtil.nullObj, false, message,
					HttpStatus.PAYMENT_REQUIRED.value()), HttpStatus.PAYMENT_REQUIRED);
		}

		if (e.getExceptionCat().equals(AppConst.EXCEPTION_CAT.PRECONDITION_FAILED_412)) {
			return new ResponseEntity<>(new ResponseEnvelope<ObjectNode>(AppUtil.nullObj, false, message,
					HttpStatus.PRECONDITION_FAILED.value()), HttpStatus.PRECONDITION_REQUIRED);
		}

		if (e.getExceptionCat().equals(AppConst.EXCEPTION_CAT.UNSUPPORTED_MEDIA_TYPE_415)) {
			return new ResponseEntity<>(new ResponseEnvelope<ObjectNode>(AppUtil.nullObj, false, message,
					HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
		}
		return new ResponseEntity<>(
				new ResponseEnvelope<ObjectNode>(AppUtil.nullObj, false, message, HttpStatus.BAD_REQUEST.value()),
				HttpStatus.BAD_REQUEST);
	}

	@SuppressWarnings("rawtypes")
	@ExceptionHandler(UsernameNotFoundException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ResponseEntity<ResponseEnvelope> handleUsernameNotFoundException(UsernameNotFoundException e) {
		return new ResponseEntity<>(
				new ResponseEnvelope<ObjectNode>(AppUtil.nullObj, false,
						env.getProperty(RestErrorConstants.USERNOTFOUND), HttpStatus.BAD_REQUEST.value()),
				HttpStatus.BAD_REQUEST);
	}
}
