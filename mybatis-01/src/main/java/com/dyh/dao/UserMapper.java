package com.dyh.dao;

import com.dyh.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {

    //獲取全部用戶
    List<User> getUserList();

    //根據id查詢用戶
    User getUserById(int id);

    User getUserById2(Map<String, Object> map);

    //插入用戶
    int addUser(User user);

    // 用map不需要知道數據庫裏有什麽，只需要查詢字段
    int addUser2(Map<String, Object> map);

    //修改用戶
    int updateUser(User user);

    //刪除用戶
    int deleteUser(int id);
}
