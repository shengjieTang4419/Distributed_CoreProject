package com.framework.middleware.abstractService;

import java.util.List;

/**
 * 分组接口 继承List<TSource>
 * 
 * @author hangwen
 * 
 * @param <TKey>
 *            分组项类型
 * @param <TSource>
 *            源类型
 */
public interface IGroup<TKey, TSource> extends List<TSource> {
	TKey getKey();
}
