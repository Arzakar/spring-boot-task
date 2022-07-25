package com.rntgroup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.util.List;

@SpringBootApplication
public class ResourceApplication {

	@Autowired
	ApplicationContext context;

	@Autowired
	Environment env;

	public static void main(String[] args) {
		SpringApplication.run(ResourceApplication.class, args);
	}

	@Bean
	public CommandLineRunner initialize() {
		return args -> {
			System.out.println("hello world");
			System.out.println(List.of(env.getActiveProfiles()));
		};
	}
}
