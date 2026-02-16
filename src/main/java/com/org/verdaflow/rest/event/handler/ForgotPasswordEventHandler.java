package com.org.verdaflow.rest.event.handler;

import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.org.verdaflow.rest.config.common.AppConst;
import com.org.verdaflow.rest.config.common.StringConst;
import com.org.verdaflow.rest.event.ForgotPasswordEvent;
import com.org.verdaflow.rest.mail.MailService;

@Component
public class ForgotPasswordEventHandler implements ApplicationListener<ForgotPasswordEvent> {
	private static Logger log = LoggerFactory.getLogger("ForgotPasswordEventHandler.class");
	@Autowired
	private Environment env;
	@Autowired
	private VelocityEngine velocityEngine;
	@Autowired
	private MailService mailService;

	@Override
	public void onApplicationEvent(ForgotPasswordEvent event) {
		log.info("onApplicationEvent");
		String baseUrl;
		if (AppConst.USER_ROLE.ADMIN == event.getRoleId() || AppConst.USER_ROLE.CUSTOMER == event.getRoleId())
			baseUrl = env.getProperty(StringConst.WEB_URL_RESET_PASWRD_ADMIN);
		else
			baseUrl = env.getProperty(StringConst.WEB_URL_RESET_PASWRD);

		log.info("onApplicationEvent :: roleId " + event.getRoleId());

		String resetPassLink = new StringBuilder(baseUrl).append("?c=").append(event.getVerificationCode())
				.append("&u=").append(event.getUserId()).append("&r=").append(event.getRoleId()).toString();
		log.info("onApplicationEvent :: resetPassLink " + resetPassLink);

		String subject = StringConst.SUBJECT_ORG_RESET_PASWRD;

		Map<String, Object> model = new HashMap<>();
		model.put(StringConst.NAME, event.getUserName());
		model.put(StringConst.RESET_PASS_LINK, resetPassLink);

		String msg = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "templates/emails/ForgotPassword.vsl",
				StringConst.UTF_8, model);
		log.info("onApplicationEvent :: msg " + msg);

		try {
			mailService.sendMail(env.getProperty(StringConst.INFO_EMAIL_ID), event.getUserMail(), subject, msg);
		} catch (MessagingException e) {
			log.error(new StringBuilder(StringConst.ERROR_SENDING_MAIL_TO).append(event.getUserMail())
					.append(StringConst.ERROR_MESSAGE_COLON).append(e.getMessage()).toString(), e);
		}
	}
}
