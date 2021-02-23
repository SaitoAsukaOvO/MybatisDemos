package com.dyh.dao;

import com.dyh.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    //根據id查詢用戶
    User getUserById(int id);

    //分页1
    List<User> getUserByLimit(Map<String,Integer> map);

    //分页2
    List<User> getUserByRowBounds();
}
