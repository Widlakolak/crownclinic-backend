package com.crown.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CrownclinicBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrownclinicBackendApplication.class, args);
	}

}
