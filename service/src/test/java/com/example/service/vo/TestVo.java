package com.example.service.vo;

import com.example.service.constants.TestEnum;
import lombok.Data;

@Data
public class TestVo {
    private String name;
    private String sex;

    private TestEnum t;
}
