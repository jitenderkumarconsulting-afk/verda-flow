package com.org.verdaflow.rest.notification.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.org.verdaflow.rest.common.model.IdModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(value = Include.NON_NULL)
public class NotificationModel extends IdModel {

	private String title;
	private String body;
	private String bodyFormatted;
	private Boolean isRead;
	private Integer orderId;
	private Integer notificationType;
	private Date createdAt;

	public NotificationModel(int id, String title, String body, String bodyFormatted, boolean isRead,
			int notificationType, Date createdAt) {
		super(id);
		this.title = title;
		this.body = body;
		this.bodyFormatted = bodyFormatted;
		this.isRead = isRead;
		this.notificationType = notificationType;
		this.createdAt = createdAt;
	}

	public NotificationModel(int id, String title, String body, String bodyFormatted, boolean isRead, int orderId,
			int notificationType, Date createdAt) {
		super(id);
		this.title = title;
		this.body = body;
		this.bodyFormatted = bodyFormatted;
		this.isRead = isRead;
		this.orderId = orderId;
		this.notificationType = notificationType;
		this.createdAt = createdAt;
	}

}