package com.org.verdaflow.rest.api.driver.model;

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
public class DriverLocationDetailModel extends IdModel {

	private Integer driverId;

	private Double lat;
	private Double lng;
	private Double rotation;

	private String route;
	private Integer orderId;

	public DriverLocationDetailModel(Integer id, Integer driverId, Double lat, Double lng, Double rotation,
			String route, Integer orderId) {
		super(id);
		this.driverId = driverId;
		this.lat = lat;
		this.lng = lng;
		this.rotation = rotation;
		this.route = route;
		this.orderId = orderId;
	}

}
