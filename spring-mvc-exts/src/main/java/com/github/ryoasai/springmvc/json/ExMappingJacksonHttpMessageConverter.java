package com.github.ryoasai.springmvc.json;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

public class ExMappingJacksonHttpMessageConverter extends MappingJacksonHttpMessageConverter {

	private ConversionService conversionService;

	public ConversionService getConversionService() {
		return conversionService;
	}

	@Inject
	public void setConversionService(ConversionService conversionService) {
		this.conversionService = conversionService;
	}
	
	@PostConstruct
	public void init() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializerFactory(
				new ConversionServiceSerializerFactory(conversionService));
		
		
		setObjectMapper(objectMapper);
	}
}
