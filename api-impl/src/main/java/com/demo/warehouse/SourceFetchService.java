package com.demo.warehouse;

import static com.demo.Assertions.containsKey;
import static com.demo.warehouse.FileType.CSV;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMap;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SourceFetchService {

	private Map<FileType, SourceImporter> methods;

	@Qualifier("CsvSourceImporter")
	private final SourceImporter csvSourceImporter;
	// open to add other source types

	public void importToDB(SourceFile sourceFile) {
		FileType fileType = sourceFile.getType();
		containsKey(methods, fileType, "File type=%s unsupported", fileType);

		methods.get(fileType).readAndSave(sourceFile.getFileName());
	}

	@VisibleForTesting
	@PostConstruct
	void initializeMethods() {
		// @formatter:off
		this.methods = ImmutableMap.of(
				CSV, csvSourceImporter
		);
		// @formatter:on
	}
}
