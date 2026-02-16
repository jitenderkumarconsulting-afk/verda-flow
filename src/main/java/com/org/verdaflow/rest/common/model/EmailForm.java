package com.org.verdaflow.rest.common.model;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

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
		FourthLevelValidation.class, EmailForm.class })
public class EmailForm {

	@NotNull(groups = FirstLevelValidation.class)
	@Length(max = 40, groups = SecondLevelValidation.class)
	@Email(groups = ThirdLevelValidation.class)
	@Pattern(regexp = "(?!.*(<\\s*script|<\\s*alert)).*$", message = "{V.STRING_REQUIRED}", groups = FourthLevelValidation.class)
	private String email;

}
