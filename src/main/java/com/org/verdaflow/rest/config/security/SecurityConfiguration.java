package com.org.verdaflow.rest.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.org.verdaflow.rest.common.enums.UserRoleEnum;
import com.org.verdaflow.rest.config.common.StringConst;
import com.org.verdaflow.rest.config.security.handler.AuthenticationExceptionHandler;
import com.org.verdaflow.rest.config.security.handler.AuthorizationExceptionHandler;
import com.org.verdaflow.rest.config.security.jwt.CORSFilter;
import com.org.verdaflow.rest.config.security.jwt.JwtAuthenticationTokenFilter;

@Configuration
@EnableWebSecurity(debug = false)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	// exception handlers
	@Autowired
	private AuthenticationExceptionHandler authenticationExceptionHandler;
	@Autowired
	private AuthorizationExceptionHandler authorizationExceptionHandler;
	@Autowired
	private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

	@Autowired
	private CORSFilter corsFilter;

	public SecurityConfiguration() {
		super(true);
	}

	String[] allowedUrls = { "/api/v1/auth/**" };
	String[] adminAllowedUrls = { "/api/v1/admin/**" };
	String[] userAllowedUrls = { "/api/v1/user/**" };
	String[] dispatcherAllowedUrls = { "/api/v1/dispatcher/**" };
	String[] driverAllowedUrls = { "/api/v1/driver/**" };
	String[] customerAllowedUrls = { "/api/v1/customer/**" };
	String[] masterAllowedUrls = { "/api/v1/master/**" };

	@Override
	public void configure(WebSecurity webSecurity) throws Exception {
		webSecurity.ignoring().antMatchers(allowedUrls);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// rules for Authentication and Authorization on URL basis
		http.authorizeRequests().antMatchers(HttpMethod.OPTIONS, StringConst.FILE_SEPARATOR_DOUBLE_MULTIPLICATION)
				.permitAll().antMatchers(allowedUrls).permitAll().antMatchers(adminAllowedUrls)
				.hasAnyAuthority(String.valueOf(UserRoleEnum.ADMIN)).antMatchers(userAllowedUrls)
				.hasAnyAuthority(String.valueOf(UserRoleEnum.DISPATCHER), String.valueOf(UserRoleEnum.DRIVER),
						String.valueOf(UserRoleEnum.CUSTOMER))
				.antMatchers(dispatcherAllowedUrls).hasAnyAuthority(String.valueOf(UserRoleEnum.DISPATCHER))
				.antMatchers(driverAllowedUrls).hasAnyAuthority(String.valueOf(UserRoleEnum.DRIVER))
				.antMatchers(customerAllowedUrls).hasAnyAuthority(String.valueOf(UserRoleEnum.CUSTOMER))
				.antMatchers(masterAllowedUrls)
				.hasAnyAuthority(String.valueOf(UserRoleEnum.ADMIN), String.valueOf(UserRoleEnum.DISPATCHER),
						String.valueOf(UserRoleEnum.DRIVER), String.valueOf(UserRoleEnum.CUSTOMER))
				.and().anonymous();

		// exceptions and errors handling
		http.exceptionHandling().authenticationEntryPoint(authenticationExceptionHandler) // handler for Authentication
																							// failed exception
				.accessDeniedHandler(authorizationExceptionHandler); // handler for Authorization denied exception

		http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
		http.addFilterBefore(corsFilter, ChannelProcessingFilter.class);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}
