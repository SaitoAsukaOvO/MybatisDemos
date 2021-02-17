package com.dyh.dao;

import com.dyh.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {

    //獲取全部用戶
    List<User> getUserList();

    //根據id查詢用戶
    User getUserById(int id);

    //插入用戶
    int addUser(User user);

    //修改用戶
    int updateUser(User user);

    //刪除用戶
    int deleteUser(int id);
}
