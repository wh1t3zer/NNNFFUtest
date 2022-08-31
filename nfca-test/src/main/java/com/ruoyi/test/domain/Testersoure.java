package com.ruoyi.test.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Testersoure {
    private String id;
    private Integer no;
    private Integer score;
    private String module;
    private String title;
    private String detail;
    private String reason;
    private Integer adopter;
    private Integer adopter2;
    private String operator;
    private String Reason;


    private String openId;
    public String getOpenId() {
        return openId;
    }
    public void setOpenId(String openId) {
        this.openId = openId;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String Reason) {
        this.Reason = Reason;
    }

    public Integer getAdopter() {
        return adopter;
    }

    public void setAdopter(Integer adopter) {
        this.adopter = adopter;
    }

    public Integer getAdopter2() {
        return adopter2;
    }

    public void setAdopter2(Integer adopter2) {
        this.adopter2 = adopter2;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

//    public String getReason1() {
//        return Reason;
//    }
//
//    public void setReason1(String Reason) {
//        this.Reason = Reason;
//    }

    @Override
    public String toString() {
        return "Testersoure{" +
                "id='" + id + '\'' +
                ", no=" + no +
                ", score=" + score +
                ", module='" + module + '\'' +
                ", title='" + title + '\'' +
                ", detail='" + detail + '\'' +
                ", reason='" + reason + '\'' +
                ", adopter=" + adopter +
                ", adopter2=" + adopter2 +
                ", operator=" + operator +
                '}';
    }
}





