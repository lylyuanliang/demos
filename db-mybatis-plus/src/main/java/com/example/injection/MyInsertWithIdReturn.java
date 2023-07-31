package com.example.injection;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * mybatis-plus扩展sql注入器-自定义sql-插入并返回id
 */
public class MyInsertWithIdReturn extends AbstractMethod {

    private static final String METHOD_NAME = "insertWithIdReturn";



    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        // 直接复用insert的sql
        String sql = SqlMethod.INSERT_ONE.getSql();

        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        KeyGenerator generator = new IdReturnGenerator(tableInfo);

        return this.addInsertMappedStatement(mapperClass, modelClass, METHOD_NAME, sqlSource,
                generator, null, null);
    }
}
