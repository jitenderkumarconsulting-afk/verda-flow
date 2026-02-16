package com.org.verdaflow.rest.api.dispatcher.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.org.verdaflow.rest.api.master.model.MasterModel;
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
public class ProductEffectMappingModel extends IdModel {

	private Integer productId;
	private MasterModel effect;
	private Integer value;

	public ProductEffectMappingModel(Integer id, Integer productId, MasterModel effect, Integer value) {
		super(id);
		this.productId = productId;
		this.effect = effect;
		this.value = value;
	}

}
