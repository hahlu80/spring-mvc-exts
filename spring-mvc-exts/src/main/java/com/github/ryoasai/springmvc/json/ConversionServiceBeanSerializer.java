package com.github.ryoasai.springmvc.json;

import java.io.IOException;
import java.util.Collection;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.BeanPropertyWriter;
import org.codehaus.jackson.map.ser.BeanSerializer;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.convert.ConversionService;

public class ConversionServiceBeanSerializer extends BeanSerializer {

	private ConversionService conversionService;
	
	private BeanWrapper beanWrapper;
	
	public ConversionServiceBeanSerializer(ConversionService conversionService, Class<?> type,
			BeanPropertyWriter[] writers) {
		
		this(conversionService, type, writers, null);
	}

	public ConversionServiceBeanSerializer(ConversionService conversionService, Class<?> type,
			Collection<BeanPropertyWriter> props) {
		this(conversionService, type, props.toArray(new BeanPropertyWriter[props.size()]));
	}
	
	public ConversionServiceBeanSerializer(ConversionService conversionService, Class<?> type,
			BeanPropertyWriter[] props, BeanPropertyWriter[] filteredProps) {
		super(type, props, filteredProps); // FIXME store direct array reference.

		this.conversionService = conversionService;

		replaceBeanPropertyWriters(_props);
		
	}

	private void replaceBeanPropertyWriters(BeanPropertyWriter[] propWriters) {
		for (int i = 0; i < propWriters.length; i++) {
			propWriters[i] = new ConversionServiceBeanPropertyWriter(this, conversionService, propWriters[i]);
		}
	}

	@Override
	protected void serializeFields(Object bean, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonGenerationException {
		
		this.beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(bean);
		
		super.serializeFields(bean, jgen, provider);
	}

	public BeanWrapper getBeanWrapper() {
		return beanWrapper;
	}
	
	
	
	

}
