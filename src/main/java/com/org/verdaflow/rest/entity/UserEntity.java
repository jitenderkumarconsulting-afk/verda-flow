package com.org.verdaflow.rest.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Where;

import com.org.verdaflow.rest.common.enums.UserRoleEnum;
import com.org.verdaflow.rest.entity.base.BaseEntity;

/**
 * The persistent class for the user database table.
 */
@Entity
@Table(name = "user")
@NamedQuery(name = "UserEntity.findAll", query = "SELECT u FROM UserEntity u")
public class UserEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "email")
	private String email;

	@Column(name = "country_code")
	private String countryCode;

	@Column(name = "mobile_number")
	private String mobileNumber;

	@Column(name = "password")
	private String password;

	@Column(name = "push_notification_status")
	private boolean pushNotificationStatus = Boolean.TRUE;

	@Column(name = "active")
	private boolean active = Boolean.TRUE;

	// bi-directional one-to-many association to DeviceDetail
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	@Where(clause = "is_deleted = 0")
	private List<DeviceDetail> deviceDetails;

	// bi-directional one-to-many association to UserRoleMapping
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	@Where(clause = "is_deleted = 0")
	private List<UserRoleMapping> userRoleMappings;

	// bi-directional one-to-many association to AuditLogin
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	@Where(clause = "is_deleted = 0")
	private List<AuditLogin> auditLogins;

	// bi-directional one-to-one association to ForgotPassword
	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
	private ForgotPassword forgotPassword;

	// bi-directional one-to-one association to UserDispatcherDetail
	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
	private UserDispatcherDetail userDispatcherDetail;

	// bi-directional one-to-one association to UserDriverDetail
	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
	private UserDriverDetail userDriverDetail;

	// bi-directional one-to-one association to UserCustomerDetail
	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
	private UserCustomerDetail userCustomerDetail;

	// bi-directional one-to-many association to FavoriteDispatcherMapping
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	@Where(clause = "is_deleted = 0")
	private List<FavoriteDispatcherMapping> favoriteDispatcherMappings;

	// bi-directional one-to-many association to FavoriteProductMapping
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	@Where(clause = "is_deleted = 0")
	private List<FavoriteProductMapping> favoriteProductMappings;

	// bi-directional one-to-many association to CartDetail
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	@Where(clause = "is_deleted = 0")
	private List<CartDetail> cartDetails;

	// bi-directional one-to-many association to Order
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	@Where(clause = "is_deleted = 0")
	private List<Order> orders;

	// bi-directional one-to-many association to FavoriteOrderMapping
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	@Where(clause = "is_deleted = 0")
	private List<FavoriteOrderMapping> favoriteOrderMappings;

	// bi-directional one-to-many association to AuditPromoCode
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	@Where(clause = "is_deleted = 0")
	private List<AuditPromoCode> auditPromoCodes;

	// bi-directional one-to-many association to Notification
	@OneToMany(mappedBy = "user")
	@Where(clause = "is_deleted = 0")
	private List<Notification> notifications;

	// bi-directional one-to-one association to UserRatingDetail
	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
	private UserRatingDetail userRatingDetail;

	@Transient
	private UserRoleEnum userRole;

	public UserEntity() {
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isPushNotificationStatus() {
		return pushNotificationStatus;
	}

	public void setPushNotificationStatus(boolean pushNotificationStatus) {
		this.pushNotificationStatus = pushNotificationStatus;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<DeviceDetail> getDeviceDetails() {
		return deviceDetails;
	}

	public void setDeviceDetails(List<DeviceDetail> deviceDetails) {
		this.deviceDetails = deviceDetails;
	}

	public List<UserRoleMapping> getUserRoleMappings() {
		return userRoleMappings;
	}

	public void setUserRoleMappings(List<UserRoleMapping> userRoleMappings) {
		this.userRoleMappings = userRoleMappings;
	}

	public List<AuditLogin> getAuditLogins() {
		return auditLogins;
	}

	public void setAuditLogins(List<AuditLogin> auditLogins) {
		this.auditLogins = auditLogins;
	}

	public ForgotPassword getForgotPassword() {
		return forgotPassword;
	}

	public void setForgotPassword(ForgotPassword forgotPassword) {
		this.forgotPassword = forgotPassword;
	}

	public UserDispatcherDetail getUserDispatcherDetail() {
		return userDispatcherDetail;
	}

	public void setUserDispatcherDetail(UserDispatcherDetail userDispatcherDetail) {
		this.userDispatcherDetail = userDispatcherDetail;
	}

	public UserDriverDetail getUserDriverDetail() {
		return userDriverDetail;
	}

	public void setUserDriverDetail(UserDriverDetail userDriverDetail) {
		this.userDriverDetail = userDriverDetail;
	}

	public UserCustomerDetail getUserCustomerDetail() {
		return userCustomerDetail;
	}

	public void setUserCustomerDetail(UserCustomerDetail userCustomerDetail) {
		this.userCustomerDetail = userCustomerDetail;
	}

	public List<FavoriteDispatcherMapping> getFavoriteDispatcherMappings() {
		return favoriteDispatcherMappings;
	}

	public void setFavoriteDispatcherMappings(List<FavoriteDispatcherMapping> favoriteDispatcherMappings) {
		this.favoriteDispatcherMappings = favoriteDispatcherMappings;
	}

	public List<FavoriteProductMapping> getFavoriteProductMappings() {
		return favoriteProductMappings;
	}

	public void setFavoriteProductMappings(List<FavoriteProductMapping> favoriteProductMappings) {
		this.favoriteProductMappings = favoriteProductMappings;
	}

	public List<CartDetail> getCartDetails() {
		return cartDetails;
	}

	public void setCartDetails(List<CartDetail> cartDetails) {
		this.cartDetails = cartDetails;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public List<FavoriteOrderMapping> getFavoriteOrderMappings() {
		return favoriteOrderMappings;
	}

	public void setFavoriteOrderMappings(List<FavoriteOrderMapping> favoriteOrderMappings) {
		this.favoriteOrderMappings = favoriteOrderMappings;
	}

	public List<AuditPromoCode> getAuditPromoCodes() {
		return auditPromoCodes;
	}

	public void setAuditPromoCodes(List<AuditPromoCode> auditPromoCodes) {
		this.auditPromoCodes = auditPromoCodes;
	}

	public List<Notification> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}

	public UserRatingDetail getUserRatingDetail() {
		return userRatingDetail;
	}

	public void setUserRatingDetail(UserRatingDetail userRatingDetail) {
		this.userRatingDetail = userRatingDetail;
	}

	public UserRoleEnum getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRoleEnum userRole) {
		this.userRole = userRole;
	}

}
