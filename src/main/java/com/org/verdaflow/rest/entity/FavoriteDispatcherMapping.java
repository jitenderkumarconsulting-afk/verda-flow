package com.org.verdaflow.rest.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.org.verdaflow.rest.entity.base.BaseEntity;

/**
 * The persistent class for the favorite_dispatcher_mapping database table.
 * 
 */
@Entity
@Table(name = "favorite_dispatcher_mapping")
@NamedQuery(name = "FavoriteDispatcherMapping.findAll", query = "SELECT f FROM FavoriteDispatcherMapping f")
public class FavoriteDispatcherMapping extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	// bi-directional many-to-one association to UserEntity
	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;

	// bi-directional many-to-one association to UserDispatcherDetail
	@ManyToOne
	@JoinColumn(name = "dispatcher_id")
	private UserDispatcherDetail userDispatcher;

	@Column(name = "is_fav")
	private boolean isFav;

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public UserDispatcherDetail getUserDispatcher() {
		return userDispatcher;
	}

	public void setUserDispatcher(UserDispatcherDetail userDispatcher) {
		this.userDispatcher = userDispatcher;
	}

	public boolean isFav() {
		return isFav;
	}

	public void setFav(boolean isFav) {
		this.isFav = isFav;
	}

}
