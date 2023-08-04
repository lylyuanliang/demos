package com.example.mapperextra.mapper.register;

import com.example.mapperextra.provider.MyInsertProvider;
import org.apache.ibatis.annotations.InsertProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * 自定义方法
 * @RegisterMapper 注解可以避免 mappers 参数配置，通用 Mapper 检测到该接口被继承时，会自动注册。
 * @param <T>
 */
@RegisterMapper
public interface InsertDmWithIdMapper<T> {
    /**
     * 达梦插入
     * @param entity
     * @return
     */
    @InsertProvider(type= MyInsertProvider.class, method = "dynamicSQL")
    Integer insertDmWithId(T entity);
}
