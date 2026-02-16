package com.org.verdaflow.rest.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.org.verdaflow.rest.entity.base.BaseEntity;

/**
 * The persistent class for the audit_rating database table.
 */
@Entity
@Table(name = "audit_rating")
@NamedQuery(name = "AuditRating.findAll", query = "SELECT r FROM AuditRating r")
public class AuditRating extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "order_rating_detail_id", nullable = false)
	private OrderRatingDetail orderRatingDetail;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;

	@OneToOne
	@JoinColumn(name = "by_user_id", nullable = false)
	private UserEntity byUser;

	@Column(name = "rating")
	private float rating;

	@Column(name = "note")
	private String note;

	public AuditRating() {
	}

	public OrderRatingDetail getOrderRatingDetail() {
		return orderRatingDetail;
	}

	public void setOrderRatingDetail(OrderRatingDetail orderRatingDetail) {
		this.orderRatingDetail = orderRatingDetail;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public UserEntity getByUser() {
		return byUser;
	}

	public void setByUser(UserEntity byUser) {
		this.byUser = byUser;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
