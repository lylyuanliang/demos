package com.example.injection.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

/**
 * 用户表
 * 设置逻辑删除字段,并且逻辑删除字段不 select 出来
 *
 * @author miemie
 * @since 2018-08-12
 */
@Data
@TableName("custom_user_dm")
@Builder
public class CustomUserDm {
    /**
     * 不指定type的话, mp默认使用IdType.NONE
     */
    @TableId(value = "\"id\"", type=IdType.ASSIGN_ID)
    private Long id;
    private String name;
    private Integer age;
    private String email;
}
