package com.bootcamp.demo.repository;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

import com.bootcamp.demo.pojos.Employee;

public interface EmployeeRepository extends ReactiveCassandraRepository<Employee,Integer> {

}
