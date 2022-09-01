package com.ruoyi.test.domain;

import com.ruoyi.common.annotation.Excel;
import lombok.*;

@Data
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class ExportInfo {

    @Excel(name = "姓名")
    private String name;        //姓名

    @Excel(name = "学号",cellType = Excel.ColumnType.NUMERIC)
    private Integer no;         //学号

    @Excel(name = "班级")
    private String className;   //班级

    @Excel(name = "年级")
    private Integer grade;      //年级

    @Excel(name = "发展性素质测评",cellType = Excel.ColumnType.NUMERIC)
    private Integer score1;     //发展性素质测评

    @Excel(name = "基础性素质测评",cellType = Excel.ColumnType.NUMERIC)
    private Integer score2;     //基础性素质测评



    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getScore1() {
        return score1;
    }

    public void setScore1(Integer score1) {
        this.score1 = score1;
    }

    public Integer getScore2() {
        return score2;
    }

    public void setScore2(Integer score2) {
        this.score2 = score2;
    }
}
