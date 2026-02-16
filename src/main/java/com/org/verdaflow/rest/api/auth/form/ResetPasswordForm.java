package com.org.verdaflow.rest.api.auth.form;

import javax.validation.GroupSequence;
import javax.validation.constraints.Min;
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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@GroupSequence({ FirstLevelValidation.class, SecondLevelValidation.class, ThirdLevelValidation.class,
		FourthLevelValidation.class, ResetPasswordForm.class })
public class ResetPasswordForm {

	@Min(1)
	private int userId;

	@Min(1)
	private int roleId;

	@NotNull(groups = FirstLevelValidation.class)
	@NotEmpty(groups = SecondLevelValidation.class)
	@Length(max = 50, groups = ThirdLevelValidation.class)
	@Pattern(regexp = "(?!.*(<\\s*script|<\\s*alert)).*$", message = "{V.STRING_REQUIRED}", groups = FourthLevelValidation.class)
	private String verificationCode;

	@Pattern(regexp = "(?!.*(<\\s*script|<\\s*alert)).*$", message = "{V.STRING_REQUIRED}", groups = FourthLevelValidation.class)
	private String password;

}
