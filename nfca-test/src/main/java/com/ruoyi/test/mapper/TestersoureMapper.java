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


    List<Testersoure> queryTitlesListByNo(Testersoure testersoure);



//    List<Testersoure> selectTestUserByNo(Testersoure testersoure);
}
