package com.udla.markenx_service;

import org.springframework.boot.SpringApplication;

public class TestMarkenxServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(MarkenxServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
