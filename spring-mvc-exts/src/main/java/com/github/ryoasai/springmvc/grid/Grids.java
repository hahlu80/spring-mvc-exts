package com.github.ryoasai.springmvc.grid;

import java.util.List;

import org.springframework.core.convert.ConversionService;

public class Grids {
	private Grids() {}

	public static Object[][] toArray(List<?> beanList, ConversionService conversionService, String... props) {
		Grid grid = new PojoListGrid(beanList, conversionService, props);
		return toArray(grid);
	}
	
	public static Object[][] toArray(List<?> beanList, String... props) {
		return toArray(beanList, null, props);
	}
	
	public static Object[][] toArray(Grid grid) {
		Object[][] result = new Object[grid.rows()][grid.columns()];
		for (int row = 0; row < grid.rows(); row++) {
			for (int col = 0; col < grid.columns(); col++) {
				result[row][col] = grid.get(row, col);
			}	
		}
		
		return result;
	}
}
