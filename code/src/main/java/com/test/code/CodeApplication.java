package com.test.code;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CodeApplication {

	public static void main(String[] args) {
		System.out.println("11111");
		System.out.println("master");
		System.out.println("hotfix branch code change");
		SpringApplication.run(CodeApplication.class, args);
	}
}