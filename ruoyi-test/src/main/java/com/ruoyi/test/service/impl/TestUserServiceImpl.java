package com.ruoyi.test.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.test.mapper.TestUserMapper;
import com.ruoyi.test.domain.TestUser;
import com.ruoyi.test.service.ITestUserService;

/**
 * testService业务层处理
 * 
 * @author nfca
 * @date 2022-07-12
 */
@Service
public class TestUserServiceImpl implements ITestUserService 
{
    @Autowired
    private TestUserMapper testUserMapper;

    /**
     * 查询test
     * 
     * @param userId test主键
     * @return test
     */
    @Override
    public TestUser selectTestUserByUserId(Long userId)
    {
        return testUserMapper.selectTestUserByUserId(userId);
    }

    /**
     * 查询test列表
     * 
     * @param testUser test
     * @return test
     */
    @Override
    public List<TestUser> selectTestUserList(TestUser testUser)
    {
        return testUserMapper.selectTestUserList(testUser);
    }

    /**
     * 修改test
     * 
     * @param testUser test
     * @return 结果
     */
    @Override
    public int updateTestUser(TestUser testUser)
    {
        testUser.setUpdateTime(DateUtils.getNowDate());
        return testUserMapper.updateTestUser(testUser);
    }

    /**
     * 批量删除test
     * 
     * @param userIds 需要删除的test主键
     * @return 结果
     */
    @Override
    public int deleteTestUserByUserIds(Long[] userIds)
    {
        return testUserMapper.deleteTestUserByUserIds(userIds);
    }

    /**
     * 删除test信息
     * 
     * @param userId test主键
     * @return 结果
     */
    @Override
    public int deleteTestUserByUserId(Long userId)
    {
        return testUserMapper.deleteTestUserByUserId(userId);
    }
}
