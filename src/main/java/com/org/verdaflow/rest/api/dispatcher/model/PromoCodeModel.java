package com.org.verdaflow.rest.api.dispatcher.model;

import java.util.Date;

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
public class PromoCodeModel extends IdModel {

	private Integer dispatcherId;
	private String name;
	private Float discount;
	private Date startDate;
	private Date endDate;
	private Integer promoCodeType;
	private Boolean active;

	private Date createdAt;

	public PromoCodeModel(Integer id, Integer dispatcherId, String name, Float discount, Date startDate, Date endDate,
			Integer promoCodeType, Boolean active, Date createdAt) {
		super(id);
		this.dispatcherId = dispatcherId;
		this.name = name;
		this.discount = discount;
		this.startDate = startDate;
		this.endDate = endDate;
		this.promoCodeType = promoCodeType;
		this.active = active;
		this.createdAt = createdAt;
	}

	public PromoCodeModel(Integer id, Integer dispatcherId, String name, Float discount, Date startDate, Date endDate,
			Integer promoCodeType, Boolean active) {
		super(id);
		this.dispatcherId = dispatcherId;
		this.name = name;
		this.discount = discount;
		this.startDate = startDate;
		this.endDate = endDate;
		this.promoCodeType = promoCodeType;
		this.active = active;
	}

}
