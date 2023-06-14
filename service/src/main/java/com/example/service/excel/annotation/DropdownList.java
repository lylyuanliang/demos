package com.example.service.excel.annotation;

import java.lang.annotation.*;

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

    @interface Cascading {
        /**
         * 类型
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
