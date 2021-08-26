package com.bootcamp.demo.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRecord {

	@JsonProperty("emp_id")
	private int id;
	@JsonProperty("name")
	private String name;
	@JsonProperty("phone")
	private String phone;
	@JsonProperty("city")
	private String city;
	@JsonProperty("java_exp")
	private Double javaExp;
	@JsonProperty("spring_exp")
	private Double springExp;
	@JsonProperty("status")
	private String status;
}
