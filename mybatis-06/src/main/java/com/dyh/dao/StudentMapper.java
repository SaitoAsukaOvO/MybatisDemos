package com.dyh.dao;

import com.dyh.pojo.Student;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface StudentMapper {
    //查询所有学生信息，以及对应老师信息
    List<Student> getStudent();
    List<Student> getStudent2();
}
