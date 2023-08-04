package com.example.injection.config;

import com.example.injection.MyInjector;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.example.injection.mapper")
public class MybatisPlusConfig {
    @Bean
    public MyInjector myInjector() {
        return new MyInjector();
    }
}
