package com.github.ryoasai.springmvc.json;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.BeanPropertyWriter;
import org.codehaus.jackson.type.JavaType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;

public class ConversionServiceBeanPropertyWriter extends BeanPropertyWriter {

	private ConversionService conversionService;
	private ConversionServiceBeanSerializer beanSerializer;

	public ConversionServiceBeanPropertyWriter(
			ConversionServiceBeanSerializer beanSerializer,
			ConversionService conversionService, BeanPropertyWriter base) {
		super(base);
		this.beanSerializer = beanSerializer;
		this.conversionService = conversionService;
	}

	@Override
	public void serializeAsField(Object bean, JsonGenerator jgen,
			SerializerProvider prov) throws Exception {

		Object value = get(bean);

		BeanWrapper beanWrapper = beanSerializer.getBeanWrapper();

		TypeDescriptor fieldDesc = beanWrapper
				.getPropertyTypeDescriptor(getName());
		TypeDescriptor strDesc = TypeDescriptor.valueOf(String.class);
		
		if (fieldDesc != null && BeanUtils.isSimpleValueType(fieldDesc.getType()) 
				&& this.conversionService.canConvert(fieldDesc, strDesc)) {

			Object formattedValue = this.conversionService.convert(value,
					fieldDesc, strDesc);
			doSerializeAsField(bean, formattedValue, jgen, prov);

		} else {
			doSerializeAsField(bean, value, jgen, prov);
		}

	}

	private void doSerializeAsField(Object bean, Object value,
			JsonGenerator jgen, SerializerProvider prov) throws Exception {
		// Null handling is bit different, check that first
		if (value == null) {
			if (!_suppressNulls) {
				jgen.writeFieldName(_name);
				prov.getNullValueSerializer().serialize(value, jgen, prov);
			}
			return;
		}
		// For non-nulls, first: simple check for direct cycles
		if (value == bean) {
			_reportSelfReference(bean);
		}
		if (_suppressableValue != null && _suppressableValue.equals(value)) {
			return;
		}
		
		JsonSerializer<Object> ser = _serializer;
		if (ser == null) {
			Class<?> cls = value.getClass();
			if (_nonTrivialBaseType != null) {
				JavaType t = _nonTrivialBaseType.forcedNarrowBy(cls);
				ser = prov.findValueSerializer(t);
			} else {
				ser = prov.findValueSerializer(cls);
			}
		}
		jgen.writeFieldName(_name);
		if (_typeSerializer == null) {
			ser.serialize(value, jgen, prov);
		} else {
			ser.serializeWithType(value, jgen, prov, _typeSerializer);
		}
	}

}
