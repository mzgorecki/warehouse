package com.demo.warehouse;

import static com.demo.Assertions.isNotEmpty;

import java.util.List;

import org.springframework.stereotype.Service;

import com.demo.SourceFileProperties;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SourceInitializerService implements SourceInitializer {

	private final SourceFileProperties sourceFileProperties;
	private final SourceFetchService sourceFetchService;

	@Override
	public void importData() {
		// source could be selected through configuration or input rest call, but keeping it simple
		SourceFile sourceFile = getFirstSource();
		sourceFetchService.importToDB(sourceFile);
	}

	private SourceFile getFirstSource() {
		List<SourceFile> sources = sourceFileProperties.getSources();
		isNotEmpty(sources, "Source list is empty");
		return sourceFileProperties.getSources().get(0);
	}
}
