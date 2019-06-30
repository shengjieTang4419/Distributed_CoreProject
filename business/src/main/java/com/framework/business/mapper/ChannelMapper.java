package com.framework.business.mapper;

import com.framework.middleware.shareBean.Channel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChannelMapper {
    int insert(Channel record);

    int insertSelective(Channel record);

    Channel findById(Integer i);
}