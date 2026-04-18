package com.hospital.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.hospital.auth.config.HospitalServiceProperties;
import com.hospital.auth.config.JwtProperties;
import com.hospital.auth.config.PatientServiceProperties;

@SpringBootApplication
@EnableConfigurationProperties({ JwtProperties.class, HospitalServiceProperties.class, PatientServiceProperties.class })
public class AuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

}
