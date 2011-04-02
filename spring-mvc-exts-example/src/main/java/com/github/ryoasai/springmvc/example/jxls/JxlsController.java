package com.github.ryoasai.springmvc.example.jxls;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import com.github.ryoasai.springmvc.jxls.Jxls;
import com.github.ryoasai.springmvc.jxls.JxlsFileReader;

@Controller
@RequestMapping("/jxls")
public class JxlsController {

	@Inject
	JxlsFileReader jxlsFileReader;

	@RequestMapping("/department")
	@Jxls(filename = "department.xls")
	public Department department() {
		return buildSampleDepartment();
	}

	@RequestMapping("/department_filename_in_model")
	@Jxls(filename = "filenameKey")
	public String departmentFileNameInModel(Model model) {
		model.addAttribute(buildSampleDepartment());
		// Filename can be specified as model value.
		model.addAttribute("filenameKey", "test.xls");

		return "/jxls/department";
	}

	private Resource uploadFileTemplate = new ClassPathResource(
			"department.xml", getClass());

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String handleFormUpload(@RequestParam("file") MultipartFile file,
			Model model) throws InvalidFormatException, IOException,
			SAXException {

		Department department = new Department();
		model.addAttribute(department);
		jxlsFileReader.read(uploadFileTemplate, file, model);

		System.out.println(department);
		System.out.println(department.getChief());
		System.out.println(department.getStaff());

		return "redirect:/";
	}

	private Department buildSampleDepartment() {
		Department department = new Department("IT");
		Calendar calendar = Calendar.getInstance();
		calendar.set(1970, 12, 2);
		Date d1 = calendar.getTime();
		calendar.set(1980, 2, 15);
		Date d2 = calendar.getTime();
		calendar.set(1976, 7, 20);
		Date d3 = calendar.getTime();
		calendar.set(1968, 5, 6);
		Date d4 = calendar.getTime();
		calendar.set(1978, 8, 17);
		Date d5 = calendar.getTime();
		Employee chief = new Employee("Derek", 35, 3000, 0.30, d1);
		department.setChief(chief);
		Employee elsa = new Employee("Elsa", 28, 1500, 0.15, d2);
		department.addEmployee(elsa);
		Employee oleg = new Employee("Oleg", 32, 2300, 0.25, d3);
		department.addEmployee(oleg);
		Employee neil = new Employee("Neil", 34, 2500, 0.00, d4);
		department.addEmployee(neil);
		Employee maria = new Employee("Maria", 34, 1700, 0.15, d5);
		department.addEmployee(maria);
		Employee john = new Employee("John", 35, 2800, 0.20, d2);
		department.addEmployee(john);
		maria.setSuperior(oleg);
		oleg.setSuperior(john);
		neil.setSuperior(john);

		return department;
	}
}
