package com.example.injection.method;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.example.injection.IdReturnGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * mybatis-plus扩展sql注入器-自定义sql-插入并返回id
 */
public class MyInsertWithIdReturn extends AbstractMethod {

    /**
     * 方法名, 这个方法名要和 com.example.injection.mapper.MyBaseMapper 中的方法名保持一致
     */
    private static final String METHOD_NAME = "insertWithIdReturn";

    public MyInsertWithIdReturn() {
        // 调用父类有参构造函数(无参构造函数已过期)
        super(METHOD_NAME);
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        // 直接复用insert的sql
        String sql = SqlMethod.INSERT_ONE.getSql();

        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        KeyGenerator generator = new IdReturnGenerator(tableInfo);

        return this.addInsertMappedStatement(mapperClass, modelClass, sqlSource,
                generator, null, null);
    }
}
