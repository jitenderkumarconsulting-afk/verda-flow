package com.org.verdaflow.rest.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.org.verdaflow.rest.entity.base.BaseEntity;

/**
 * The persistent class for the product_price_details database table.
 * 
 */
@Entity
@Table(name = "product_price_details")
@NamedQuery(name = "ProductPriceDetail.findAll", query = "SELECT p FROM ProductPriceDetail p")
public class ProductPriceDetail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	// bi-directional many-to-One association to Product
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@Column(name = "priceType")
	private int priceType;

	@Column(name = "price")
	private BigDecimal price;

	@Column(name = "is_available")
	private boolean isAvailable;

	public ProductPriceDetail() {
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getPriceType() {
		return priceType;
	}

	public void setPriceType(int priceType) {
		this.priceType = priceType;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

}
