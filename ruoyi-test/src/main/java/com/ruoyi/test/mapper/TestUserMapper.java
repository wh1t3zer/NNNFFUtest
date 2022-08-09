package com.ruoyi.test.mapper;

import java.util.List;
import com.ruoyi.test.domain.TestUser;

/**
 * testMapper接口
 * 
 * @author nfca
 * @date 2022-07-12
 */
public interface TestUserMapper 
{
    /**
     * 查询test
     * 
     * @param userId test主键
     * @return test
     */
    public TestUser selectTestUserByUserId(Long userId);

    /**
     * 查询test列表
     * 
     * @param testUser test
     * @return test集合
     */
    public List<TestUser> selectTestUserList(TestUser testUser);

    /**
     * 新增test
     * 
     * @param testUser test
     * @return 结果
     */
    public int insertTestUser(TestUser testUser);

    /**
     * 修改test
     * 
     * @param testUser test
     * @return 结果
     */
    public int updateTestUser(TestUser testUser);


    /**
     * 批量删除test
     * 
     * @param userIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTestUserByUserIds(Long[] userIds);


    public int backTestUser(TestUser testUser);
}
