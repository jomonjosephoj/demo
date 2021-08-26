package com.bootcamp.demo.services;

import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ReactiveKafkaProducerService {

	private final ReactiveKafkaProducerTemplate<String, String> reactiveKafkaProducerTemplate;

	public ReactiveKafkaProducerService(ReactiveKafkaProducerTemplate<String, String> reactiveKafkaProducerTemplate) {
		this.reactiveKafkaProducerTemplate = reactiveKafkaProducerTemplate;
	}

	public void send(String topic, String data) {
		reactiveKafkaProducerTemplate.send(topic, data).doOnSuccess(senderResult -> log.info(String
				.format("Post to %s data -> %s, offset -> %s", topic, data, senderResult.recordMetadata().offset())))
				.subscribe();
	}

}
