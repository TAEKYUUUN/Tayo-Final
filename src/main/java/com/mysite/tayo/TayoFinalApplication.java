package com.mysite.tayo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class TayoFinalApplication {

	public static void main(String[] args) {
		SpringApplication.run(TayoFinalApplication.class, args);
	}
}
