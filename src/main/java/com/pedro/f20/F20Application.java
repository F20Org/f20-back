package com.pedro.f20;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
@EnableScheduling
public class F20Application {

	public static void main(String[] args) {
		SpringApplication.run(F20Application.class, args);
	}

	@Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
