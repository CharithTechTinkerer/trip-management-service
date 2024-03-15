package com.sep.tripmanagementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.sep.tripplatformmanagmentservice.configuration.entity")
public class TripManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TripManagementServiceApplication.class, args);
	}

}
