package com.example.service.excel.demo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.example.service.excel.annotation.ColHidden;
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

    @ExcelProperty(value = "test2", order = 2)
    @DropdownList(cascadingReferFiled = "test", cascadingValueList = {
            @DropdownList.Cascading(key = "重庆市1111111", valueList = {"1111", "111111111"}),
            @DropdownList.Cascading(key = "xxxxx", valueList = {"2222", "2222222"}),
            @DropdownList.Cascading(key = "dfdfdfdf", valueList = {"333", "3333333333"})
    })
    private String test2;

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

    @ExcelProperty(value = "隐藏测试类型大类,此列隐藏", order = 5)
    @DropdownList(valueList = {"type1", "type2", "type3"})
    @ColHidden
    private String hiddenType;

    @ExcelProperty(value = "隐藏测试类小类,此列不隐藏", order = 6)
    @DropdownList(cascadingReferFiled = "hiddenType", cascadingValueList = {
            @DropdownList.Cascading(key = "type1", valueList = {"type1-1", "type1-1-1", "type1-1-1-1"}),
            @DropdownList.Cascading(key = "type2", valueList = {"type2-2", "type2-2-2", "type2-2-2-2"}),
            @DropdownList.Cascading(key = "type3", valueList = {"type3-3", "type3-3-3", "type3-3-3-3"}),
    })
    private String hiddenTypeSub;

    @ExcelProperty(value = "其他")
    private String other;

    @ExcelProperty(value = "其他,此列隐藏")
    @ColHidden
    private String otherHidden;

    @ExcelProperty(value = "类型测试")
    @DropdownList(valueList = {"type11", "type12", "type13", "type14"})
    private String elseTestType;

    /**
     * 主要测试, type11为一种下拉框, type12,type13,type14为一种下拉框, 用__else_cascading_sub_default__来简化配置
     */
    @ExcelProperty(value = "类型测试-sub")
    @DropdownList(cascadingReferFiled = "elseTestType", cascadingValueList = {
           @DropdownList.Cascading(key = "type11", valueList = {"type1-13v", "type1-13-v1", "type1-1-13v-v1"}),
            @DropdownList.Cascading(key = "__else_cascading_sub_default__", valueList = {"type1---------------", "type1-1---------1", "type1-1-341v----------"}),
    })
    private String elseTestTypeSub;

}
