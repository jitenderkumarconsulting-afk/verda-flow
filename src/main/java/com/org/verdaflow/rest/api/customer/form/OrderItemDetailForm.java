package com.org.verdaflow.rest.api.customer.form;

import java.math.BigDecimal;

import javax.validation.GroupSequence;
import javax.validation.constraints.Min;

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
		FourthLevelValidation.class, OrderItemDetailForm.class })
public class OrderItemDetailForm {

	@Min(1)
	private int productId;

	@Min(1)
	private int productPriceDetailId;

	private boolean isDiscounted;

	private BigDecimal discountedPrice;

}
