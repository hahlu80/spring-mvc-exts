package com.github.ryoasai.springmvc.example.json;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.github.ryoasai.springmvc.utils.DateUtils;

public class Person {

    @NotNull
    private String name;

    private int age;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    public Person() {
        this("test", 30, DateUtils.create(2010, 5, 2));
    }

    public Person(String name, int age, Date birthDate) {
        this.name = name;
        this.age = age;
        this.birthDate = birthDate;
    }

    @Size(max = 15)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
}
