package com.example.apiserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ApiserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiserverApplication.class, args);
		System.out.println("Started...");
	}

}
