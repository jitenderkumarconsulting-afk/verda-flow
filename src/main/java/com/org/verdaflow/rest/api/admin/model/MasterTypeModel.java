package com.org.verdaflow.rest.api.admin.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.org.verdaflow.rest.common.model.IdModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(value = Include.NON_NULL)
public class MasterTypeModel extends IdModel {

	private String name;
	private Boolean active;
	private Date createdAt;

	public MasterTypeModel(Integer id, String name, Boolean active, Date createdAt) {
		super(id);
		this.name = name;
		this.active = active;
		this.createdAt = createdAt;
	}

}
