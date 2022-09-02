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
    public int updateReasonByNo2(Testersoure testersoure) {
        return testersoureMapper.updateReasonByNo2(testersoure);
    }

    @Override
    public int updateScoreById(Testersoure testersoure) {
        return testersoureMapper.updateScoreById(testersoure);
    }

    @Override
    public int updateAdopterById(Testersoure testersoure) {
       return  testersoureMapper.updateAdopterById(testersoure);
    }


    @Override
    public List<Testersoure> getAwardListByNo(Testersoure testersoure) {
        return testersoureMapper.queryTitlesListByNo(testersoure);
    }
    @Override
    public int updateAdopt2ByNo(Testersoure testersoure) {
        return testersoureMapper.updateAdopt2ByNo(testersoure);
    }

    @Override
    public int updateAdoptByNo(Testersoure testersoure) {
        return testersoureMapper.updateAdoptByNo(testersoure);
    }

    @Override
    public int updateAdopt2ByNos(Testersoure testersoure) {
        return testersoureMapper.updateAdopt2ByNos(testersoure);
    }

    @Override
    public int updateAdoptByNos(String string) {
        return testersoureMapper.updateAdoptByNos(string);
    }

}
