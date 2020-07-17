package com.antique.auction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication//(exclude = { SecurityAutoConfiguration.class})
public class AntiqueApplication {

	public static void main(String[] args) {
		SpringApplication.run(AntiqueApplication.class, args);
	}

}
