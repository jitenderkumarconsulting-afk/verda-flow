package com.org.verdaflow.rest.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.org.verdaflow.rest.entity.base.BaseEntity;

/**
 * The persistent class for the user_rating_details database table.
 */
@Entity
@Table(name = "user_rating_details")
@NamedQuery(name = "UserRatingDetail.findAll", query = "SELECT d FROM UserRatingDetail d")
public class UserRatingDetail extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	// bi-directional one-to-one association to UserEntity
	@OneToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;

	@Column(name = "avg")
	private float avg;

	@Column(name = "total")
	private float total;

	@Column(name = "count")
	private int count;

	// bi-directional one-to-many association to AuditRating
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	@Where(clause = "is_deleted = 0")
	private List<AuditRating> auditRatings;

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public float getAvg() {
		return avg;
	}

	public void setAvg(float avg) {
		this.avg = avg;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<AuditRating> getAuditRatings() {
		return auditRatings;
	}

	public void setAuditRatings(List<AuditRating> auditRatings) {
		this.auditRatings = auditRatings;
	}

}
