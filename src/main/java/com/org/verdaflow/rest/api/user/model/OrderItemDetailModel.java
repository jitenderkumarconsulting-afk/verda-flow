package com.org.verdaflow.rest.api.user.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.org.verdaflow.rest.api.dispatcher.model.ProductModel;
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
public class OrderItemDetailModel extends IdModel {

	private ProductModel productModel;
	private Integer priceType;
	private BigDecimal price;
	private BigDecimal discountedPrice;
	private Boolean isDiscounted;

	public OrderItemDetailModel(Integer id, ProductModel productModel, Integer priceType, BigDecimal price,
			BigDecimal discountedPrice, Boolean isDiscounted) {
		super(id);
		this.productModel = productModel;
		this.priceType = priceType;
		this.price = price;
		this.discountedPrice = discountedPrice;
		this.isDiscounted = isDiscounted;
	}

}
