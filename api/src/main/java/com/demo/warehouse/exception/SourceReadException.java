package com.demo.warehouse.exception;

public class SourceReadException extends RuntimeException {

	public SourceReadException(String format, Object... args) {
		super(String.format(format, args));
	}
}
