package com.example.service.excel.demo;

import com.alibaba.excel.annotation.ExcelIgnore;
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
    private String largeType;

    @ExcelProperty(value = "市", order = 1)
    private String smallType;

    /**
     * 类型
     * <p>test1</p>
     * <p>test2</p>
     */
    @ExcelIgnore
    private String type;

    /**
     * 根据type字段, 暂时不同的下拉框
     */
    @ExcelProperty(value = "类型选择", order = 2)
    @DropdownList(cascadingReferFiled = "type", cascadingValueList = {
            @DropdownList.Cascading(key = "test1", valueList = {"aaa", "bbbb", "cccc", "ddddd"}),
            @DropdownList.Cascading(key = "test2", valueList = {"类型22", "类型223", "类型224", "类型225"})
    })
    private String typeSelect;
}