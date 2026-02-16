package com.org.verdaflow.rest.api.admin.form;

import java.math.BigDecimal;

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
		FourthLevelValidation.class, RegisterDispatcherForm.class })
public class RegisterDispatcherForm extends EmailForm {

	// dispatcher user Id.
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

	// User Dispatcher Details
	@NotNull(groups = FirstLevelValidation.class)
	@NotEmpty(groups = SecondLevelValidation.class)
	@Length(max = 255, groups = ThirdLevelValidation.class)
	@Pattern(regexp = "(?!.*(<\\s*script|<\\s*alert)).*$", message = "{V.STRING_REQUIRED}", groups = FourthLevelValidation.class)
	private String storeName;

	@NotNull(groups = FirstLevelValidation.class)
	@NotEmpty(groups = SecondLevelValidation.class)
	@Length(max = 255, groups = ThirdLevelValidation.class)
	@Pattern(regexp = "(?!.*(<\\s*script|<\\s*alert)).*$", message = "{V.STRING_REQUIRED}", groups = FourthLevelValidation.class)
	private String managerName;

	@NotNull(groups = FirstLevelValidation.class)
	@NotEmpty(groups = SecondLevelValidation.class)
	@Length(max = 255, groups = ThirdLevelValidation.class)
	// @Pattern(regexp = "(?!.*(<\\s*script|<\\s*alert)).*$", message =
	// "{V.STRING_REQUIRED}", groups = FourthLevelValidation.class)
	private String address;

	@NotNull(groups = FirstLevelValidation.class)
	private Double lat;

	@NotNull(groups = FirstLevelValidation.class)
	private Double lng;

	@NotNull(groups = FirstLevelValidation.class)
	@NotEmpty(groups = SecondLevelValidation.class)
	@Pattern(regexp = "(?!.*(<\\s*script|<\\s*alert)).*$", message = "{V.STRING_REQUIRED}", groups = ThirdLevelValidation.class)
	private String image;

	@NotNull(groups = FirstLevelValidation.class)
	@NotEmpty(groups = SecondLevelValidation.class)
	@Pattern(regexp = "(?!.*(<\\s*script|<\\s*alert)).*$", message = "{V.STRING_REQUIRED}", groups = ThirdLevelValidation.class)
	private String imageThumb;

	@Min(1)
	private int etaId;

	@NotNull(groups = FirstLevelValidation.class)
	private BigDecimal deliveryCharges;

}
