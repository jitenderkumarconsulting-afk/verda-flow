package com.org.verdaflow.rest.api.auth.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.org.verdaflow.rest.api.user.model.UserRatingDetailModel;
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
public class UserModel extends IdModel {

	private String email;
	private String countryCode;
	private String mobileNumber;
	private Boolean pushNotificationStatus;
	private Boolean active;
	private Date createdAt;

	private UserRatingDetailModel userRatingDetail;

	public UserModel(Integer id, String email, String countryCode, String mobileNumber, Boolean pushNotificationStatus,
			Boolean active, Date createdAt, UserRatingDetailModel userRatingDetail) {
		super(id);
		this.email = email;
		this.countryCode = countryCode;
		this.mobileNumber = mobileNumber;
		this.pushNotificationStatus = pushNotificationStatus;
		this.active = active;
		this.createdAt = createdAt;
		this.userRatingDetail = userRatingDetail;
	}

}
