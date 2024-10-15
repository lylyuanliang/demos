package com.example.service.cases.resolverdemo;

import com.example.common.resolver.annotation.SubClassAnnotation;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@SubClassAnnotation(type = "type2")
@Data
public class Param2 extends ParamBase{
    private String param2Value;
    private Integer param2Value2;
}
