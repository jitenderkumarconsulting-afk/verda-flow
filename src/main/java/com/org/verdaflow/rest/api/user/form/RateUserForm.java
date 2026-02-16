package com.org.verdaflow.rest.api.user.form;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

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
		FourthLevelValidation.class, RateUserForm.class })
public class RateUserForm {

	// @NotEmpty(groups = FirstLevelValidation.class)
	// @NotNull(groups = SecondLevelValidation.class)
	private float rating;

	@NotNull(groups = FirstLevelValidation.class)
	@NotEmpty(groups = SecondLevelValidation.class)
	@Length(max = 255, groups = ThirdLevelValidation.class)
	// @Pattern(regexp = "(?!.*(<\\s*script|<\\s*alert)).*$", message =
	// "{V.STRING_REQUIRED}", groups = FourthLevelValidation.class)
	private String note;

}
