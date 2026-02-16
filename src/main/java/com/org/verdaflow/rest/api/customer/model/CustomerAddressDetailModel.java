package com.org.verdaflow.rest.api.customer.model;

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
public class CustomerAddressDetailModel extends IdModel {

	private String name;
	private String countryCode;
	private String phoneNumber;
	private String address;
	private Double lat;
	private Double lng;
	private Date createdAt;

	private Boolean isDefault;

	public CustomerAddressDetailModel(Integer id, String name, String countryCode, String phoneNumber, String address,
			Double lat, Double lng, Date createdAt) {
		super(id);
		this.name = name;
		this.countryCode = countryCode;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.lat = lat;
		this.lng = lng;
		this.createdAt = createdAt;
	}

	public CustomerAddressDetailModel(Integer id, String name, String countryCode, String phoneNumber, String address,
			Double lat, Double lng, Date createdAt, Boolean isDefault) {
		super(id);
		this.name = name;
		this.countryCode = countryCode;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.lat = lat;
		this.lng = lng;
		this.createdAt = createdAt;
		this.isDefault = isDefault;
	}

}
