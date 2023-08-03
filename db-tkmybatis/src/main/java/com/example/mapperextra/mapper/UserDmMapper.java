package com.example.mapperextra.mapper;

import com.example.mapperextra.entity.CustomUserDm;
import tk.mybatis.mapper.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface UserDmMapper extends Mapper<CustomUserDm>, InsertDmWithIdMapper<CustomUserDm> {
}
