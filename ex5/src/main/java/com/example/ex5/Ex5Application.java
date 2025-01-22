package com.example.ex5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Ex5Application {

	public static void main(String[] args) {
		SpringApplication.run(Ex5Application.class, args);
		System.out.println("http://localhost:8080/ex5");
	}

}
