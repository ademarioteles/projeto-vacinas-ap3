package com.vacinas.ap3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class Ap3Application {

	public static void main(String[] args) {
		SpringApplication.run(Ap3Application.class, args);
	}

}
