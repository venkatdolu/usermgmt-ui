package com.wipro.usermgmt.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
public class UsermgmtUiApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsermgmtUiApplication.class, args);
	}

}
