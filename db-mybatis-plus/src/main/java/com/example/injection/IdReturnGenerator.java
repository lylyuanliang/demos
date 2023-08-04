package com.example.injection;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 * 回查id生成器, 这里只是做个步骤记录, 实际使用建议直接 使用 org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator
 */
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
           // 这里实现取id以及设置id的功能, 见org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator
        } catch (Exception e) {
            throw new ExecutorException("Error getting generated key or setting result to parameter object. Cause: " + e, e);
        }
    }
}
