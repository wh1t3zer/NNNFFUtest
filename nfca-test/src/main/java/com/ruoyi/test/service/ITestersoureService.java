package com.ruoyi.test.service;

import com.ruoyi.test.domain.TestUser;
import com.ruoyi.test.domain.Testersoure;

import java.util.List;

public interface ITestersoureService {

    int updateReasonByNo(Testersoure testersoure);

    int updateReasonByNo2(Testersoure testersoure);

    int updateScoreById(Testersoure testersoure);

    int updateAdopterById(Testersoure testersoure);

    List<Testersoure> getAwardListByNo(Testersoure testersoure);

//    List<Testersoure> selectTestUserByNo(Testersoure testersoure);
    //通过学号修改adopt2 adopt
    int updateAdopt2ByNo(Testersoure testersoure);
    int updateAdoptByNo(Testersoure testersoure);
    //批量修改adopt2的值为1    (string 为学号数组)
    int updateAdopt2ByNos(Testersoure testersoure);
    int updateAdoptByNos(Testersoure testersoure);


}
