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
@Table(name = "driver_inventory_details")
@NamedQuery(name = "DriverInventoryDetail.findAll", query = "SELECT d FROM DriverInventoryDetail d")
public class DriverInventoryDetail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	// bi-directional many-to-one association to DriverInventory
	@ManyToOne
	@JoinColumn(name = "driver_inventory_id")
	private DriverInventory driverInventory;

	// bi-directional many-to-one association to ProductPriceDetail
	@ManyToOne
	@JoinColumn(name = "product_price_detail_id")
	private ProductPriceDetail productPriceDetail;

	@Column(name = "quantity")
	private int quantity;

	public DriverInventoryDetail() {

	}

	public DriverInventory getDriverInventory() {
		return driverInventory;
	}

	public void setDriverInventory(DriverInventory driverInventory) {
		this.driverInventory = driverInventory;
	}

	public ProductPriceDetail getProductPriceDetail() {
		return productPriceDetail;
	}

	public void setProductPriceDetail(ProductPriceDetail productPriceDetail) {
		this.productPriceDetail = productPriceDetail;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}