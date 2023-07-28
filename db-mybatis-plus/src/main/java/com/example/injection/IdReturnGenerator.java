package com.example.injection;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;

import java.sql.Statement;

public class IdReturnGenerator implements KeyGenerator {
    /**
     * 在执行insert之前，设置属性order="BEFORE"
     * @param executor
     * @param mappedStatement
     * @param statement
     * @param o
     */
    @Override
    public void processBefore(Executor executor, MappedStatement mappedStatement, Statement statement, Object o) {

    }

    /**
     * 在执行insert之后，设置属性order="AFTER"
     * @param executor
     * @param mappedStatement
     * @param statement
     * @param o
     */
    @Override
    public void processAfter(Executor executor, MappedStatement mappedStatement, Statement statement, Object o) {

    }
}
