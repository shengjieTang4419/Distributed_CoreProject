package com.framework.middleware.abstractUtil;

import com.framework.middleware.abstractService.IFilter;
import com.framework.middleware.abstractService.IGroup;
import com.framework.middleware.abstractService.ISelector;
import com.framework.middleware.abstractServiceimpl.DefaultGroup;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 在没有用java8之前 先这么凑合吧 持续完善
 * 
 * @author hangwen
 * 
 */
public class LinqUtil {

	/**
	 * 集合中是否所有项都满足条件
	 * 
	 * @param source
	 *            源集合
	 * @param filter
	 *            条件接口
	 * @return 所有项都在filter返回true,则返回true,否则false
	 */
	public static <TSource> boolean all(Iterable<TSource> source, IFilter<TSource> filter) {
		boolean hasFilter = filter != null;
		if (!hasFilter) {
			return true;
		}

		for (TSource t : source) {
			if (!filter.filte(t)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 集合中是否至少一项满足条件
	 * 
	 * @param source
	 *            源集合
	 * @param filter
	 *            条件接口
	 * @return 只要一项在filter返回true,则返回true,否则false
	 */
	public static <TSource> boolean any(Iterable<TSource> source, IFilter<TSource> filter) {
		boolean hasFilter = filter != null;
		if (!hasFilter) {
			return true;
		}

		for (TSource t : source) {
			if (filter.filte(t)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取集合的数量
	 * 
	 * @param source
	 *            源集合
	 * @return
	 */
	public static <TSource> Integer count(Iterable<TSource> source) {
		return count(source, null);
	}

	/**
	 * 获取经过过滤后的集合的数量
	 * 
	 * @param source
	 *            源集合
	 * @param filter
	 *            过滤器
	 * @return
	 */
	public static <TSource> Integer count(Iterable<TSource> source, IFilter<TSource> filter) {
		return where(source, filter).size();
	}

	/**
	 * 计算集合的合计值
	 * 
	 * @param source
	 * @param selector
	 * @return
	 */
	public static <TSource> Double sum(Iterable<TSource> source, ISelector<TSource, Double> selector) {
		List<Double> select = LinqUtil.select(source, selector);

		return MathUtil.sum(select.toArray());
	}

	/**
	 * 计算集合的平均值
	 * 
	 * @param source
	 * @param selector
	 * @return
	 */
	public static <TSource> Double avg(Iterable<TSource> source, ISelector<TSource, Double> selector) {
		List<Double> select = LinqUtil.select(source, selector);

		return MathUtil.avg(select.toArray());
	}

	/**
	 * 去除集合的重复值
	 * 
	 * @param source
	 *            源集合
	 * @return
	 */
	public static <TSource> Set<TSource> distinct(Iterable<TSource> source) {
		return distinct(source, null);
	}

	/**
	 * 去除集合的重复值
	 * 
	 * @param source
	 *            源集合
	 * @param comparator
	 *            比较器，返回0表示两个相等
	 * @return
	 */
	public static <TSource> Set<TSource> distinct(Iterable<TSource> source, Comparator<TSource> comparator) {
		if (comparator == null) {
			comparator = getDefaultComparator();
		}

		Set<TSource> set = new LinkedHashSet<TSource>();
		for (TSource t1 : source) {
			boolean add = true;
			for (TSource t2 : set) {
				int c = comparator.compare(t1, t2);
				if (c == 0) {
					add = false;
					break;
				}
			}

			if (add) {
				set.add(t1);
			}
		}
		return set;
	}

	/**
	 * 获取两个集合的减集：first-second,即属于first但不属于second的部分
	 * 
	 * @param first
	 *            第一个集合
	 * @param second
	 *            第二个集合
	 * @return
	 */
	public static <TSource> List<TSource> except(Iterable<TSource> first, Iterable<TSource> second) {
		return except(first, second, null);
	}

	/**
	 * 获取两个集合的减集：first-second,即属于first但不属于second的部分
	 * 
	 * @param first
	 *            第一个集合
	 * @param second
	 *            第二个集合
	 * @param comparator
	 *            比较器，返回0表示两者相等
	 * @return
	 */
	public static <TSource> List<TSource> except(Iterable<TSource> first, Iterable<TSource> second,
			Comparator<TSource> comparator) {
		if (comparator == null) {
			comparator = getDefaultComparator();
		}

		List<TSource> rs = new ArrayList<TSource>();

		for (TSource f : first) {
			boolean existInSecond = false;
			for (TSource s : second) {
				int c = comparator.compare(f, s);
				if (c == 0) {
					existInSecond = true;
					break;
				}
			}

			if (!existInSecond) {
				rs.add(f);
			}
		}

		return rs;
	}

	/**
	 * 获取两个集合的减集：first-second,即属于first但不属于second的部分
	 * 
	 * @param first
	 *            第一个集合
	 * @param second
	 *            第二个集合
	 * @return
	 */
	public static <TSource> List<TSource> except(Set<TSource> first, Set<TSource> second) {
		List<TSource> rs = new ArrayList<TSource>();

		for (TSource f : first) {
			boolean existInSecond = second.contains(f);

			if (!existInSecond) {
				rs.add(f);
			}
		}

		return rs;
	}

	/**
	 * 找出第一个符合条件的项 找不到返回null
	 * 
	 * @param <TSource>
	 *            集合项的类型
	 * 
	 * @param source
	 *            源集合
	 * @param filter
	 *            过滤器
	 * @return
	 */
	public static <TSource> TSource first(Iterable<TSource> source, IFilter<TSource> filter) {
		if (source == null) {
			return null;
		}
		boolean hasFilter = filter != null;
		for (TSource t : source) {
			if (hasFilter && !filter.filte(t)) {
				continue;
			}
			return t;
		}
		return null;
	}

	/**
	 * 根据 keySelector 获取集合的Map
	 * 
	 * @param source
	 * @param keySelector
	 * @return
	 */
	public static <TKey, TSource> Map<TKey, TSource> getMap(Iterable<TSource> source,
			ISelector<TSource, TKey> keySelector) {
		ISelector<TSource, TSource> resultSelector = new ISelector<TSource, TSource>() {

			@Override
			public TSource select(TSource source) {
				return source;
			}
		};

		return getMap(source, keySelector, resultSelector);
	}

	/**
	 * 根据 keySelector 获取集合的Map,并对集合进行映射转换
	 * 
	 * @param source
	 * @param keySelector
	 * @param resultSelector
	 * @return
	 */
	public static <TKey, TSource, TResult> Map<TKey, TResult> getMap(Iterable<TSource> source,
			ISelector<TSource, TKey> keySelector, ISelector<TSource, TResult> resultSelector) {
		Map<TKey, TResult> map = new LinkedHashMap<TKey, TResult>();

		for (TSource item : source) {
			TKey key = keySelector.select(item);
			TResult result = resultSelector.select(item);
			map.put(key, result);
		}

		return map;
	}

	/**
	 * 根据 keySelector 对集合进行分组
	 * 
	 * @param <TKey>
	 *            键类型
	 * @param <TSource>
	 *            集合项的类型
	 * @param source
	 *            源集合
	 * @param keySelector
	 *            键选择器 返回值作为分组依据
	 * @return
	 */
	public static <TKey, TSource> List<IGroup<TKey, TSource>> groupBy(Iterable<TSource> source,
			ISelector<TSource, TKey> keySelector) {

		ISelector<TSource, TSource> resultSelector = new ISelector<TSource, TSource>() {

			@Override
			public TSource select(TSource item) {
				return item;
			}
		};

		return groupByAndGetElement(source, keySelector, resultSelector);
	}

	/**
	 * 根据 keySelector 对集合进行分组，然后根据elementSelector返回每个项的投影
	 * 
	 * @param <TKey>
	 *            键类型
	 * @param <TSource>
	 *            集合项的类型
	 * @param <TElement>
	 *            返回集合的项类型
	 * @param source
	 *            源集合
	 * @param keySelector
	 *            键选择器 返回值作为分组依据
	 * @param resultSelector
	 *            source对应的返回值
	 * @return
	 */
	public static <TKey, TSource, TElement> List<IGroup<TKey, TElement>> groupByAndGetElement(Iterable<TSource> source,
			ISelector<TSource, TKey> keySelector, ISelector<TSource, TElement> resultSelector) {
		List<IGroup<TKey, TElement>> groups = new ArrayList<IGroup<TKey, TElement>>();
		if (source == null || keySelector == null || resultSelector == null) {
			return groups;
		}
		Map<TKey, IGroup<TKey, TElement>> map = new LinkedHashMap<TKey, IGroup<TKey, TElement>>();
		for (TSource s : source) {
			TKey keyValue = keySelector.select(s);
			if (keyValue == null)
				continue;
			IGroup<TKey, TElement> group = map.get(keyValue);
			if (group == null) {
				group = new DefaultGroup<TKey, TElement>(keyValue);
				groups.add(group);
				map.put(keyValue, group);
			}
			TElement r = resultSelector.select(s);
			group.add(r);
		}

		return groups;
	}

	/**
	 * 根据 keySelector 对集合进行分组，然后根据resultSelector返回每个组对应的结果
	 * 
	 * @param <TKey>
	 *            键类型
	 * @param <TSource>
	 *            集合项的类型
	 * @param <TResult>
	 *            返回结果项类型
	 * @param source
	 *            源集合
	 * @param keySelector
	 *            键选择器 返回值作为分组依据
	 * @param resultSelector
	 *            分组对应的返回值
	 * @return
	 */
	public static <TKey, TSource, TResult> List<TResult> groupByAndGetResult(Iterable<TSource> source,
			ISelector<TSource, TKey> keySelector, ISelector<IGroup<TKey, TSource>, TResult> resultSelector) {

		List<IGroup<TKey, TSource>> groups = groupBy(source, keySelector);
		List<TResult> results = new ArrayList<TResult>();
		if (resultSelector == null)
			return results;
		for (IGroup<TKey, TSource> group : groups) {
			TResult r = resultSelector.select(group);
			results.add(r);
		}
		return results;
	}

	/**
	 * 获取两个集合的交集合：first∩second,即属于first又属于second的部分
	 * 
	 * @param first
	 *            第一个集合
	 * @param second
	 *            第二个集合
	 * @return
	 */
	public static <TSource> List<TSource> intersect(Iterable<TSource> first, Iterable<TSource> second) {
		return intersect(first, second, null);
	}

	/**
	 * 获取两个集合的交集合：first∩second,即属于first又属于second的部分
	 * 
	 * @param first
	 *            第一个集合
	 * @param second
	 *            第二个集合
	 * @param comparator
	 *            比较器，返回0表示两者相等
	 * @return
	 */
	public static <TSource> List<TSource> intersect(Iterable<TSource> first, Iterable<TSource> second,
			Comparator<TSource> comparator) {
		if (comparator == null) {
			comparator = getDefaultComparator();
		}

		List<TSource> rs = new ArrayList<TSource>();

		for (TSource f : first) {
			boolean existInSecond = false;
			for (TSource s : second) {
				int c = comparator.compare(f, s);
				if (c == 0) {
					existInSecond = true;
					break;
				}
			}

			if (existInSecond) {
				rs.add(f);
			}
		}

		return rs;
	}

	/**
	 * 获取两个集合的交集合：first∩second,即属于first又属于second的部分
	 * 
	 * @param first
	 *            第一个集合
	 * @param second
	 *            第二个集合
	 * @return
	 */
	public static <TSource> List<TSource> intersect(Set<TSource> first, Set<TSource> second) {
		List<TSource> rs = new ArrayList<TSource>();

		for (TSource f : first) {
			boolean existInSecond = second.contains(f);

			if (existInSecond) {
				rs.add(f);
			}
		}

		return rs;
	}

	/**
	 * 从集合中迭代返回指定值
	 * 
	 * @param <TSource>
	 *            返回集合项的类型
	 * @param <TResult>
	 *            源集合项的类型
	 * 
	 * @param source
	 *            源集合
	 * @param selector
	 *            选择器
	 * @return
	 */
	public static <TSource, TResult> List<TResult> select(Iterable<TSource> source, ISelector<TSource, TResult> selector) {
		List<TResult> rs = new ArrayList<TResult>();
		if (source == null || selector == null) {
			return rs;
		}
		for (TSource t : source) {
			TResult R = selector.select(t);
			if (R != null)
				rs.add(R);
		}
		return rs;
	}

	/**
	 * 过滤集合
	 * 
	 * @param <TSource>
	 *            集合项的类型
	 * 
	 * @param source
	 *            源集合
	 * @param filter
	 *            过滤器
	 * @return
	 */
	public static <TSource> List<TSource> where(Iterable<TSource> source, IFilter<TSource> filter) {
		List<TSource> rs = new ArrayList<TSource>();
		if (source == null) {
			return rs;
		}
		boolean hasFilter = filter != null;
		for (TSource t : source) {
			if (hasFilter && !filter.filte(t)) {
				continue;
			}
			rs.add(t);
		}
		return rs;
	}

	/**
	 * 多个条件过滤集合
	 * 
	 * @param <TSource>
	 *            集合项的类型
	 * 
	 * @param source
	 *            源集合
	 * @param filters
	 *            过滤器集合
	 * @return
	 */
	public static <TSource> List<TSource> where(Iterable<TSource> source, IFilter<TSource>... filters) {
		List<TSource> rs = new ArrayList<TSource>();
		if (source == null) {
			return rs;
		}
		boolean hasFilter = filters != null && filters.length > 0;
		for (TSource t : source) {
			boolean add = true;
			if (hasFilter) {
				for (IFilter<TSource> filter : filters) {
					if (filter != null && !filter.filte(t)) {
						add = false;
						continue;
					}
				}
			}

			if (add) {
				rs.add(t);
			}

		}
		return rs;
	}

	private static <TSource> Comparator<TSource> getDefaultComparator() {
		return new Comparator<TSource>() {

			@Override
			public int compare(TSource o1, TSource o2) {
				return o1.equals(o2) ? 0 : -1;
			}
		};
	}
}
