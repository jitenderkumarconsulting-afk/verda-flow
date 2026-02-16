package com.org.verdaflow.rest.api.dispatcher.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(value = Include.NON_NULL)
public class DriverInventoryDetailModel {

	private Integer driverInventoryId;
	private ProductPriceDetailModel productPriceDetail;
	private Integer priceType;
	private Integer quantity;

}
