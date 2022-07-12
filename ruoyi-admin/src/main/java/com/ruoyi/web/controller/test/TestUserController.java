package com.ruoyi.web.controller.test;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.test.domain.TestUser;
import com.ruoyi.test.service.ITestUserService;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * testController
 * 
 * @author nfca
 * @date 2022-07-12
 */
@RestController
@RequestMapping("/test/class")
public class TestUserController extends BaseController
{
    @Autowired
    private ITestUserService testUserService;

    /**
     * 查询test列表
     */
    @PreAuthorize("@ss.hasPermi('test:test:list')")
    @GetMapping("/list")
    public TableDataInfo list(TestUser testUser)
    {
        startPage();
        List<TestUser> list = testUserService.selectTestUserList(testUser);
        return getDataTable(list);
    }



    /**
     * 获取test详细信息
     */
    @PreAuthorize("@ss.hasPermi('test:test:query')")
    @GetMapping(value = "/{userId}")
    public AjaxResult getInfo(@PathVariable("userId") Long userId)
    {
        return AjaxResult.success(testUserService.selectTestUserByUserId(userId));
    }

    /**
     * 修改test
     */
    @PreAuthorize("@ss.hasPermi('test:test:edit')")
    @Log(title = "test", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TestUser testUser)
    {
        return toAjax(testUserService.updateTestUser(testUser));
    }

    /**
     * 删除test
     */
    @PreAuthorize("@ss.hasPermi('test:test:remove')")
    @Log(title = "test", businessType = BusinessType.DELETE)
	@DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds)
    {
        return toAjax(testUserService.deleteTestUserByUserIds(userIds));
    }
}
