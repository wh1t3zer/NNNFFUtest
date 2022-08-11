package com.ruoyi.web.controller.test;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.test.domain.Student;
import com.ruoyi.test.domain.TestUser;
import com.ruoyi.test.domain.Testersoure;
import com.ruoyi.test.service.IStudentService;
import com.ruoyi.test.service.ITestUserService;
import com.ruoyi.test.service.ITestersoureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * testController
 * 
 * @author nfca
 * @date 2022-07-12
 */
@RestController
@RequestMapping("/test/class")
public class TestUserClassController extends BaseController
{
    @Autowired
    private ITestUserService testUserService;


    @Autowired
    private IStudentService studentService;

    @Autowired
    private ITestersoureService testersoureService;
    /**
     * 查询test列表
     */
/*    @PreAuthorize("@ss.hasPermi('test:class:list')")
//    @PreAuthorize("@ss.hasRole('instructor','boss','monitor')")
    @GetMapping("/list")
    public TableDataInfo list(TestUser testUser)
    {
        startPage();
        List<TestUser> list = testUserService.selectTestUserList(testUser);
        return getDataTable(list);
    }*/

    /**
     * 查询学生列表
     */
    @PreAuthorize("@ss.hasPermi('test:class:list')")
//    @PreAuthorize("@ss.hasRole('instructor','boss','monitor')")
    @GetMapping("/list")
    public TableDataInfo list(Student student)
    {
        startPage();
        List<Student> list = studentService.selectAllStudents(student);
        return getDataTable(list);
    }

    /*
    *  ID查询学生
    * */
    /*@PreAuthorize("@ss.hasRole('boss')")
    @GetMapping(value = "/{no}")
    public AjaxResult getInfo(@PathVariable("no") Integer no)
    {
        return AjaxResult.success(studentMapper.selectStudentByUserId(no));
    }*/



    /**
     * 获取test详细信息
     */
    /*@PreAuthorize("@ss.hasPermi('test:class:query')")
//    @PreAuthorize("@ss.hasRole('instructor','boss','monitor')")
    @GetMapping(value = "/{userId}")
    public AjaxResult getInfo(@PathVariable("userId") Long userId)
    {
        return AjaxResult.success(testUserService.selectTestUserByUserId(userId));
    }*/

    @PreAuthorize("@ss.hasPermi('test:class:query')")
//    @PreAuthorize("@ss.hasRole('instructor','boss','monitor')")
    @GetMapping(value = "/{no}")
    public AjaxResult getInfo(@PathVariable("no") Integer no)
    {
        return AjaxResult.success(studentService.selectTestUserByUserId(no));
    }





    /**
     * 修改test
     */
    @PreAuthorize("@ss.hasPermi('test:class:edit')")
//    @PreAuthorize("@ss.hasRole('instructor','boss','monitor')")
    @Log(title = "test", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TestUser testUser)
    {
        return toAjax(testUserService.updateTestUser(testUser));
    }

    /**
     * 删除test
     */
    @PreAuthorize("@ss.hasPermi('test:class:remove')")
//    @PreAuthorize("@ss.hasRole('instructor','boss','monitor')")
    @Log(title = "test", businessType = BusinessType.DELETE)
	@DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds)
    {
        return toAjax(testUserService.deleteTestUserByUserIds(userIds));
    }


    /**
     * 驳回学生申请
     */
    @PreAuthorize("@ss.hasPermi('test:class:back')")
    @Log(title = "back", businessType = BusinessType.UPDATE)
    @PutMapping("backUser")
    /*public AjaxResult backTestUser(@RequestBody TestUser testUser)
    {
        return toAjax(testUserService.backTestUser(testUser));
    }*/
    public AjaxResult backTestUser(@RequestBody Testersoure testersoure){
        Student student = new Student();
        student.setStatus("2");
        student.setNo(testersoure.getNo());
        studentService.updateStatusByNo(student);
        return toAjax(testersoureService.updateReasonByNo(testersoure));
    }

    /**
     * 批量通过学生申请
     */
    @PreAuthorize("@ss.hasPermi('test:class:access')")
    @Log(title = "access", businessType = BusinessType.UPDATE)
    @PutMapping("/{nos}")
    public AjaxResult accessUser(@PathVariable String nos){
        return toAjax(studentService.updateStatusByNos(nos));
    }

    /**
     * 通过单个学生
     */
    @PreAuthorize("@ss.hasPermi('test:class:access2')")
    @Log(title = "access2", businessType = BusinessType.UPDATE)
    @PutMapping("accessUser2")
    public AjaxResult accessUser2(@RequestBody Student student){
        student.setStatus("1");
        return toAjax(studentService.updateStatusByNo(student));
    }



}

