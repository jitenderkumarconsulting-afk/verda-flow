package com.org.verdaflow.rest.config.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.org.verdaflow.rest.common.enums.UserRoleEnum;
import com.org.verdaflow.rest.config.common.StringConst;
import com.org.verdaflow.rest.error.RestErrorConstants;

@Component
public class JwtAuthenticationTokenFilter extends GenericFilterBean {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	public static final Logger log = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);

	public JwtAuthenticationTokenFilter(UserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil) {
		this.userDetailsService = userDetailsService;
		this.jwtTokenUtil = jwtTokenUtil;
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;

		String authToken = httpRequest.getHeader(StringConst.TOKEN_HEADER);
		String username = null;
		int roleId = 0;

		if (authToken != null) {
			username = jwtTokenUtil.getUsernameFromToken(authToken);
			roleId = jwtTokenUtil.getRoleIdFromToken(authToken);
		}

		if (username != null && roleId != 0 && SecurityContextHolder.getContext().getAuthentication() == null) {
			String userNameWithRoleId = new StringBuilder(username).append(JwtTokenUtil.USERNAME_ROLEID_JOINING_SYMBOL)
					.append(roleId).toString();

			UserDetails userDetails = this.userDetailsService.loadUserByUsername(userNameWithRoleId);

			if (null != userDetails) {
				if (jwtTokenUtil.validateToken(authToken, userDetails, (HttpServletResponse) response)) {
					if (isAccountActive(userDetails)) {
						UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities());
						authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
						SecurityContextHolder.getContext().setAuthentication(authentication);
						httpRequest.setAttribute(StringConst.USER_DETAILS, userDetails);
					} else {
						request.setAttribute(RestErrorConstants.AUTHENTICATION_ERROR,
								RestErrorConstants.UNAUTHORIZED_ACCOUNT_DEACTIVATED_MSG);
					}
				} else {
					request.setAttribute(RestErrorConstants.AUTHENTICATION_ERROR,
							RestErrorConstants.UNAUTHORIZED_TOKEN_EXPIRED_MSG);
				}
			} else {
				request.setAttribute(RestErrorConstants.AUTHENTICATION_ERROR,
						RestErrorConstants.UNAUTHORIZED_INVALID_TOKEN_MSG);
			}
		} else {
			request.setAttribute(RestErrorConstants.AUTHENTICATION_ERROR,
					RestErrorConstants.UNAUTHORIZED_INVALID_TOKEN_MSG);
		}

		chain.doFilter(request, response);
	}

	private boolean isAccountActive(UserDetails userDetails) {
		JwtUser user = (JwtUser) userDetails;
		boolean active = true;

		if (null == user || null == user.getUserEntity() || !user.getUserEntity().isActive())
			active = false;
		else {
			switch (UserRoleEnum.valueOf(user.getRoleId())) {
				case DISPATCHER:
					if (null == user.getUserEntity().getUserDispatcherDetail()
							|| !user.getUserEntity().getUserDispatcherDetail().isActive())
						active = false;
					break;

				case DRIVER:
					if (null == user.getUserEntity().getUserDriverDetail()
							|| !user.getUserEntity().getUserDriverDetail().isActive()
							|| null == user.getUserEntity().getUserDriverDetail().getUserDispatcherDetail()
							|| !user.getUserEntity().getUserDriverDetail().getUserDispatcherDetail().isActive())
						active = false;
					break;

				case CUSTOMER:
					if (null == user.getUserEntity().getUserCustomerDetail()
							|| !user.getUserEntity().getUserCustomerDetail().isActive())
						active = false;
					break;

				default:
					break;
			}
		}

		return active;
	}

}
