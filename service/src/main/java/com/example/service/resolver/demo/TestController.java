package com.example.service.resolver.demo;

import com.alibaba.fastjson.JSONObject;
import com.example.service.resolver.annotation.CustomRequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/test/{param}")
    public String test(@PathVariable String param) {

        return "test" + param;
    }

    @PostMapping("/test")
    public String test(@CustomRequestBody ParamBase param) {

        return JSONObject.toJSONString(param);
    }
}
