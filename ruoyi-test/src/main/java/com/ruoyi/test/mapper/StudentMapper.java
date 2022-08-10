package com.ruoyi.test.mapper;

import com.ruoyi.test.domain.Student;

import java.util.List;

public interface StudentMapper {

    //查询所有学生
    public List<Student> selectAllStudentList(Student student);

    // ID查询学生
    public Student selectTestUserByUserId(Integer no);

    public int updateStatusByNo(Student student);


    public int updateStatusByNos(String nos);


}
