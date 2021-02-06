package com.demo.warehouse.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.warehouse.SourceImporter;
import com.demo.warehouse.exception.SourceReadException;
import com.demo.warehouse.model.AdEntry;
import com.demo.warehouse.repository.AdEntryDAO;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CsvSourceImporter implements SourceImporter {

	private final AdEntryDAO adEntryDAO;

	@Transactional
	@Override
	public void readAndSave(String sourceName) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource(sourceName).getInputStream()))) {
			HeaderColumnNameMappingStrategy<CSVEntry> strategy = new HeaderColumnNameMappingStrategy<>();
			strategy.setType(CSVEntry.class);

			CsvToBean<CSVEntry> csvToBean = new CsvToBeanBuilder<CSVEntry>(reader).withMappingStrategy(strategy)
					.withIgnoreLeadingWhiteSpace(true)
					.build();

			csvToBean.stream().forEach(this::saveEntity);
		} catch (IOException e) {
			throw new SourceReadException("Cannot read source file: " + e.getMessage());
		}
	}

	private void saveEntity(CSVEntry csvEntry) {
		AdEntry adEntry = CsvToAdEntryMapper.map(csvEntry);
		adEntryDAO.save(adEntry);
	}
}