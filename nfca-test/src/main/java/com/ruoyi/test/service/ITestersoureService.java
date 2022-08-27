package com.ruoyi.test.service;

import com.ruoyi.test.domain.TestUser;
import com.ruoyi.test.domain.Testersoure;

import java.util.List;

public interface ITestersoureService {

    int updateReasonByNo(Testersoure testersoure);

    int updateScoreById(Testersoure testersoure);

    List<Testersoure> getAwardListByNo(Testersoure testersoure);

//    List<Testersoure> selectTestUserByNo(Testersoure testersoure);

}
