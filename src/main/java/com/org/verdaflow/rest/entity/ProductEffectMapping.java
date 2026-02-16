package com.org.verdaflow.rest.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.org.verdaflow.rest.entity.base.BaseEntity;

/**
 * The persistent class for the product_effect_mapping database table.
 * 
 */
@Entity
@Table(name = "product_effect_mapping")
@NamedQuery(name = "ProductEffectMapping.findAll", query = "SELECT pem FROM ProductEffectMapping pem")
public class ProductEffectMapping extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	// bi-directional many-to-one association to Product
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	// bi-directional one-to-one association to MasterEffect
	@OneToOne
	@JoinColumn(name = "effect_id")
	private MasterEffect masterEffect;

	@Column(name = "value")
	private int value;

	public ProductEffectMapping() {

	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public MasterEffect getMasterEffect() {
		return masterEffect;
	}

	public void setMasterEffect(MasterEffect masterEffect) {
		this.masterEffect = masterEffect;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}