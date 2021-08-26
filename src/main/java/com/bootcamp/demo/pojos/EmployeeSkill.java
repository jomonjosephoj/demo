package com.bootcamp.demo.pojos;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Table("emp_skill")
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeSkill {
	
	@PrimaryKey("emp_id")
	@JsonProperty("emp_id")
	private int id;
	@Column("java_exp")
	@JsonProperty("java_exp")
	private double javaExp;
	@Column("spring_exp")
	@JsonProperty("spring_exp")
	private double springExp;

}
