package com.org.verdaflow.rest.api.auth.form;

import javax.validation.GroupSequence;
import javax.validation.constraints.Min;

import com.org.verdaflow.rest.common.model.EmailForm;
import com.org.verdaflow.rest.util.validation.FirstLevelValidation;
import com.org.verdaflow.rest.util.validation.FourthLevelValidation;
import com.org.verdaflow.rest.util.validation.SecondLevelValidation;
import com.org.verdaflow.rest.util.validation.ThirdLevelValidation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@GroupSequence({ FirstLevelValidation.class, SecondLevelValidation.class, ThirdLevelValidation.class,
		FourthLevelValidation.class, ForgotPasswordForm.class })
public class ForgotPasswordForm extends EmailForm {

	@Min(1)
	private int roleId;

}
