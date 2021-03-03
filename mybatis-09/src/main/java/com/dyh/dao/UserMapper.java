package com.dyh.dao;

import com.dyh.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    User queryUserById(@Param("id") int id);

    int updateUser(User user);
}
