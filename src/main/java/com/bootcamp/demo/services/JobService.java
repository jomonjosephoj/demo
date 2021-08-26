package com.bootcamp.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.bootcamp.demo.pojos.EmployeeRecord;
import com.bootcamp.demo.pojos.EmployeeSkillRequest;
import com.bootcamp.demo.pojos.Job;
import com.bootcamp.demo.pojos.JobResponse;
import com.bootcamp.demo.repository.JobRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.core.HazelcastInstance;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Log4j2
public class JobService {
	@Autowired
	JobRepository jobRepository;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	HazelcastInstance hzInstance;

	@Value(value = "${job.details.map.name}")
	private String jobDetailMapName;

	@Value(value = "${employee.service.url}")
	private String employeServiceUrl;

	public Mono<JobResponse> createJobProfile(Job job) {
		return Mono.just(job).flatMap(record -> jobRepository.findById(record.getId()))
				.flatMap(record -> respondExisting(record)).switchIfEmpty(createRecord(job));
	}

	private Mono<JobResponse> createRecord(Job job) {

		return jobRepository.save(job).flatMap(record -> {
			try {
				JobResponse jobResponse = objectMapper.readValue(objectMapper.writeValueAsString(record),
						JobResponse.class);
				jobResponse.setStatus("Created");
				return Mono.just(jobResponse);
			} catch (Exception ex) {
				JobResponse jobResponse = new JobResponse();
				jobResponse.setStatus("Error");
				return Mono.just(jobResponse);
			}
		});

	}

	private Mono<JobResponse> respondExisting(Job record) {
		JobResponse jobResponse;
		try {
			jobResponse = objectMapper.readValue(objectMapper.writeValueAsString(record), JobResponse.class);
			jobResponse.setStatus("Already Exists");
		} catch (Exception ex) {
			jobResponse = new JobResponse();
			jobResponse.setStatus("Error");
		}
		return Mono.just(jobResponse);
	}

	public Flux<EmployeeRecord> findEmpForJobID(int jobId) {
		return jobRepository.findById(jobId).flatMapMany(job -> findEmployeeProfiles(job)).switchIfEmpty(Flux.empty());
	}

	private Flux<EmployeeRecord> findEmployeeProfiles(Job job) {
		EmployeeSkillRequest request = new EmployeeSkillRequest(job.getJavaExp(), job.getSpringExp());
		return WebClient.create(employeServiceUrl).post().uri("/findEmpSkillset")
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.body(Mono.just(request), EmployeeSkillRequest.class).retrieve().bodyToFlux(EmployeeRecord.class);
	}

	public Mono<Job> getJobProfileFromCache(Job job) {
		Job jobDetail = (Job) hzInstance.getMap(jobDetailMapName).get(job.getId());
		if (jobDetail == null) {
			return findJobDetails(job).doOnSuccess(record -> writeJobDetailsToCache(record));
		} else {
			return Mono.just(jobDetail);
		}
	}

	private Mono<Job> findJobDetails(Job job) {
		log.info("Read job details from db for job_id : " + job.getId());
		return Mono.just(job).flatMap(record -> jobRepository.findById(record.getId())).switchIfEmpty(Mono.empty());
	}

	private Mono<Job> writeJobDetailsToCache(Job record) {
		hzInstance.getMap(jobDetailMapName).put(record.getId(), record);
		return Mono.just(record);

	}

	public Mono<JobResponse> deleteJob(int id) {
		jobRepository.deleteById(id).subscribe();
		JobResponse jobResponse = new JobResponse();
		jobResponse.setStatus(String.format("Job id %s deleted", id));
		return Mono.just(jobResponse);
	}

}
