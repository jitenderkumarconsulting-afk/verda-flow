package com.org.verdaflow.rest.config.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class GlobalControllerInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private Environment env;

	public static final Logger log = LoggerFactory.getLogger(GlobalControllerInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		response.setHeader("Access-Control-Allow-Origin", env.getProperty("Access-Control-Allow-Origin"));
		response.setHeader("Access-Control-Allow-Methods", env.getProperty("Access-Control-Allow-Methods"));
		response.setHeader("Access-Control-Max-Age", env.getProperty("Access-Control-Max-Age"));
		response.setHeader("Access-Control-Allow-Headers", env.getProperty("Access-Control-Allow-Headers"));
		response.setHeader("X-Frame-Options", env.getProperty("X-Frame-Options"));
		response.setHeader("X-Content-Type-Options", env.getProperty("X-Content-Type-Options"));
		response.setHeader("Cache-Control", env.getProperty("Cache-Control"));
		response.setHeader("Pragma", env.getProperty("Pragma"));
		response.setHeader("X-XSS-Protection", env.getProperty("X-XSS-Protection"));

		return super.preHandle(request, response, handler);
	}
}
