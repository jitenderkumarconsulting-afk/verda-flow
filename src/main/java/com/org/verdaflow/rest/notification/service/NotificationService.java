package com.org.verdaflow.rest.notification.service;

import com.org.verdaflow.rest.entity.DeviceDetail;
import com.org.verdaflow.rest.notification.model.NotificationModel;

public interface NotificationService {

	/**
	 * Send OneSignal notification to Web.
	 * 
	 * @param notificationModel
	 * @param deviceDetail
	 */
	void sendOneSignalNotification(NotificationModel notificationModel, DeviceDetail deviceDetail);

	/**
	 * Send FCM notification to Android/iOS.
	 * 
	 * @param notificationModel
	 * @param deviceDetail
	 */
	void sendFcmNotification(NotificationModel notificationModel, DeviceDetail deviceDetail);

}
