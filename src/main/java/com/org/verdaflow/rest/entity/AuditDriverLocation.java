package com.org.verdaflow.rest.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.org.verdaflow.rest.entity.base.BaseEntity;

@Entity
@Table(name = "audit_driver_location")
@NamedQuery(name = "AuditDriverLocation.findAll", query = "SELECT l FROM AuditDriverLocation l")
public class AuditDriverLocation extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "driver_location_detail_id")
	private DriverLocationDetail driverLocationDetail;

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

	public AuditDriverLocation() {

	}

	public DriverLocationDetail getDriverLocationDetail() {
		return driverLocationDetail;
	}

	public void setDriverLocationDetail(DriverLocationDetail driverLocationDetail) {
		this.driverLocationDetail = driverLocationDetail;
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

}
