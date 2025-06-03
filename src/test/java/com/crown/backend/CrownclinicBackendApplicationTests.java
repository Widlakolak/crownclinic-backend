package com.crown.backend;

import com.crown.backend.service.JwtService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;

@SpringBootTest
class CrownclinicBackendApplicationTests {

	@TestConfiguration
	static class MockSecurityBeans {
		@Bean
		public JwtService jwtService() {
			return Mockito.mock(JwtService.class);
		}

		@Bean
		public UserDetailsService userDetailsService() {
			return Mockito.mock(UserDetailsService.class);
		}
	}

	@Test
	void contextLoads() {
	}

}
