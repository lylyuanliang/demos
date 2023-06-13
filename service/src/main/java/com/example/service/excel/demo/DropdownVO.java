package com.example.service.excel.demo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.example.service.excel.annotation.DropdownList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DropdownVO {

    @ExcelProperty("test")
    @DropdownList(valueList = {"重庆市1111111", "xxxxx", "dfdfdfdf"})
    private String test;

    @ExcelProperty("省")
    @DropdownList(valueList = {"浙江省", "广东省", "重庆市"})
    private String largeType ;

    @ExcelProperty(value = "市", order = 1)
    private String smallType ;
}