package com.study.excel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
public class Excel1Application {

	public static void main(String[] args) {
		SpringApplication.run(Excel1Application.class, args);
	}

}
