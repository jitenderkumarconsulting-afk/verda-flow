package com.org.verdaflow.rest.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStatusModel {

	private boolean updateStatus;
	private boolean isApprovalReq;

}
