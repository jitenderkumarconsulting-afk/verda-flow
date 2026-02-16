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
import com.org.verdaflow.rest.event.ActivateDriverEvent;
import com.org.verdaflow.rest.mail.MailService;

@Component
public class ActivateDriverEventHandler implements ApplicationListener<ActivateDriverEvent> {
	public static final Logger log = LoggerFactory.getLogger(ActivateDriverEventHandler.class);

	@Autowired
	private Environment env;

	@Autowired
	private VelocityEngine velocityEngine;

	@Autowired
	private MailService mailService;

	@Override
	public void onApplicationEvent(ActivateDriverEvent event) {
		log.info("onApplicationEvent");
		String subject = StringConst.SUBJECT_ORG_ACTIVATE_DRIVER;

		Map<String, Object> model = new HashMap<>();
		model.put(StringConst.NAME, event.getName());

		String msg = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
				"templates/emails/ActivateDriver.vsl", StringConst.UTF_8, model);
		log.info("onApplicationEvent :: msg " + msg);

		try {
			mailService.sendMail(env.getProperty(StringConst.INFO_EMAIL_ID), event.getEmail(), subject, msg);
		} catch (MessagingException e) {
			log.error(new StringBuilder(StringConst.ERROR_SENDING_ACTIVATE_DRIVER_MAIL_TO).append(event.getEmail())
					.append(StringConst.ERROR_MESSAGE_COLON).append(e.getMessage()).toString(), e);
		}
	}
}
