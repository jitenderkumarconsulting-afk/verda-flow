package com.org.verdaflow.rest.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.org.verdaflow.rest.entity.base.BaseEntity;

/**
 * The persistent class for the order_price_details database table.
 * 
 */
@Entity
@Table(name = "order_price_details")
@NamedQuery(name = "OrderPriceDetail.findAll", query = "SELECT o FROM OrderPriceDetail o")
public class OrderPriceDetail extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	// bi-directional one-to-one association to Order
	@OneToOne
	@JoinColumn(name = "order_id")
	private Order order;

	@Column(name = "sub_total")
	private BigDecimal subTotal;

	@Column(name = "tax")
	private BigDecimal tax;

	@Column(name = "delivery_charges")
	private BigDecimal deliveryCharges;

	@Column(name = "total")
	private BigDecimal total;

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public BigDecimal getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}

	public BigDecimal getTax() {
		return tax;
	}

	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	public BigDecimal getDeliveryCharges() {
		return deliveryCharges;
	}

	public void setDeliveryCharges(BigDecimal deliveryCharges) {
		this.deliveryCharges = deliveryCharges;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

}
