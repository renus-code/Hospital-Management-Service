package com.hospital.hms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.hospital.hms.config.AuthServiceProperties;
import com.hospital.hms.config.JwtProperties;

@SpringBootApplication
@EnableConfigurationProperties({ AuthServiceProperties.class, JwtProperties.class })
public class HospitalServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HospitalServiceApplication.class, args);
	}

}
