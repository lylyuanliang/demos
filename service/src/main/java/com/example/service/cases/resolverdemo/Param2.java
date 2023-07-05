package com.example.service.cases.resolverdemo;

import com.example.service.resolver.annotation.SubClassAnnotation;

@SubClassAnnotation(type = "type2")
public class Param2 extends ParamBase{
    private String param2Value;
    private Integer param2Value2;

    public String getParam2Value() {
        return param2Value;
    }

    public void setParam2Value(String param2Value) {
        this.param2Value = param2Value;
    }

    public Integer getParam2Value2() {
        return param2Value2;
    }

    public void setParam2Value2(Integer param2Value2) {
        this.param2Value2 = param2Value2;
    }
}
