package com.example.service.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ReflectionUtils {
    /**
     * 获取class所有field(包括父类)
     *
     * @param clazz 实体类类型
     * @return
     */
    public static List<Field> getAllFields(Class<?> clazz) {
        return getAllFields(clazz, null);
    }

    /**
     * 获取class所有field(包括父类)
     *
     * @param clazz     实体类类型
     * @param predicate 一个接口, 用来过滤field, 满足该条件的才会返回
     *                  <p>为空时获取全部</p>
     *                  <p>不为空时满足该条件的才会返回</p>
     * @return
     */
    public static List<Field> getAllFields(Class<?> clazz, Predicate<Field> predicate) {
        List<Field> annotatedFields = new ArrayList<>();
        // 循环获取(包括父类)
        while (clazz != null) {
            // 遍历实体类的字段
            for (Field field : clazz.getDeclaredFields()) {
                if (predicate == null || predicate.test(field)) {
                    annotatedFields.add(field);
                }
            }

            // 获取父类
            clazz = clazz.getSuperclass();
        }


        return annotatedFields;
    }
}
