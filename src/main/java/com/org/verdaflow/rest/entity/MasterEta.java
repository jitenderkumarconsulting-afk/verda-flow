package com.org.verdaflow.rest.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.org.verdaflow.rest.entity.base.BaseEntity;

/**
 * The persistent class for the master_etas database table.
 * 
 */
@Entity
@Table(name = "master_etas")
@NamedQuery(name = "MasterEta.findAll", query = "SELECT m FROM MasterEta m")
public class MasterEta extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "name")
	private String name;

	@Column(name = "active")
	private boolean active = Boolean.TRUE;

	public MasterEta() {
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