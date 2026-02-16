package com.org.verdaflow.rest.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.org.verdaflow.rest.entity.base.BaseEntity;

/**
 * The persistent class for the master_effects database table.
 * 
 */
@Entity
@Table(name = "master_effects")
@NamedQuery(name = "MasterEffect.findAll", query = "SELECT e FROM MasterEffect e")
public class MasterEffect extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "name")
	private String name;

	@Column(name = "active")
	private boolean active = Boolean.TRUE;

	public MasterEffect() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}