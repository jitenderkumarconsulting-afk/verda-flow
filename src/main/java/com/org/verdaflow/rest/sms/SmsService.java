package com.org.verdaflow.rest.sms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.AuthenticationException;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.org.verdaflow.rest.config.common.AppConst;
import com.org.verdaflow.rest.config.common.StringConst;
import com.org.verdaflow.rest.error.AppException;

@Service
public class SmsService {

	private static final Logger logger = LoggerFactory.getLogger(SmsService.class);

	@Autowired
	Environment env;

	/**
	 * Send custom SMS to an user.
	 * 
	 * @param mobileNumber
	 * @param message
	 * @return
	 */
	public boolean sendSMS(String mobileNumber, String message) {
		try {
			return sendMessage(mobileNumber, message);
		} catch (Exception e) {
			logger.error(StringConst.UNABLE_TO_SEND_MESSAGE_COLON, e);
			throw new AppException(StringConst.OTP_SEND_FAILURE, AppConst.EXCEPTION_CAT.BAD_REQUEST_400);
		}
	}

	/**
	 * Send custom SMS to list of users.
	 * 
	 * @param mobileNumbersList
	 * @param message
	 * @return
	 */
	public boolean sendSMS(List<String> mobileNumbersList, String message) {
		try {
			return sendMessage(mobileNumbersList, message);
		} catch (Exception e) {
			logger.error(StringConst.UNABLE_TO_SEND_MESSAGE_COLON, e);
			throw new AppException(StringConst.OTP_SEND_FAILURE, AppConst.EXCEPTION_CAT.BAD_REQUEST_400);
		}
	}

	/**
	 * Message sender to a single user.
	 * 
	 * @param mobileNumber
	 * @param message
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws AuthenticationException
	 */
	private boolean sendMessage(String mobileNumber, String message)
			throws ClientProtocolException, IOException, AuthenticationException {
		boolean sentStatus = false;
		HttpPost httpPost = new HttpPost(env.getProperty(StringConst.SMS_API_BASE_URL));

		try (CloseableHttpClient client = HttpClients.createDefault()) {
			List<NameValuePair> params = new ArrayList<>();
			params.add(new BasicNameValuePair(StringConst.USER, env.getProperty(StringConst.SMS_API_USER)));
			params.add(new BasicNameValuePair(StringConst.PASS, env.getProperty(StringConst.SMS_API_PASWORD)));
			params.add(new BasicNameValuePair(StringConst.SID, env.getProperty(StringConst.SMS_API_SID)));
			params.add(new BasicNameValuePair(StringConst.SMS_ARRAY_ZERO_ZERO,
					new StringBuilder(env.getProperty(StringConst.SMS_COUNTRY_CODE)).append(mobileNumber).toString()));
			params.add(new BasicNameValuePair(StringConst.SMS_ARRAY_ZERO_ONE, message));
			params.add(new BasicNameValuePair(StringConst.SMS_ARRAY_ZERO_TWO, StringConst.ONE_TO_NINE));
			httpPost.setEntity(new UrlEncodedFormEntity(params));

			CloseableHttpResponse response = client.execute(httpPost);
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode())
				sentStatus = true;
		} catch (Exception e) {
			logger.error(StringConst.UNABLE_TO_SEND_MESSAGE_COLON, e);
			throw new AppException(StringConst.OTP_SEND_FAILURE, AppConst.EXCEPTION_CAT.BAD_REQUEST_400);
		}

		return sentStatus;
	}

	/**
	 * Message sender to multiple users.
	 * 
	 * @param mobileNumbersList
	 * @param message
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws AuthenticationException
	 */
	private boolean sendMessage(List<String> mobileNumbersList, String message)
			throws ClientProtocolException, IOException, AuthenticationException {
		boolean sentStatus = false;
		HttpPost httpPost = new HttpPost(env.getProperty(StringConst.SMS_API_BASE_URL));

		try (CloseableHttpClient client = HttpClients.createDefault()) {
			List<NameValuePair> params = new ArrayList<>();
			params.add(new BasicNameValuePair(StringConst.USER, env.getProperty(StringConst.SMS_API_USER)));
			params.add(new BasicNameValuePair(StringConst.PASS, env.getProperty(StringConst.SMS_API_PASWORD)));
			params.add(new BasicNameValuePair(StringConst.SID, env.getProperty(StringConst.SMS_API_SID)));

			String param;
			for (int i = 0; i < mobileNumbersList.size(); i++) {
				param = new StringBuilder(StringConst.SMS_BRACKET_OPEN).append(i).append(StringConst.BRACKET_CLOSE)
						.toString();
				params.add(new BasicNameValuePair(new StringBuilder(param).append(StringConst.ARRAY_ZERO).toString(),
						new StringBuilder(env.getProperty(StringConst.SMS_COUNTRY_CODE))
								.append(mobileNumbersList.get(i)).toString()));
				params.add(new BasicNameValuePair(new StringBuilder(param).append(StringConst.ARRAY_ONE).toString(),
						message));
				params.add(new BasicNameValuePair(new StringBuilder(param).append(StringConst.ARRAY_TWO).toString(),
						mobileNumbersList.get(i)));
			}

			httpPost.setEntity(new UrlEncodedFormEntity(params));

			CloseableHttpResponse response = client.execute(httpPost);
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode())
				sentStatus = true;
		} catch (Exception e) {
			logger.error(StringConst.UNABLE_TO_SEND_MESSAGE_COLON, e);
			throw new AppException(StringConst.OTP_SEND_FAILURE, AppConst.EXCEPTION_CAT.BAD_REQUEST_400);
		}

		return sentStatus;
	}

	public static void main(String[] args) {
		try {
			throw new IOException("Testing");
		} catch (Exception e) {
			logger.info(StringConst.UNABLE_TO_READ_INPUT_MESSAGE_FROM_FILE_COLON, e);
		}

	}
}
