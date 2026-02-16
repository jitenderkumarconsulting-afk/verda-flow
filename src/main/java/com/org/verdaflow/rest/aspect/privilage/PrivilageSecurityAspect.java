package com.org.verdaflow.rest.aspect.privilage;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.org.verdaflow.rest.config.common.StringConst;
import com.org.verdaflow.rest.config.security.jwt.JwtUser;
import com.org.verdaflow.rest.exception.CustomeAccessDeniedException;

@Aspect
@Component
public class PrivilageSecurityAspect {

	@Before("@annotation(RequiredPrivilages)")
	public void secureFunction(JoinPoint joinPoint) throws Throwable {
		RequiredPrivilages requiredAnnotation = getRequiredPrivilageAnnotation(joinPoint);
		verifyPrivilages(requiredAnnotation, joinPoint.getArgs());
	}

	private void verifyPrivilages(RequiredPrivilages requiredAnnotation, Object[] args) throws AuthenticationException {
		PrivilageEnum[] privilageEnum = requiredAnnotation.value();
		JwtUser jwtUser = (JwtUser) args[0];
		ArrayList<? extends GrantedAuthority> authRoleList = (ArrayList<? extends GrantedAuthority>) jwtUser
				.getAuthorities();
		GrantedAuthority auth = authRoleList.get(0);
		String role = auth.getAuthority();
		if (!privilageEnum[0].role.equalsIgnoreCase(role)) {
			throw new CustomeAccessDeniedException(StringConst.NOT_AUTHORIZED_TO_PERFORM_THIS_OPERATION_DOT);
		}
	}

	private RequiredPrivilages getRequiredPrivilageAnnotation(JoinPoint joinPoint) throws NoSuchMethodException {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Class<?> clazz = signature.getDeclaringType();
		Method method = clazz.getDeclaredMethod(signature.getName(), signature.getParameterTypes());
		return method.getAnnotation(RequiredPrivilages.class);
	}

}
