package com.bootcamp.demo.repository;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

import com.bootcamp.demo.pojos.Job;

public interface JobRepository extends ReactiveCassandraRepository<Job,Integer> {

}
