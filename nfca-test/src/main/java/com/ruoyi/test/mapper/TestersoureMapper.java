package com.ruoyi.test.mapper;

import com.ruoyi.test.domain.TestUser;
import com.ruoyi.test.domain.Testersoure;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TestersoureMapper {
//    @Select("select * from testerSoure")


    int updateReasonByNo(Testersoure testersoure);

    int updateScoreById(Testersoure testersoure);


    List<Testersoure> queryTitlesListByNo(Testersoure testersoure);


    String selectOpenIdByNo(int no);


//    List<Testersoure> selectTestUserByNo(Testersoure testersoure);
}
