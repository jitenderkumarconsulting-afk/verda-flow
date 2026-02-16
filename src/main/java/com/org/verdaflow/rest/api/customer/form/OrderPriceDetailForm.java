package com.org.verdaflow.rest.api.customer.form;

import java.math.BigDecimal;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotNull;

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
		FourthLevelValidation.class, OrderPriceDetailForm.class })
public class OrderPriceDetailForm {

	@NotEmpty(groups = FirstLevelValidation.class)
	@NotNull(groups = SecondLevelValidation.class)
	private BigDecimal subTotal;

	@NotEmpty(groups = FirstLevelValidation.class)
	@NotNull(groups = SecondLevelValidation.class)
	private BigDecimal tax;

	@NotEmpty(groups = FirstLevelValidation.class)
	@NotNull(groups = SecondLevelValidation.class)
	private BigDecimal deliveryCharges;

	@NotEmpty(groups = FirstLevelValidation.class)
	@NotNull(groups = SecondLevelValidation.class)
	private BigDecimal total;

}
