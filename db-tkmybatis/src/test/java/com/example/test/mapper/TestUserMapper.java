package com.example.test.mapper;

import cn.hutool.core.lang.Assert;
import com.example.mapperextra.entity.CustomUserDm;
import com.example.mapperextra.mapper.UserDmMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestUserMapper {

    @Autowired
    private UserDmMapper userDmMapper;

    @Test
    public void testInsertDmWithId() {
        System.out.println(("----- InsertDmWithId method test ------"));
        CustomUserDm user = CustomUserDm.builder()
                .email("lylyuanliang@qq.com")
                .age(1)
                .name("lylyuanliang")
                .build();
        userDmMapper.insertDmWithId(user);
        System.out.println(user);
    }

    @Test
    public void testInsert() {
        System.out.println(("----- Insert method test ------"));
        CustomUserDm user = CustomUserDm.builder()
                .email("lylyuanliang@qq.com")
                .age(1)
                .name("lylyuanliang")
                .build();
        userDmMapper.insert(user);
        System.out.println(user);
    }
}
