package com.ruoyi.test.domain;

import lombok.*;

@Data
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class Student {
    private Integer id;
    private String name;
    private Integer no;
    private Integer grade;
    private String openid;
    private Integer room;
    private String CEE;
    private String birthday;
    //private Integer classId;
    private String classId;
    private String major;
    private Integer mid;
    private Integer cid;
    private Integer Tpid;
    private Integer state;
    private String alipay;
    private String GPA;
    private String status;
    private String classValue;
    private String majorValue;
    private String gradeValue;
    private Integer operator;

    public Integer getOperator() {
        return operator;
    }

    public void setOperator(Integer operator) {
        this.operator = operator;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Integer getRoom() {
        return room;
    }

    public void setRoom(Integer room) {
        this.room = room;
    }

    public String getCEE() {
        return CEE;
    }

    public void setCEE(String CEE) {
        this.CEE = CEE;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Integer getTpid() {
        return Tpid;
    }

    public void setTpid(Integer tpid) {
        Tpid = tpid;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getAlipay() {
        return alipay;
    }

    public void setAlipay(String alipay) {
        this.alipay = alipay;
    }

    public String getGPA() {
        return GPA;
    }

    public void setGPA(String GPA) {
        this.GPA = GPA;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClassValue() {
        return classValue;
    }

    public void setClassValue(String classValue) { this.classValue = classValue; }

    public String getMajorValue() {
        return majorValue;
    }

    public void setMajorValue(String majorValue) { this.majorValue = majorValue; }
}
