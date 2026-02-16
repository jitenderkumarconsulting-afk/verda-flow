package com.org.verdaflow.rest.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.org.verdaflow.rest.config.common.StringConst;

@Component
public class AuthManagerConfig {
	public static final Logger LOG = LoggerFactory.getLogger(AuthManagerConfig.class);
	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	public void configGlobal(AuthenticationManagerBuilder auth) {
		try {
			auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
			auth.authenticationProvider(authenticationProvider());
		} catch (Exception ex) {
			LOG.error(StringConst.ERROR_WHILE_CONFIG_AUTH_MANAGER_BUILDER, ex);
		}

	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
		return authenticationProvider;
	}
}
