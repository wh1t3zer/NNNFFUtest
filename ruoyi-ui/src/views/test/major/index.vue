<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--班级学生数据-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
          <el-form-item label="学号" prop="userName">
            <el-input
              v-model="queryParams.userName"
              placeholder="请输入学号"
              clearable
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="手机号码" prop="phonenumber">
            <el-input
              v-model="queryParams.phonenumber"
              placeholder="请输入手机号码"
              clearable
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          
        <el-form-item label="班级" prop="class">
            <el-select
              v-model="queryParams.class"
              placeholder="请选择班级"
              clearable
              style="width: 240px"
            >
              <el-option
                v-for="dict in dict.type.sys_class_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>


          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
            <el-button
              type="success"
              plain
              icon="el-icon-check"
              size="mini"
              :disabled="multiple"
              @click=""
              v-hasPermi="['']"
            >通过</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="danger"
              plain
              icon="el-icon-back"
              size="mini"
              :disabled="multiple"
              @click=""
              v-hasPermi="['']"
            >驳回</el-button>
          </el-col>
        </el-row>
        <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="用户id" align="center" key="userId" prop="userId" v-if="columns[0].visible" />
          <el-table-column label="学号" align="center" key="userName" prop="userName" v-if="columns[1].visible" :show-overflow-tooltip="true" />
          <el-table-column label="姓名" align="center" key="nickName" prop="nickName" v-if="columns[2].visible" :show-overflow-tooltip="true" />
          <el-table-column label="性别" align="center" key="sex" prop="sex" v-if="columns[3].visible" :show-overflow-tooltip="true" />
          <el-table-column label="班级" align="center" key="class" prop="class" v-if="columns[4].visible" :show-overflow-tooltip="true" />
          <el-table-column label="院系" align="center" key="deptName" prop="dept.deptName" v-if="columns[5].visible" :show-overflow-tooltip="true" />
          <el-table-column label="手机号码" align="center" key="phonenumber" prop="phonenumber" v-if="columns[6].visible" width="120" />
          <el-table-column label="状态" align="center" key="status" prop="status" v-if="columns[7].visible" :show-overflow-tooltip="true" />
          
          
          <el-table-column
            label="操作"
            align="center"
            width="160"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope" v-if="scope.row.userId !== 1">
              
              <el-button
                size="mini"
                type="text"
                icon="el-icon-d-arrow-right"
                @click="handleDelete(scope.row)"
                v-hasPermi="['system:user:edit']"
              >编辑</el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="total>0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </el-col>
    </el-row>

  </div>
</template>

<script>
import { listUser, getUser, delUser, addUser, updateUser, resetUserPwd, changeUserStatus } from "@/api/system/user";
import { getToken } from "@/utils/auth";



export default {
  name: "User",
  dicts: ['sys_normal_disable', 'sys_user_sex','sys_class_type'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: false,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 用户表格数据
      userList: null,
      // 弹出层标题
      title: "",
      // 部门树选项
      deptOptions: undefined,
      // 是否显示弹出层
      open: false,
      // 院系名称
      deptName: undefined,
      // 日期范围
      dateRange: [],
      // 角色选项
      roleOptions: [],
      // 表单参数
      form: {},
      defaultProps: {
        children: "children",
        label: "label"
      },
      
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: undefined,
        phonenumber: undefined,
        status: undefined,
        deptId: undefined
      },
      // 列信息
      columns: [
        { key: 0, label: `用户编号`, visible: true },
        { key: 1, label: `用户名称`, visible: true },
        { key: 2, label: `用户昵称`, visible: true },
        { key: 3, label: `性别`, visible: true },
        { key: 4, label: `班级`, visible: true },
        { key: 5, label: `院系`, visible: true },
        { key: 6, label: `手机电话`, visible: true },
        { key: 7, label: `状态`, visible: true }
      ],
      // 表单校验
      rules: {
        
      }
    };
  },
  created() {
    this.getList();
    
  },
  methods: {
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      listUser(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.userList = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    // 筛选节点
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    // 节点单击事件
    handleNodeClick(data) {
      this.queryParams.deptId = data.id;
      this.handleQuery();
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        userId: undefined,
        deptId: undefined,
        userName: undefined,
        nickName: undefined,
        password: undefined,
        phonenumber: undefined,
        email: undefined,
        sex: undefined,
        status: "0",
        remark: undefined,
        postIds: [],
        roleIds: []
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.userId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    
  }
};
</script>