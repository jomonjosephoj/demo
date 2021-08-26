package com.bootcamp.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.demo.pojos.EmployeeRecord;
import com.bootcamp.demo.pojos.Job;
import com.bootcamp.demo.pojos.JobResponse;
import com.bootcamp.demo.services.JobService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping
public class JobController {

	@Autowired
	JobService jobService;

	@PostMapping("/createJobProfile")
	public Mono<JobResponse> createJobProfile(@RequestBody Job job) {
		return jobService.createJobProfile(job);
	}

	@PostMapping("/findEmpForJobID")
	public Flux<EmployeeRecord> findEmpForJobID(@RequestBody Job job) {
		return jobService.findEmpForJobID(job.getId());
	}

	@PostMapping("/getJobProfileFromCache")
	public Mono<Job> getJobProfileFromCache(@RequestBody Job job) {
		return jobService.getJobProfileFromCache(job);
	}
	
	@GetMapping("/deleteJob/{id}")
	public Mono<JobResponse> deleteJob(@PathVariable int id) {
		return jobService.deleteJob(id);
	}

}
