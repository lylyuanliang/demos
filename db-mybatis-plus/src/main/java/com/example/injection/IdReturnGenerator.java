package com.example.injection;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.Statement;

public class IdReturnGenerator implements KeyGenerator {
    private TableInfo tableInfo;

    public IdReturnGenerator(TableInfo tableInfo) {
        this.tableInfo = tableInfo;
    }

    /**
     * 在执行insert之前，设置属性order="BEFORE"
     *
     * @param executor
     * @param mappedStatement
     * @param statement
     * @param o
     */
    @Override
    public void processBefore(Executor executor, MappedStatement mappedStatement, Statement statement, Object o) {

    }

    /**
     * 在执行insert之后，设置属性order="AFTER", 参考 org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator
     *
     * @param executor
     * @param mappedStatement
     * @param statement
     * @param o
     */
    @Override
    public void processAfter(Executor executor, MappedStatement mappedStatement, Statement statement, Object o) {
        /*
        简略版的实现
         */
        try (ResultSet rs = statement.getGeneratedKeys()) {

            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(tableInfo.getKeyProperty(), o.getClass());
            while (rs.next()) {
                long id = rs.getLong("id");
                propertyDescriptor.getWriteMethod().invoke(o, id);
            }
        } catch (Exception e) {
            throw new ExecutorException("Error getting generated key or setting result to parameter object. Cause: " + e, e);
        }
    }
}
