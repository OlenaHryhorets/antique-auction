package com.antique.auction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AntiqueApplication {

	public static void main(String[] args) {
		SpringApplication.run(AntiqueApplication.class, args);
	}

}
