package com.demo.warehouse;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.demo.SourceFileProperties;
import com.google.common.collect.ImmutableList;

@ExtendWith(MockitoExtension.class)
class SourceInitializerServiceTest {

	@Mock
	private SourceFileProperties sourceFileProperties;

	@Mock
	private SourceFetchService sourceFetchService;

	private SourceInitializerService service;

	@BeforeEach
	void setUp() {
		this.service = new SourceInitializerService(sourceFileProperties, sourceFetchService);
	}

	@Test
	void shouldImportFirstSourceFile() {
		SourceFile source1 = mock(SourceFile.class);
		SourceFile source2 = mock(SourceFile.class);
		List<SourceFile> sources = ImmutableList.of(source1, source2);

		given(sourceFileProperties.getSources()).willReturn(sources);

		service.importData();

		verify(sourceFetchService).importToDB(source1);
		verify(sourceFetchService, never()).importToDB(source2);
	}

	@Test
	void shouldFailWhenSourceListIsEmpty() {
		List<SourceFile> sources = ImmutableList.of();

		given(sourceFileProperties.getSources()).willReturn(sources);

		assertThatThrownBy(() -> service.importData()).isInstanceOf(IllegalArgumentException.class).hasMessage("Source list is empty");

		verify(sourceFetchService, never()).importToDB(any());
	}
}