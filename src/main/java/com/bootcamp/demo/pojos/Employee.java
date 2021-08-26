package com.bootcamp.demo.pojos;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Table("emp")
@AllArgsConstructor
public class Employee {

	@PrimaryKey("emp_id")
	private int id;
	@Column("emp_name")
	private String name;
	@Column("emp_phone")
	private String phone;
	@Column("emp_city")
	private String city;

}
