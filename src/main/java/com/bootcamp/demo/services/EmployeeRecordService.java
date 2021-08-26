package com.bootcamp.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bootcamp.demo.pojos.EmployeeRecord;
import com.bootcamp.demo.pojos.EmployeeSkill;
import com.bootcamp.demo.repository.EmployeeRepository;
import com.bootcamp.demo.repository.EmployeeSkillRepository;
import com.bootcamp.demo.util.Utils;

import reactor.core.publisher.Mono;

@Service
public class EmployeeRecordService {
	@Autowired
	EmployeeSkillRepository employeeSkillRepository;

	@Autowired
	EmployeeRepository employeeRepository;

	public Mono<EmployeeRecord> createEmployee(Mono<EmployeeRecord> employeeRecord) {
		return employeeRecord.flatMap(record -> getEmployeeRecord(record.getId()))
				.flatMap(record -> respondExisting(record)).switchIfEmpty(createRecord(employeeRecord));
	}

	private Mono<EmployeeRecord> createRecord(Mono<EmployeeRecord> employeeRecord) {
		return employeeRecord.flatMap(record -> employeeRepository.save(Utils.getEmployeePart(record)))
				.zipWith(createEmployeeSkill(employeeRecord))
				.map(tuple -> Utils.createRecord(tuple.getT1(), tuple.getT2())).flatMap(record -> {
					record.setStatus("Created");
					return Mono.just(record);
				});
	}

	private Mono<EmployeeRecord> respondExisting(EmployeeRecord record) {
		record.setStatus("Already Exists");
		return Mono.just(record);
	}

	private Mono<EmployeeSkill> createEmployeeSkill(Mono<EmployeeRecord> employeeRecord) {
		return employeeRecord.flatMap(record -> employeeSkillRepository.save(Utils.getEmployeeSkillPart(record)));
	}

	public Mono<EmployeeRecord> getEmployeeRecord(int id) {
		return employeeRepository.findById(id)
				.flatMap(employee -> Mono.just(employee).zipWith(employeeSkillRepository.findById(id))
						.map(tuple -> Utils.createRecord(tuple.getT1(), tuple.getT2())))
				.switchIfEmpty(Mono.empty());
	}

	public Mono<EmployeeSkill> getEmployeeSkillById(int id) {
		return employeeSkillRepository.findById(id);
	}
}
