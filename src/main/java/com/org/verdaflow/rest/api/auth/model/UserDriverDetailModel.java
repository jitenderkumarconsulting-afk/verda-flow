package com.org.verdaflow.rest.api.auth.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.org.verdaflow.rest.api.admin.model.UserDetailModel;
import com.org.verdaflow.rest.api.driver.model.DriverLocationDetailModel;
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
public class UserDriverDetailModel extends IdModel {

	private String name;
	private String year;
	private String model;
	private String make;
	private String vehicleRegistrationPhoto;
	private String driverLicensePhoto;
	private String carLicensePlatePhoto;
	private String image;
	private String imageThumb;
	private ApplicationStatus applicationStatus;
	private Boolean active;
	private Boolean isOnline;

	private Integer assignedIncompleteOrdersCount;
	private Boolean isAssigned;

	private UserDetailModel dispatcherUserDetail;

	private DriverLocationDetailModel driverLocationDetail;

	public UserDriverDetailModel(Integer id, String name, String year, String model, String make,
			String vehicleRegistrationPhoto, String driverLicensePhoto, String carLicensePlatePhoto, String image,
			String imageThumb, ApplicationStatus applicationStatus, Boolean active, Boolean isOnline,
			DriverLocationDetailModel driverLocationDetail) {
		super(id);
		this.name = name;
		this.year = year;
		this.model = model;
		this.make = make;
		this.vehicleRegistrationPhoto = vehicleRegistrationPhoto;
		this.driverLicensePhoto = driverLicensePhoto;
		this.carLicensePlatePhoto = carLicensePlatePhoto;
		this.image = image;
		this.imageThumb = imageThumb;
		this.applicationStatus = applicationStatus;
		this.active = active;
		this.isOnline = isOnline;
		this.driverLocationDetail = driverLocationDetail;
	}

	public UserDriverDetailModel(Integer id, String name, String image, String imageThumb, Boolean isOnline,
			Integer assignedIncompleteOrdersCount, Boolean isAssigned, DriverLocationDetailModel driverLocationDetail) {
		super(id);
		this.name = name;
		this.image = image;
		this.imageThumb = imageThumb;
		this.isOnline = isOnline;
		this.assignedIncompleteOrdersCount = assignedIncompleteOrdersCount;
		this.isAssigned = isAssigned;
		this.driverLocationDetail = driverLocationDetail;
	}

	public UserDriverDetailModel(Integer id, String name, String year, String model, String make,
			String vehicleRegistrationPhoto, String driverLicensePhoto, String carLicensePlatePhoto, String image,
			String imageThumb, ApplicationStatus applicationStatus, Boolean active, Boolean isOnline,
			UserDetailModel dispatcherUserDetail, DriverLocationDetailModel driverLocationDetail) {
		super(id);
		this.name = name;
		this.year = year;
		this.model = model;
		this.make = make;
		this.vehicleRegistrationPhoto = vehicleRegistrationPhoto;
		this.driverLicensePhoto = driverLicensePhoto;
		this.carLicensePlatePhoto = carLicensePlatePhoto;
		this.image = image;
		this.imageThumb = imageThumb;
		this.applicationStatus = applicationStatus;
		this.active = active;
		this.isOnline = isOnline;
		this.dispatcherUserDetail = dispatcherUserDetail;
		this.driverLocationDetail = driverLocationDetail;
	}

}
