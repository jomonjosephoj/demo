package com.bootcamp.demo;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.bootcamp.demo.pojos.EmployeeRecord;
import com.bootcamp.demo.pojos.EmployeeSkillRequest;
import com.bootcamp.demo.pojos.Job;
import com.bootcamp.demo.pojos.JobResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@Log4j2
@TestMethodOrder(OrderAnnotation.class)
public class AssignmentTests {

	private static final String URL = "http://localhost:8080";
	private static final ObjectMapper OM = new ObjectMapper();
	private static List<String> employeeDataList = new ArrayList<>();
	private static List<String> jobDataList = new ArrayList<>();

	@BeforeAll
	public static void init() throws InterruptedException {
		log.info("Test data set up");
		for (int i = 100001; i <= 100006; i++) {
			WebClient.create(URL).get().uri("/deleteEmployee/" + i).retrieve().bodyToMono(EmployeeRecord.class)
					.subscribe(record -> writeJson(record));
		}
		for (int i = 5001; i <= 5003; i++) {
			WebClient.create(URL).get().uri("/deleteJob/" + i).retrieve().bodyToMono(JobResponse.class)
					.subscribe(record -> writeJson(record));
		}

		employeeDataList.add(
				"{\"name\":\"victor1\",\"phone\":\"0203040506\",\"city\":\"hugo1\",\"emp_id\":100001,\"java_exp\":3.0,\"spring_exp\":7.0}");
		employeeDataList.add(
				"{\"name\":\"victor2\",\"phone\":\"0203040506\",\"city\":\"hugo2\",\"emp_id\":100002,\"java_exp\":4.0,\"spring_exp\":6.0}");
		employeeDataList.add(
				"{\"name\":\"victor3\",\"phone\":\"0203040506\",\"city\":\"hugo3\",\"emp_id\":100003,\"java_exp\":5.0,\"spring_exp\":5.0}");
		employeeDataList.add(
				"{\"name\":\"victor4\",\"phone\":\"0203040506\",\"city\":\"hugo4\",\"emp_id\":100004,\"java_exp\":6.0,\"spring_exp\":4.0}");
		employeeDataList.add(
				"{\"name\":\"victor5\",\"phone\":\"0203040506\",\"city\":\"hugo5\",\"emp_id\":100005,\"java_exp\":7.0,\"spring_exp\":3.0}");
		employeeDataList.add(
				"{\"name\":\"\",\"phone\":\"0203040506\",\"city\":\"hugo5\",\"emp_id\":100006,\"java_exp\":7.0,\"spring_exp\":3.0}");

		jobDataList.add("{\"job_id\":5001,\"job_name\":\"developer\",\"java_exp\":4.0,\"spring_exp\":3.0}");
		jobDataList.add("{\"job_id\":5002,\"job_name\":\"senior developer\",\"java_exp\":6.0,\"spring_exp\":4.0}");
		jobDataList.add("{\"job_id\":5003,\"job_name\":\"scrum master\",\"java_exp\":0.0,\"spring_exp\":0.0}");
		Thread.sleep(5000l);

	}

	@Test
	@Order(1)
	@DisplayName("Test initial creation of records")
	public void testCreations() throws InterruptedException {
		log.info("Test initial creation of records");
		employeeDataList.stream().map(employeeData -> {
			try {
				return OM.readValue(employeeData, EmployeeRecord.class);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return new EmployeeRecord();
			}
		}).forEach(employeeRecord -> {
			WebClient.create(URL).post().uri("/createEmployee")
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
					.body(Mono.just(employeeRecord), EmployeeSkillRequest.class).retrieve()
					.bodyToMono(EmployeeRecord.class).subscribe(record -> writeJson(record));
		});

		jobDataList.stream().map(jobData -> {
			try {
				return OM.readValue(jobData, Job.class);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return new Job();
			}
		}).forEach(jobProfile -> {
			WebClient.create(URL).post().uri("/createJobProfile")
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
					.body(Mono.just(jobProfile), Job.class).retrieve().bodyToMono(JobResponse.class)
					.subscribe(record -> writeJson(record));
		});
		Thread.sleep(5000l);
	}

	@Test
	@Order(2)
	@DisplayName("Test duplicate creation of records")
	public void testDuplicates() throws InterruptedException {
		log.info("Test duplicate creation of records");
		employeeDataList.stream().map(employeeData -> {
			try {
				return OM.readValue(employeeData, EmployeeRecord.class);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return new EmployeeRecord();
			}
		}).forEach(employeeRecord -> {
			WebClient.create(URL).post().uri("/createEmployee")
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
					.body(Mono.just(employeeRecord), EmployeeSkillRequest.class).retrieve()
					.bodyToMono(EmployeeRecord.class).subscribe(record -> writeJson(record));
		});

		jobDataList.stream().map(jobData -> {
			try {
				return OM.readValue(jobData, Job.class);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return new Job();
			}
		}).forEach(jobProfile -> {
			WebClient.create(URL).post().uri("/createJobProfile")
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
					.body(Mono.just(jobProfile), Job.class).retrieve().bodyToMono(JobResponse.class)
					.subscribe(record -> writeJson(record));
		});
		Thread.sleep(5000l);
	}

	@Test
	@Order(3)
	@DisplayName("Test finding employees with match skills")
	public void testFindEmpSkillset() throws InterruptedException {
		log.info("Test finding employees with match skills");
		String data = "{\"java_exp\":2}";
		EmployeeSkillRequest esr = null;
		try {
			esr = OM.readValue(data, EmployeeSkillRequest.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		WebClient.create(URL).post().uri("/findEmpSkillset")
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(Mono.just(esr), Job.class)
				.retrieve().bodyToFlux(EmployeeRecord.class).collectList().subscribe(record -> writeJson(record));
		Thread.sleep(5000l);
	}

	@Test
	@Order(4)
	@DisplayName("Test finding employees for job")
	public void testFindEmpForJobID() throws InterruptedException {
		log.info("Test finding employees for job");
		String data = "{\"job_id\":5001}";
		Job jobReq = null;
		try {
			jobReq = OM.readValue(data, Job.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		WebClient.create(URL).post().uri("/findEmpForJobID")
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(Mono.just(jobReq), Job.class)
				.retrieve().bodyToFlux(EmployeeRecord.class).collectList().subscribe(record -> writeJson(record));
		Thread.sleep(5000l);
	}

	@Test
	@Order(5)
	@DisplayName("Test finding jobs from cache")
	public void testGetJobProfileFromCache() throws InterruptedException {
		log.info("Test finding jobs from cache");
		String data = "{\"job_id\":5001}";
		Job jobReq = null;
		try {
			jobReq = OM.readValue(data, Job.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		WebClient.create(URL).post().uri("/getJobProfileFromCache")
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(Mono.just(jobReq), Job.class)
				.retrieve().bodyToMono(Job.class).subscribe(record -> writeJson(record));
		WebClient.create(URL).post().uri("/getJobProfileFromCache")
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(Mono.just(jobReq), Job.class)
				.retrieve().bodyToMono(Job.class).subscribe(record -> writeJson(record));
		WebClient.create(URL).post().uri("/getJobProfileFromCache")
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(Mono.just(jobReq), Job.class)
				.retrieve().bodyToMono(Job.class).subscribe(record -> writeJson(record));
		Thread.sleep(5000l);
	}
	
	private static void writeJson(Object obj) {
		try {
			log.info(OM.writeValueAsString(obj));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	@AfterAll
	public static void waitForAll() throws InterruptedException {
		// Thread.sleep(5000l);
	}
}
