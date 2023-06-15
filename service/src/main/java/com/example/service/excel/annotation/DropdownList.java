package com.example.service.excel.annotation;

import java.lang.annotation.*;

/**
 * 下拉框注解, 用来配置下拉框的列表
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DropdownList {
    /**
     * 下拉框的值
     *
     * @return
     */
    String[] valueList() default {};

    /**
     * 级联关联字段
     *
     * @return
     */
    String cascadingReferFiled() default "";

    /**
     * 级联数据列表
     *
     * @return
     */
    Cascading[] cascadingValueList() default {};

    /**
     * 级联注解, 用来配置子项列表
     */
    @interface Cascading {
        /**
         * 类型
         *   __cascading_sub_default__ 表示默认列表（即如果配置此项, 当上一级分类中没有匹配项时, 展示此分类）
         *
         * @return
         */
        String key();

        /**
         * 下拉框的值列表
         *
         * @return
         */
        String[] valueList();

    }
}
