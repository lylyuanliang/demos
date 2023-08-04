package com.example.common.resolver.annotation;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomRequestBody {
    /**
     * 用来区分类型的字段 的名称, 这里默认type字段, 如果有需要, 自行修改
     * @return
     */
    String typeName() default "type";
}
