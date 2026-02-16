package com.org.verdaflow.rest.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.org.verdaflow.rest.common.enums.ApplicationStatus;
import com.org.verdaflow.rest.entity.base.BaseEntity;

/**
 * The persistent class for the user_business_details database table.
 * 
 */
@Entity
@Table(name = "user_customer_details")
@NamedQuery(name = "UserCustomerDetail.findAll", query = "SELECT u FROM UserCustomerDetail u")
public class UserCustomerDetail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "image")
	private String image;

	@Column(name = "image_thumb")
	private String imageThumb;

	@Column(name = "application_status")
	@Enumerated(EnumType.STRING)
	private ApplicationStatus applicationStatus = ApplicationStatus.PENDING;

	@Column(name = "active")
	private boolean active = Boolean.TRUE;

	// bi-directional one-to-one association to UserEntity
	@OneToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;

	// bi-directional one-to-many association to CustomerAddressDetail
	@OneToMany(mappedBy = "userCustomerDetail", fetch = FetchType.LAZY)
	@Where(clause = "is_deleted = 0")
	private List<CustomerAddressDetail> customerAddressDetails;

	public UserCustomerDetail() {

	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImageThumb() {
		return imageThumb;
	}

	public void setImageThumb(String imageThumb) {
		this.imageThumb = imageThumb;
	}

	public ApplicationStatus getApplicationStatus() {
		return applicationStatus;
	}

	public void setApplicationStatus(ApplicationStatus applicationStatus) {
		this.applicationStatus = applicationStatus;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public List<CustomerAddressDetail> getCustomerAddressDetails() {
		return customerAddressDetails;
	}

	public void setCustomerAddressDetails(List<CustomerAddressDetail> customerAddressDetails) {
		this.customerAddressDetails = customerAddressDetails;
	}

}
