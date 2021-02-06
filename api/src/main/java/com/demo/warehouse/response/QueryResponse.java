package com.demo.warehouse.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class QueryResponse {

	private List<String> columnNames;
	private List<List<String>> results;
}
