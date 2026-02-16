package com.org.verdaflow.rest.entity;

import java.io.Serializable;
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
@Table(name = "user_driver_details")
@NamedQuery(name = "UserDriverDetail.findAll", query = "SELECT u FROM UserDriverDetail u")
public class UserDriverDetail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "name")
	private String name;

	@Column(name = "year")
	private String year;

	@Column(name = "model")
	private String model;

	@Column(name = "make")
	private String make;

	@Column(name = "vehicle_registration_photo")
	private String vehicleRegistrationPhoto;

	@Column(name = "driver_license_photo")
	private String driverLicensePhoto;

	@Column(name = "car_license_plate_photo")
	private String carLicensePlatePhoto;

	@Column(name = "image")
	private String image;

	@Column(name = "image_thumb")
	private String imageThumb;

	@Column(name = "application_status")
	@Enumerated(EnumType.STRING)
	private ApplicationStatus applicationStatus = ApplicationStatus.PENDING;

	@Column(name = "is_online")
	private boolean isOnline;

	@Column(name = "active")
	private boolean active = Boolean.TRUE;

	// bi-directional one-to-one association to UserEntity
	@OneToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;

	// bi-directional many-to-one association to UserDispatcherDetail
	@ManyToOne
	@JoinColumn(name = "dispatcher_id")
	private UserDispatcherDetail userDispatcherDetail;

	// bi-directional one-to-many association to DriverOrderMapping
	@OneToMany(mappedBy = "userDriverDetail", fetch = FetchType.LAZY)
	@Where(clause = "is_deleted = 0")
	private List<DriverOrderMapping> driverOrderMappings;

	// bi-directional one-to-one association to DriverLocationDetail
	@OneToOne(mappedBy = "userDriverDetail", fetch = FetchType.LAZY)
	@Where(clause = "is_deleted = 0")
	private DriverLocationDetail driverLocationDetail;

	public UserDriverDetail() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getVehicleRegistrationPhoto() {
		return vehicleRegistrationPhoto;
	}

	public void setVehicleRegistrationPhoto(String vehicleRegistrationPhoto) {
		this.vehicleRegistrationPhoto = vehicleRegistrationPhoto;
	}

	public String getDriverLicensePhoto() {
		return driverLicensePhoto;
	}

	public void setDriverLicensePhoto(String driverLicensePhoto) {
		this.driverLicensePhoto = driverLicensePhoto;
	}

	public String getCarLicensePlatePhoto() {
		return carLicensePlatePhoto;
	}

	public void setCarLicensePlatePhoto(String carLicensePlatePhoto) {
		this.carLicensePlatePhoto = carLicensePlatePhoto;
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

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
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

	public UserDispatcherDetail getUserDispatcherDetail() {
		return userDispatcherDetail;
	}

	public void setUserDispatcherDetail(UserDispatcherDetail userDispatcherDetail) {
		this.userDispatcherDetail = userDispatcherDetail;
	}

	public List<DriverOrderMapping> getDriverOrderMappings() {
		return driverOrderMappings;
	}

	public void setDriverOrderMappings(List<DriverOrderMapping> driverOrderMappings) {
		this.driverOrderMappings = driverOrderMappings;
	}

	public DriverLocationDetail getDriverLocationDetail() {
		return driverLocationDetail;
	}

	public void setDriverLocationDetail(DriverLocationDetail driverLocationDetail) {
		this.driverLocationDetail = driverLocationDetail;
	}

}
