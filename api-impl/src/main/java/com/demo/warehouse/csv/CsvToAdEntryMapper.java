package com.demo.warehouse.csv;

import static com.demo.Constants.DATE_FORMAT;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import com.demo.warehouse.model.AdEntry;
import com.demo.warehouse.model.AdEntryID;

public class CsvToAdEntryMapper {

	public static AdEntry map(CSVEntry csvEntry) {
		LocalDate daily = LocalDate.from(DateTimeFormatter.ofPattern(DATE_FORMAT).parse(csvEntry.getDaily()));
		// @formatter:off
		return new AdEntry(
				new AdEntryID(UUID.randomUUID()),
				csvEntry.getDataSource(),
				csvEntry.getCampaign(),
				daily,
				csvEntry.getClicks(),
				csvEntry.getImpressions()
		);
		// @formatter:on
	}
}
