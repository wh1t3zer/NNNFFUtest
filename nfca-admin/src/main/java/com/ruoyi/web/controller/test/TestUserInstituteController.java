package com.ruoyi.web.controller.test;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.domain.server.Sys;
import com.ruoyi.test.domain.ExportInfo;
import com.ruoyi.test.domain.Student;
import com.ruoyi.test.domain.TestUser;
import com.ruoyi.test.domain.Testersoure;
import com.ruoyi.test.service.IExportInfoService;
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
@RequestMapping("/test/institute")
public class TestUserInstituteController extends BaseController
{
    @Autowired
    private ITestUserService testUserService;


    @Autowired
    private IStudentService studentService;

    @Autowired
    private IExportInfoService iexportInfoService;

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
    @PreAuthorize("@ss.hasPermi('test:institute:list')")
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

    @PreAuthorize("@ss.hasPermi('test:institute:query')")
//    @PreAuthorize("@ss.hasRole('instructor','boss','monitor')")
    @GetMapping(value = "/{no}")
    public AjaxResult getInfo(@PathVariable("no") Integer no)
    {
        return AjaxResult.success(studentService.selectTestUserByUserId(no));
    }





    /**
     * 驳回学生申请
     */
    @PreAuthorize("@ss.hasPermi('test:institute:back')")
    @Log(title = "back", businessType = BusinessType.UPDATE)
    @PutMapping("/backUser")
    /*public AjaxResult backTestUser(@RequestBody TestUser testUser)
    {
        return toAjax(testUserService.backTestUser(testUser));
    }*/
    public AjaxResult backUser(@RequestBody Testersoure testersoure){

        Student student = new Student();
        student.setStatus("2");
        testersoure.setAdopter2(2);
        student.setNo(testersoure.getNo());
        studentService.updateStatusByNo(student);
        testersoureService.updateReasonByNo2(testersoure);
        System.out.println("--------------------");
        System.out.println(testersoure);
        System.out.println("--------------------");
        return toAjax(testersoureService.updateReasonByNo2(testersoure));
    }


      /*批量通过学生申请*/
    //@PreAuthorize("@ss.hasPermi('test:institute:access')")
    @Log(title = "access", businessType = BusinessType.UPDATE)
    @PutMapping("/access")
    public AjaxResult accessUser(@RequestBody Testersoure testersoure){
        testersoureService.updateAdopt2ByNos(testersoure);
        return toAjax(studentService.updateStatusByNos(testersoure.getNos()));
    }

    /**
     * 通过单个学生
     */
    @PreAuthorize("@ss.hasPermi('test:institute:access2')")
    @Log(title = "access2", businessType = BusinessType.UPDATE)
    @PutMapping("/accessUser2")
    public AjaxResult accessUser2(@RequestBody Student student){
        student.setStatus("1");
        Testersoure testersoure = new Testersoure();
        testersoure.setNo(student.getNo());
        testersoure.setAdopter2(1);
        testersoure.setOperator(student.getOperator());
        testersoureService.updateAdopt2ByNo(testersoure);
        return toAjax(studentService.updateStatusByNo(student));
    }

    /**
     * 查询奖项
     * @param
     * @return
     */

    @PostMapping("/getAwards")
    public List<Testersoure> handleGetAwards(@RequestBody Testersoure testersoure){

        return testersoureService.getAwardListByNo(testersoure);

    }

    /**
     * 修改各个小点的成绩
     */
//    @PreAuthorize("@ss.hasPermi('test:institute:updateScore')")
//    @Log(title = "updateScore", businessType = BusinessType.UPDATE)
    @PutMapping("/updateScore")

    public AjaxResult updateScore(@RequestBody Testersoure testersoure){
        return toAjax(testersoureService.updateScoreById(testersoure));
    }


    /*
     * 导出
     * */
    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('test:institute:export')")
    @GetMapping("/export")
    public AjaxResult export(ExportInfo exportInfo)
    {
        List<ExportInfo> ExportInfolist = iexportInfoService.selectExportInfo();
        ExcelUtil<ExportInfo> util = new ExcelUtil<ExportInfo>(ExportInfo.class);
        return util.exportExcel(ExportInfolist, "用户数据");
    }


}

