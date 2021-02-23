package com.dyh.dao;

import com.dyh.pojo.User;

public interface UserMapper {
    //根據id查詢用戶
    User getUserById(int id);

}
