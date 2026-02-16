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
 * The persistent class for the favorite_order_mapping database table.
 * 
 */
@Entity
@Table(name = "favorite_order_mapping")
@NamedQuery(name = "FavoriteOrderMapping.findAll", query = "SELECT f FROM FavoriteOrderMapping f")
public class FavoriteOrderMapping extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	// bi-directional many-to-one association to UserEntity
	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;

	// bi-directional one-to-one association to Order
	@OneToOne
	@JoinColumn(name = "order_id")
	private Order order;

	@Column(name = "is_fav")
	private boolean isFav;

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public boolean isFav() {
		return isFav;
	}

	public void setFav(boolean isFav) {
		this.isFav = isFav;
	}

}
