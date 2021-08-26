package com.bootcamp.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bootcamp.demo.pojos.Employee;
import com.bootcamp.demo.repository.EmployeeRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EmployeeService {
	@Autowired
    EmployeeRepository employeeRepository;
	
    public void updateAllEmployees(List<Employee> employees) {
        Flux<Employee> savedEmployees = employeeRepository.saveAll(employees);
        savedEmployees.subscribe();
    }
    
    public void updateEmployee(Employee employee) {
        Mono<Employee> saveEmployee = employeeRepository.save(employee);
        saveEmployee.subscribe();
    }

    public Flux<Employee> getAllEmployees() {
        Flux<Employee> employees =  employeeRepository.findAll();
        return employees;
    }

    public Mono<Employee> getEmployeeById(int id) {
        return employeeRepository.findById(id);
    }
}
