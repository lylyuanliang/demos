package com.example.mapperextra.entity;


import lombok.Builder;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户表
 * 设置逻辑删除字段,并且逻辑删除字段不 select 出来
 *
 * @author miemie
 * @since 2018-08-12
 */
@Data
@Table(name = "custom_user")
@Builder
public class CustomUser {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private String name;
    private Integer age;
    private String email;
}
