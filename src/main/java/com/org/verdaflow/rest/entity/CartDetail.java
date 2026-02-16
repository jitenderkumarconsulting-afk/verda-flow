package com.org.verdaflow.rest.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.org.verdaflow.rest.entity.base.BaseEntity;

/**
 * The persistent class for the customer_cart_details database table.
 * 
 */
@Entity
@Table(name = "cart_details")
@NamedQuery(name = "CartDetail.findAll", query = "SELECT c FROM CartDetail c")
public class CartDetail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	// bi-directional many-to-one association to UserDispatcherDetail
	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;

	// bi-directional many-to-one association to Product
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	// bi-directional many-to-one association to ProductPriceDetail
	@ManyToOne
	@JoinColumn(name = "product_price_detail_id")
	private ProductPriceDetail productPriceDetail;

	public CartDetail() {

	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public ProductPriceDetail getProductPriceDetail() {
		return productPriceDetail;
	}

	public void setProductPriceDetail(ProductPriceDetail productPriceDetail) {
		this.productPriceDetail = productPriceDetail;
	}

}