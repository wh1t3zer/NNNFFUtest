package com.ruoyi.test.service;

import java.util.List;
import com.ruoyi.test.domain.TestUser;
/**
 * testService接口
 * 
 * @author nfca
 * @date 2022-07-12
 */
public interface ITestUserService 
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
     * 修改test
     * 
     * @param testUser test
     * @return 结果
     */
    public int updateTestUser(TestUser testUser);

    /**
     * 批量删除test
     * 
     * @param userIds 需要删除的test主键集合
     * @return 结果
     */
    public int deleteTestUserByUserIds(Long[] userIds);


    public int backTestUser(TestUser testUser);
}
