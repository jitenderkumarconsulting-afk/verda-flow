package com.org.verdaflow.rest.api.auth.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.org.verdaflow.rest.api.master.model.MasterModel;
import com.org.verdaflow.rest.api.user.model.UserRatingDetailModel;
import com.org.verdaflow.rest.common.enums.ApplicationStatus;
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
public class UserDispatcherDetailModel extends IdModel {

	private String storeName;
	private String managerName;
	private String address;
	private Double lat;
	private Double lng;
	private String image;
	private String imageThumb;
	private MasterModel eta;
	private ApplicationStatus applicationStatus;
	private Boolean active;

	private Integer driversCount;
	private Integer ordersCount;

	private Boolean isFav;
	private Double distance;

	private BigDecimal deliveryCharges;

	private UserRatingDetailModel userRatingDetail;

	public UserDispatcherDetailModel(Integer id, String storeName, String managerName, String address, Double lat,
			Double lng, String image, String imageThumb, MasterModel eta, ApplicationStatus applicationStatus,
			Boolean active, BigDecimal deliveryCharges) {
		super(id);
		this.storeName = storeName;
		this.managerName = managerName;
		this.address = address;
		this.lat = lat;
		this.lng = lng;
		this.image = image;
		this.imageThumb = imageThumb;
		this.eta = eta;
		this.applicationStatus = applicationStatus;
		this.active = active;
		this.deliveryCharges = deliveryCharges;
	}

	public UserDispatcherDetailModel(Integer id, String storeName, String managerName, String address, Double lat,
			Double lng, String image, String imageThumb, MasterModel eta, ApplicationStatus applicationStatus,
			Boolean active, BigDecimal deliveryCharges, Integer driversCount, Integer ordersCount) {
		super(id);
		this.storeName = storeName;
		this.managerName = managerName;
		this.address = address;
		this.lat = lat;
		this.lng = lng;
		this.image = image;
		this.imageThumb = imageThumb;
		this.eta = eta;
		this.applicationStatus = applicationStatus;
		this.active = active;
		this.driversCount = driversCount;
		this.ordersCount = ordersCount;
		this.deliveryCharges = deliveryCharges;
	}

	public UserDispatcherDetailModel(Integer id, String storeName, String managerName, String address, Double lat,
			Double lng, String image, String imageThumb, MasterModel eta, ApplicationStatus applicationStatus,
			Boolean active, Boolean isFav, Double distance, BigDecimal deliveryCharges,
			UserRatingDetailModel userRatingDetail) {
		super(id);
		this.storeName = storeName;
		this.managerName = managerName;
		this.address = address;
		this.lat = lat;
		this.lng = lng;
		this.image = image;
		this.imageThumb = imageThumb;
		this.eta = eta;
		this.applicationStatus = applicationStatus;
		this.active = active;
		this.isFav = isFav;
		this.distance = distance;
		this.deliveryCharges = deliveryCharges;
		this.userRatingDetail = userRatingDetail;
	}

}
