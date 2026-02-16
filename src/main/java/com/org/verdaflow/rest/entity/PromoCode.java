package com.org.verdaflow.rest.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.org.verdaflow.rest.common.enums.PromoCodeType;
import com.org.verdaflow.rest.entity.base.BaseEntity;

/**
 * The persistent class for the master_effects database table.
 * 
 */
@Entity
@Table(name = "promo_codes")
@NamedQuery(name = "PromoCode.findAll", query = "SELECT p FROM PromoCode p")
public class PromoCode extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	// bi-directional many-to-one association to UserDispatcherDetail
	@ManyToOne
	@JoinColumn(name = "dispatcher_id")
	private UserDispatcherDetail userDispatcherDetail;

	@Column(name = "name")
	private String name;

	@Column(name = "discount")
	private float discount;

	@Column(name = "start_date")
	private Date startDate;

	@Column(name = "end_date")
	private Date endDate;

	@Column(name = "promo_code_type")
	@Enumerated(EnumType.STRING)
	private PromoCodeType promoCodeType = PromoCodeType.MULTIPLE_USE;

	@Column(name = "active")
	private boolean active = Boolean.TRUE;

	// bi-directional one-to-many association to AuditPromoCode
	@OneToMany(mappedBy = "promoCode", fetch = FetchType.LAZY)
	@Where(clause = "is_deleted = 0")
	private List<AuditPromoCode> auditPromoCodes;

	public PromoCode() {

	}

	public UserDispatcherDetail getUserDispatcherDetail() {
		return userDispatcherDetail;
	}

	public void setUserDispatcherDetail(UserDispatcherDetail userDispatcherDetail) {
		this.userDispatcherDetail = userDispatcherDetail;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getDiscount() {
		return discount;
	}

	public void setDiscount(float discount) {
		this.discount = discount;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public PromoCodeType getPromoCodeType() {
		return promoCodeType;
	}

	public void setPromoCodeType(PromoCodeType promoCodeType) {
		this.promoCodeType = promoCodeType;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<AuditPromoCode> getAuditPromoCodes() {
		return auditPromoCodes;
	}

	public void setAuditPromoCodes(List<AuditPromoCode> auditPromoCodes) {
		this.auditPromoCodes = auditPromoCodes;
	}

}