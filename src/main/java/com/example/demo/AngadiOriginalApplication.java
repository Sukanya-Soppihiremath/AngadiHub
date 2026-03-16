package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
	    scanBasePackages = {
	        "com.example.demo",
	        "com.example.demo.Controllers",
	        "com.example.demo.Services",
	        "com.example.demo.Repositories",
	        "com.example.demo.Entities",
	        "com.example.demo.dto",
	        "com.example.demo.Filter",
	        "com.example.demo.admincontrollers",
	        "com.example.demo.adminservices"
	    }
	)

public class AngadiOriginalApplication {

	public static void main(String[] args) {
		SpringApplication.run(AngadiOriginalApplication.class, args);
	}

}
