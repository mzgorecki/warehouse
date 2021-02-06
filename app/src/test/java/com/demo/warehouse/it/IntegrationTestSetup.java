package com.demo.warehouse.it;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

import javax.annotation.PostConstruct;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.demo.warehouse.Application;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.config.ObjectMapperConfig;
import com.jayway.restassured.config.RestAssuredConfig;

@RunWith(SpringRunner.class)
@ContextConfiguration
@SpringBootTest(classes = Application.class, webEnvironment = DEFINED_PORT)
public abstract class IntegrationTestSetup {

	private static final String APPLICATION_JSON = "application/json";

	@Autowired
	private ObjectMapper mapper;

	@Value("${server.port}")
	private int port;

	@PostConstruct
	public void configureRestAssured() {
		RestAssured.port = this.port;
		RestAssured.config = RestAssuredConfig.config()
				.objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory((cls, charset) -> mapper));
		RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(APPLICATION_JSON).build();
	}
}