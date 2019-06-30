package com.framework.business;

import com.framework.middleware.cache.RedisCacheConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
@EnableCaching(proxyTargetClass = true)
@Import(RedisCacheConfig.class) //引用middleware核心类的配置类。
@SpringBootApplication
@MapperScan(value = "com.framework.business.mapper")
@ImportResource(value = {"classpath:providers.xml"}) // 使用 consumer.xml 配置；
public class BusinessApplication {

    public static void main(String[] args) {
        SpringApplication.run(BusinessApplication.class, args);
    }

}
