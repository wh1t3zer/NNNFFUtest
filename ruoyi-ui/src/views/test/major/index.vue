<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--班级学生数据-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryParams" size="small" :rules="rules" :inline="true" v-show="showSearch" label-width="68px">



          <el-form-item label="学号" prop="no">
            <el-input
              v-model="queryParams.no"
              placeholder="请输入学号"
              clearable
              style="width: 200px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>

          <el-form-item label="学生姓名" prop="name">
            <el-input
              v-model="queryParams.name"
              placeholder="请输入学生姓名"
              clearable
              style="width: 200px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <br>
          <el-form-item  prop="major" label="专业">
            <el-select v-model="majorValue" clearable placeholder="请选择" @change="selectMajor(majorValue)" style="width: 240px" >
              <el-option
                v-for="item in majorOptions"
                :key="item.majorValue"
                :label="item.majorValue"
                :value="item.majorValue"
              >
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item  prop="status" label="状态">
            <el-select v-model="value" clearable placeholder="请选择" @change="selectStatus(value)" style="width: 240px">
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>


          <el-form-item style="margin-left:20px">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery('queryParams')">搜索</el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="warning" icon="el-icon-refresh" size="mini" @click="reset">重置</el-button>
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
              @click="handleAccess"
              v-hasPermi="['test:major:access']"
            >通过</el-button>
          </el-col>
          <!-- <el-col :span="1.5" >
            <el-button
              type="danger"
              plain
              icon="el-icon-back"
              size="mini"
              :disabled="multiple"
              @click="handleBack"
              v-hasPermi="['test:major:back']"
            >驳回</el-button>
          </el-col> -->
        </el-row>
        <el-table v-loading="loading" :data="testList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="50" align="center" />
          <!--          <el-table-column label="用户id" align="center" key="userId" prop="userId" v-if="columns[0].visible" />-->
          <el-table-column label="学号" align="center" key="no" prop="no" v-if="columns[1].visible" :show-overflow-tooltip="true" />
          <el-table-column label="姓名" align="center" key="name" prop="name" v-if="columns[2].visible" :show-overflow-tooltip="true" />
          <!-- <el-table-column label="性别" align="center" key="sex" prop="sex" v-if="columns[3].visible" :show-overflow-tooltip="true" /> -->
          <el-table-column label="年级" align="center" key="grade" prop="grade"/>
          <el-table-column label="班级" align="center" key="classId" prop="classId" v-if="columns[4].visible" :show-overflow-tooltip="true" />
          <!-- <el-table-column label="院系" align="center" key="deptName" prop="dept.deptName" v-if="columns[5].visible" :show-overflow-tooltip="true" /> -->
          <el-table-column label="专业" align="center" key="major" prop="major" v-if="columns[6].visible" width="120" />
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
              <el-button type="success" @click="handleEdit(scope.row)">编辑<i class="el-icon-edit"></i></el-button>
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

    <el-dialog title="详情页" :visible.sync="outerVisible">
      <el-form label-width="100px" :disabled="true">
        <el-form-item label="用户名">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="学号">
          <el-input v-model="form.no" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="班级">
          <el-input v-model="form.classId" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item
          label="加分图"
          prop="images"
          width="180">
          <template slot-scope="scope">
            <!--   :src 里面写的是nfca那边传来的图片   -->
            <img :src="form" style="width:100px;height:50px;"/>

          </template>
        </el-form-item>

      </el-form>
      <el-dialog
        width="50%"
        title="驳回理由"
        :visible.sync="innerVisible"
        append-to-body>

        <el-input
          type="textarea"
          placeholder="请输入内容"
          v-model="reason"
          maxlength="100"
          show-word-limit
        >
        </el-input>
        <div slot="footer" class="dialog-footer">
          <el-button @click="innerVisible = false" type="info">取消</el-button>
          <el-popconfirm
            style="margin-left: 5px"
            confirm-button-text='确定咯'
            cancel-button-text='取消'
            icon="el-icon-info"
            icon-color="red"
            title="您确定要驳回吗？"
            @confirm="double(form.no,reason)"
          >
            <el-button  type="danger" slot="reference" >确定</el-button>
          </el-popconfirm>

        </div>
      </el-dialog>
      <div slot="footer" class="dialog-footer">
        <el-button @click="outerVisible = false" type="info">取消</el-button>
        <el-button type="warning" @click="innerVisible = true">驳回</el-button>
        <!--<el-button @click="dialogFormVisible = false" type="warning">驳回</el-button>-->
        <el-button @click="handleAccess2(form.no)" type="success">通过</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { TestUser, getTestUser,  backUser, accessUser,accessUser2 } from "@/api/test/major";
  import { getToken } from "@/utils/auth";
  import EditTable from "../../tool/gen/editTable.vue";
  import * as ElementUI from "element-ui";


  ElementUI.Dialog.props.closeOnClickModal.default = false; //弹框点及其他区域不关闭


  export default {
    name: "user",
    data() {
      return {
        options: [
          {
            value: '通过',
            label: '通过'
          }, {
            value: '驳回',
            label: '驳回'
          }, {
            value: '审核中',
            label: '审核中'
          }],
        value: '',
        majorOptions: [
          {
            majorValue: '计算机科学与技术',
            label: '计算机科学与技术'
          }, {
            majorValue: '数据科学与大数据技术',
            label: '数据科学与大数据技术'
          },{
            majorValue: '电子信息科学与技术',
            label: '电子信息科学与技术'
          },{
            majorValue: '电气工程及其自动化',
            label: '电气工程及其自动化'
          },{
            majorValue: '软件工程',
            label: '软件工程'
          },{
            majorValue: '智能科学与技术',
            label: '智能科学与技术'
          }, {
            majorValue: '通信工程',
            label: '通信工程'
          }],
        majorValue: '',
        //驳回原因
        reason:'',
        // 遮罩层
        loading: true,
        // 选中数组
        ids: [],
        nos:[],
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
          no:'',
          name: undefined,
          status: undefined,
        },
        // 表单校验
        rules: {
          no:[
            { pattern: /^[0-9]$/, message:"请输入正确的学号", trigger:"blur" },
            { min: 1, max: 10, message: '请输入正确的学号长度', trigger: 'blur' }
          ]
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
        outerVisible: false,
        innerVisible: false
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
          console.log(this.testList)  //一页的数据
          console.log(this.total)     //总数据数量
        });
      },

      // 筛选节点
      filterNode(value, data) {
        if (!value)
          return true;
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
      handleQuery(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.queryParams.pageNum = 1;
            this.getList();
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.userId);
        this.nos = selection.map(item => item.no);
        this.single = selection.length != 1;
        this.multiple = !selection.length;
      },
      //格式化性别
      changesex(row) {
        if (row.sex == "0") {
          return "男";
        }
        if (row.sex == "1") {
          return "女";
        }
      },
      handleDetail(row){
        const userId = row.userId || this.ids;
        getUser(userId).then(response => {
          this.form = response.data;

          this.open = true;
          this.title = "详细页";
        });
      },
      /*
      * ID查询学生
      * */



      /**
       * 驳回按钮操作
       * @param no 学生学号
       * @param reason 驳回原因
       */
      handleBack(no,reason) {
        backUser(no,reason);
        this.handleQuery();
        /*getTestUser(userIds).then(response =>{
          console.log(response.data)
        })
         console.log(userIds)*/


        //   this.$modal.confirm('确认要驳回所选项的申请吗？').then(
        //   getTestUser(userIds)
        //   ).then(response => {
        // // this.getList()
        // // this.$modal.msgSuccess(text + "成功");
        //   console.log(response.data)
        //   }).then(function(){
        //     return backUser(userId,status)
        //   }).catch(function() {
        //   row.status = row.status === "2";
        //   });
      },

      /**
       * 批量通过按钮
       * @param row
       */
      handleAccess(row) {
        const nos = row.no || this.nos;

        accessUser(nos)
        this.handleQuery();

        /*let text = row.status === "1";
        const userIds = row.userId || this.ids;
        const status = row.status || this.ids.status
        this.$modal.confirm('确认要通过所选项的申请吗？').then(function() {
        return accessUser(userIds, status);
        }).then(() => {

    this.$modal.msgSuccess(text + "成功");
      }).catch(function() {
    row.status = row.status === "1";
      });
    this.getList()*/
      },

      /**
       * 通过按钮
       * @param no 学生学号
       */
      handleAccess2(no){
        accessUser2(no)
        this.handleQuery();
        this.outerVisible = false

      },


      //编辑
      handleEdit(row){
        //const nos = row.no;

        this.form = Object.assign({},row)
        //this.dialogFormVisible = true
        this.outerVisible = true
      },
      double(no,reason){
        this.handleBack(no,reason)
        this.innerVisible = false;
        this.outerVisible = false
      },
      //重置按钮
      reset(){
        this.queryParams.name =''
        this.queryParams.no =''
        this.value = ''
        this.getList()
      },

      //选择状态按钮
      selectStatus(value){
        console.log(value)
      },
      //选择状态按钮
      selectMajor(majorValue){
        console.log(majorValue)
      }


    },
    components: { EditTable }
  };
</script>
