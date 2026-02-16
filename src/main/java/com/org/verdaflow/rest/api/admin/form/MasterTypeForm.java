package com.org.verdaflow.rest.api.admin.form;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

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
		FourthLevelValidation.class, MasterTypeForm.class })
public class MasterTypeForm {

	private int id;

	@NotNull(groups = FirstLevelValidation.class)
	@NotEmpty(groups = SecondLevelValidation.class)
	@Length(max = 255, groups = ThirdLevelValidation.class)
	@Pattern(regexp = "(?!.*(<\\s*script|<\\s*alert)).*$", message = "{V.STRING_REQUIRED}", groups = FourthLevelValidation.class)
	private String name;

}
