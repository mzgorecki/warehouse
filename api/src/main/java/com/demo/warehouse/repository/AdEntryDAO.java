package com.demo.warehouse.repository;

import com.demo.warehouse.model.AdEntry;
import com.demo.warehouse.request.QueryRequest;
import com.demo.warehouse.response.QueryResponse;

public interface AdEntryDAO {

	QueryResponse query(QueryRequest queryRequest);

	void save(AdEntry adEntry);
}
