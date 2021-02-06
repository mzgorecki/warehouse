package com.demo.warehouse.request;

import java.util.Set;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

// validator to this class should be added to check parameter consistency and prevent SQL errors (caught in QueryAdsService)
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class QueryRequest {

	@NotEmpty
	private Set<MetricOperation> metricOperations;

	private Set<Dimension> dimensionGroupings;

	private Set<DimensionFilter> dimensionFilters;
}
