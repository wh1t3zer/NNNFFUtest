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

          <el-form-item  prop="class" label="班级">
            <el-select v-model="queryParams.classValue" clearable placeholder="请选择" @change="selectClass(classValue)" style="width: 200px" >
              <el-option
                v-for="item in classOptions"
                :key="item.classValue"
                :label="item.classValue"
                :value="item.classValue"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <br>
          <el-form-item  prop="major" label="专业">
            <el-select v-model="queryParams.majorValue" clearable placeholder="请选择" @change="selectMajor(majorValue)" style="width: 200px" >
              <el-option
                v-for="item in majorOptions"
                :key="item.majorValue"
                :label="item.majorValue"
                :value="item.majorValue"
                @keyup.enter.native="handleQuery"
              >
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item  prop="status" label="状态">
            <el-select v-model="queryParams.status" clearable placeholder="请选择" @change="selectStatus(value)" style="width: 200px">
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
                @keyup.enter.native="handleQuery"
              >
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item  prop="class" label="年级">
            <el-select v-model="queryParams.gradeValue" clearable placeholder="请选择" @change="selectClass(gradeValue)" style="width: 200px" >
              <el-option
                v-for="item in gradeOptions"
                :key="item.gradeValue"
                :label="item.gradeValue"
                :value="item.gradeValue"
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
              v-hasPermi="['test:institute:access']"
            >通过</el-button>
          </el-col>
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

    <el-dialog title="详情页" :visible.sync="outerVisible" custom-class="getdialogstyle">
      <el-form label-width="160px" :disabled="true" v-loading="loading" :data="awardsList" >
      <el-row>
        <el-col :span="12">
        <el-form-item label="用户名" >

          <el-input v-model="form.name" autocomplete="off" ></el-input>
        </el-form-item>
        </el-col>

        <el-col :span="12">
        <el-form-item label="学号">
          <el-input v-model="form.no" autocomplete="off"></el-input>
        </el-form-item>
         </el-col>

         <el-col :span="12">
        <el-form-item label="班级">
          <el-input v-model="form.classId" autocomplete="off"></el-input>
        </el-form-item>
         </el-col>
        <el-col :span="12">
          <el-form-item label="1、政治素质">
            <el-input v-model="this.politics" autocomplete="off" type="textarea" :autosize="{ minRows: 1}" ></el-input>
          </el-form-item>
        </el-col>


        <el-col :span="12">
          <el-form-item label="2、思想素质">
            <el-input v-model="this.ideology" autocomplete="off" type="textarea" :autosize="{ minRows: 1}"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="3、道德素质">
            <el-input v-model="this.morality" autocomplete="off" type="textarea" :autosize="{ minRows: 1}"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="4、组织素质">
            <el-input v-model="this.organ" autocomplete="off" type="textarea" :autosize="{ minRows: 1}"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="5、法纪素质">
            <el-input v-model="this.law" autocomplete="off" type="textarea" :autosize="{ minRows: 1}"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="6、学习与生活素质">
            <el-input v-model="this.learning" autocomplete="off" type="textarea" :autosize="{ minRows: 1}"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="7、实践与创新素质">
            <el-input v-model="this.development" autocomplete="off" type="textarea" :autosize="{ minRows: 1}"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="8、科学文化素质">
            <el-input v-model="this.scientific" autocomplete="off" type="textarea" :autosize="{ minRows: 1}"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="9、身体素质">
            <el-input v-model="this.physical" autocomplete="off" type="textarea" :autosize="{ minRows: 1}"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="10、心理素质">
            <el-input v-model="this.mental" autocomplete="off" type="textarea" :autosize="{ minRows: 1}"></el-input>
          </el-form-item>
        </el-col>


        <el-col :span="12">
          <el-form-item label="11、荣誉称号加分">
            <el-input v-model="this.honorary" autocomplete="off" type="textarea" :autosize="{ minRows: 1}"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="12、文体艺术等竞赛或活动加分">
            <el-input v-model="this.competition" autocomplete="off" type="textarea" :autosize="{ minRows: 1}"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="13、社会工作加分">
            <el-input v-model="this.socialWork" autocomplete="off" type="textarea" :autosize="{ minRows: 1}"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="14、知识与技能加分">
            <el-input v-model="this.knowl" autocomplete="off" type="textarea" :autosize="{ minRows: 1}"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="15、日常操行减分">
            <el-input v-model="this.daily" autocomplete="off" type="textarea" :autosize="{ minRows: 1}"></el-input>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="16、期末成绩">
            <el-input v-model="this.achievement" autocomplete="off" type="textarea" :autosize="{ minRows: 1}" ></el-input>
            <el-label >加权平均分为：</el-label>
            <el-label style="color:red">{{this.score}}/100</el-label>
            <el-label>{{this.totalscore}}</el-label>
          </el-form-item>
        </el-col>

        </el-row>

       <el-form-item label="加分图">
          <el-form-item label="(单击图片可预览)">
          </el-form-item>
        </el-form-item>
        <el-row >
        <!-- <img  v-for="(images,index) in img" :key="index" :src="'https://static-nfuca-1302505692.cos-website.ap-guangzhou.myqcloud.com/img/upload/raw/' + img[index]" style="width:270px;height:150px;"/> -->
        <template>
        <div v-for="(imgs, index) in img" v-viewer="{movable:false}">
          <ul id="yihangxianshiduotu">
            <li><img  :src="'https://static-nfuca-1302505692.cos-website.ap-guangzhou.myqcloud.com/img/upload/raw/' + img[index].id+'.jpg'" style="width:270px;height:150px;"/></li>
          </ul>
        </div>
        </template>

      </el-row>


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
<style>
  .getdialogstyle{
    width: 80%;
  }
  #yihangxianshiduotu li
  {
    float: left;
    display: inline;
  }

  img{
    width: 300px;
    height: 200px;
    margin: 10px;
  }
