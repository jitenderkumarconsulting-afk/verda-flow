package com.org.verdaflow.rest.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.org.verdaflow.rest.entity.base.BaseEntity;

/**
 * The persistent class for the product_aggregate_details database table.
 * 
 */
@Entity
@Table(name = "product_aggregate_details")
@NamedQuery(name = "ProductAggregateDetail.findAll", query = "SELECT d FROM ProductAggregateDetail d")
public class ProductAggregateDetail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	// bi-directional many-to-one association to ProductAggregate
	@ManyToOne
	@JoinColumn(name = "product_aggregate_id")
	private ProductAggregate productAggregate;

	// bi-directional many-to-one association to UserEntity
	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;

	public ProductAggregateDetail() {

	}

	public ProductAggregate getProductAggregate() {
		return productAggregate;
	}

	public void setProductAggregate(ProductAggregate productAggregate) {
		this.productAggregate = productAggregate;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

}