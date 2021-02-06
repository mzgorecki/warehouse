package com.demo.warehouse;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.demo.warehouse.csv.CsvSourceImporter;

@ExtendWith(MockitoExtension.class)
class SourceFetchServiceTest {

	@Mock
	private CsvSourceImporter csvSourceImporter;

	private SourceFetchService service;

	@BeforeEach
	void setUp() {
		this.service = new SourceFetchService(csvSourceImporter);
		service.initializeMethods();
	}

	@Test
	void shouldImportToDB() {
		String sourceName = "test.csv";
		FileType fileType = FileType.CSV;
		SourceFile sourceFile = mock(SourceFile.class);

		given(sourceFile.getType()).willReturn(fileType);
		given(sourceFile.getFileName()).willReturn(sourceName);

		service.importToDB(sourceFile);

		verify(csvSourceImporter).readAndSave(sourceName);
	}
}