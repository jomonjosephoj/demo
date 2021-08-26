package com.bootcamp.demo.services;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Service;

import com.bootcamp.demo.pojos.EmployeeRecord;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;

@Service
@Log4j2
public class ReactiveConsumerService implements CommandLineRunner {

	private final ReactiveKafkaConsumerTemplate<String, String> reactiveKafkaConsumerTemplate;

	@Autowired
	ReactiveKafkaProducerService kafkaProducerService;

	@Autowired
	ObjectMapper objectMapper;

	@Value(value = "${employee_updates_topic}")
	private String employeeUpdatesTopic;

	@Value(value = "${employee_dlq_topic}")
	private String dlqTopic;

	public ReactiveConsumerService(ReactiveKafkaConsumerTemplate<String, String> reactiveKafkaConsumerTemplate) {
		this.reactiveKafkaConsumerTemplate = reactiveKafkaConsumerTemplate;
	}

	private Flux<String> consumeString() {
		return reactiveKafkaConsumerTemplate.receiveAutoAck()
				.doOnNext(consumerRecord -> log.info("received key={}, value={} from topic={}, offset={}",
						consumerRecord.key(), consumerRecord.value(), consumerRecord.topic(), consumerRecord.offset()))
				.map(ConsumerRecord::value).doOnNext(data -> validateMessage(data));
	}

	private String validateMessage(String data) {
		try {
			EmployeeRecord record = objectMapper.readValue(data, EmployeeRecord.class);
			if (record.getId() == 0 || record.getCity().trim().isEmpty() || record.getName().trim().isEmpty()
					|| record.getPhone().trim().isEmpty()) {
				kafkaProducerService.send(dlqTopic, data);
			} else {
				kafkaProducerService.send(employeeUpdatesTopic, data);
			}
		} catch (Exception e1) {
			log.error("", e1);
			try {
				kafkaProducerService.send(dlqTopic, data);
			} catch (Exception e2) {
				log.error("", e2);
			}
		}
		return data;
	}

	@Override
	public void run(String... args) {
		consumeString().subscribe();
	}
}