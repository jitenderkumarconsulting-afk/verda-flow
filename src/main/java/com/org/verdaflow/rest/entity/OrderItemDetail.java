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
 * The persistent class for the orders database table.
 * 
 */
@Entity
@Table(name = "order_item_details")
@NamedQuery(name = "OrderItemDetail.findAll", query = "SELECT o FROM OrderItemDetail o")
public class OrderItemDetail extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	// bi-directional many-to-one association to Order
	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;

	// bi-directional Many-to-one association to Product
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@Column(name = "price_type")
	private int priceType;

	@Column(name = "price")
	private BigDecimal price;

	@Column(name = "is_discounted")
	private boolean isDiscounted;

	@Column(name = "discounted_price")
	private BigDecimal discountedPrice;

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
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

	public boolean isDiscounted() {
		return isDiscounted;
	}

	public void setDiscounted(boolean isDiscounted) {
		this.isDiscounted = isDiscounted;
	}

	public BigDecimal getDiscountedPrice() {
		return discountedPrice;
	}

	public void setDiscountedPrice(BigDecimal discountedPrice) {
		this.discountedPrice = discountedPrice;
	}

}
