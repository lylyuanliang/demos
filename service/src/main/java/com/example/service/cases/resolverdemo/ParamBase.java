package com.example.service.cases.resolverdemo;

public class ParamBase {
    public static final String TYPE_NAME = "type";
    private String type;

    private String commonParam1;
    private String commonParam2;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCommonParam1() {
        return commonParam1;
    }

    public void setCommonParam1(String commonParam1) {
        this.commonParam1 = commonParam1;
    }

    public String getCommonParam2() {
        return commonParam2;
    }

    public void setCommonParam2(String commonParam2) {
        this.commonParam2 = commonParam2;
    }
}
