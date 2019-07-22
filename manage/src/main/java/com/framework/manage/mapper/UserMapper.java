package com.framework.manage.mapper;

import com.framework.manage.bean.User;
import com.framework.middleware.abstractMapping.BusinessMetaMapping;
import com.framework.middleware.abstractMapping.BusinessOperationMapping;
import com.framework.middleware.annotation.RefBusinessMeta;
import com.framework.middleware.annotation.RefBusinessOperation;
import org.apache.ibatis.annotations.Mapper;

@Mapper
//@RefBusinessMeta(BusinessMetaMapping.USER)
public interface UserMapper {
    // @RefBusinessOperation(BusinessOperationMapping.CREATE)
    void doInsert(User record);
    User findById(Integer id);
}