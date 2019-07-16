package com.framework.middleware.abstractUtil;

import java.util.Collection;
import java.util.Map;

public final class CollectionUtil {

	private CollectionUtil() {
	}

	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	public static boolean isEmpty(Map<?, ?> map) {
		return map == null || map.isEmpty();
	}

	public static <T> boolean isEmpty(T[] t) {
		return t == null || t.length == 0;
	}

	public static boolean isNotEmpty(Collection<?> collection) {
		return !isEmpty(collection);
	}

	public static boolean isNotEmpty(Map<?, ?> map) {
		return !isEmpty(map);
	}

	public static <T> boolean isNotEmpty(T[] t) {
		return !isEmpty(t);
	}
}
