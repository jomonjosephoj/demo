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
public class EmployeeSkillRequest {
	
	@JsonProperty("java_exp")
	private double javaExp;
	@JsonProperty("spring_exp")
	private double springExp;

}
