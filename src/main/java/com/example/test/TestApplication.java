package com.example.test;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;

@SpringBootApplication
public class TestApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(TestApplication.class);
		app.setAdditionalProfiles("dev");
		app.run(args);
	}

}
