package com.ruoyi.test.service.impl;

import com.ruoyi.test.domain.Student;
import com.ruoyi.test.domain.TestUser;
import com.ruoyi.test.mapper.StudentMapper;
import com.ruoyi.test.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class StudentServiceImpl implements IStudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public List<Student> selectAllStudents(Student student) {
        return studentMapper.selectAllStudentList(student);
    }

    @Override
    public Student selectTestUserByUserId(Integer no) {
        return studentMapper.selectTestUserByUserId(no);
    }


}
