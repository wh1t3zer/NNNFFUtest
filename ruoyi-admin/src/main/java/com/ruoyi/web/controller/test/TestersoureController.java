package com.ruoyi.web.controller.test;

import com.ruoyi.test.domain.Testersoure;
import com.ruoyi.test.mapper.TestersoureMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test/class")
@Slf4j
public class TestersoureController {
    @Autowired
    private TestersoureMapper testersoureMapper;

    @GetMapping("/findAll")
    public List<Testersoure> index(){
        return testersoureMapper.findAll();
    }
}
