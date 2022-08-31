package com.ruoyi.test.service.impl;

import com.ruoyi.test.domain.Student;
import com.ruoyi.test.domain.TestUser;
import com.ruoyi.test.mapper.StudentMapper;
import com.ruoyi.test.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements IStudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public List<Student> selectAllStudents(Student student) {
        return studentMapper.selectAllStudentList(student);
    }

    @Override
    public List<Student> selectStudents(Student student) {
        return studentMapper.selectStudents(student);
    }

    @Override
    public List<Student> selectClassStudents(Student student) {
        return studentMapper.selectClassStudentList(student);
    }

    @Override
    public List<Student> selectMajorStudents(Student student) {
        return studentMapper.selectMajorStudentList(student);
    }

    @Override
    public Student selectTestUserByUserId(Integer no) {
        return studentMapper.selectTestUserByUserId(no);
    }

    @Override
    public int updateStatusByNo(Student student) {
        return studentMapper.updateStatusByNo(student);
    }

    @Override
    public int updateStatusByNos(String nos) {
        return studentMapper.updateStatusByNos(nos);
    }


}
