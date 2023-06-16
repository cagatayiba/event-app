package com.event.eventmanagementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class EventManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventManagementServiceApplication.class, args);
	}

}
