package com.org.verdaflow.rest.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomModel {

	private Integer id;
	private String storeName;
	private Long distance;

}
