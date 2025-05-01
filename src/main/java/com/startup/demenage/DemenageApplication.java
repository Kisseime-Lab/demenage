package com.startup.demenage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(enableDefaultTransactions = false)
public class DemenageApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemenageApplication.class, args);
	}

}
