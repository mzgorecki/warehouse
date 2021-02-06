package com.demo.warehouse.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice(basePackages = "com.demo.warehouse")
public class WarehouseExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(IllegalStateException.class)
	@ResponseBody
	public JsonError handleIllegalStateException(IllegalStateException exception) {
		warn(exception.getMessage(), exception);
		return getJsonMessage(exception);
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(SourceReadException.class)
	@ResponseBody
	public JsonError handleSourceReadException(SourceReadException exception) {
		warn(exception.getMessage(), exception);
		return getJsonMessage(exception);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidRequestException.class)
	@ResponseBody
	public JsonError handleSourceReadException(InvalidRequestException exception) {
		warn(exception.getMessage(), exception);
		return getJsonMessage(exception);
	}

	private JsonError getJsonMessage(Exception exception) {
		return JsonErrorBuilder.withMessage(exception.getMessage());
	}

	private void warn(String message, Exception exception) {
		LOG.warn("exception={}, type={}", message, exception.getClass().getName());
	}
}
