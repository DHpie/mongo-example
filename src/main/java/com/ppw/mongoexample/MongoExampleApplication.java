package com.ppw.mongoexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MongoExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(MongoExampleApplication.class, args);
	}
}
