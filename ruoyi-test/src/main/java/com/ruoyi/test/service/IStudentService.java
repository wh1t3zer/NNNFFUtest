package com.ruoyi.test.service;

import com.ruoyi.test.domain.Student;

import java.util.List;

public interface IStudentService {
    /*
    *
    * */
    public List<Student> selectAllStudents(Student student);

    public Student selectTestUserByUserId(Integer no);
}
