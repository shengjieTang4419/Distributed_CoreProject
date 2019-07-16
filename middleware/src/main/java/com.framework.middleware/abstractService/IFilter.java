package com.framework.middleware.abstractService;

/**
 * 过滤器接口
 * 
 * @author hangwen
 * 
 * @param <TSource>
 *            过滤项类型
 */
public interface IFilter<TSource> {
	boolean filte(TSource t);
}
