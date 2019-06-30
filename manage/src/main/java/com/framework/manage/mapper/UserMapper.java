package com.framework.manage.mapper;

import com.framework.manage.bean.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    int insert(User record);
    int insertSelective(User record);
    User findById(Integer id);
}