</style>
<script>
  import { TestUser, backUser, accessUser,accessUser2,getAwards } from "@/api/test/institute";
  import { getToken } from "@/utils/auth";
  import EditTable from "../../tool/gen/editTable.vue";
  import * as ElementUI from "element-ui";
  import {majorOptionsini} from "../../../../static/config.js"
  import {classOptionsini} from "../../../../static/config.js"
  import {gradeOptionsini} from "../../../../static/config.js"

  ElementUI.Dialog.props.closeOnClickModal.default = true; //弹框点及其他区域不关闭


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

        classOptions:classOptionsini.classOptions,
        classValue: '',
        majorOptions: majorOptionsini.majorOptions,
        majorValue: '',
        gradeOptions:gradeOptionsini.gradeOptions,
        gradeValue:'',
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
        //用户奖项数据
        awardsList: [],
        politics: "",
        ideology: "",
        morality: "",
        organ: "",
        law: "",
        learning: "",
        development: "",
        scientific: "",
        physical: "",
        mental: "",
        honorary: "",
        competition: "",
        socialWork: "",
        knowl: "",
        daily: "",
        achievement: "",
        img:"",
        images:"",
        score:"",
        socre1:"",
        totalscore:"",
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
          major: undefined,
        },
        // 表单校验
        rules: {
          no:[
            { pattern: /^[0-9]*$/, message:"请输入正确的学号", trigger:"blur" },
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
        this.getList();
      },

      /**
       * 批量通过按钮
       * @param row
       */
      handleAccess(row) {
        const nos = row.no || this.nos;

        accessUser(nos)
        this.getList();
      },

      /**
       * 通过按钮
       * @param no 学生学号
       */
      handleAccess2(no){
        accessUser2(no);
        this.outerVisible = false
        this.getList();
      },

      /**
       * 查询奖项内容
       * @param no 学号
       * @param module 奖项模块
       * @moduleParam
       *
       * 代码笨重 后期修改
       */
       async handleGetAwards(no){
         this.resetAwards();
         await getAwards(no).then(response => {
           this.awardsList = response;
           for(var i = 0; i < this.awardsList.length; i++){

             switch (this.awardsList[i].module){
               case "politics":
                 this.politics = this.awardsList[i].title;
                 break;
               case "morality":
                 this.morality = this.awardsList[i].title;
                 break;
               case "organ":
                 this.organ = this.awardsList[i].title;
                 break;
               case "law":
                 this.law = this.awardsList[i].title;
                 break;
               case "learning":
                 this.learning = this.awardsList[i].title;
                 this.awardsList[i].id='';
                 break;
               case "development":
                 this.development = this.awardsList[i].title;
                 break;
               case "scientific":
                 this.scientific = this.awardsList[i].title;
                 break;
               case "physical":
                 this.physical = this.awardsList[i].title;
                 break;
               case "mental":
                 this.mental = this.awardsList[i].title;
                 break;
               case "honorary":
                 this.honorary = this.awardsList[i].title;
                 break;
               case "competition":
                 this.competition = this.awardsList[i].title;
                 break;
               case "socialWork":
                 this.socialWork = this.awardsList[i].title;
                 break;
               case "knowl":
                 this.knowl = this.awardsList[i].title;
                 break;
               case "daily":
                 this.daily = this.awardsList[i].title;
                 this.awardsList[i].id='';
                 break;
               case "ideology":
                 this.ideology = this.awardsList[i].title;
                 break;
               case "achievement":
                 this.achievement = this.awardsList[i].detail;
                 this.score=this.awardsList[i].score/100;
                 this.awardsList[i].id="";
                 break;
             }
           }
          let result = this.awardsList.map(current => {return {id: current.id}});
            for(let i=result.length-1;i>=0;i--){
              if(result[i].id==""){
             //   console.log(i)
                result.splice(i,1)
              }
            }
          this.img=result
          })
      },

      //编辑
      handleEdit(row){
        this.handleGetAwards(row.no);
        this.form = Object.assign({},row)
        //this.dialogFormVisible = true
        this.outerVisible = true
      },

      /**
       * 重置奖项列表值
       */
      resetAwards(){
        this.achievement = "";
        this.politics = "";
        this.ideology = "";
        this.morality = "";
        this.organ = "";
        this.law = "";
        this.learning = "";
        this.development = "";
        this.scientific = "";
        this.physical = "";
        this.mental = "";
        this.honorary = "";
        this.competition = "";
        this.socialWork = "";
        this.knowl = "";
        this.daily = "";
        this.score = "";
        this.img = "";
        this.score1 = "";
        this.totalscore = "";
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
      //截取多张图片id
        splitJoin(imgId){
      	return imgId.split(',');
      },
      //选择状态按钮
      selectStatus(value){
        console.log(value)
      },
      //选择专业状态按钮
      selectMajor(majorValue){
        console.log(majorValue)
      },
      //选择状态按钮
      selectClass(classValue){
        console.log(classValue)
      }
    },
    components: { EditTable }
  };
</script>
