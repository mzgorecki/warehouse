package com.demo.warehouse.it;

import static com.demo.warehouse.request.Dimension.CAMPAIGN;
import static com.demo.warehouse.request.Dimension.DATASOURCE;
import static com.demo.warehouse.request.Dimension.DATE;
import static com.demo.warehouse.request.Metric.CLICKS;
import static com.demo.warehouse.request.Metric.CTR;
import static com.demo.warehouse.request.Metric.IMPRESSIONS;
import static com.demo.warehouse.request.Operation.AVG;
import static com.demo.warehouse.request.Operation.NONE;
import static com.demo.warehouse.request.Operation.SUM;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.demo.warehouse.exception.ErrorMessage;
import com.demo.warehouse.exception.JsonError;
import com.demo.warehouse.request.DimensionFilter;
import com.demo.warehouse.request.MetricOperation;
import com.demo.warehouse.request.QueryRequest;
import com.demo.warehouse.response.QueryResponse;
import com.google.common.collect.ImmutableSet;

public class QueriesIntegrationTest extends IntegrationTestSetup {

	@Autowired
	private QueryDispatcher queryDispatcher;

	@Test
	public void shouldSelectTotalClicksImpressionsAndCtrForGoogleAdsOn12Nov2019() {
		// @formatter:off
		QueryRequest request = QueryRequest.builder()
			.dimensionFilters(ImmutableSet.of(
				DimensionFilter.builder()
					.dimension(DATASOURCE)
					.value("Google Ads")
					.build(),
				DimensionFilter.builder()
					.dimension(DATE)
					.value("11/12/19")
					.build()
				)
			)
			.metricOperations(ImmutableSet.of(
				MetricOperation.builder()
					.metric(CLICKS)
					.operation(NONE)
					.build(),
				MetricOperation.builder()
					.metric(IMPRESSIONS)
					.operation(NONE)
					.build(),
				MetricOperation.builder()
					.metric(CTR)
					.operation(NONE)
					.build()
				)
			)
			.build();
		// @formatter:on

		QueryResponse response = queryDispatcher.query(request);

		assertThat(response).isNotNull();
		assertThat(response.getColumnNames()).isNotEmpty();
		assertThat(response.getColumnNames()).hasSize(3);
		assertThat(response.getResults()).hasSize(6);
	}

	@Test
	public void shouldSelectCTRPerDataSourceAndCampaign() {
		// @formatter:off
		QueryRequest request = QueryRequest.builder()
			.dimensionGroupings(ImmutableSet.of(
					DATASOURCE,
					CAMPAIGN
				)
			)
			.metricOperations(ImmutableSet.of(
				MetricOperation.builder()
					.metric(CTR)
					.operation(AVG)
					.build()
				)
			)
			.build();
		// @formatter:on

		QueryResponse response = queryDispatcher.query(request);

		assertThat(response).isNotNull();
		assertThat(response.getColumnNames()).isNotEmpty();
		assertThat(response.getColumnNames()).hasSize(3);
		assertThat(response.getResults()).hasSize(185);
	}

	@Test
	public void shouldSelectDailyImpressions() {
		// @formatter:off
		QueryRequest request = QueryRequest.builder()
			.dimensionGroupings(ImmutableSet.of(
					DATE
				)
			)
			.metricOperations(ImmutableSet.of(
				MetricOperation.builder()
					.metric(IMPRESSIONS)
					.operation(SUM)
					.build()
				)
			)
			.build();
		// @formatter:on

		QueryResponse response = queryDispatcher.query(request);

		assertThat(response).isNotNull();
		assertThat(response.getColumnNames()).isNotEmpty();
		assertThat(response.getColumnNames()).hasSize(2);
		assertThat(response.getResults()).hasSize(410);
	}

	@Test
	public void shouldFailWhenMissingMetrics() {
		// @formatter:off
		QueryRequest request = QueryRequest.builder()
			.dimensionGroupings(ImmutableSet.of(
					DATE
				)
			)
			.build();
		// @formatter:on

		JsonError response = queryDispatcher.query(request, SC_BAD_REQUEST).as(JsonError.class);

		assertThat(response).isNotNull();
		assertThat(response.getErrors()).contains(new ErrorMessage("metricOperations", null));
	}

	@Test
	public void shouldFailWhenSqlQueryIsInvalid() {
		// grouping by Date, but selecting Impressions - SQL exception
		// @formatter:off
		QueryRequest request = QueryRequest.builder()
			.dimensionGroupings(ImmutableSet.of(
					DATE
				)
			)
			.metricOperations(ImmutableSet.of(
				MetricOperation.builder()
					.metric(IMPRESSIONS)
					.operation(NONE)
					.build()
				)
			)
			.build();
		// @formatter:on

		JsonError response = queryDispatcher.query(request, SC_BAD_REQUEST).as(JsonError.class);

		assertThat(response).isNotNull();
		assertThat(response.getErrors()).contains(new ErrorMessage(null, "Invalid request - SQL error."));
	}
}
