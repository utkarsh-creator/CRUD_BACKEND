package com.example.crudapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication

@EnableJpaRepositories(basePackages = "com.example.crudapp.repository") // ✅ Ensure JPA repositories are found
@EntityScan(basePackages = "com.example.crudapp.model") // ✅ Ensure entities are scanned
public class CrudappApplication {
	public static void main(String[] args) {
		SpringApplication.run(CrudappApplication.class, args);
	}
}
