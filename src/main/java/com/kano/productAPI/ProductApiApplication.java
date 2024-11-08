package com.kano.productAPI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProductApiApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(ProductApiApplication.class);

	@Value("${spring.profiles.active:empty}")
	private String activeProfile;

	public static void main(String[] args) {
		SpringApplication.run(ProductApiApplication.class, args);
	}

	@Override
	public void run(String... args) {
		logger.info("---");
		logger.info("Product API is running");
		logger.info("Active Profile: " + activeProfile);
	}
}
