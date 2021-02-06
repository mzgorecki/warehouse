package com.demo.warehouse.web;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.warehouse.QueryService;
import com.demo.warehouse.request.QueryRequest;
import com.demo.warehouse.response.QueryResponse;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class QueryController {

	private final QueryService queryService;

	@GetMapping("/query")
	public QueryResponse query(@Valid @RequestBody QueryRequest queryRequest) {
		LOG.info("Start /query request={}", queryRequest);
		QueryResponse entries = queryService.query(queryRequest);
		LOG.info("End /query");
		return entries;
	}
}
