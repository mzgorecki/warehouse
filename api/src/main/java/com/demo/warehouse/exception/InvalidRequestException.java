package com.demo.warehouse.exception;

public class InvalidRequestException extends RuntimeException {

	public InvalidRequestException(String format, Object... args) {
		super(String.format(format, args));
	}
}
