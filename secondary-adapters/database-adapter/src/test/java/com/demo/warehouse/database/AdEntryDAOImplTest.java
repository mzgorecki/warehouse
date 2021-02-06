package com.demo.warehouse.database;

import static com.demo.warehouse.request.Dimension.DATASOURCE;
import static com.demo.warehouse.request.Dimension.DATE;
import static com.demo.warehouse.request.Metric.CLICKS;
import static com.demo.warehouse.request.Metric.CTR;
import static com.demo.warehouse.request.Metric.IMPRESSIONS;
import static com.demo.warehouse.request.Operation.AVG;
import static com.demo.warehouse.request.Operation.NONE;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.demo.warehouse.model.AdEntry;
import com.demo.warehouse.model.AdEntryID;
import com.demo.warehouse.repository.AdEntryDAO;
import com.demo.warehouse.request.DimensionFilter;
import com.demo.warehouse.request.MetricOperation;
import com.demo.warehouse.request.QueryRequest;
import com.demo.warehouse.response.QueryResponse;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

// just few tests as a demo
class AdEntryDAOImplTest extends DAOTest {

	@Autowired
	private AdEntryDAO dao;

	@PersistenceContext
	private EntityManager em;

	@Test
	void shouldFilterDimension() {
		AdEntry entry1 = new AdEntry(new AdEntryID(UUID.randomUUID()), "datasource1", "campaign1", LocalDate.now(), 1L, 2L);
		AdEntry entry2 = new AdEntry(new AdEntryID(UUID.randomUUID()), "datasource2", "campaign2", LocalDate.now().minusDays(1), 3L, 4L);
		dao.save(entry1);
		dao.save(entry2);

		em.flush();

		// @formatter:off
		QueryRequest request = QueryRequest.builder()
			.dimensionFilters(ImmutableSet.of(
				DimensionFilter.builder()
					.dimension(DATASOURCE)
					.value("datasource1")
					.build()
				)
			).metricOperations(ImmutableSet.of(
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

		QueryResponse response = dao.query(request);

		assertThat(response).isNotNull();
		assertThat(response.getColumnNames()).isNotEmpty();
		assertThat(response.getColumnNames()).isEqualTo(ImmutableList.of("CLICKS", "IMPRESSIONS", "CTR"));
		assertThat(response.getResults()).isNotEmpty();
		assertThat(response.getResults()).isEqualTo(ImmutableList.of(ImmutableList.of("1", "2", "50.0")));
	}

	@Test
	void shouldAggregateByDateAndReturnAverageCTR() {
		// CTR - (1/2 + 4/10) / 2 = 45
		AdEntry entry1 = new AdEntry(new AdEntryID(UUID.randomUUID()), "datasource1", "campaign1", LocalDate.of(2020, 1, 2), 1L, 2L);
		AdEntry entry2 = new AdEntry(new AdEntryID(UUID.randomUUID()), "datasource2", "campaign2", LocalDate.of(2020, 1, 2), 4L, 10L);
		// CTR - 5/20 = 25
		AdEntry entry3 = new AdEntry(new AdEntryID(UUID.randomUUID()), "datasource3", "campaign3", LocalDate.of(2020, 3, 4), 5L, 20L);
		dao.save(entry1);
		dao.save(entry2);
		dao.save(entry3);

		em.flush();

		// @formatter:off
		QueryRequest request = QueryRequest.builder()
			.dimensionGroupings(ImmutableSet.of(
				DATE
			))
			.metricOperations(ImmutableSet.of(
				MetricOperation.builder()
					.metric(CTR)
					.operation(AVG)
					.build()
				)
			)
			.build();
		// @formatter:on

		QueryResponse response = dao.query(request);

		assertThat(response).isNotNull();
		assertThat(response.getColumnNames()).isNotEmpty();
		assertThat(response.getColumnNames()).isEqualTo(ImmutableList.of("CTR", "DATE"));
		assertThat(response.getResults()).isNotEmpty();
		assertThat(response.getResults()).isEqualTo(ImmutableList.of(
				ImmutableList.of("45.0", "2020-01-02"),
				ImmutableList.of("25.0", "2020-03-04")
		));
	}
}
