package com.example.injection.test;

import cn.hutool.core.lang.Assert;
import com.example.injection.entity.CustomUser;
import com.example.injection.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class TestUserMapper {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelectList() {
        System.out.println(("----- selectAll method test ------"));
        List<CustomUser> userList = userMapper.selectList(null);
        Assert.isTrue(5 == userList.size(), "");
        userList.forEach(System.out::println);
    }

    @Test
    @Transactional(rollbackFor = Exception.class)
    public void testInsertWithIdReturn() {
        System.out.println(("----- insertWithIdReturn method test ------"));
        CustomUser user = CustomUser.builder()
                .email("lylyuanliang@qq.com")
                .age(1)
                .name("lylyuanliang")
                .build();
        userMapper.insertWithIdReturn(user);
        System.out.println(user);
    }

    @Test
    @Transactional(rollbackFor = Exception.class)
    public void testInsert() {
        System.out.println(("----- Insert method test ------"));
        CustomUser user = CustomUser.builder()
                .email("lylyuanliang@qq.com")
                .age(1)
                .name("lylyuanliang")
                .build();
        userMapper.insert(user);
        System.out.println(user);
    }
}
