package com.example.service.excel.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DropdownList {
    /**
     * 下拉框的值
     * @return
     */
    String[] valueList();
}
