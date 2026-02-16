package com.org.verdaflow.rest.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.org.verdaflow.rest.common.enums.NotificationType;
import com.org.verdaflow.rest.entity.base.BaseEntity;

@Entity
@Table(name = "notifications")
@NamedQuery(name = "Notification.findAll", query = "SELECT u FROM Notification u")
public class Notification extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "title")
	private String title;

	@Column(name = "body")
	private String body;

	@Column(name = "body_formatted")
	private String bodyFormatted;

	@Column(name = "is_read")
	private boolean isRead;

	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;

	@ManyToOne
	@JoinColumn(name = "created_by")
	private UserEntity createdBy;

	@Column(name = "notification_type")
	@Enumerated(EnumType.STRING)
	private NotificationType notificationType;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getBodyFormatted() {
		return bodyFormatted;
	}

	public void setBodyFormatted(String bodyFormatted) {
		this.bodyFormatted = bodyFormatted;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public UserEntity getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserEntity createdBy) {
		this.createdBy = createdBy;
	}

	public NotificationType getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(NotificationType notificationType) {
		this.notificationType = notificationType;
	}

}