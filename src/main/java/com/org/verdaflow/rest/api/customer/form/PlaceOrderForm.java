package com.org.verdaflow.rest.api.customer.form;

import java.util.List;

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
		FourthLevelValidation.class, PlaceOrderForm.class })
public class PlaceOrderForm {

	@Min(1)
	private int dispatcherId;

	private int promoCodeId;

	private int customerAddressDetailId;

	@Min(1)
	private int deliveryType;

	// @NotEmpty(groups = FirstLevelValidation.class)
	// @NotNull(groups = SecondLevelValidation.class)
	private List<OrderItemDetailForm> orderItemDetails;

	// @NotEmpty(groups = FirstLevelValidation.class)
	// @NotNull(groups = SecondLevelValidation.class)
	private OrderPriceDetailForm orderPriceDetail;

	// @NotEmpty(groups = FirstLevelValidation.class)
	// @NotNull(groups = SecondLevelValidation.class)
	private boolean shouldEmptyCart;

}
