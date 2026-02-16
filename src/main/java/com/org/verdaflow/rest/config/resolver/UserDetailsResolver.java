package com.org.verdaflow.rest.config.resolver;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.org.verdaflow.rest.config.common.StringConst;

public class UserDetailsResolver implements HandlerMethodArgumentResolver {

	public boolean supportsParameter(MethodParameter methodParameter) {
		return true;
	}

	public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
			NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
		HttpServletRequest request = (HttpServletRequest) nativeWebRequest.getNativeRequest();
		return request.getAttribute(StringConst.USER_DETAILS);
	}
}
