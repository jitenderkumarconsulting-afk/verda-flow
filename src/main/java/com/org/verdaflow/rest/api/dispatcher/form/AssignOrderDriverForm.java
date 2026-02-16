package com.org.verdaflow.rest.api.dispatcher.form;

import javax.validation.GroupSequence;
import javax.validation.constraints.Min;

import com.org.verdaflow.rest.util.validation.FirstLevelValidation;
import com.org.verdaflow.rest.util.validation.FourthLevelValidation;
import com.org.verdaflow.rest.util.validation.SecondLevelValidation;
import com.org.verdaflow.rest.util.validation.ThirdLevelValidation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@GroupSequence({ FirstLevelValidation.class, SecondLevelValidation.class, ThirdLevelValidation.class,
		FourthLevelValidation.class, AssignOrderDriverForm.class })
public class AssignOrderDriverForm {

	@Min(1)
	private int orderId;

	@Min(1)
	private int driverId;

}