package com.org.verdaflow.rest.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.org.verdaflow.rest.entity.base.BaseEntity;

/**
 * The persistent class for the customer_address_details database table.
 * 
 */
@Entity
@Table(name = "customer_address_details")
@NamedQuery(name = "CustomerAddressDetail.findAll", query = "SELECT a FROM CustomerAddressDetail a")
public class CustomerAddressDetail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	// bi-directional many-to-One association to UserCustomerDetail
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private UserCustomerDetail userCustomerDetail;

	@Column(name = "name")
	private String name;

	@Column(name = "country_code")
	private String countryCode;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "address")
	private String address;

	@Column(name = "lat")
	private Double lat;

	@Column(name = "lng")
	private Double lng;

	@Column(name = "is_default")
	private boolean isDefault = Boolean.FALSE;

	public CustomerAddressDetail() {
	}

	public UserCustomerDetail getUserCustomerDetail() {
		return userCustomerDetail;
	}

	public void setUserCustomerDetail(UserCustomerDetail userCustomerDetail) {
		this.userCustomerDetail = userCustomerDetail;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

}
