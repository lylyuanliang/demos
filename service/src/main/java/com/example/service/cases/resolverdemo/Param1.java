package com.example.service.cases.resolverdemo;

import com.example.common.resolver.annotation.SubClassAnnotation;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@SubClassAnnotation(type = "type1")
@Data
public class Param1 extends ParamBase{
    private String param1Value;
    private String param1Value1;
}
