package com.ruoyi.web.controller.test;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.test.domain.TestUser;
import com.ruoyi.test.service.ITestUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * testController
 * 
 * @author nfca
 * @date 2022-07-12
 */
@RestController
@RequestMapping("/test/major")
public class TestUserMajorController extends BaseController
{
    @Autowired
    private ITestUserService testUserService;

    /**
     * 查询test列表
     */
    @PreAuthorize("@ss.hasPermi('test:major:list')")
//    @PreAuthorize("@ss.hasRole('instructor')")
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
    @PreAuthorize("@ss.hasPermi('test:major:query')")
//    @PreAuthorize("@ss.hasRole('instructor','boss')")
    @GetMapping(value = "/{userId}")
    public AjaxResult getInfo(@PathVariable("userId") Long userId)
    {
        return AjaxResult.success(testUserService.selectTestUserByUserId(userId));
    }

    /**
     * 修改test
     */
    @PreAuthorize("@ss.hasPermi('test:major:edit')")
//    @PreAuthorize("@ss.hasRole('instructor','boss')")
    @Log(title = "test", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TestUser testUser)
    {
        return toAjax(testUserService.updateTestUser(testUser));
    }

    /**
     * 删除test
     */
    @PreAuthorize("@ss.hasPermi('test:major:remove')")
//    @PreAuthorize("@ss.hasRole('instructor','boss')")
    @Log(title = "test", businessType = BusinessType.DELETE)
	@DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds)
    {
        return toAjax(testUserService.deleteTestUserByUserIds(userIds));
    }



    @RequestMapping(value = "/detail/{userId}")
    public String detailTest(@PathVariable("userId") Long userId, Model model)
    {
        System.out.println("hhhhhhhhhhhhhhhhhh  " + userId + "  nnnnnn");
        System.out.println("nimade");
        return "redirect:test/class";

    }

}





















