package com.bootcamp.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.demo.pojos.EmployeeRecord;
import com.bootcamp.demo.pojos.EmployeeSkillRequest;
import com.bootcamp.demo.services.EmployeeRecordService;
import com.bootcamp.demo.services.EmployeeService;
import com.bootcamp.demo.services.EmployeeSkillService;
import com.bootcamp.demo.services.ReactiveKafkaProducerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class EmployeeController {
	
	@Value(value = "${app_updates_topic}")
	private String topic;

	@Autowired
	EmployeeService employeeService;

	@Autowired
	EmployeeSkillService employeeSkillService;

	@Autowired
	EmployeeRecordService employeeRecordService;

	@Autowired
	ReactiveKafkaProducerService kafkaProducerService;

	@Autowired
	ObjectMapper objectMapper;

	@PostMapping("/createEmployee")
	public Mono<EmployeeRecord> createEmployee(@RequestBody EmployeeRecord employeeRecord)
			throws JsonProcessingException {
		kafkaProducerService.send(topic,objectMapper.writeValueAsString(employeeRecord));
		return employeeRecordService.createEmployee(Mono.just(employeeRecord));
	}

	@PostMapping("/findEmpSkillset")
	public Flux<EmployeeRecord> findEmployeeSkillSet(@RequestBody EmployeeSkillRequest employeeSkillRequest) {
		return employeeSkillService.getEmployeesWithSkill(employeeSkillRequest);
	}
	
	@GetMapping("/deleteEmployee/{id}")
	public Mono<EmployeeRecord> deleteEmployee(@PathVariable int id) {
		return employeeSkillService.deleteEmployee(id);
	}

}
