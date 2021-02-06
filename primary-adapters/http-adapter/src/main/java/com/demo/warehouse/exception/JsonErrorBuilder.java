package com.demo.warehouse.exception;

import java.util.ArrayList;
import java.util.List;

public class JsonErrorBuilder {

	private JsonErrorBuilder() {
	}

	public static JsonError withMessage(String message) {
		return withMessage(null, message);
	}

	public static JsonError withMessage(String field, String message) {
		List<ErrorMessage> errors = new ArrayList<ErrorMessage>();
		errors.add(new ErrorMessage(field, message));
		return new JsonError(errors);
	}
}
