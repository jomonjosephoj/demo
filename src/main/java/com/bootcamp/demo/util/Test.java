package com.bootcamp.demo.util;

import reactor.core.publisher.Mono;

public class Test {

	public static void main(String[] args) {
		Test t = new Test();
		t.monoCaller();

	}

	public void monoCaller() {
		Mono<String> str = Mono.just("Man");
		testMono(str).subscribe(s -> System.out.println(s));
	}

	public Mono<String> testMono(Mono<String> str) {
		return str.flatMap(s -> transform(s)).switchIfEmpty(Mono.defer(() -> doSomethingOnEmpty()));
	}

	public Mono<String> doSomethingOnEmpty() {
		return Mono.just("Empty");
	}

	public Mono<String> transform(String s) {
		return Mono.just(s + "x");
	}

}
