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
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.org.verdaflow.rest.common.enums.ProductAggregateType;
import com.org.verdaflow.rest.entity.base.BaseEntity;

/**
 * The persistent class for the product_aggregates database table.
 * 
 */
@Entity
@Table(name = "product_aggregates")
@NamedQuery(name = "ProductAggregate.findAll", query = "SELECT a FROM ProductAggregate a")
public class ProductAggregate extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	// bi-directional many-to-one association to Product
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@Column(name = "product_aggregate_type")
	@Enumerated(EnumType.STRING)
	private ProductAggregateType productAggregateType;

	@Column(name = "count")
	private int count;

	// bi-directional one-to-many association to ProductAggregateDetail
	@OneToMany(mappedBy = "productAggregate", fetch = FetchType.LAZY)
	@Where(clause = "is_deleted = 0")
	private List<ProductAggregateDetail> productAggregateDetails;

	public ProductAggregate() {

	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public ProductAggregateType getProductAggregateType() {
		return productAggregateType;
	}

	public void setProductAggregateType(ProductAggregateType productAggregateType) {
		this.productAggregateType = productAggregateType;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<ProductAggregateDetail> getProductAggregateDetails() {
		return productAggregateDetails;
	}

	public void setProductAggregateDetails(List<ProductAggregateDetail> productAggregateDetails) {
		this.productAggregateDetails = productAggregateDetails;
	}

}