package com.example.injection.method;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * mybatis-plus扩展sql注入器-自定义sql-插入并返回id
 * (注意, mp原生insert方法已经支持这个功能, 修改实体类id的type即可, 比如 @TableId(value = "id",type = IdType.AUTO))
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

    /**
     *
     * @param mapperClass mapper 接口
     * @param modelClass  mapper 泛型
     * @param tableInfo   数据库表反射信息
     * @return
     */
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        // 直接复用insert的sql,
        String sql = SqlMethod.INSERT_ONE.getSql();


        String columnScript = SqlScriptUtils.convertTrim(tableInfo.getAllInsertSqlColumnMaybeIf(null),
                LEFT_BRACKET, RIGHT_BRACKET, null, COMMA);
        String valuesScript = SqlScriptUtils.convertTrim(tableInfo.getAllInsertSqlPropertyMaybeIf(null),
                LEFT_BRACKET, RIGHT_BRACKET, null, COMMA);
        String tableName = tableInfo.getTableName();
        // 替换%s占位符
        sql = String.format(sql, tableName, columnScript, valuesScript);

        String keyProperty = null;
        String keyColumn = null;
        KeyGenerator keyGenerator = NoKeyGenerator.INSTANCE;
        // 表包含主键处理逻辑,如果不包含主键当普通字段处理
        if (StringUtils.isNotBlank(tableInfo.getKeyProperty())) {
            if (tableInfo.getIdType() == IdType.AUTO) {
                /* 自增主键 */
                keyGenerator = Jdbc3KeyGenerator.INSTANCE;
                keyProperty = tableInfo.getKeyProperty();
                keyColumn = tableInfo.getKeyColumn();
            } else if (null != tableInfo.getKeySequence()) {
                keyGenerator = TableInfoHelper.genKeyGenerator(this.methodName, tableInfo, builderAssistant);
                keyProperty = tableInfo.getKeyProperty();
                keyColumn = tableInfo.getKeyColumn();
            }
        }

        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addInsertMappedStatement(mapperClass, modelClass, sqlSource,
                keyGenerator, keyProperty, keyColumn);
    }
}
