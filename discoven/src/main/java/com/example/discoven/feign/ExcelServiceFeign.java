package com.example.discoven.feign;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "service", path = "/excel")
public interface ExcelServiceFeign {
    /**
     * @return feign.Response, feign跨服务导出excel需要写上这个返回, 另外, 如果是导入,
     * 还需要加上consumes = "multipart/form-data", 例如consumes = "multipart/form-data"
     */
    @GetMapping("/downloadDropdown")
    Response downloadDropdown();
}
