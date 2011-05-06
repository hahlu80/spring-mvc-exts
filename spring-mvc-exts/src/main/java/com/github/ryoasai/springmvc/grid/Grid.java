package com.github.ryoasai.springmvc.grid;

public interface Grid {

	Object get(int row, int column);

	void set(int row, int column, Object value);

	int columns();

	int rows();

	Object[][] toArray2D();
}
