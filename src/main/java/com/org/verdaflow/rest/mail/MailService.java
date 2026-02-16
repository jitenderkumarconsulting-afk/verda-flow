package com.org.verdaflow.rest.mail;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {

	public static final Logger LOG = LoggerFactory.getLogger(MailService.class);

	@Autowired
	private JavaMailSender mailSender;

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	/**
	 * Mail sender for a single user.
	 * 
	 * @param from
	 * @param to
	 * @param subject
	 * @param msg
	 * @throws MessagingException
	 */
	public void sendMail(String from, String to, String subject, String msg) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		message.setSubject(subject);

		MimeMessageHelper helper;
		helper = new MimeMessageHelper(message, true);
		helper.setFrom(from);
		helper.setTo(to);
		helper.setText(msg, true);

		mailSender.send(message);
	}

	/**
	 * Mail sender for multiple user.
	 * 
	 * @param from
	 * @param to
	 * @param subject
	 * @param msg
	 * @throws MessagingException
	 */
	public void sendMail(String from, String[] to, String subject, String msg) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		message.setSubject(subject);

		MimeMessageHelper helper;
		helper = new MimeMessageHelper(message, true);
		helper.setFrom(from);
		helper.setTo(to);
		helper.setText(msg, true);

		mailSender.send(message);
	}

	/**
	 * Mail sender to a single user with attachment.
	 * 
	 * @param from
	 * @param to
	 * @param subject
	 * @param msg
	 * @param file
	 * @throws MessagingException
	 */
	public void sendMailWithAttachment(String from, String to, String subject, String msg, File file)
			throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		message.setSubject(subject);

		MimeMessageHelper helper;
		helper = new MimeMessageHelper(message, true);
		helper.setFrom(from);
		helper.setTo(to);
		helper.setText(msg, true);
		helper.addAttachment(file.getName(), file);

		mailSender.send(message);
	}

}
