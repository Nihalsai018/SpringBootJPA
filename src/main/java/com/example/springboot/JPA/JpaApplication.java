package com.example.springboot.JPA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class JpaApplication {
	public static void main(String[] args) {
		SpringApplication.run(JpaApplication.class, args);

	}


//	@Bean
//	public RestClient restClient() {
//		// Configure the RestClient with any specific settings or defaults
//		return RestClient.builder()
//				.defaultHeader("Content-Type", "application/json") // Default headers if needed
//				.build();
//	}
}
	