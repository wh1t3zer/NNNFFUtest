<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--院系学生数据-->
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

          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
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
              v-hasPermi="['test:test:list']"
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
              v-hasPermi="['test:test:list']"
            >驳回</el-button>
          </el-col>
        </el-row>
        <el-table v-loading="loading" :data="testList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="用户id" align="center" key="userId" prop="userId" v-if="columns[0].visible" />
          <el-table-column label="学号" align="center" key="userName" prop="userName" v-if="columns[1].visible" :show-overflow-tooltip="true" />
          <el-table-column label="姓名" align="center" key="nickName" prop="nickName" v-if="columns[2].visible" :show-overflow-tooltip="true" />
          <el-table-column label="性别" align="center" key="sex" prop="sex" :formatter="changesex" />
          <el-table-column label="班级" align="center" key="classname" prop="classname" v-if="columns[4].visible" :show-overflow-tooltip="true" />
          <!-- <el-table-column label="院系" align="center" key="deptName" prop="dept.deptName" v-if="columns[5].visible" :show-overflow-tooltip="true" /> -->
          <el-table-column label="手机号码" align="center" key="phonenumber" prop="phonenumber" v-if="columns[6].visible" width="120" />
          <el-table-column label="状态" align="center" key="status" prop="status"  >
            <template slot-scope="scope">
              <el-tag type="info" effect="dark" v-if="scope.row.status==0">审核中</el-tag>
              <el-tag type="success" effect="dark" v-if="scope.row.status==1">通过</el-tag>
              <el-tag type="warning" effect="dark" v-if="scope.row.status==2">驳回</el-tag>
            </template>
          </el-table-column>

          <el-table-column
            label="操作"
            align="center"
            width="160"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope" >

              <el-button
                size="mini"
                type="text"
                icon="el-icon-d-arrow-right"
                @click="handleDetail(scope.row)"
                v-hasPermi="['test:test:edit']"
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
import { TestUser, getTestUser } from "@/api/test/institute";
import { getToken } from "@/utils/auth";



export default {
  name: "user",
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
      testList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 院系名称
      deptName: undefined,
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
        nickName: undefined,
        status: undefined,
        deptId: undefined
      },
      // 列信息
      columns: [
        { key: 0, label: `用户编号`, visible: true },
        { key: 1, label: `学号`, visible: true },
        { key: 2, label: `姓名`, visible: true },
        { key: 3, label: `性别`, visible: true },
        { key: 4, label: `班级`, visible: true },
        { key: 5, label: `院系`, visible: true },
        { key: 6, label: `手机号码`, visible: true },
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
      TestUser(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.testList = response.rows;
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
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.userId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    //格式化性别
    changesex(row,index){
      if(row.sex=="0"){
        return "男";
      }
      if(row.sex=="1"){
        return "女";
      }
    },
  }
};
</script>
