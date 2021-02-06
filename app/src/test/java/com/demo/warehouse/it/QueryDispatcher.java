package com.demo.warehouse.it;

import static com.jayway.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;

import org.springframework.stereotype.Service;

import com.demo.warehouse.request.QueryRequest;
import com.demo.warehouse.response.QueryResponse;
import com.jayway.restassured.response.ExtractableResponse;
import com.jayway.restassured.response.Response;

@Service
public class QueryDispatcher {

	private static final String API_QUERY_URL = "/api/query";

	public ExtractableResponse<Response> query(QueryRequest request, int httpCode) {
		// @formatter:off
		return given()
				.body(request)
				.get(API_QUERY_URL)
				.then()
					.statusCode(httpCode)
				.extract();
		// @formatter:on
	}

	public QueryResponse query(QueryRequest request) {
		return query(request, SC_OK).as(QueryResponse.class);
	}
}
