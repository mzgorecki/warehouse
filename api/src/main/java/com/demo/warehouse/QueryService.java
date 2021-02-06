package com.demo.warehouse;

import com.demo.warehouse.request.QueryRequest;
import com.demo.warehouse.response.QueryResponse;

public interface QueryService {

	QueryResponse query(QueryRequest queryRequest);
}
