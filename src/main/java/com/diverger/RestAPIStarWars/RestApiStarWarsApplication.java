package com.diverger.RestAPIStarWars;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RestApiStarWarsApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestApiStarWarsApplication.class, args);
	}

}
