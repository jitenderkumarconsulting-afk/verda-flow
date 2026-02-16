package com.org.verdaflow.rest.api.auth.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.org.verdaflow.rest.api.customer.model.CustomerAddressDetailModel;
import com.org.verdaflow.rest.common.enums.ApplicationStatus;
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
public class UserCustomerDetailModel extends IdModel {

	private String firstName;
	private String lastName;
	private String image;
	private String imageThumb;
	private ApplicationStatus applicationStatus;
	private Boolean active;

	private List<CustomerAddressDetailModel> customerAddressDetailModel;

	public UserCustomerDetailModel(Integer id, String firstName, String lastName, String image, String imageThumb,
			ApplicationStatus applicationStatus, Boolean active) {
		super(id);
		this.firstName = firstName;
		this.lastName = lastName;
		this.image = image;
		this.imageThumb = imageThumb;
		this.applicationStatus = applicationStatus;
		this.active = active;
	}

	public UserCustomerDetailModel(Integer id, String firstName, String lastName, String image, String imageThumb,
			ApplicationStatus applicationStatus, Boolean active,
			List<CustomerAddressDetailModel> customerAddressDetailModel) {
		super(id);
		this.firstName = firstName;
		this.lastName = lastName;
		this.image = image;
		this.imageThumb = imageThumb;
		this.applicationStatus = applicationStatus;
		this.active = active;
		this.customerAddressDetailModel = customerAddressDetailModel;
	}

}
