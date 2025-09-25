package com.example.karyaflow;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableAsync
@SpringBootApplication
public class 	KaryaflowApplication {

	public static void main(String[] args) {
		SpringApplication.run(KaryaflowApplication.class, args);
	}
}
