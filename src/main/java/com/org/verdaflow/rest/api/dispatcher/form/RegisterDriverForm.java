package com.org.verdaflow.rest.api.dispatcher.form;

import javax.validation.GroupSequence;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.org.verdaflow.rest.common.model.EmailForm;
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
		FourthLevelValidation.class, RegisterDriverForm.class })
public class RegisterDriverForm extends EmailForm {

	// driver user id
	private int id;

	// User Details
	@NotEmpty(groups = FirstLevelValidation.class)
	@NotNull(groups = SecondLevelValidation.class)
	@Length(max = 7, groups = ThirdLevelValidation.class)
	@Pattern(regexp = "(?!.*(<\\s*script|<\\s*alert)).*$", message = "{V.STRING_REQUIRED}", groups = FourthLevelValidation.class)
	private String countryCode;

	@NotEmpty(groups = FirstLevelValidation.class)
	@NotNull(groups = SecondLevelValidation.class)
	@Length(max = 15, groups = ThirdLevelValidation.class)
	@Pattern(regexp = "(?!.*(<\\s*script|<\\s*alert)).*$", message = "{V.STRING_REQUIRED}", groups = FourthLevelValidation.class)
	private String mobileNumber;

	// User Driver Details
	@NotNull(groups = FirstLevelValidation.class)
	@NotEmpty(groups = SecondLevelValidation.class)
	@Length(max = 255, groups = ThirdLevelValidation.class)
	@Pattern(regexp = "(?!.*(<\\s*script|<\\s*alert)).*$", message = "{V.STRING_REQUIRED}", groups = FourthLevelValidation.class)
	private String name;

	@Min(1800)
	private int year;

	@NotNull(groups = FirstLevelValidation.class)
	@NotEmpty(groups = SecondLevelValidation.class)
	@Length(max = 50, groups = ThirdLevelValidation.class)
	@Pattern(regexp = "(?!.*(<\\s*script|<\\s*alert)).*$", message = "{V.STRING_REQUIRED}", groups = FourthLevelValidation.class)
	private String model;

	@NotNull(groups = FirstLevelValidation.class)
	@NotEmpty(groups = SecondLevelValidation.class)
	@Length(max = 50, groups = ThirdLevelValidation.class)
	@Pattern(regexp = "(?!.*(<\\s*script|<\\s*alert)).*$", message = "{V.STRING_REQUIRED}", groups = FourthLevelValidation.class)
	private String make;

	@NotNull(groups = FirstLevelValidation.class)
	@NotEmpty(groups = SecondLevelValidation.class)
	@Pattern(regexp = "(?!.*(<\\s*script|<\\s*alert)).*$", message = "{V.STRING_REQUIRED}", groups = ThirdLevelValidation.class)
	private String vehicleRegistrationPhoto;

	@NotNull(groups = FirstLevelValidation.class)
	@NotEmpty(groups = SecondLevelValidation.class)
	@Pattern(regexp = "(?!.*(<\\s*script|<\\s*alert)).*$", message = "{V.STRING_REQUIRED}", groups = ThirdLevelValidation.class)
	private String driverLicensePhoto;

	@NotNull(groups = FirstLevelValidation.class)
	@NotEmpty(groups = SecondLevelValidation.class)
	@Pattern(regexp = "(?!.*(<\\s*script|<\\s*alert)).*$", message = "{V.STRING_REQUIRED}", groups = ThirdLevelValidation.class)
	private String carLicensePlatePhoto;

	@NotNull(groups = FirstLevelValidation.class)
	@NotEmpty(groups = SecondLevelValidation.class)
	@Pattern(regexp = "(?!.*(<\\s*script|<\\s*alert)).*$", message = "{V.STRING_REQUIRED}", groups = ThirdLevelValidation.class)
	private String image;

	@NotNull(groups = FirstLevelValidation.class)
	@NotEmpty(groups = SecondLevelValidation.class)
	@Pattern(regexp = "(?!.*(<\\s*script|<\\s*alert)).*$", message = "{V.STRING_REQUIRED}", groups = ThirdLevelValidation.class)
	private String imageThumb;

}
