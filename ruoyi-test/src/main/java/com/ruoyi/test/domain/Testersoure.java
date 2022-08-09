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
    private Integer operator;


}