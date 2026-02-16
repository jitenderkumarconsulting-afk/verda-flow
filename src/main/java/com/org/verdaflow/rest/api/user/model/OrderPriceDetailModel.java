package com.org.verdaflow.rest.api.user.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.org.verdaflow.rest.common.model.IdModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(value = Include.NON_NULL)
public class OrderPriceDetailModel extends IdModel {

	private Integer orderId;
	private BigDecimal subTotal;
	private BigDecimal tax;
	private BigDecimal deliveryCharges;
	private BigDecimal total;

	public OrderPriceDetailModel(Integer id, Integer orderId, BigDecimal subTotal, BigDecimal tax,
			BigDecimal deliveryCharges,
			BigDecimal total) {
		super(id);
		this.orderId = orderId;
		this.subTotal = subTotal;
		this.tax = tax;
		this.deliveryCharges = deliveryCharges;
		this.total = total;
	}

}
