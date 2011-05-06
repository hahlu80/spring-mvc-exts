package com.github.ryoasai.springmvc.grid;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

import com.github.ryoasai.springmvc.utils.DateUtils;

public class PojoListGrid_WithConversionTest {

	PojoListGrid target;

	SamplePojo samplePojo1 = new SamplePojo();
	SamplePojo samplePojo2 = new SamplePojo();

	List<SamplePojo> samplePojoList = Arrays.asList(samplePojo1, samplePojo2);

	FormattingConversionService conversionService;

	@Before
	public void setUp() throws Exception {
		FormattingConversionServiceFactoryBean factoryBean = new FormattingConversionServiceFactoryBean();
		factoryBean.afterPropertiesSet();
		conversionService = factoryBean.getObject();

		samplePojo1.setStrField("Hello");
		samplePojo2.setIntField(100);
		samplePojo2.setDateField(DateUtils.create(2010, 5, 6));

		target = new PojoListGrid(samplePojoList, conversionService,  "strField", "intField",
				"dateField");
	}

	@Test
	public void columns() {
		assertThat(target.columns(), is(3));
		
	}

	@Test
	public void rows() {
		assertThat(target.rows(), is(2));
	}

	@Test
	public void get() {
		assertThat((String) target.get(0, 0), is("Hello"));
		assertThat((Integer)target.get(0, 1), is(0));
		assertThat(target.get(0, 2), is(nullValue()));

		assertThat(target.get(1, 0), is(nullValue()));
		assertThat((Integer) target.get(1, 1), is(100));
		assertThat((String) target.get(1, 2), is("2010-05-06"));
	}

	@Test
	public void set() {
		target.set(0, 1, 200);
		target.set(1, 2, "2011-12-04");
		assertThat((Integer) target.get(0, 1), is(200));
		assertThat((String) target.get(1, 2), is("2011-12-04"));
	}

	@Test
	public void toArray2D() {
		Object[][] expected = { { "Hello", 0, null }, { null, 100 , "2010-05-06"} };
		assertArrayEquals(expected, target.toArray2D());
	}

}
