package com.org.verdaflow.rest.api.dispatcher.model;

import java.util.Date;
import java.util.List;

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
public class ProductModel extends IdModel {

	private String name;
	private Integer groupType;
	private String description;
	private String image;
	private String imageThumb;
	private Float thc;
	private Float cbd;
	private Integer dispatcherId;
	private MasterModel category;
	private MasterModel type;
	private PromoCodeModel promoCode;
	private List<ProductEffectMappingModel> productEffectMappings;
	private List<ProductPriceDetailModel> productPriceDetails;

	private Boolean active;
	private Date createdAt;

	private Integer soldCount;
	private Integer likedCount;
	private Boolean isFav;

	public ProductModel(Integer id, String name, Integer groupType, String description, String image, String imageThumb,
			Float thc, Float cbd, Integer dispatcherId, MasterModel category, MasterModel type,
			PromoCodeModel promoCode, List<ProductEffectMappingModel> productEffectMappings,
			List<ProductPriceDetailModel> productPriceDetails) {
		super(id);
		this.name = name;
		this.groupType = groupType;
		this.description = description;
		this.image = image;
		this.imageThumb = imageThumb;
		this.thc = thc;
		this.cbd = cbd;
		this.dispatcherId = dispatcherId;
		this.category = category;
		this.type = type;
		this.promoCode = promoCode;
		this.productEffectMappings = productEffectMappings;
		this.productPriceDetails = productPriceDetails;
	}

	public ProductModel(Integer id, String name, Integer groupType, String description, String image, String imageThumb,
			Float thc, Float cbd, Integer dispatcherId, MasterModel category, MasterModel type,
			PromoCodeModel promoCode, List<ProductEffectMappingModel> productEffectMappings,
			List<ProductPriceDetailModel> productPriceDetails, Boolean active, Date createdAt, Integer soldCount,
			Integer likedCount) {
		super(id);
		this.name = name;
		this.groupType = groupType;
		this.description = description;
		this.image = image;
		this.imageThumb = imageThumb;
		this.thc = thc;
		this.cbd = cbd;
		this.dispatcherId = dispatcherId;
		this.category = category;
		this.type = type;
		this.promoCode = promoCode;
		this.productEffectMappings = productEffectMappings;
		this.productPriceDetails = productPriceDetails;
		this.active = active;
		this.createdAt = createdAt;
		this.soldCount = soldCount;
		this.likedCount = likedCount;
	}

	public ProductModel(Integer id, String name, Integer groupType, String description, String image, String imageThumb,
			Float thc, Float cbd, Integer dispatcherId, MasterModel category, MasterModel type,
			PromoCodeModel promoCode, List<ProductEffectMappingModel> productEffectMappings,
			List<ProductPriceDetailModel> productPriceDetails, Boolean isFav) {
		super(id);
		this.name = name;
		this.groupType = groupType;
		this.description = description;
		this.image = image;
		this.imageThumb = imageThumb;
		this.thc = thc;
		this.cbd = cbd;
		this.dispatcherId = dispatcherId;
		this.category = category;
		this.type = type;
		this.promoCode = promoCode;
		this.productEffectMappings = productEffectMappings;
		this.productPriceDetails = productPriceDetails;
		this.isFav = isFav;
	}

}
