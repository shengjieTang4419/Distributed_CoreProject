package com.framework.middleware.abstractService;

/**
 * 选择器接口
 * 
 * @author hangwen
 * 
 * @param <TSource>源类型
 * @param <TRsult>返回类型
 */
public interface ISelector<TSource, TRsult> {
	TRsult select(TSource source);
}
