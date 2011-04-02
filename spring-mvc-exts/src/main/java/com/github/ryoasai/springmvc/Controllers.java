package com.github.ryoasai.springmvc;

import java.lang.reflect.Method;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class Controllers {

	public static final String HANDLER_METHOD_KEY = "_HANDLER_METHOD_KEY";
	
	private Controllers() {}
	
	public static Method getCurrentHandlerMethod() {
		return (Method)RequestContextHolder.getRequestAttributes().getAttribute(HANDLER_METHOD_KEY, RequestAttributes.SCOPE_REQUEST);
	}
}
