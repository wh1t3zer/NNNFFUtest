package com.ruoyi.web.controller.test;

import com.ruoyi.test.domain.Testersoure;
import com.ruoyi.test.mapper.TestersoureMapper;
import com.ruoyi.test.service.ITestersoureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


public class TestersoureController {
    @Autowired
    private TestersoureMapper testersoureMapper;

    @Autowired
    private ITestersoureService testersoureService;
    /**
     * 查询奖项
     * @param
     * @return
     */
//    @PreAuthorize("@ss.hasPermi('test:class:getAwards')")
//    @PostMapping("/getAwards")
//    public List<Testersoure> handleGetAwards(@RequestBody Testersoure testersoure){
//        /*List<Testersoure> testersoureList = testersoureService.getAwardsListByModuleAndNo(testersoure);
//        //拼接奖项内容title为 xxx,xxx,xxx 格式
//        String title = "";
//        for(int i = 0; i < testersoureList.size(); i++){
//            title += testersoureList.get(i).getTitle();
//            title += ",";
//        }
//        if (title != "") {
//            title = title.substring(0,title.length() - 1);
//        }
//        return title;*/
//
//        return testersoureService.getAwardListByNo(testersoure);
//
//    }

}
