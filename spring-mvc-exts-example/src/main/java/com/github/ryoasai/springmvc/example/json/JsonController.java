package com.github.ryoasai.springmvc.example.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.ryoasai.springmvc.grid.Grids;

@Controller
@RequestMapping("/json")
public class JsonController {

	@Inject
	ConversionService conversionService;
	
	@RequestMapping(value="/jsonList", method=RequestMethod.GET)
	@ResponseBody
	public List<Person> jsonList() {
		return findPersonList();		
	}
	
	@RequestMapping(value="/jsonTable", method=RequestMethod.GET)
	@ResponseBody
	public Object[][] jsonTable() {
		List<Person> personList = findPersonList();
		return Grids.toArray(personList, conversionService, "name", "age", "birthDate");
	}
	
	private List<Person> findPersonList() {
		List<Person> personList = new ArrayList<Person>();
		for (int i = 0; i < 10; i++) {
			personList.add(new Person("test", i, new Date()));
		}
		
		return personList;
	}
}
