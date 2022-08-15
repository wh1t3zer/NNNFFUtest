package com.ruoyi.test.service;

import com.ruoyi.test.domain.Testersoure;

import java.util.List;

public interface ITestersoureService {

    int updateReasonByNo(Testersoure testersoure);

    List<Testersoure> getAwardsListByModuleAndNo(Testersoure testersoure);

}
