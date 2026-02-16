package com.org.verdaflow.rest.config.security.jwt;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.org.verdaflow.rest.config.common.AppConst;
import com.org.verdaflow.rest.entity.UserEntity;

public final class JwtUserFactory {

	private JwtUserFactory() {
	}

	public static JwtUser create(UserEntity user, int deviceId, int roleId) {
		List<GrantedAuthority> authorityList = user.getUserRoleMappings().stream()
				.map(authority -> new SimpleGrantedAuthority(authority.getMasterRole().getRole().name()))
				.collect(Collectors.toList());

		if (AppConst.USER_ROLE.ADMIN == roleId) {
			return new JwtUser(user.getId(), user.getPassword(), user.getEmail(), authorityList, deviceId, user,
					roleId);
		} else {
			return new JwtUser(user.getId(), user.getPassword(), user.getCountryCode() + user.getMobileNumber(),
					authorityList, deviceId, user,
					roleId);
		}
	}
}
