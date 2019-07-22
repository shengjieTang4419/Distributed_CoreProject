package com.framework.middleware.shareMapper;

import com.framework.middleware.shareBean.Operation;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OperationMapper {
    int deleteByPrimaryKey(String id);

    int insert(Operation record);

    int insertSelective(Operation record);

    Operation selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Operation record);

    int updateByPrimaryKey(Operation record);
}