package com.org.verdaflow.rest.api.customer.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.org.verdaflow.rest.api.dispatcher.model.ProductModel;
import com.org.verdaflow.rest.api.dispatcher.model.ProductPriceDetailModel;
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
public class CartDetailModel extends IdModel {

	private ProductModel product;
	private ProductPriceDetailModel productPriceDetail;

	private Date createdAt;
	private Date modifiedAt;

	private Boolean isProductAvailable;
	private Boolean isProductPriceDetailAvailable;

	public CartDetailModel(Integer id, ProductModel product, ProductPriceDetailModel productPriceDetail, Date createdAt,
			Date modifiedAt, Boolean isProductAvailable, Boolean isProductPriceDetailAvailable) {
		super(id);
		this.product = product;
		this.productPriceDetail = productPriceDetail;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
		this.isProductAvailable = isProductAvailable;
		this.isProductPriceDetailAvailable = isProductPriceDetailAvailable;
	}

}
