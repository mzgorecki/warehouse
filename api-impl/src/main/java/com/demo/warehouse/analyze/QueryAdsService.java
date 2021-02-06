package com.demo.warehouse.analyze;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.warehouse.QueryService;
import com.demo.warehouse.exception.InvalidRequestException;
import com.demo.warehouse.repository.AdEntryDAO;
import com.demo.warehouse.request.QueryRequest;
import com.demo.warehouse.response.QueryResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class QueryAdsService implements QueryService {

	private final AdEntryDAO adEntryDAO;

	@Transactional(readOnly = true)
	@Override
	public QueryResponse query(QueryRequest queryRequest) {
		try {
			return adEntryDAO.query(queryRequest);
		} catch (DataAccessException e) {
			throw new InvalidRequestException("Invalid request - SQL error.");
		}
	}
}
