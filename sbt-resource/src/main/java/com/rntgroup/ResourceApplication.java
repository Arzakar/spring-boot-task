package com.rntgroup;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.util.List;

@Slf4j
@SpringBootApplication
public class ResourceApplication {

	@Autowired
	private Environment env;

	public static void main(String[] args) {
		SpringApplication.run(ResourceApplication.class, args);
	}

	@Bean
	public CommandLineRunner initialize() {
		return args -> {
			log.info("hello world");
			log.info("Active profiles is " + List.of(env.getActiveProfiles()));
		};
	}
}
