package com.github.ryoasai.springmvc.flashmap;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class FlashMap {

	static final String FLASH_SCOPE_ATTRIBUTE = FlashMap.class.getName();
	
	public static Map<String, Object> getCurrent(HttpServletRequest request) {
		HttpSession session = request.getSession(); 

		@SuppressWarnings("unchecked")
		Map<String, Object> flash = (Map<String, Object>) session.getAttribute(FLASH_SCOPE_ATTRIBUTE);
		if (flash == null) {
			flash = new HashMap<String, Object>();
			session.setAttribute(FLASH_SCOPE_ATTRIBUTE, flash);
		}
		
		return flash;
	}
	
	private FlashMap() {}
}
