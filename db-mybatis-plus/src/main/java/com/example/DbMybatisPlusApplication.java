package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.injection.mapper")
public class DbMybatisPlusApplication {
    public static void main(String[] args) {
        SpringApplication.run(DbMybatisPlusApplication.class, args);
    }
}
