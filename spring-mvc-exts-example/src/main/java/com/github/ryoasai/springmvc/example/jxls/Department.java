package com.github.ryoasai.springmvc.example.jxls;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Sample Department bean to demostrate main excel export features
 * author: Leonid Vysochyn
 */
public class Department {
    private String name;
    private Employee chief = new Employee();
    private List<Employee> staff = new ArrayList<Employee>();

    public Department(String name) {
        this.name = name;
    }

    public Department(String name, Employee chief, List<Employee> staff) {
        this.name = name;
        this.chief = chief;
        this.staff = staff;
    }

    public Department() {
	}

	public void addEmployee(Employee employee) {
        staff.add(employee);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Employee getChief() {
        return chief;
    }

    public void setChief(Employee chief) {
        this.chief = chief;
    }

    public List<Employee> getStaff() {
        return staff;
    }

    public void setStaff(List<Employee> staff) {
        this.staff = staff;
    }

    @Override
    public String toString() {
    	return ReflectionToStringBuilder.toString(this, ToStringStyle.SIMPLE_STYLE);
    }
}
