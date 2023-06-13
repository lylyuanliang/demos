package com.example.service.resolver.demo;

import com.example.service.resolver.annotation.SubClassAnnotation;

@SubClassAnnotation(type = "type1")
public class Param1 extends ParamBase{
    private String param1Value;
    private String param1Value1;

    public String getParam1Value() {
        return param1Value;
    }

    public void setParam1Value(String param1Value) {
        this.param1Value = param1Value;
    }

    public String getParam1Value1() {
        return param1Value1;
    }

    public void setParam1Value1(String param1Value1) {
        this.param1Value1 = param1Value1;
    }
}
