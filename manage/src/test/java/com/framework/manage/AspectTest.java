package com.framework.manage;

import com.framework.manage.bean.User;
import com.framework.manage.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author ：shengjie.tang
 * @date ：Created in 2019/7/19 16:22
 * @description：切面测试
 * @modified By：
 * @version: 1$
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@MapperScan(value = "com.framework.*.*apper")
public class AspectTest {

    @Autowired
    UserMapper mapper;

    @Test
    public void test(){
        User user = new User();
        user.setCode("001");
        user.setName("001号");
        user.setChannelid(1);
        user.setStatus("0");
        mapper.doInsert(user);
    }
}
