package com.demo.warehouse.request;

public enum Dimension {

	// @formatter:off
	DATE(DimensionType.DATE),
	CAMPAIGN(DimensionType.STRING),
	DATASOURCE(DimensionType.STRING);
	// @formatter:on

	private DimensionType dimensionType;

	Dimension(DimensionType dimensionType) {
		this.dimensionType = dimensionType;
	}

	public DimensionType getDimensionType() {
		return dimensionType;
	}
}
