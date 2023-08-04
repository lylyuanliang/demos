package com.example.mapperextra.mapper;

import com.example.mapperextra.entity.CustomUser;
import tk.mybatis.mapper.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface UserMapper extends Mapper<CustomUser> {
}
