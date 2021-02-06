package com.demo.warehouse.database;

import org.hibernate.criterion.AggregateProjection;

@FunctionalInterface
public interface AggregateProjectionOperation {

	AggregateProjection perform(String propertyName);
}
