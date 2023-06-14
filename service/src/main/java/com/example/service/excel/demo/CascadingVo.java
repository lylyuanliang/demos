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
public class CascadingVo {

    @ExcelProperty(value = "test",order = 1)
    @DropdownList(valueList = {"重庆市1111111", "xxxxx", "dfdfdfdf"})
    private String test;

    @ExcelProperty(value = "省", order = 12)
    @DropdownList(valueList = {"浙江省", "广东省", "重庆市"})
    private String largeType;

    @ExcelProperty(value = "市", order = 22)
    @DropdownList(cascadingReferFiled = "largeType", cascadingValueList = {
            @DropdownList.Cascading(key = "浙江省", valueList = {"杭州市", "宁波市"}),
            @DropdownList.Cascading(key = "广东省", valueList = {"广州市", "深圳市"}),
            @DropdownList.Cascading(key = "重庆市", valueList = {"万州区", "云阳县"})
    })
    private String smallType;

}
