package com.org.verdaflow.rest.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.org.verdaflow.rest.entity.base.BaseEntity;

@Entity
@Table(name = "driver_location_details")
@NamedQuery(name = "DriverLocationDetail.findAll", query = "SELECT d FROM DriverLocationDetail d")
public class DriverLocationDetail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@OneToOne
	@JoinColumn(name = "driver_id")
	private UserDriverDetail userDriverDetail;

	@Column(name = "lat")
	private Double lat;

	@Column(name = "lng")
	private Double lng;

	@Column(name = "rotation")
	private Double rotation;

	@Column(name = "route")
	private String route;

	// bi-directional many-to-one association to Order
	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;

	// bi-directional one-to-many association to AuditDriverLocation
	@OneToMany(mappedBy = "driverLocationDetail", fetch = FetchType.LAZY)
	@Where(clause = "is_deleted = 0")
	private List<AuditDriverLocation> auditDriverLocations;

	public DriverLocationDetail() {

	}

	public UserDriverDetail getUserDriverDetail() {
		return userDriverDetail;
	}

	public void setUserDriverDetail(UserDriverDetail userDriverDetail) {
		this.userDriverDetail = userDriverDetail;
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

	public Double getRotation() {
		return rotation;
	}

	public void setRotation(Double rotation) {
		this.rotation = rotation;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public List<AuditDriverLocation> getAuditDriverLocations() {
		return auditDriverLocations;
	}

	public void setAuditDriverLocations(List<AuditDriverLocation> auditDriverLocations) {
		this.auditDriverLocations = auditDriverLocations;
	}

}
