package com.framework.middleware.abstractUtil;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * 
 * @Description Redis工具类
 * @Author shengjie.tang
 * @Date 2019年3月10日
 * @Version 1.0
 */
public class RedisUtil {
	private static final Logger logger = Logger.getLogger(RedisUtil.class);
	
	@Autowired
	private static RedisConnectionFactory redisConnectionFactory;
	private static volatile RedisConnection redisConnection = null;
	
	//私有化构造方法。
	private RedisUtil() {
		RedisUtil.getConnectionInstance();
	}
	
	//DCL 双重锁定检查
	public static RedisConnection getConnectionInstance() {
		if (redisConnection != null) {
			synchronized (RedisConnection.class) {
				if(redisConnection == null){
					redisConnection = redisConnectionFactory.getConnection();
				}
			}
		}
		return redisConnection;
	}

	/**
	 * redis塞值
	 * @param key
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static Boolean setValue(Object key, Object value){
		redisConnectionFactory.getConnection();
		Boolean resultFlag = redisConnection.set(serialize(key).getBytes(), serialize(value).getBytes());
		if (resultFlag) {
			logger.debug("");
		} else {
			logger.error("");
		}
		closeStream(redisConnection);
		return resultFlag;
	}

	/**
	 * redis 塞值 对于批量塞值，你不能每一次都开流，再关闭流
	 * 
	 * @param key
	 * @param value
	 * @param expire
	 *            限制时间（s）
	 * @return
	 * @throws Exception
	 */
	public static Boolean setValue(Object key, Object value, int expire) {
		Boolean resultFlag = redisConnection.setEx(serialize(key).getBytes(), expire, serialize(value).getBytes());
		if (resultFlag) {
			logger.debug("");
		} else {
			logger.error("");
		}
		closeStream(redisConnection);
		return resultFlag;
	}
	
	public static Long remove(Object key) throws Exception {
		Long keys = redisConnection.del(serialize(key).getBytes());
		closeStream(redisConnection);
		return keys;
	}

	private static void closeStream(RedisConnection redisConnection) {
		if (null != redisConnection) {
			redisConnection.close();
		}
	}

	private static String serialize(Object obj) {
		if (null == obj) {
			logger.error("");
		}
		return String.valueOf(obj);
	}
}
