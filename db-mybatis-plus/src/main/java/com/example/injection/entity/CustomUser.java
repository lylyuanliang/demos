package com.example.injection.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户表
 * 设置逻辑删除字段,并且逻辑删除字段不 select 出来
 *
 * @author miemie
 * @since 2018-08-12
 */
@Data
@TableName("custom_user")
@Builder
public class CustomUser {
    private Long id;
    private String name;
    private Integer age;
    private String email;
}
