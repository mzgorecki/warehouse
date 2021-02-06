package com.demo.warehouse;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class InitializationService {

	private final SourceInitializer sourceInitializer;

	@EventListener(ApplicationReadyEvent.class)
	public void initDataSource() {
		sourceInitializer.importData();
	}
}
