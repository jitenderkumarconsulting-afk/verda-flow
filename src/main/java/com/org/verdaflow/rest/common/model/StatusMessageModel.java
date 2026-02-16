package com.org.verdaflow.rest.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusMessageModel {

	private Boolean status;
	private String messsage;

}
