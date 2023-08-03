package com.example.injection.test;

import cn.hutool.core.lang.Assert;
import com.example.injection.entity.CustomUser;
import com.example.injection.entity.CustomUserDm;
import com.example.injection.mapper.UserDmMapper;
import com.example.injection.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestUserMapper {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserDmMapper userDmMapper;

    @Test
    public void testSelectList() {
        System.out.println(("----- selectAll method test ------"));
        List<CustomUser> userList = userMapper.selectList(null);
        Assert.isTrue(5 == userList.size(), "");
        userList.forEach(System.out::println);
    }

    @Test
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
}
