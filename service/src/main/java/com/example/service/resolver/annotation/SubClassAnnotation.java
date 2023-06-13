package com.example.service.resolver.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SubClassAnnotation {
    String type() default "";
}
