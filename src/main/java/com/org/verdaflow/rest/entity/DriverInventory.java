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
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.org.verdaflow.rest.entity.base.BaseEntity;

@Entity
@Table(name = "driver_inventory")
@NamedQuery(name = "DriverInventory.findAll", query = "SELECT di FROM DriverInventory di")
public class DriverInventory extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	// bi-directional many-to-one association to UserDriverDetail
	@ManyToOne
	@JoinColumn(name = "driver_id")
	private UserDriverDetail userDriverDetail;

	// bi-directional many-to-one association to Product
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@Column(name = "is_in_stock")
	private boolean isInStock;

	// bi-directional one-to-many association to DriverInventoryDetail
	@OneToMany(mappedBy = "driverInventory", fetch = FetchType.LAZY)
	@Where(clause = "is_deleted = 0")
	private List<DriverInventoryDetail> driverInventoryDetails;

	public DriverInventory() {

	}

	public UserDriverDetail getUserDriverDetail() {
		return userDriverDetail;
	}

	public void setUserDriverDetail(UserDriverDetail userDriverDetail) {
		this.userDriverDetail = userDriverDetail;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public boolean isInStock() {
		return isInStock;
	}

	public void setInStock(boolean isInStock) {
		this.isInStock = isInStock;
	}

	public List<DriverInventoryDetail> getDriverInventoryDetails() {
		return driverInventoryDetails;
	}

	public void setDriverInventoryDetails(List<DriverInventoryDetail> driverInventoryDetails) {
		this.driverInventoryDetails = driverInventoryDetails;
	}

}