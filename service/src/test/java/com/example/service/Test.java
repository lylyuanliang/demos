package com.example.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.service.constants.TestEnum;
import com.example.service.vo.TestVo;
import org.apache.poi.ss.formula.functions.T;

import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        JSONArray objects = JSONObject.parseArray("[1,2,3,4]");
        Map<String, Object> map = new HashMap<>();
        map.put("2", objects);

        System.out.println(JSONObject.toJSONString(map));
    }
}
