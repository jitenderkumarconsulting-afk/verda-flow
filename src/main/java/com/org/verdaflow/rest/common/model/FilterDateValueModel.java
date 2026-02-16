package com.org.verdaflow.rest.common.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterDateValueModel {

	private Date minDate;
	private Date maxDate;

}
