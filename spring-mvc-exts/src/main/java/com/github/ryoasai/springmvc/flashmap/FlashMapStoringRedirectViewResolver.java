/*
 * Copyright 2004-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.ryoasai.springmvc.flashmap;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

/**
 * View resolver that can delegate to the specialized view which can store model data to a flash map.
 * 
 * @author Ryo
 */
public class FlashMapStoringRedirectViewResolver extends UrlBasedViewResolver {

	public static final String REDIRECT_WITH_FLASH_URL_PREFIX = "redirect_with_flash:";

	private String redirectWithFlashUrlPrefix = REDIRECT_WITH_FLASH_URL_PREFIX;

	public FlashMapStoringRedirectViewResolver() {
		setViewClass(FlashMapStoringRedirectView.class);
	}

	public String getRedirectWithFlashUrlPrefix() {
		return redirectWithFlashUrlPrefix;
	}

	public void setRedirectWithFlashUrlPrefix(String redirectWithFlashUrlPrefix) {
		this.redirectWithFlashUrlPrefix = redirectWithFlashUrlPrefix;
	}

	
	@Override
	protected Class<FlashMapStoringRedirectView> requiredViewClass() {
		return FlashMapStoringRedirectView.class;
	}

	@Override
	protected View createView(String viewName, Locale locale) throws Exception {
		// If this resolver is not supposed to handle the given view,
		// return null to pass on to the next resolver in the chain.
		if (!canHandle(viewName, locale)) {
			return null;
		}
		
		if (viewName.startsWith(getRedirectWithFlashUrlPrefix())) {
			String redirectUrl = viewName.substring(getRedirectWithFlashUrlPrefix().length());
			return new FlashMapStoringRedirectView(redirectUrl, isRedirectContextRelative(), isRedirectHttp10Compatible());
		}
		
		return null;
	}

	private static class FlashMapStoringRedirectView extends RedirectView implements View {

		public FlashMapStoringRedirectView(String redirectUrl, boolean redirectContextRelative, boolean redirectHttp10Compatible) {
			super(redirectUrl, redirectContextRelative, redirectHttp10Compatible);
			setExposeModelAttributes(false); // Do not expose model attributes as URL params.
		}

		@Override
		protected void renderMergedOutputModel(
				Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
				throws IOException {

			// Store the model map in the flash map.
			FlashMap.getCurrent(request).putAll(model);
			
			super.renderMergedOutputModel(model, request, response);
		}

	}
}
