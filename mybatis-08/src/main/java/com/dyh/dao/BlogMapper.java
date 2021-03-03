package com.dyh.dao;

import com.dyh.pojo.Blog;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface BlogMapper {

    int addBlog(Blog blog);

    List<Blog> queryBlogsIF(Map map);

    List<Blog> queryBlogsChoose(Map map);

    int UpdateBlog(Map map);
}
