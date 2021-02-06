package com.demo.warehouse;

@FunctionalInterface
public interface SourceImporter {

	void readAndSave(String sourceName);
}
