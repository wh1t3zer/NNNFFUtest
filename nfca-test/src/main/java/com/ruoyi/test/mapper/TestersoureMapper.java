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

    int updateReasonByNo2(Testersoure testersoure);

    int updateScoreById(Testersoure testersoure);

    int updateAdopterById(Testersoure testersoure);


    List<Testersoure> queryTitlesListByNo(Testersoure testersoure);


    String selectOpenIdByNo(int no);

    int updateAdopter2ById(Testersoure testersoure);

    int updateAdopt2ByNo(Testersoure testersoure);
    int updateAdoptByNo(Testersoure testersoure);

//    List<Testersoure> selectTestUserByNo(Testersoure testersoure);

    int updateAdopt2ByNos(String string);
    int updateAdoptByNos(String string);

//    List<Testersoure> selectTestUserByNo(Testersoure testersoure);
}
