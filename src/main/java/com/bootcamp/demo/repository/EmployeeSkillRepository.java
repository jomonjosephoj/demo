package com.bootcamp.demo.repository;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

import com.bootcamp.demo.pojos.EmployeeSkill;

import reactor.core.publisher.Flux;

public interface EmployeeSkillRepository extends ReactiveCassandraRepository<EmployeeSkill,Integer> {
	
	@Query("select * from emp_skill where java_exp >= ?0 and spring_exp >= ?1 allow filtering")
    Flux<EmployeeSkill> findSkilledEmployees(double java_exp,double spring_exp);

}
