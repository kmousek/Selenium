package com.kmousek.springboot.reserveCourt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringbootReserveCourtApplication {

	public static void main(String[] args) {
//		SpringApplication.run(SpringbootMyBatis1Application.class, args);
		SpringApplicationBuilder builder = new SpringApplicationBuilder(SpringbootReserveCourtApplication.class);
		builder.headless(false);
		ConfigurableApplicationContext context = builder.run(args);
	}

}
