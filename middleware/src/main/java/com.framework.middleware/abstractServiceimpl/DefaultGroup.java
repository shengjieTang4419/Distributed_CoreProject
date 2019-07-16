package com.framework.middleware.abstractServiceimpl;

import com.framework.middleware.abstractService.IGroup;

import java.util.ArrayList;
import java.util.Collection;

public class DefaultGroup<TKey, TSource> extends ArrayList<TSource> implements IGroup<TKey, TSource> {

	private static final long serialVersionUID = 2134982113914246682L;

	private TKey key;

	public DefaultGroup(TKey key) {
		super();
		this.key = key;
	}

	public DefaultGroup(TKey key, Collection<TSource> c) {
		super(c);
		this.key = key;
	}

	@Override
	public TKey getKey() {
		return key;
	}

}
