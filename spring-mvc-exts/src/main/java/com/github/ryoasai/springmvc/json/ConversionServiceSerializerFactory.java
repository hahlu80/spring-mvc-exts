package com.github.ryoasai.springmvc.json;

import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.ser.AnyGetterWriter;
import org.codehaus.jackson.map.ser.BeanPropertyWriter;
import org.codehaus.jackson.map.ser.BeanSerializer;
import org.codehaus.jackson.map.ser.BeanSerializerFactory;
import org.codehaus.jackson.map.ser.MapSerializer;
import org.codehaus.jackson.type.JavaType;
import org.springframework.core.convert.ConversionService;

public class ConversionServiceSerializerFactory extends BeanSerializerFactory {

	private ConversionService conversionService;

	public ConversionServiceSerializerFactory(
			ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	@Override
	protected JsonSerializer<Object> constructBeanSerializer(
			SerializationConfig config, BasicBeanDescription beanDesc) {

		// First: any detectable (auto-detect, annotations) properties to
		// serialize?
		List<BeanPropertyWriter> props = findBeanProperties(config, beanDesc);
		AnnotatedMethod anyGetter = beanDesc.findAnyGetter();
		// No properties, no serializer
		// 16-Oct-2010, tatu: Except that @JsonAnyGetter needs to count as
		// getter
		if (props == null || props.size() == 0) {
			if (anyGetter == null) {
				/*
				 * 27-Nov-2009, tatu: Except that as per [JACKSON-201], we are
				 * ok with that as long as it has a recognized class annotation
				 * (which may come from a mix-in too)
				 */
				if (beanDesc.hasKnownClassAnnotations()) {
					return BeanSerializer.createDummy(beanDesc.getBeanClass());
				}
				return null;
			}
			props = Collections.emptyList();
		} else {
			// Any properties to suppress?
			props = filterBeanProperties(config, beanDesc, props);
			// Do they need to be sorted in some special way?
			props = sortBeanProperties(config, beanDesc, props);
		}
		
		/////////////////////////////
		BeanSerializer ser = new ConversionServiceBeanSerializer(conversionService, beanDesc.getBeanClass(), props);
		/////////////////////////////
		
		if (anyGetter != null) { // since 1.6
			JavaType type = anyGetter.getType(beanDesc.bindingsForBeanType());
			// copied from BasicSerializerFactory.buildMapSerializer():
			boolean staticTyping = config
					.isEnabled(SerializationConfig.Feature.USE_STATIC_TYPING);
			JavaType valueType = type.getContentType();
			TypeSerializer typeSer = createTypeSerializer(valueType, config);
			MapSerializer mapSer = MapSerializer.construct(
					/* ignored props */null, type, staticTyping, typeSer);
			ser.setAnyGetter(new AnyGetterWriter(anyGetter, mapSer));
		}

		// One more thing: need to gather view information, if any:
		ser = processViews(config, beanDesc, ser, props);
		return ser;
	}

}
