package com.test.code;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodeApplication.class, args);
	}
	

    //공통적으로 쓸 수 있는 것.
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}