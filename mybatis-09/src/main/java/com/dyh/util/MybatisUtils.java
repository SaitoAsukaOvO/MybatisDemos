package com.dyh.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

//sqlSessionFactory
public class MybatisUtils {
    private static SqlSessionFactory sqlSessionFactory;
    static {
        try {
            //使用mybatis第一步
            String source = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(source);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static SqlSession getSqlSession() {
        //自动commit
        return sqlSessionFactory.openSession(true);
    }

}
