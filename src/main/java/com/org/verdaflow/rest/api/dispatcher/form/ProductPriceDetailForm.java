package com.org.verdaflow.rest.api.dispatcher.form;

import java.math.BigDecimal;

import javax.validation.GroupSequence;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
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
		FourthLevelValidation.class, ProductPriceDetailForm.class })
public class ProductPriceDetailForm {

	@Min(value = 1, groups = FirstLevelValidation.class)
	private int priceType;

	@DecimalMin(value = "0.01", groups = FirstLevelValidation.class)
	@DecimalMax(value = "999.99", groups = SecondLevelValidation.class)
	private BigDecimal price;

}
