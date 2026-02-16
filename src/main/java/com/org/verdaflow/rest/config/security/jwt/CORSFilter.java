package com.org.verdaflow.rest.config.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
public class CORSFilter extends GenericFilterBean {

	@Autowired
	private Environment env;

	public static final Logger log = LoggerFactory.getLogger(CORSFilter.class);

	@Override
	public void doFilter(ServletRequest request, javax.servlet.ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;

		httpServletResponse.setHeader("Access-Control-Allow-Origin", env.getProperty("Access-Control-Allow-Origin"));
		httpServletResponse.setHeader("Access-Control-Allow-Methods", env.getProperty("Access-Control-Allow-Methods"));
		httpServletResponse.setHeader("Access-Control-Max-Age", env.getProperty("Access-Control-Max-Age"));
		httpServletResponse.setHeader("Access-Control-Allow-Headers", env.getProperty("Access-Control-Allow-Headers"));
		httpServletResponse.setHeader("X-Frame-Options", env.getProperty("X-Frame-Options"));
		httpServletResponse.setHeader("X-Content-Type-Options", env.getProperty("X-Content-Type-Options"));
		httpServletResponse.setHeader("Cache-Control", env.getProperty("Cache-Control"));
		httpServletResponse.setHeader("Pragma", env.getProperty("Pragma"));
		httpServletResponse.setHeader("X-XSS-Protection", env.getProperty("X-XSS-Protection"));

		filterChain.doFilter(httpServletRequest, response);

	}

}
