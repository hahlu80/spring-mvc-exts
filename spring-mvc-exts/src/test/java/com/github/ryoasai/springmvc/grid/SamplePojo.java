package com.github.ryoasai.springmvc.grid;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class SamplePojo {

	private String strField;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date dateField;

	private int intField;

	public String getStrField() {
		return strField;
	}

	public void setStrField(String hoge) {
		this.strField = hoge;
	}

	public Date getDateField() {
		return dateField;
	}

	public void setDateField(Date foo) {
		this.dateField = foo;
	}

	public int getIntField() {
		return intField;
	}

	public void setIntField(int bar) {
		this.intField = bar;
	}

}
