package com.org.verdaflow.rest.api.dispatcher.form;

import java.util.List;

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
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@GroupSequence({ FirstLevelValidation.class, SecondLevelValidation.class, ThirdLevelValidation.class,
		FourthLevelValidation.class, ProductForm.class })
public class ProductForm {

	private int id;

	@NotNull(groups = FirstLevelValidation.class)
	@NotEmpty(groups = SecondLevelValidation.class)
	@Length(max = 255, groups = ThirdLevelValidation.class)
	@Pattern(regexp = "(?!.*(<\\s*script|<\\s*alert)).*$", message = "{V.STRING_REQUIRED}", groups = FourthLevelValidation.class)
	private String name;

	@Min(1)
	private int groupType;

	@NotNull(groups = FirstLevelValidation.class)
	@NotEmpty(groups = SecondLevelValidation.class)
	@Pattern(regexp = "(?!.*(<\\s*script|<\\s*alert)).*$", message = "{V.STRING_REQUIRED}", groups = ThirdLevelValidation.class)
	private String image;

	@NotNull(groups = FirstLevelValidation.class)
	@NotEmpty(groups = SecondLevelValidation.class)
	@Pattern(regexp = "(?!.*(<\\s*script|<\\s*alert)).*$", message = "{V.STRING_REQUIRED}", groups = ThirdLevelValidation.class)
	private String imageThumb;

	@NotNull(groups = FirstLevelValidation.class)
	@NotEmpty(groups = SecondLevelValidation.class)
	@Length(max = 255, groups = ThirdLevelValidation.class)
	// @Pattern(regexp = "(?!.*(<\\s*script|<\\s*alert)).*$", message =
	// "{V.STRING_REQUIRED}", groups = FourthLevelValidation.class)
	private String description;

	// @NotEmpty(groups = FirstLevelValidation.class)
	// @NotNull(groups = SecondLevelValidation.class)
	private float thc;

	// @NotEmpty(groups = FirstLevelValidation.class)
	// @NotNull(groups = SecondLevelValidation.class)
	private float cbd;

	@Min(1)
	private int categoryId;

	@Min(1)
	private int typeId;

	private int promoCodeId;

	@NotNull(groups = FirstLevelValidation.class)
	private List<ProductEffectMappingForm> productEffectMappings;

	@NotNull(groups = FirstLevelValidation.class)
	@NotEmpty(groups = SecondLevelValidation.class)
	private List<ProductPriceDetailForm> productPriceDetails;

}
