package com.framework.middleware.abstractUtil;

import java.math.BigDecimal;

public class DoubleUtil {

	public static Double parse(Object o) {
		if (o == null || o.equals(""))
			return null;

		Class<? extends Object> clazz = o.getClass();
		if (clazz.equals(Double.class) || clazz.equals(double.class)) {
			return (Double) o;
		}

		try {
			String v = String.valueOf(o);
			boolean isPercent = v.endsWith("%");
			if (isPercent) {
				v = v.substring(0, v.length() - 1);
			}

			if (v.contains(",")) {
				v = v.replace(",", "");
			}

			Double d = Double.valueOf(v);
			if (isPercent) {
				d = d / 100;
			}

			return d;

		} catch (Exception e) {
			return 0d;
		}
	}

	/**
	 * Double类型相除（v1/v2）BigDecimal.ROUND_HALF_UP 四舍五入
	 * 
	 * @param scale
	 *            小数点后保留位数
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @return Double
	 */
	public static Double div(double v1, double v2, int scale) {
		BigDecimal bd1 = new BigDecimal(String.valueOf(v1));
		BigDecimal bd2 = new BigDecimal(String.valueOf(v2));
		BigDecimal bd = bd1.divide(bd2, scale, BigDecimal.ROUND_HALF_UP);
		return new Double(bd.doubleValue());
	}

	/**
	 * Double类型相加
	 * 
	 * @param v1
	 * @param v2
	 * @return Double
	 */
	public static Double add(double v1, double v2) {
		BigDecimal bd1 = new BigDecimal(String.valueOf(v1));
		BigDecimal bd2 = new BigDecimal(String.valueOf(v2));
		BigDecimal bd = bd1.add(bd2);
		return new Double(bd.doubleValue());
	}

	/**
	 * Double类型相减 (v1-v2)
	 * 
	 * @param v1
	 * @param v2
	 * @return Double
	 */
	public static Double sub(double v1, double v2) {
		BigDecimal bd1 = new BigDecimal(Double.toString(v1));
		BigDecimal bd2 = new BigDecimal(Double.toString(v2));
		BigDecimal bd = bd1.subtract(bd2);
		return new Double(bd.doubleValue());
	}

	/**
	 * Double类型相乘
	 * 
	 * @param v1
	 * @param v2
	 * @return Double
	 */
	public static Double mul(double v1, double v2) {
		BigDecimal bd1 = new BigDecimal(String.valueOf(v1));
		BigDecimal bd2 = new BigDecimal(String.valueOf(v2));
		BigDecimal bd = bd1.multiply(bd2);
		return new Double(bd.doubleValue());
	}
	
	/**
	 * double 类型精度
	 * 
	 * @param value
	 * @param scale
	 * @param roundingMode
	 *            BigDecimal.ROUND_HALF_UP
	 * @return
	 */
	public static Double round(Double value, int scale, int roundingMode) {
		if (value == null) {
			return value;
		}
		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(scale, roundingMode);
		return bd.doubleValue();
	}
}
