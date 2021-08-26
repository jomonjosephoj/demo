package com.bootcamp.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bootcamp.demo.pojos.Employee;
import com.bootcamp.demo.pojos.EmployeeRecord;
import com.bootcamp.demo.pojos.EmployeeSkill;
import com.bootcamp.demo.pojos.EmployeeSkillRequest;
import com.bootcamp.demo.repository.EmployeeRepository;
import com.bootcamp.demo.repository.EmployeeSkillRepository;
import com.bootcamp.demo.util.Utils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EmployeeSkillService {
	@Autowired
	EmployeeSkillRepository employeeSkillRepository;

	@Autowired
	EmployeeRepository employeeRepository;

	public void updateEmployeeSkill(EmployeeSkill employeeSkill) {
		Mono<EmployeeSkill> saveEmployeeSkill = employeeSkillRepository.save(employeeSkill);
		saveEmployeeSkill.subscribe();
	}

	public Mono<EmployeeSkill> getEmployeeSkillById(int id) {
		return employeeSkillRepository.findById(id);
	}

	public Flux<EmployeeSkill> getEmployeeSkillById(double j, double s) {
		return employeeSkillRepository.findSkilledEmployees(j, s);
	}

	public Flux<EmployeeRecord> getEmployeesWithSkill(EmployeeSkillRequest request) {
		double javaExp = request.getJavaExp();
		double springExp = request.getSpringExp();
		Flux<EmployeeSkill> skilledEmplloyees = employeeSkillRepository.findSkilledEmployees(javaExp, springExp);
		return skilledEmplloyees.flatMap(skilledEmployee -> findEmployee(skilledEmployee.getId())
				.zipWith(Mono.just(skilledEmployee)).map(tuple -> Utils.createRecord(tuple.getT1(), tuple.getT2())));
	}

	private Mono<Employee> findEmployee(int id) {
		return employeeRepository.findById(id);
	}
	
	public Mono<EmployeeRecord> deleteEmployee(int id) {
		employeeRepository.deleteById(id).subscribe();
		employeeSkillRepository.deleteById(id).subscribe();
		EmployeeRecord er = new EmployeeRecord();
		er.setStatus(String.format("Employeed id %s deleted",id));
		return Mono.just(er);	
	}
}
