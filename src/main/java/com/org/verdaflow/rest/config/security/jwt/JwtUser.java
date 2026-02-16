package com.org.verdaflow.rest.config.security.jwt;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.org.verdaflow.rest.entity.UserEntity;

public class JwtUser implements UserDetails {

	private static final long serialVersionUID = 1L;
	private final int id;
	private final String password;
	private final String email;
	private final Collection<? extends GrantedAuthority> authorities;
	private final int deviceId;
	private final UserEntity userEntity;
	private final int roleId;

	public JwtUser(int id, String password, String email, Collection<? extends GrantedAuthority> authorities,
			int deviceId, UserEntity userEntity, int roleId) {
		this.id = id;
		this.password = password;
		this.email = email;
		this.authorities = authorities;
		this.deviceId = deviceId;
		this.userEntity = userEntity;
		this.roleId = roleId;
	}

	@JsonIgnore
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return true;
	}

	public boolean isEnabled() {
		return true;
	}

	public int getId() {
		return id;
	}

	public String getPassword() {
		return password;
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public int getDeviceId() {
		return deviceId;
	}

	public UserEntity getUserEntity() {
		return userEntity;
	}

	public int getRoleId() {
		return roleId;
	}

	public String getUsername() {
		return email;
	}

	public String getEmail() {
		return email;
	}
}
