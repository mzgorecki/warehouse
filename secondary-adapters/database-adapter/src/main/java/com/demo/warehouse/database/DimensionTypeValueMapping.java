package com.demo.warehouse.database;

@FunctionalInterface
public interface DimensionTypeValueMapping {

	Object value(String dimension);
}
