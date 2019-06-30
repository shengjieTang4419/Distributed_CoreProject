package com.framework.middleware.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Description 解决redis缓存时 乱码问题
 * @Author shengjie.tang
 * @Date 2019年3月11日
 * @Version 1.0
 */
@Configuration
@EnableCaching
public class RedisCacheConfig {
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
    	
    	RedisSerializer<String> redisSerializer = new StringRedisSerializer();
    	GenericJackson2JsonRedisSerializer jsonRedisSerializer =new GenericJackson2JsonRedisSerializer();
    	
    	// 配置序列化
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
        		.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
        		.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jsonRedisSerializer))      
        		.disableCachingNullValues();
      
        RedisCacheManager cacheManager = RedisCacheManager.builder(factory)
                .cacheDefaults(config)
                .build();
        return cacheManager;
    }
	
}
