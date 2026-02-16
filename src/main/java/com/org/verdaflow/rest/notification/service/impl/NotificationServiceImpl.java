package com.org.verdaflow.rest.notification.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.verdaflow.rest.common.enums.DeviceType;
import com.org.verdaflow.rest.config.common.AppConst;
import com.org.verdaflow.rest.config.common.StringConst;
import com.org.verdaflow.rest.entity.DeviceDetail;
import com.org.verdaflow.rest.notification.model.AndroidFcmModel;
import com.org.verdaflow.rest.notification.model.AndroidFcmNotificationModel;
import com.org.verdaflow.rest.notification.model.IosFcmModel;
import com.org.verdaflow.rest.notification.model.IosFcmNotificationModel;
import com.org.verdaflow.rest.notification.model.NotificationModel;
import com.org.verdaflow.rest.notification.service.NotificationService;
import com.org.verdaflow.rest.util.AppUtil;

@Service
public class NotificationServiceImpl implements NotificationService {
	// Initialize the Log4j logger.
	public static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

	@Autowired
	private AppUtil appUtil;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private Environment env;

	@Override
	public void sendOneSignalNotification(NotificationModel notificationModel, DeviceDetail deviceDetail) {
		log.info("sendOneSignalNotification");
		// RestTemplate restTemplate = new RestTemplate();
		//
		// HttpHeaders headers = new HttpHeaders();
		// headers.set("Authorization", appUtil.getOneSignalRestAPIKey());
		// headers.set("Content-Type", "application/json");
		//
		// String modelAsJSONString = "";
		// String input = "";
		//
		// try {
		// modelAsJSONString = objectMapper.writeValueAsString(notificationModel);
		// log.info("NotificationServiceImpl.sendOneSignalNotification ::
		// modelAsJSONString " + modelAsJSONString);
		//
		// input = "{\r\n" + " \"app_id\": \"" + appUtil.getOneSignalRestAppId() +
		// "\",\r\n"
		// + " \"include_player_ids\": [\"" + deviceDetail.getDeviceToken() + "\"],\r\n"
		// + " \"data\": {\r\n"
		// + " \"model\": " + modelAsJSONString + " \r\n" + " },\r\n" + " \"contents\":
		// {\r\n"
		// + " \"en\": \"" + notificationModel.getBody() + "\"\r\n" + " }\r\n" + "}";
		// log.info("NotificationServiceImpl.sendOneSignalNotification :: input " +
		// input);
		//
		// HttpEntity<String> entity = new HttpEntity<>(input, headers);
		// String res = restTemplate.postForObject(AppUtil.ONE_SIGNAL_ENDPOINT, entity,
		// String.class);
		// log.info("NotificationServiceImpl.sendOneSignalNotification :: res " + res);
		// } catch (Exception e) {
		// log.info("NotificationServiceImpl.sendOneSignalNotification :: error " +
		// e.getMessage());
		// }
	}

	@Override
	public void sendFcmNotification(NotificationModel notificationModel, DeviceDetail deviceDetail) {
		log.info("sendFcmNotification");
		try {
			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.set(StringConst.AUTHORIZATION, StringConst.KEY + appUtil.getFCMserverKey());
			headers.set(StringConst.CONTENT_TYPE, StringConst.APPLICATION_JSON);

			String modelAsJsonString = null;

			if (DeviceType.ANDROID.name().equals(deviceDetail.getDeviceType().name())) {
				AndroidFcmModel fcmModel = new AndroidFcmModel(
						deviceDetail.getDeviceToken(), new AndroidFcmNotificationModel(notificationModel.getTitle(),
								notificationModel.getBody(), env.getProperty(StringConst.NOTIFICATION_PRIORITY)),
						notificationModel);
				modelAsJsonString = objectMapper.writeValueAsString(fcmModel);
			} else if (DeviceType.IOS.name().equals(deviceDetail.getDeviceType().name())) {
				IosFcmModel fcmModel = new IosFcmModel(deviceDetail.getDeviceToken(),
						new IosFcmNotificationModel(AppConst.NUMBER.ZERO, StringConst.DEFAULT,
								notificationModel.getTitle(), notificationModel.getBody(), notificationModel),
						env.getProperty(StringConst.NOTIFICATION_PRIORITY));
				modelAsJsonString = objectMapper.writeValueAsString(fcmModel);
			}

			if (null != modelAsJsonString) {
				log.info("sendFcmNotification :: modelAsJsonString " + modelAsJsonString);
				HttpEntity<String> httpEntity = new HttpEntity<String>(modelAsJsonString, headers);
				String response = restTemplate.postForObject(AppUtil.FCM_ENDPOINT, httpEntity, String.class);
				log.info("sendFcmNotification :: response " + response);
			}
		} catch (Exception e) {
			log.info("sendFcmNotification :: ERROR " + e.getMessage());
		}
	}

}
