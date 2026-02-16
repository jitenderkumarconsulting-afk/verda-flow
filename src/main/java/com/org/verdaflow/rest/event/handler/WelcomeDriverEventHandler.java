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

import com.org.verdaflow.rest.config.common.StringConst;
import com.org.verdaflow.rest.event.WelcomeDriverEvent;
import com.org.verdaflow.rest.mail.MailService;

@Component
public class WelcomeDriverEventHandler implements ApplicationListener<WelcomeDriverEvent> {
	public static final Logger log = LoggerFactory.getLogger(WelcomeDriverEventHandler.class);

	@Autowired
	private Environment env;

	@Autowired
	private VelocityEngine velocityEngine;

	@Autowired
	private MailService mailService;

	@Override
	public void onApplicationEvent(WelcomeDriverEvent event) {
		log.info("onApplicationEvent");
		String subject = StringConst.SUBJECT_ORG_WELCOME_DRIVER;

		Map<String, Object> model = new HashMap<>();
		model.put(StringConst.EMAIL, event.getEmail());
		model.put(StringConst.MOBILE_NUMBER, event.getMobileNumber());
		model.put(StringConst.PASWRD, event.getPassword());
		model.put(StringConst.NAME, event.getName());

		String msg = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "templates/emails/WelcomeDriver.vsl",
				StringConst.UTF_8, model);
		log.info("onApplicationEvent :: msg " + msg);

		try {
			mailService.sendMail(env.getProperty(StringConst.INFO_EMAIL_ID), event.getEmail(), subject, msg);
		} catch (MessagingException e) {
			log.error(new StringBuilder(StringConst.ERROR_SENDING_WELCOME_DRIVER_MAIL_TO).append(event.getEmail())
					.append(StringConst.ERROR_MESSAGE_COLON).append(e.getMessage()).toString(), e);
		}
	}
}
