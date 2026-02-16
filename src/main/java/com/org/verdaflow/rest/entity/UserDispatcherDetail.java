package com.org.verdaflow.rest.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.Transient;

import org.hibernate.annotations.Where;

import com.org.verdaflow.rest.common.enums.ApplicationStatus;
import com.org.verdaflow.rest.entity.base.BaseEntity;

/**
 * The persistent class for the user_business_details database table.
 * 
 */
@Entity
@Table(name = "user_dispatcher_details")
@NamedQuery(name = "UserDispatcherDetail.findAll", query = "SELECT u FROM UserDispatcherDetail u")
public class UserDispatcherDetail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "store_name")
	private String storeName;

	@Column(name = "manager_name")
	private String managerName;

	@Column(name = "address")
	private String address;

	@Column(name = "lat")
	private Double lat;

	@Column(name = "lng")
	private Double lng;

	@Column(name = "image")
	private String image;

	@Column(name = "image_thumb")
	private String imageThumb;

	// bi-directional one-to-one association to MasterEta
	@OneToOne
	@JoinColumn(name = "eta_id")
	private MasterEta eta;

	@Column(name = "delivery_charges")
	private BigDecimal deliveryCharges;

	@Column(name = "application_status")
	@Enumerated(EnumType.STRING)
	private ApplicationStatus applicationStatus = ApplicationStatus.PENDING;

	@Column(name = "active")
	private boolean active = Boolean.TRUE;

	// bi-directional one-to-one association to UserEntity
	@OneToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;

	// bi-directional one-to-many association to UserDriverDetail
	@OneToMany(mappedBy = "userDispatcherDetail", fetch = FetchType.LAZY)
	@Where(clause = "is_deleted = 0")
	private List<UserDriverDetail> userDriverDetails;

	// bi-directional one-to-many association to PromoCode
	@OneToMany(mappedBy = "userDispatcherDetail", fetch = FetchType.LAZY)
	@Where(clause = "is_deleted = 0")
	private List<PromoCode> promoCodes;

	// bi-directional one-to-many association to Product
	@OneToMany(mappedBy = "userDispatcherDetail", fetch = FetchType.LAZY)
	@Where(clause = "is_deleted = 0")
	private List<Product> products;

	@Transient
	private Double distance;

	public UserDispatcherDetail() {

	}

	public UserDispatcherDetail(UserDispatcherDetail userDispatcherDetail, double distance) {
		super();
		this.setId(userDispatcherDetail.getId());
		this.storeName = userDispatcherDetail.getStoreName();
		this.managerName = userDispatcherDetail.getManagerName();
		this.address = userDispatcherDetail.getAddress();
		this.lat = userDispatcherDetail.getLat();
		this.lng = userDispatcherDetail.getLng();
		this.image = userDispatcherDetail.getImage();
		this.imageThumb = userDispatcherDetail.getImageThumb();
		this.eta = userDispatcherDetail.getEta();
		this.deliveryCharges = userDispatcherDetail.getDeliveryCharges();
		this.applicationStatus = userDispatcherDetail.getApplicationStatus();
		this.active = userDispatcherDetail.isActive();

		this.user = userDispatcherDetail.getUser();
		this.userDriverDetails = userDispatcherDetail.getUserDriverDetails();
		this.promoCodes = userDispatcherDetail.getPromoCodes();
		this.products = userDispatcherDetail.getProducts();

		this.distance = distance;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
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

	public MasterEta getEta() {
		return eta;
	}

	public void setEta(MasterEta eta) {
		this.eta = eta;
	}

	public BigDecimal getDeliveryCharges() {
		return deliveryCharges;
	}

	public void setDeliveryCharges(BigDecimal deliveryCharges) {
		this.deliveryCharges = deliveryCharges;
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

	public List<UserDriverDetail> getUserDriverDetails() {
		return userDriverDetails;
	}

	public void setUserDriverDetails(List<UserDriverDetail> userDriverDetails) {
		this.userDriverDetails = userDriverDetails;
	}

	public List<PromoCode> getPromoCodes() {
		return promoCodes;
	}

	public void setPromoCodes(List<PromoCode> promoCodes) {
		this.promoCodes = promoCodes;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

}
