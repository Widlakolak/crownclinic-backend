package com.crown.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

@EnableScheduling
@SpringBootApplication
@EnableJdbcHttpSession
public class CrownclinicBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrownclinicBackendApplication.class, args);
	}

}
