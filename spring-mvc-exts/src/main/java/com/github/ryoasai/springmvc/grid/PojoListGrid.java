package com.github.ryoasai.springmvc.grid;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

public class PojoListGrid implements Grid {

	private List<Object> pojoList;
	private String[] props;

	private BeanWrapperImpl beanWrapper = new BeanWrapperImpl();
	private ConversionService conversionService;
	
	public PojoListGrid(List<?> pojoList, ConversionService conversionService, String... props) {
		this.pojoList = new ArrayList<Object>(pojoList);
		this.props = props;
		this.conversionService = conversionService;
		beanWrapper.setConversionService(conversionService);
	}

	public PojoListGrid(List<?> pojoList, String... props) {
		this(pojoList, null, props);
	}

	@Override
	public Object get(int row, int column) {
		Object pojo = pojoList.get(row);
		beanWrapper.setWrappedInstance(pojo);

		TypeDescriptor fieldDesc = beanWrapper
				.getPropertyTypeDescriptor(props[column]);
		TypeDescriptor strDesc = TypeDescriptor.valueOf(String.class);

		Object value = beanWrapper.getPropertyValue(props[column]);

		if (canConvert(value, fieldDesc, strDesc)) {
			return this.conversionService.convert(value, fieldDesc, strDesc);
		} else {
			return value;
		}
	}

	private boolean canConvert(Object value, TypeDescriptor fieldDesc, TypeDescriptor strDesc) {
		return value != null 
		 && conversionService != null 
				&& BeanUtils.isSimpleValueType(fieldDesc.getType()) 
				&& fieldDesc != null 
				&& (fieldDesc.getAnnotation(NumberFormat.class) != null || fieldDesc.getAnnotation(DateTimeFormat.class) != null)
				&& this.conversionService.canConvert(fieldDesc, strDesc);
	}
	
	@Override
	public void set(int row, int column, Object value) {
		Object pojo = pojoList.get(row);
		beanWrapper.setWrappedInstance(pojo);

		TypeDescriptor fieldDesc = beanWrapper
		.getPropertyTypeDescriptor(props[column]);
		TypeDescriptor strDesc = TypeDescriptor.valueOf(String.class);

		if (canConvert(value, fieldDesc, strDesc)) {
			beanWrapper.setPropertyValue(props[column], conversionService.convert(value, strDesc, fieldDesc));
		} else {
			beanWrapper.setPropertyValue(props[column], value);
		}
	}

	@Override
	public int columns() {
		return props.length;
	}

	@Override
	public int rows() {
		return pojoList.size();
	}

	@Override
	public Object[][] toArray2D() {
		Object[][] result = new Object[rows()][columns()];
		for (int row = 0; row < rows(); row++) {
			for (int column = 0; column < columns(); column++) {
				result[row][column] = get(row, column);
			}
		}
		return result;
	}
}
