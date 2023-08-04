package com.example.injection.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 定义方法
 *
 * @param <T>
 */
public interface MyBaseMapper<T> extends BaseMapper<T> {
    /**
     * 自定义插入方法
     *
     * @param entity 实体
     * @return 插入并返回id, (返回的id会填充在entity的id字段中)
     */
    Integer insertWithIdReturn(T entity);

    /**
     * 自定义插入方法(插入达梦数据库)
     *
     * @param entity 实体
     * @return 插入并返回id, (返回的id会填充在entity的id字段中)
     */
    Integer insertDmWithId(T entity);
}
