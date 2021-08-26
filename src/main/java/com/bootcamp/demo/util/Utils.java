package com.bootcamp.demo.util;

import com.bootcamp.demo.pojos.Employee;
import com.bootcamp.demo.pojos.EmployeeRecord;
import com.bootcamp.demo.pojos.EmployeeSkill;


public class Utils {

	public static Employee getEmployeePart(EmployeeRecord record) {
		return new Employee(record.getId(), record.getName(), record.getPhone(), record.getCity());
	}

	public static EmployeeSkill getEmployeeSkillPart(EmployeeRecord record) {
		return new EmployeeSkill(record.getId(), record.getJavaExp(), record.getSpringExp());
	}

	public static EmployeeRecord createRecord(Employee e, EmployeeSkill es) {
		return new EmployeeRecord(e.getId(), e.getName(), e.getPhone(), e.getCity(), es.getJavaExp(),
				es.getSpringExp(), "");
	}
}