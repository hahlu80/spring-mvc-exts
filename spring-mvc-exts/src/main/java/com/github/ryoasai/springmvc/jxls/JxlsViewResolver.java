package com.github.ryoasai.springmvc.jxls;

import java.lang.reflect.Method;
import java.util.Locale;

import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.github.ryoasai.springmvc.Controllers;

/**
 * {@link org.springframework.web.servlet.ViewResolver} implementation that
 * resolves instances of {@link JxlsView} by translating
 * the supplied view name into the URL of an excel file.
 * 
 * @author Ryo Asai
 */
public class JxlsViewResolver extends UrlBasedViewResolver {

	public JxlsViewResolver() {
		setViewClass(JxlsView.class);
	}
	
	/**
	 * Requires the view class to be a subclass of {@link JxlsView}.
	 */
	@Override
	protected Class<JxlsView> requiredViewClass() {
		return JxlsView.class;
	}

	@Override
	protected boolean canHandle(String viewName, Locale locale) {
		Method currentHandlerMethod = Controllers.getCurrentHandlerMethod();
		
		return 
			currentHandlerMethod != null
			&& currentHandlerMethod.isAnnotationPresent(Jxls.class) 
			&& super.canHandle(viewName, locale);
	}

}
