package com.org.verdaflow.rest.common.form;

import javax.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdForm {

	@Min(1)
	private int id;

}
