package com.bootcamp.demo.pojos;

import java.io.Serializable;

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
@Table("job")
@AllArgsConstructor
@NoArgsConstructor
public class Job implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@PrimaryKey("job_id")
	@JsonProperty("job_id")
	private int id;
	
	@Column("job_name")
	@JsonProperty("job_name")
	private String jobName;
	
	@Column("java_exp")
	@JsonProperty("java_exp")
	private double javaExp;
	
	@Column("spring_exp")
	@JsonProperty("spring_exp")
	private double springExp;

}
