package com.org.verdaflow.rest.api.dispatcher.model;

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
public class ProductPriceDetailModel extends IdModel {

	private Integer productId;
	private Integer priceType;
	private BigDecimal price;

	public ProductPriceDetailModel(Integer id, Integer productId, Integer priceType, BigDecimal price) {
		super(id);
		this.productId = productId;
		this.priceType = priceType;
		this.price = price;
	}

}
