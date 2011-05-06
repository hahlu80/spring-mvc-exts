package com.github.ryoasai.springmvc.utils;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class DateUtilsTest {

	@Test
	public void createYMD() {
		Date date = DateUtils.create(2010, 4, 12);
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		assertThat(df.format(date), is("2010-04-12 00:00:00"));
	}
	
	@Test
	public void createYMD_HMS() {
		Date date = DateUtils.create(2010, 4, 12, 1, 2, 3);
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		assertThat(df.format(date), is("2010-04-12 01:02:03"));
	}
}
