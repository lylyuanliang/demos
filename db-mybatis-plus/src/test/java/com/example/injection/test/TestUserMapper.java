package com.example.injection.test;

import cn.hutool.core.lang.Assert;
import com.example.injection.entity.User;
import com.example.injection.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestUserMapper {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testInsertWithIdReturn() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        Assert.isTrue(5 == userList.size(), "");
        userList.forEach(System.out::println);
    }
}
