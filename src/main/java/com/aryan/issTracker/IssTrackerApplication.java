package com.aryan.issTracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IssTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(IssTrackerApplication.class, args);
	}

}
