package com.github.ryoasai.springmvc;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Aspect
public class HandlerMethodExposingAspect {

	@Pointcut("execution(@org.springframework.web.bind.annotation.RequestMapping * *(..))")
	public void handlerMethod() {}
	
	@Before("handlerMethod()")
	public void interceptHandlerMethod(JoinPoint jp) {
		
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		MethodSignature methodSignature = (MethodSignature) jp.getSignature();
		
		requestAttributes.setAttribute(Controllers.HANDLER_METHOD_KEY, methodSignature.getMethod(), RequestAttributes.SCOPE_REQUEST);
	}
}
