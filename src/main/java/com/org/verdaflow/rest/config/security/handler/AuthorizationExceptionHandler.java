package com.org.verdaflow.rest.config.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.verdaflow.rest.config.common.StringConst;
import com.org.verdaflow.rest.dto.ResponseEnvelope;
import com.org.verdaflow.rest.error.RestErrorConstants;

@Component
public class AuthorizationExceptionHandler implements AccessDeniedHandler {

	private static final Logger logger = LoggerFactory.getLogger(AuthorizationExceptionHandler.class);

	@SuppressWarnings("rawtypes")
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		logger.error(StringConst.EXCEPTION, accessDeniedException);
		response.setContentType(StringConst.APPLICATION_FORWARD_SLASH_JSON);
		response.setCharacterEncoding(StringConst.UTF_8_SMALL);
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);

		ResponseEnvelope responseMessage = new ResponseEnvelope(false, RestErrorConstants.PLEASELOGIN_MSG,
				RestErrorConstants.FORBIDDEN_CODE);

		ObjectMapper objectMapper = new ObjectMapper();
		String message = objectMapper.writeValueAsString(responseMessage);

		response.getWriter().write(message);
	}
}
