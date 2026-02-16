package com.org.verdaflow.rest.config.security.handler;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.verdaflow.rest.config.common.StringConst;
import com.org.verdaflow.rest.dto.ResponseEnvelope;
import com.org.verdaflow.rest.error.RestErrorConstants;

@Component
public class AuthenticationExceptionHandler implements AuthenticationEntryPoint, Serializable {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationExceptionHandler.class);
	private static final long serialVersionUID = -8970718410437077606L;

	@SuppressWarnings("rawtypes")
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {
		logger.error(StringConst.EXCEPTION, authException);
		response.setContentType(StringConst.APPLICATION_FORWARD_SLASH_JSON);
		response.setCharacterEncoding(StringConst.UTF_8_SMALL);
		response.setStatus(HttpServletResponse.SC_OK);

		String errorMessage = request.getAttribute(RestErrorConstants.AUTHENTICATION_ERROR) != null
				? request.getAttribute(RestErrorConstants.AUTHENTICATION_ERROR).toString()
				: RestErrorConstants.UNAUTHORIZED_MSG;
		logger.error("errorMessage ", errorMessage);

		ResponseEnvelope responseMessage = new ResponseEnvelope(false, errorMessage,
				RestErrorConstants.UNAUTHORIZE_CODE_LOGOUT);
		response.setStatus(RestErrorConstants.UNAUTHORIZE_CODE_LOGOUT);

		ObjectMapper objectMapper = new ObjectMapper();
		String message = objectMapper.writeValueAsString(responseMessage);

		response.getWriter().write(message);
	}
}