package com.org.verdaflow.rest.config.security;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.org.verdaflow.rest.common.enums.UserRoleEnum;
import com.org.verdaflow.rest.config.common.AppConst;
import com.org.verdaflow.rest.config.common.StringConst;
import com.org.verdaflow.rest.config.security.jwt.JwtTokenUtil;
import com.org.verdaflow.rest.config.security.jwt.JwtUser;
import com.org.verdaflow.rest.entity.DeviceDetail;
import com.org.verdaflow.rest.entity.UserEntity;
import com.org.verdaflow.rest.repo.DeviceDetailRepo;
import com.org.verdaflow.rest.repo.UserEntityRepo;

@Component
public class CustomUserDetailsService implements UserDetailsService {
	public static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

	@Autowired
	private UserEntityRepo userService;

	@Autowired
	private DeviceDetailRepo deviceDetailRepo;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
		String[] str = s.split(JwtTokenUtil.USERNAME_ROLEID_JOINING_SYMBOL);

		UserEntity user = userService.checkUserExistence(str[0], Integer.parseInt(str[1]));

		List<GrantedAuthority> authorityList = new ArrayList<>();
		int deviceId = 0;

		if (user == null) {
			authorityList.add(new SimpleGrantedAuthority(StringConst.INVALID_USER));
			return new JwtUser(0, StringConst.INVALID_PASWORD, StringConst.INVALID_EMAIL, authorityList, deviceId, null,
					0);
		} else {
			user.setUserRole(UserRoleEnum.valueOf(Integer.parseInt(str[1])));

			DeviceDetail sessionEntity = deviceDetailRepo.findByUser(user.getId());

			if (sessionEntity != null)
				deviceId = sessionEntity.getId();

			authorityList = user.getUserRoleMappings().stream()
					.map(authority -> new SimpleGrantedAuthority(authority.getMasterRole().getRole().name()))
					.collect(Collectors.toList());

			if (AppConst.USER_ROLE.ADMIN == user.getUserRoleMappings().get(0).getMasterRole().getId()) {
				return new JwtUser(user.getId(), user.getPassword(), user.getEmail(), authorityList, deviceId, user,
						user.getUserRole().getId());
			} else {
				return new JwtUser(user.getId(), user.getPassword(), user.getCountryCode() + user.getMobileNumber(),
						authorityList, deviceId, user, user.getUserRole().getId());
			}
		}
	}

}
