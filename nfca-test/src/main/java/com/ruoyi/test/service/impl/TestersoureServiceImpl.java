package com.ruoyi.test.service.impl;

import com.ruoyi.test.domain.Testersoure;
import com.ruoyi.test.mapper.TestersoureMapper;
import com.ruoyi.test.service.ITestersoureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TestersoureServiceImpl implements ITestersoureService {
    @Autowired
    private TestersoureMapper testersoureMapper;


    @Override
    public int updateReasonByNo(Testersoure testersoure) {
        return testersoureMapper.updateReasonByNo(testersoure);
    }


    @Override
    public List<Testersoure> getAwardListByNo(Testersoure testersoure) {
        return testersoureMapper.queryTitlesListByNo(testersoure);
    }
}
