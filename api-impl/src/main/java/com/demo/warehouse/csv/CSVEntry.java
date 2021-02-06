package com.demo.warehouse.csv;

import com.opencsv.bean.CsvBindByName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CSVEntry {

	@CsvBindByName(column = "Datasource")
	private String dataSource;

	@CsvBindByName(column = "Campaign")
	private String campaign;

	@CsvBindByName(column = "Daily")
	private String daily;

	@CsvBindByName(column = "Clicks")
	private Long clicks;

	@CsvBindByName(column = "Impressions")
	private Long impressions;
}