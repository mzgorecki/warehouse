package com.demo.warehouse.database;

import static com.demo.Assertions.containsKey;
import static com.demo.Constants.DATE_FORMAT;
import static com.demo.warehouse.request.Dimension.CAMPAIGN;
import static com.demo.warehouse.request.Dimension.DATASOURCE;
import static com.demo.warehouse.request.Dimension.DATE;
import static com.demo.warehouse.request.Metric.CLICKS;
import static com.demo.warehouse.request.Metric.CTR;
import static com.demo.warehouse.request.Metric.IMPRESSIONS;
import static com.demo.warehouse.request.Operation.AVG;
import static com.demo.warehouse.request.Operation.MAX;
import static com.demo.warehouse.request.Operation.MIN;
import static com.demo.warehouse.request.Operation.SUM;
import static org.hibernate.criterion.Projections.groupProperty;
import static org.hibernate.criterion.Projections.projectionList;
import static org.hibernate.criterion.Restrictions.eq;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.springframework.stereotype.Repository;

import com.demo.warehouse.model.AdEntry;
import com.demo.warehouse.model.AdEntryID;
import com.demo.warehouse.repository.AdEntryDAO;
import com.demo.warehouse.request.Dimension;
import com.demo.warehouse.request.DimensionFilter;
import com.demo.warehouse.request.DimensionType;
import com.demo.warehouse.request.Metric;
import com.demo.warehouse.request.MetricOperation;
import com.demo.warehouse.request.Operation;
import com.demo.warehouse.request.QueryRequest;
import com.demo.warehouse.response.QueryResponse;
import com.google.common.collect.ImmutableMap;

@Repository
public class AdEntryDAOImpl extends AbstractHibernateDAO<AdEntry, AdEntryID> implements AdEntryDAO {

	// @formatter:off
	private Map<Operation, AggregateProjectionOperation> aggregateOperations = ImmutableMap.of(
			SUM, Projections::sum,
			MIN, Projections::min,
			MAX, Projections::max,
			AVG, Projections::avg
	);

	private Map<Dimension, String> dimensionFieldMapping = ImmutableMap.of(
			DATE, "daily",
			CAMPAIGN, "campaign",
			DATASOURCE, "dataSource"
	);

	private Map<Metric, String> metricFieldMapping = ImmutableMap.of(
			CLICKS, "clicks",
			IMPRESSIONS, "impressions",
			CTR, "ctr"
	);

	private Map<DimensionType, DimensionTypeValueMapping> dimensionTypeValueMapping = ImmutableMap.of(
			DimensionType.DATE, this::mapDate,
			DimensionType.STRING, s -> s
		);
	// @formatter:on

	public AdEntryDAOImpl() {
		super(AdEntry.class);
	}

	@Override
	public QueryResponse query(QueryRequest request) {
		Criteria criteria = criteria();
		ProjectionList projections = projectionList();
		List<String> columnNames = new ArrayList<>();

		addDimensionFilters(criteria, request.getDimensionFilters());

		addMetrics(projections, request.getMetricOperations(), columnNames);

		addDimensionGrouping(projections, request.getDimensionGroupings(), columnNames);

		criteria.setProjection(projections);

		@SuppressWarnings("unchecked")
		List<List<String>> results = mapArrayToList(criteria.list());
		return new QueryResponse(columnNames, results);
	}

	private void addMetrics(ProjectionList projections, Set<MetricOperation> metricOperations, List<String> columnNames) {
		for (MetricOperation metricOperation : metricOperations) {
			Projection projection = isNone(metricOperation.getOperation()) ?
					createSimpleProperty(metricOperation.getMetric()) :
					createMetricOperation(metricOperation);

			projections.add(projection);
			columnNames.add(metricOperation.getMetric().name());
		}
	}

	private void addDimensionFilters(Criteria criteria, Set<DimensionFilter> dimensionFilters) {
		if (dimensionFilters != null) {
			for (DimensionFilter dimensionFilter : dimensionFilters) {
				criteria.add(createDimensionCriteria(dimensionFilter));
			}
		}
	}

	private Criterion createDimensionCriteria(DimensionFilter dimensionFilter) {
		Dimension dimension = dimensionFilter.getDimension();
		DimensionType dimensionType = dimension.getDimensionType();
		String fieldName = dimensionFieldMapping.get(dimension);
		String fieldValue = dimensionFilter.getValue();
		return eq(fieldName, dimensionTypeValueMapping.get(dimensionType).value(fieldValue));
	}

	private LocalDate mapDate(String value) {
		return LocalDate.parse(value, DateTimeFormatter.ofPattern(DATE_FORMAT));
	}

	private void addDimensionGrouping(ProjectionList projections, Set<Dimension> dimensionGroupings, List<String> columnNames) {
		if (dimensionGroupings != null) {
			for (Dimension dimension : dimensionGroupings) {
				String dimensionMapping = findDimensionMapping(dimension);
				projections.add(groupProperty(dimensionMapping));
				columnNames.add(dimension.name());
			}
		}
	}

	private boolean isNone(Operation operation) {
		return operation == null || Operation.NONE.equals(operation);
	}

	private Projection createSimpleProperty(Metric metric) {
		return Projections.property(findMetricName(metric));
	}

	private Projection createMetricOperation(MetricOperation metricOperation) {
		AggregateProjectionOperation operation = findOperation(metricOperation.getOperation());
		String metricName = findMetricName(metricOperation.getMetric());
		return operation.perform(metricName);
	}

	private AggregateProjectionOperation findOperation(Operation operation) {
		containsKey(aggregateOperations, operation, "Operation=%s is not supported", operation);
		return aggregateOperations.get(operation);
	}

	private String findMetricName(Metric metric) {
		containsKey(metricFieldMapping, metric, "Metric=%s is not supported", metric);
		return metricFieldMapping.get(metric);
	}

	private String findDimensionMapping(Dimension dimension) {
		containsKey(dimensionFieldMapping, dimension, "Dimension=%s is not supported", dimension);
		return dimensionFieldMapping.get(dimension);
	}

	// due to generic nature of the results
	private List<List<String>> mapArrayToList(List<Object> list) {
		List<List<String>> results = new ArrayList<>();
		for (Object outer : list) {
			List<String> values = new ArrayList<>();
			if (outer.getClass().isArray()) {
				for (Object inner : (Object[]) outer) {
					values.add(String.valueOf(inner));
				}
			} else {
				values.add(String.valueOf(outer));
			}
			results.add(values);
		}
		return results;
	}
}
