package com.org.verdaflow.rest.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.org.verdaflow.rest.config.common.StringConst;

@Configuration
public class MailConfig {

	@Autowired
	Environment env;

	@Bean
	public JavaMailSenderImpl config() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(env.getProperty(StringConst.MAIL_HOST));
		mailSender.setPort(env.getProperty(StringConst.MAIL_PORT, Integer.class));
		mailSender.setUsername(env.getProperty(StringConst.MAIL_USERNAME));
		mailSender.setPassword(env.getProperty(StringConst.MAIL_PASWORD));
		mailSender.setJavaMailProperties(properties());
		return mailSender;
	}

	private Properties properties() {
		Properties properties = new Properties();
		properties.setProperty(StringConst.MAIL_SMTP_AUTH, env.getProperty(StringConst.MAIL_SMTP_AUTH));
		properties.setProperty(StringConst.MAIL_SMTP_SSL_ENABLE, env.getProperty(StringConst.MAIL_SMTP_SSL_ENABLE));
		return properties;
	}

}
