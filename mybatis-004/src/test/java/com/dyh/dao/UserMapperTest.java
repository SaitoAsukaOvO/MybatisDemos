package com.dyh.dao;

import com.dyh.pojo.User;
import com.dyh.util.MybatisUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserMapperTest {

    static Logger logger = Logger.getLogger(UserMapperTest.class);


    @Test
    public void getUserById() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.getUserById(2);
        System.out.println(user);

        sqlSession.close();
    }

    @Test
    public void testlog4j() {
        logger.info("info: Enter testlog4j");
        logger.debug("debug: Enter testlog4j");
        logger.error("error: Enter testlog4j");
    }

    @Test
    public void getUserByLimit() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("startIndex", 0);
        map.put("pageSize", 2);
        List<User> userByLimit = mapper.getUserByLimit(map);
        for (User user : userByLimit) {
            System.out.println(user);
        }

        sqlSession.close();
    }

    @Test
    public void getUserByRowBounds() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        //Row Bounds
        RowBounds rowBounds = new RowBounds(1, 2);


        List<User> userList = sqlSession.selectList("com.dyh.dao.UserMapper.getUserByRowBounds", null, rowBounds);
        for (User user : userList) {
            System.out.println(user);
        }

        sqlSession.close();
    }

}
