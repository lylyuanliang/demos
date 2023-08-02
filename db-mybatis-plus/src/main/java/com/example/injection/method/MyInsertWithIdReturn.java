package com.example.injection.method;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
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

    /**
     * 开启达梦自增主键插入功能,  %s表示表名
     */
    private static final String IDENTITY_INSERT_ON = "<script> set IDENTITY_INSERT %s on;";

    public MyInsertWithIdReturn() {
        // 调用父类有参构造函数(无参构造函数已过期)
        super(METHOD_NAME);
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        // 直接复用insert的sql, 需要先设置IDENTITY_INSERT on才能插入带id的sql, 或者你可以修改sql, 去掉id字段
        String sql = IDENTITY_INSERT_ON + SqlMethod.INSERT_ONE.getSql().replace("<script>", "");


        String columnScript = SqlScriptUtils.convertTrim(tableInfo.getAllInsertSqlColumnMaybeIf(null),
                LEFT_BRACKET, RIGHT_BRACKET, null, COMMA);
        String valuesScript = SqlScriptUtils.convertTrim(tableInfo.getAllInsertSqlPropertyMaybeIf(null),
                LEFT_BRACKET, RIGHT_BRACKET, null, COMMA);
        String tableName = tableInfo.getTableName();
        // 替换%s占位符
        sql = String.format(sql, tableName, tableName, columnScript, valuesScript);

        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        KeyGenerator generator = new IdReturnGenerator(tableInfo);
        return this.addInsertMappedStatement(mapperClass, modelClass, sqlSource,
                generator, null, null);
    }
}
