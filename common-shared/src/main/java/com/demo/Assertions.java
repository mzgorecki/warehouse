package com.demo;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public final class Assertions {

	private Assertions() {
		super();
	}

	public static void isNotNull(Object object, String messageTemplate, Object... formatArgs) {
		checkArgument(object != null, messageTemplate, formatArgs);
	}

	public static void isNotNull(Object object, String message) {
		checkArgument(object != null, message);
	}

	public static void isNotEmpty(String string, String message) {
		checkArgument(!Strings.isNullOrEmpty(string), message);
	}

	public static void isNotEmpty(Collection<?> collection, String message) {
		checkArgument(collection != null && !collection.isEmpty(), message);
	}

	public static void isNotEmpty(Collection<?> collection, String messageTemplate, Object... formatArgs) {
		isNotEmpty(collection, String.format(messageTemplate, formatArgs));
	}

	public static <K, V> void containsKey(Map<K, V> map, K key, String messageTemplate, Object... args) {
		if (!map.containsKey(key)) {
			throw new IllegalStateException(String.format(messageTemplate, args));
		}
	}

	private static void checkArgument(boolean expression, String templateMessage, Object... args) {
		Preconditions.checkArgument(expression, String.format(templateMessage, args));
	}
}
