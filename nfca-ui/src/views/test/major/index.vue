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

          <el-form-item  prop="class" label="年级">
            <el-select v-model="queryParams.gradeValue" clearable placeholder="请选择" @change="selectClass(gradeValue)" style="width: 200px" >
              <el-option @click.native="handleQuery('queryParams')"
                         v-for="item in gradeOptions"
                         :key="item.gradeValue"
                         :label="item.gradeValue"
                         :value="item.gradeValue"
              >
              </el-option>
            </el-select>
          </el-form-item>

          <br>
          <el-form-item  prop="class" label="班级">
            <el-select v-model="queryParams.classValue" clearable placeholder="请选择" @change="selectClass(classValue)" style="width: 200px" >
              <el-option @click.native="handleQuery('queryParams')"
                         v-for="item in classOptions"
                         :key="item.classValue"
                         :label="item.classValue"
                         :value="item.classValue"
              >
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item  prop="status" label="状态">
            <el-select v-model="queryParams.status" clearable placeholder="请选择" @change="selectStatus(value)" style="width: 200px">
              <el-option @click.native="handleQuery('queryParams')"
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
              <el-button
                type="success"
                @click="handleEdit(scope.row)"
                v-hasPermi="['test:major:edit']">编辑<i class="el-icon-edit"></i></el-button>
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

    <el-dialog title="详情页" :visible.sync="outerVisible" custom-class="getdialogstyle" >
      <el-form label-width="120px"  v-loading="loading" :data="awardsList">

        <!-- <el-row :gutter="24"> -->
        <el-col >
          <el-descriptions title="用户信息">


            <el-descriptions-item label="学生姓名">
              <el-tag size="small">{{form.name}}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="学号">
              <el-tag size="small">{{form.no}}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="班级">
              <el-tag size="small">{{form.classId}}</el-tag>
            </el-descriptions-item>
          </el-descriptions>
          <el-descriptions title="基础性素质测评（满分60分）"></el-descriptions>


          <!--1、政治素质-->
          <el-row>
            <el-form-item label="1、政治素质">
              <template v-if="this.politicsScore2.length > 0">
                <div style="display: inline-block;width: 45%">
                  <div v-for="(item,index) in this.politicsScore2" >
                    <el-input v-model="item.title" v-if="item.adopter !='2' " autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                    <el-input-number v-model="item.score" v-if="item.adopter != '2' " :precision="1" :step="0.1" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
                    <el-dialog width="50%" title="驳回理由"
                               :visible.sync="innerVisiblePolitics"
                               append-to-body>
                      <el-input type="textarea" placeholder="请输入内容" v-model="Reason" maxlength="100" show-word-limit></el-input>
                      <div slot="footer" class="dialog-footer" >
                        <el-button @click="innerVisiblePolitics = false" type="info">取消</el-button>
                        <el-popconfirm style="margin-left: 5px" confirm-button-text='确定咯' cancel-button-text='取消' icon="el-icon-info" icon-color="red" title="您确定要驳回吗？"
                                       v-hasPermi="['test:major:back']"
                                       @confirm="doublePolitics(form.no,Reason,item.openId)"
                        >
                          <el-button  type="danger" slot="reference">确定</el-button>
                        </el-popconfirm>
                      </div>
                    </el-dialog>

                    <div slot="footer" class="dialog-footer" style="display: inline;  vertical-align: middle" >
                      <el-button type="warning" size="mini" v-if="item.adopter !='2' " style="margin-left: 5px" @click="showTest1(item.id)">驳回</el-button>
                    </div>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle "      v-if="gly === 19028 && item.adopter !='2'  ">陈金涛</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 13060 && item.adopter !='2'  ">唐玉婷</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 18059 && item.adopter !='2'  ">曲楠楠</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19027 && item.adopter !='2'  ">曹永荣</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19026 && item.adopter !='2'  ">张文婷</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19133 && item.adopter !='2'  ">朱业顺</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 20002 && item.adopter !='2'  ">翟晓涵</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 20283 && item.adopter !='2'  ">王孟阳</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 21155 && item.adopter !='2'  ">穆小青</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 22074 && item.adopter !='2'  ">徐慧玲</el-button>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="item.adopter2 == '0' && item.adopter !='2'">审核状态: <span style="color: lightsalmon ">审核中</span></span>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="item.adopter2 == '1' && item.adopter !='2'">审核状态: <span style="color: green ">通过</span></span>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="item.adopter2 == '2' && item.adopter !='2'">审核状态: <span style="color: red ">驳回</span></span>
                  </div>
                </div>
                <div v-for="(item, index) in this.politicsScore2" v-viewer="{movable:true}" style="display: inline;vertical-align: middle">
                  <ul id="yihangxianshiduotu">
                    <li><el-image z-index="3000" v-if="item.adopter != '2'" :fit="fit" :src="'https://static-nfuca-1302505692.cos-website.ap-guangzhou.myqcloud.com/img/upload/raw/' + item.id+'.jpg'" style="width:200px;height:150px;"/></li>
                  </ul>
                </div>
              </template>
              <template v-else>
                <el-input autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                <el-input-number value="0.0" :precision="1" :step="0.1" :disabled="true" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
              </template>
            </el-form-item>
          </el-row>

          <!--2、思想素质-->
          <el-row>
            <el-form-item label="2、思想素质">
              <template v-if="this.ideologyScore2.length > 0">
                <div style="display: inline-block;width: 45%">
                  <div v-for="(item,index) in this.ideologyScore2" >
                    <el-input v-model="ideologyScore2[index].title" v-if="ideologyScore2[index].adopter !='2' " autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                    <el-input-number v-model="ideologyScore2[index].score" v-if="ideologyScore2[index].adopter != '2' " :precision="1" :step="0.1" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
                    <el-dialog width="50%" title="驳回理由"
                               :visible.sync="innerVisibleIdeology"
                               append-to-body>
                      <el-input type="textarea" placeholder="请输入内容" v-model="Reason" maxlength="100" show-word-limit></el-input>
                      <div slot="footer" class="dialog-footer" >
                        <el-button @click="innerVisibleIdeology = false" type="info">取消</el-button>
                        <el-popconfirm style="margin-left: 5px" confirm-button-text='确定咯' cancel-button-text='取消' icon="el-icon-info" icon-color="red" title="您确定要驳回吗？"
                                       v-hasPermi="['test:major:back']"
                                       @confirm="doubleIdeology(form.no,Reason,ideologyScore2[index].openId)"
                        >
                          <el-button  type="danger" slot="reference">确定</el-button>
                        </el-popconfirm>
                      </div>
                    </el-dialog>

                    <div slot="footer" class="dialog-footer" style="display: inline;  vertical-align: middle" >
                      <el-button type="warning" size="mini" v-if="ideologyScore2[index].adopter !='2' " style="margin-left: 5px" @click="showTest2(item.id)">驳回</el-button>
                    </div>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle "      v-if="gly === 13060 && ideologyScore2[index].adopter !='2'  ">唐玉婷</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 18059 && ideologyScore2[index].adopter !='2'  ">曲楠楠</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19027 && ideologyScore2[index].adopter !='2'  ">曹永荣</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19026 && ideologyScore2[index].adopter !='2'  ">张文婷</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19028 && ideologyScore2[index].adopter !='2'  ">陈金涛</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19133 && ideologyScore2[index].adopter !='2'  ">朱业顺</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 20002 && ideologyScore2[index].adopter !='2'  ">翟晓涵</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 20283 && ideologyScore2[index].adopter !='2'  ">王孟阳</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 21155 && ideologyScore2[index].adopter !='2'  ">穆小青</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 22074 && ideologyScore2[index].adopter !='2'  ">徐慧玲</el-button>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="ideologyScore2[index].adopter2 == '0' && ideologyScore2[index].adopter !='2'">审核状态: <span style="color: lightsalmon ">审核中</span></span>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="ideologyScore2[index].adopter2 == '1' && ideologyScore2[index].adopter !='2'">审核状态: <span style="color: green ">通过</span></span>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="ideologyScore2[index].adopter2 == '2' && ideologyScore2[index].adopter !='2'">审核状态: <span style="color: red ">驳回</span></span>
                  </div>
                </div>
                <div v-for="(imgs, index) in this.ideologyScore2" v-viewer="{movable:true}" style="display: inline;vertical-align: middle">
                  <ul id="yihangxianshiduotu">
                    <li><el-image z-index="3000" v-if="ideologyScore2[index].adopter != '2'" :fit="fit" :src="'https://static-nfuca-1302505692.cos-website.ap-guangzhou.myqcloud.com/img/upload/raw/' + ideologyScore2[index].id+'.jpg'" style="width:200px;height:150px;"/></li>
                  </ul>
                </div>
              </template>
              <template v-else>
                <el-input autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                <el-input-number value="0.0" :precision="1" :step="0.1" :disabled="true" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
              </template>
            </el-form-item>
          </el-row>

          <!--3、道德素质-->
          <el-row>
            <el-form-item label="3、道德素质">
              <template v-if="this.moralityScore2.length > 0">
                <div style="display: inline-block;width: 45%">
                  <div v-for="(item,index) in this.moralityScore2" >
                    <el-input v-model="moralityScore2[index].title" v-if="moralityScore2[index].adopter !='2' " autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                    <el-input-number v-model="moralityScore2[index].score" v-if="moralityScore2[index].adopter != '2' " :precision="1" :step="0.1" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
                    <el-dialog width="50%" title="驳回理由"
                               :visible.sync="innerVisibleMorality"
                               append-to-body>
                      <el-input type="textarea" placeholder="请输入内容" v-model="Reason" maxlength="100" show-word-limit></el-input>
                      <div slot="footer" class="dialog-footer" >
                        <el-button @click="innerVisibleMorality = false" type="info">取消</el-button>
                        <el-popconfirm style="margin-left: 5px" confirm-button-text='确定咯' cancel-button-text='取消' icon="el-icon-info" icon-color="red" title="您确定要驳回吗？"
                                       v-hasPermi="['test:major:back']"
                                       @confirm="doubleMorality(form.no,Reason,moralityScore2[index].openId)"
                        >
                          <el-button  type="danger" slot="reference">确定</el-button>
                        </el-popconfirm>
                      </div>
                    </el-dialog>

                    <div slot="footer" class="dialog-footer" style="display: inline;  vertical-align: middle" >
                      <el-button type="warning" size="mini" v-if="ideologyScore2[index].adopter !='2' " style="margin-left: 5px" @click="showTest3(item.id)">驳回</el-button>
                    </div>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle "      v-if="gly === 13060 && moralityScore2[index].adopter !='2'  ">唐玉婷</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 18059 && moralityScore2[index].adopter !='2'  ">曲楠楠</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19027 && moralityScore2[index].adopter !='2'  ">曹永荣</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19026 && moralityScore2[index].adopter !='2'  ">张文婷</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19028 && moralityScore2[index].adopter !='2'  ">陈金涛</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19133 && moralityScore2[index].adopter !='2'  ">朱业顺</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 20002 && moralityScore2[index].adopter !='2'  ">翟晓涵</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 20283 && moralityScore2[index].adopter !='2'  ">王孟阳</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 21155 && moralityScore2[index].adopter !='2'  ">穆小青</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 22074 && moralityScore2[index].adopter !='2'  ">徐慧玲</el-button>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="moralityScore2[index].adopter2 == '0' && moralityScore2[index].adopter !='2'">审核状态: <span style="color: lightsalmon ">审核中</span></span>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="moralityScore2[index].adopter2 == '1' && moralityScore2[index].adopter !='2'">审核状态: <span style="color: green ">通过</span></span>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="moralityScore2[index].adopter2 == '2' && moralityScore2[index].adopter !='2'">审核状态: <span style="color: red ">驳回</span></span>
                  </div>
                </div>
                <div v-for="(imgs, index) in this.moralityScore2" v-viewer="{movable:true}" style="display: inline;vertical-align: middle">
                  <ul id="yihangxianshiduotu">
                    <li><el-image z-index="3000" v-if="moralityScore2[index].adopter != '2'" :fit="fit" :src="'https://static-nfuca-1302505692.cos-website.ap-guangzhou.myqcloud.com/img/upload/raw/' + moralityScore2[index].id+'.jpg'" style="width:200px;height:150px;"/></li>
                  </ul>
                </div>
              </template>
              <template v-else>
                <el-input autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                <el-input-number value="0.0" :precision="1" :step="0.1" :disabled="true" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
              </template>
            </el-form-item>
          </el-row>

          <!--4、组织素质-->
          <el-row>
            <el-form-item label="4、组织素质">
              <template v-if="this.organScore2.length > 0">
                <div style="display: inline-block;width: 45%">
                  <div v-for="(item,index) in this.organScore2" >
                    <el-input v-model="item.title" v-if="item.adopter !='2' " autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                    <el-input-number v-model="item.score" v-if="item.adopter != '2' " :precision="1" :step="0.1" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
                    <el-dialog width="50%" title="驳回理由"
                               :visible.sync="innerVisibleOrgan"
                               append-to-body>
                      <el-input type="textarea" placeholder="请输入内容" v-model="Reason" maxlength="100" show-word-limit></el-input>
                      <div slot="footer" class="dialog-footer" >
                        <el-button @click="innerVisibleOrgan = false" type="info">取消</el-button>
                        <el-popconfirm style="margin-left: 5px" confirm-button-text='确定咯' cancel-button-text='取消' icon="el-icon-info" icon-color="red" title="您确定要驳回吗？"
                                       v-hasPermi="['test:major:back']"
                                       @confirm="doubleOrgan(form.no,Reason,item.openId)"
                        >
                          <el-button  type="danger" slot="reference">确定</el-button>
                        </el-popconfirm>
                      </div>
                    </el-dialog>

                    <div slot="footer" class="dialog-footer" style="display: inline;  vertical-align: middle" >
                      <el-button type="warning" size="mini" v-if="item.adopter !='2' " style="margin-left: 5px" @click="showTest4(item.id)">驳回</el-button>
                    </div>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle "      v-if="gly === 13060 && item.adopter !='2'  ">唐玉婷</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 18059 && item.adopter !='2'  ">曲楠楠</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19027 && item.adopter !='2'  ">曹永荣</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19026 && item.adopter !='2'  ">张文婷</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19028 && item.adopter !='2'  ">陈金涛</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19133 && item.adopter !='2'  ">朱业顺</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 20002 && item.adopter !='2'  ">翟晓涵</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 20283 && item.adopter !='2'  ">王孟阳</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 21155 && item.adopter !='2'  ">穆小青</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 22074 && item.adopter !='2'  ">徐慧玲</el-button>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="item.adopter2 == '0' && item.adopter !='2'">审核状态: <span style="color: lightsalmon ">审核中</span></span>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="item.adopter2 == '1' && item.adopter !='2'">审核状态: <span style="color: green ">通过</span></span>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="item.adopter2 == '2' && item.adopter !='2'">审核状态: <span style="color: red ">驳回</span></span>
                  </div>
                </div>
                <div v-for="(item, index) in this.organScore2" v-viewer="{movable:true}" style="display: inline;vertical-align: middle">
                  <ul id="yihangxianshiduotu">
                    <li><el-image z-index="3000" v-if="item.adopter != '2'" :fit="fit" :src="'https://static-nfuca-1302505692.cos-website.ap-guangzhou.myqcloud.com/img/upload/raw/' + item.id+'.jpg'" style="width:200px;height:150px;"/></li>
                  </ul>
                </div>
              </template>
              <template v-else>
                <el-input autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                <el-input-number value="0.0" :precision="1" :step="0.1" :disabled="true" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
              </template>
            </el-form-item>
          </el-row>


          <!--5、法纪素质-->
          <el-row>
            <el-form-item label="5、法纪素质">
              <template v-if="this.lawScore2.length > 0">
                <div style="display: inline-block;width: 45%">
                  <div v-for="(item,index) in this.lawScore2" >
                    <el-input v-model="item.title" v-if="item.adopter !='2' " autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                    <el-input-number v-model="item.score" v-if="item.adopter != '2' " :precision="1" :step="0.1" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
                    <el-dialog width="50%" title="驳回理由"
                               :visible.sync="innerVisibleLaw"
                               append-to-body>
                      <el-input type="textarea" placeholder="请输入内容" v-model="Reason" maxlength="100" show-word-limit></el-input>
                      <div slot="footer" class="dialog-footer" >
                        <el-button @click="innerVisibleLaw = false" type="info">取消</el-button>
                        <el-popconfirm style="margin-left: 5px" confirm-button-text='确定咯' cancel-button-text='取消' icon="el-icon-info" icon-color="red" title="您确定要驳回吗？"
                                       v-hasPermi="['test:major:back']"
                                       @confirm="doubleLaw(form.no,Reason,item.openId)"
                        >
                          <el-button  type="danger" slot="reference">确定</el-button>
                        </el-popconfirm>
                      </div>
                    </el-dialog>

                    <div slot="footer" class="dialog-footer" style="display: inline;  vertical-align: middle" >
                      <el-button type="warning" size="mini" v-if="item.adopter !='2' " style="margin-left: 5px" @click="showTest5(item.id)">驳回</el-button>
                    </div>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle "      v-if="gly === 13060 && item.adopter !='2'  ">唐玉婷</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 18059 && item.adopter !='2'  ">曲楠楠</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19027 && item.adopter !='2'  ">曹永荣</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19026 && item.adopter !='2'  ">张文婷</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19028 && item.adopter !='2'  ">陈金涛</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19133 && item.adopter !='2'  ">朱业顺</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 20002 && item.adopter !='2'  ">翟晓涵</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 20283 && item.adopter !='2'  ">王孟阳</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 21155 && item.adopter !='2'  ">穆小青</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 22074 && item.adopter !='2'  ">徐慧玲</el-button>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="item.adopter2 == '0' && item.adopter !='2'">审核状态: <span style="color: lightsalmon ">审核中</span></span>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="item.adopter2 == '1' && item.adopter !='2'">审核状态: <span style="color: green ">通过</span></span>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="item.adopter2 == '2' && item.adopter !='2'">审核状态: <span style="color: red ">驳回</span></span>
                  </div>
                </div>
                <div v-for="(item, index) in this.lawScore2" v-viewer="{movable:true}" style="display: inline;vertical-align: middle">
                  <ul id="yihangxianshiduotu">
                    <li><el-image z-index="3000" v-if="item.adopter != '2'" :fit="fit" :src="'https://static-nfuca-1302505692.cos-website.ap-guangzhou.myqcloud.com/img/upload/raw/' + item.id+'.jpg'" style="width:200px;height:150px;"/></li>
                  </ul>
                </div>
              </template>
              <template v-else>
                <el-input autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                <el-input-number value="0.0" :precision="1" :step="0.1" :disabled="true" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
              </template>
            </el-form-item>
          </el-row>

          <!--6、学习与生活素质-->
          <el-row>
            <el-form-item label="6、学习与生活素质">
              <template v-if="this.learningScore2.length > 0">
                <div style="display: inline-block;width: 45%">
                  <div v-for="(item,index) in this.learningScore2" >
                    <el-input v-model="item.title" v-if="item.adopter !='2' " autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                    <el-input-number v-model="item.score" v-if="item.adopter != '2' " :precision="1" :step="0.1" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
                    <el-dialog width="50%" title="驳回理由"
                               :visible.sync="innerVisibleLearning"
                               append-to-body>
                      <el-input type="textarea" placeholder="请输入内容" v-model="Reason" maxlength="100" show-word-limit></el-input>
                      <div slot="footer" class="dialog-footer" >
                        <el-button @click="innerVisibleLearning = false" type="info">取消</el-button>
                        <el-popconfirm style="margin-left: 5px" confirm-button-text='确定咯' cancel-button-text='取消' icon="el-icon-info" icon-color="red" title="您确定要驳回吗？"
                                       v-hasPermi="['test:major:back']"
                                       @confirm="doubleLearning(form.no,Reason,item.openId)"
                        >
                          <el-button  type="danger" slot="reference">确定</el-button>
                        </el-popconfirm>
                      </div>
                    </el-dialog>

                    <div slot="footer" class="dialog-footer" style="display: inline;  vertical-align: middle" >
                      <el-button type="warning" size="mini" v-if="item.adopter !='2' " style="margin-left: 5px" @click="showTest6(item.id)">驳回</el-button>
                    </div>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle "      v-if="gly === 13060 && item.adopter !='2'  ">唐玉婷</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 18059 && item.adopter !='2'  ">曲楠楠</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19027 && item.adopter !='2'  ">曹永荣</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19026 && item.adopter !='2'  ">张文婷</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19028 && item.adopter !='2'  ">陈金涛</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19133 && item.adopter !='2'  ">朱业顺</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 20002 && item.adopter !='2'  ">翟晓涵</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 20283 && item.adopter !='2'  ">王孟阳</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 21155 && item.adopter !='2'  ">穆小青</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 22074 && item.adopter !='2'  ">徐慧玲</el-button>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="item.adopter2 == '0' && item.adopter !='2'">审核状态: <span style="color: lightsalmon ">审核中</span></span>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="item.adopter2 == '1' && item.adopter !='2'">审核状态: <span style="color: green ">通过</span></span>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="item.adopter2 == '2' && item.adopter !='2'">审核状态: <span style="color: red ">驳回</span></span>
                  </div>
                </div>
                <div v-for="(item, index) in this.learningScore2" v-viewer="{movable:true}" style="display: inline;vertical-align: middle">
                  <ul id="yihangxianshiduotu">
                    <li><el-image z-index="3000" v-if="item.adopter != '2'" :fit="fit" :src="'https://static-nfuca-1302505692.cos-website.ap-guangzhou.myqcloud.com/img/upload/raw/' + item.id+'.jpg'" style="width:200px;height:150px;"/></li>
                  </ul>
                </div>
              </template>
              <template v-else>
                <el-input autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                <el-input-number value="0.0" :precision="1" :step="0.1" :disabled="true" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
              </template>
            </el-form-item>
          </el-row>

          <!--7、实践与创新素质-->
          <el-row>
            <el-form-item label="7、实践与创新素质">
              <template v-if="this.developmentScore2.length > 0">
                <div style="display: inline-block;width: 45%">
                  <div v-for="(item,index) in this.developmentScore2" >
                    <el-input v-model="item.title" v-if="item.adopter !='2' " autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                    <el-input-number v-model="item.score" v-if="item.adopter != '2' " :precision="1" :step="0.1" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
                    <el-dialog width="50%" title="驳回理由"
                               :visible.sync="innerVisibleDevelopment"
                               append-to-body>
                      <el-input type="textarea" placeholder="请输入内容" v-model="Reason" maxlength="100" show-word-limit></el-input>
                      <div slot="footer" class="dialog-footer" >
                        <el-button @click="innerVisibleDevelopment = false" type="info">取消</el-button>
                        <el-popconfirm style="margin-left: 5px" confirm-button-text='确定咯' cancel-button-text='取消' icon="el-icon-info" icon-color="red" title="您确定要驳回吗？"
                                       v-hasPermi="['test:major:back']"
                                       @confirm="doubleDevelopment(form.no,Reason,item.openId)"
                        >
                          <el-button  type="danger" slot="reference">确定</el-button>
                        </el-popconfirm>
                      </div>
                    </el-dialog>

                    <div slot="footer" class="dialog-footer" style="display: inline;  vertical-align: middle" >
                      <el-button type="warning" size="mini" v-if="item.adopter !='2' " style="margin-left: 5px" @click="showTest7(item.id)">驳回</el-button>
                    </div>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle "      v-if="gly === 13060 && item.adopter !='2'  ">唐玉婷</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 18059 && item.adopter !='2'  ">曲楠楠</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19027 && item.adopter !='2'  ">曹永荣</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19026 && item.adopter !='2'  ">张文婷</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19028 && item.adopter !='2'  ">陈金涛</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19133 && item.adopter !='2'  ">朱业顺</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 20002 && item.adopter !='2'  ">翟晓涵</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 20283 && item.adopter !='2'  ">王孟阳</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 21155 && item.adopter !='2'  ">穆小青</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 22074 && item.adopter !='2'  ">徐慧玲</el-button>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="item.adopter2 == '0' && item.adopter !='2'">审核状态: <span style="color: lightsalmon ">审核中</span></span>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="item.adopter2 == '1' && item.adopter !='2'">审核状态: <span style="color: green ">通过</span></span>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="item.adopter2 == '2' && item.adopter !='2'">审核状态: <span style="color: red ">驳回</span></span>
                  </div>
                </div>
                <div v-for="(item, index) in this.developmentScore2" v-viewer="{movable:true}" style="display: inline;vertical-align: middle">
                  <ul id="yihangxianshiduotu">
                    <li><el-image z-index="3000" v-if="item.adopter != '2'" :fit="fit" :src="'https://static-nfuca-1302505692.cos-website.ap-guangzhou.myqcloud.com/img/upload/raw/' + item.id+'.jpg'" style="width:200px;height:150px;"/></li>
                  </ul>
                </div>
              </template>
              <template v-else>
                <el-input autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                <el-input-number value="0.0" :precision="1" :step="0.1" :disabled="true" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
              </template>
            </el-form-item>
          </el-row>

          <!--8、科学文化素质-->
          <el-row>
            <el-form-item label="8、科学文化素质">
              <template v-if="this.scientificScore2.length > 0">
                <div style="display: inline-block;width: 45%">
                  <div v-for="(item,index) in this.scientificScore2" >
                    <el-input v-model="item.title" v-if="item.adopter !='2' " autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                    <el-input-number v-model="item.score" v-if="item.adopter != '2' " :precision="1" :step="0.1" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
                    <el-dialog width="50%" title="驳回理由"
                               :visible.sync="innerVisibleScientific"
                               append-to-body>
                      <el-input type="textarea" placeholder="请输入内容" v-model="Reason" maxlength="100" show-word-limit></el-input>
                      <div slot="footer" class="dialog-footer" >
                        <el-button @click="innerVisibleScientific = false" type="info">取消</el-button>
                        <el-popconfirm style="margin-left: 5px" confirm-button-text='确定咯' cancel-button-text='取消' icon="el-icon-info" icon-color="red" title="您确定要驳回吗？"
                                       v-hasPermi="['test:major:back']"
                                       @confirm="doubleScientific(form.no,Reason,item.openId)"
                        >
                          <el-button  type="danger" slot="reference">确定</el-button>
                        </el-popconfirm>
                      </div>
                    </el-dialog>

                    <div slot="footer" class="dialog-footer" style="display: inline;  vertical-align: middle" >
                      <el-button type="warning" size="mini" v-if="item.adopter !='2' " style="margin-left: 5px" @click="showTest8(item.id)">驳回</el-button>
                    </div>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle "      v-if="gly === 13060 && item.adopter !='2'  ">唐玉婷</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 18059 && item.adopter !='2'  ">曲楠楠</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19027 && item.adopter !='2'  ">曹永荣</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19026 && item.adopter !='2'  ">张文婷</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19028 && item.adopter !='2'  ">陈金涛</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19133 && item.adopter !='2'  ">朱业顺</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 20002 && item.adopter !='2'  ">翟晓涵</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 20283 && item.adopter !='2'  ">王孟阳</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 21155 && item.adopter !='2'  ">穆小青</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 22074 && item.adopter !='2'  ">徐慧玲</el-button>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="item.adopter2 == '0' && item.adopter !='2'">审核状态: <span style="color: lightsalmon ">审核中</span></span>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="item.adopter2 == '1' && item.adopter !='2'">审核状态: <span style="color: green ">通过</span></span>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="item.adopter2 == '2' && item.adopter !='2'">审核状态: <span style="color: red ">驳回</span></span>
                  </div>
                </div>
                <div v-for="(item, index) in this.scientificScore2" v-viewer="{movable:true}" style="display: inline;vertical-align: middle">
                  <ul id="yihangxianshiduotu">
                    <li><el-image z-index="3000" v-if="item.adopter != '2'" :fit="fit" :src="'https://static-nfuca-1302505692.cos-website.ap-guangzhou.myqcloud.com/img/upload/raw/' + item.id+'.jpg'" style="width:200px;height:150px;"/></li>
                  </ul>
                </div>
              </template>
              <template v-else>
                <el-input autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                <el-input-number value="0.0" :precision="1" :step="0.1" :disabled="true" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
              </template>
            </el-form-item>
          </el-row>

          <!--9、身体素质-->
          <el-row>
            <el-form-item label="9、身体素质">
              <template v-if="this.physicalScore2.length > 0">
                <div style="display: inline-block;width: 45%">
                  <div v-for="(item,index) in this.physicalScore2" >
                    <el-input v-model="item.title" v-if="item.adopter !='2' " autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                    <el-input-number v-model="item.score" v-if="item.adopter != '2' " :precision="1" :step="0.1" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
                    <el-dialog width="50%" title="驳回理由"
                               :visible.sync="innerVisiblePhysical"
                               append-to-body>
                      <el-input type="textarea" placeholder="请输入内容" v-model="Reason" maxlength="100" show-word-limit></el-input>
                      <div slot="footer" class="dialog-footer" >
                        <el-button @click="innerVisiblePhysical = false" type="info">取消</el-button>
                        <el-popconfirm style="margin-left: 5px" confirm-button-text='确定咯' cancel-button-text='取消' icon="el-icon-info" icon-color="red" title="您确定要驳回吗？"
                                       v-hasPermi="['test:major:back']"
                                       @confirm="doublePhysical(form.no,Reason,item.openId)"
                        >
                          <el-button  type="danger" slot="reference">确定</el-button>
                        </el-popconfirm>
                      </div>
                    </el-dialog>

                    <div slot="footer" class="dialog-footer" style="display: inline;  vertical-align: middle" >
                      <el-button type="warning" size="mini" v-if="item.adopter !='2' " style="margin-left: 5px" @click="showTest9(item.id)">驳回</el-button>
                    </div>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle "      v-if="gly === 13060 && item.adopter !='2'  ">唐玉婷</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 18059 && item.adopter !='2'  ">曲楠楠</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19027 && item.adopter !='2'  ">曹永荣</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19026 && item.adopter !='2'  ">张文婷</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19028 && item.adopter !='2'  ">陈金涛</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19133 && item.adopter !='2'  ">朱业顺</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 20002 && item.adopter !='2'  ">翟晓涵</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 20283 && item.adopter !='2'  ">王孟阳</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 21155 && item.adopter !='2'  ">穆小青</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 22074 && item.adopter !='2'  ">徐慧玲</el-button>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="item.adopter2 == '0' && item.adopter !='2'">审核状态: <span style="color: lightsalmon ">审核中</span></span>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="item.adopter2 == '1' && item.adopter !='2'">审核状态: <span style="color: green ">通过</span></span>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="item.adopter2 == '2' && item.adopter !='2'">审核状态: <span style="color: red ">驳回</span></span>
                  </div>
                </div>
                <div v-for="(item, index) in this.physicalScore2" v-viewer="{movable:true}" style="display: inline;vertical-align: middle">
                  <ul id="yihangxianshiduotu">
                    <li><el-image z-index="3000" v-if="item.adopter != '2'" :fit="fit" :src="'https://static-nfuca-1302505692.cos-website.ap-guangzhou.myqcloud.com/img/upload/raw/' + item.id+'.jpg'" style="width:200px;height:150px;"/></li>
                  </ul>
                </div>
              </template>
              <template v-else>
                <el-input autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                <el-input-number value="0.0" :precision="1" :step="0.1" :disabled="true" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
              </template>
            </el-form-item>
          </el-row>

          <!--10、心理素质-->
          <el-row>
            <el-form-item label="10、心理素质">
              <template v-if="this.mentalScore2.length > 0">
                <div style="display: inline-block;width: 45%">
                  <div v-for="(item,index) in this.mentalScore2" >
                    <el-input v-model="item.title" v-if="item.adopter !='2' " autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                    <el-input-number v-model="item.score" v-if="item.adopter != '2' " :precision="1" :step="0.1" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
                    <el-dialog width="50%" title="驳回理由"
                               :visible.sync="innerVisibleMental"
                               append-to-body>
                      <el-input type="textarea" placeholder="请输入内容" v-model="Reason" maxlength="100" show-word-limit></el-input>
                      <div slot="footer" class="dialog-footer" >
                        <el-button @click="innerVisibleMental = false" type="info">取消</el-button>
                        <el-popconfirm style="margin-left: 5px" confirm-button-text='确定咯' cancel-button-text='取消' icon="el-icon-info" icon-color="red" title="您确定要驳回吗？"
                                       v-hasPermi="['test:major:back']"
                                       @confirm="doubleMental(form.no,Reason,item.openId)"
                        >
                          <el-button  type="danger" slot="reference">确定</el-button>
                        </el-popconfirm>
                      </div>
                    </el-dialog>

                    <div slot="footer" class="dialog-footer" style="display: inline;  vertical-align: middle" >
                      <el-button type="warning" size="mini" v-if="item.adopter !='2' " style="margin-left: 5px" @click="showTest10(item.id)">驳回</el-button>
                    </div>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle "      v-if="gly === 13060 && item.adopter !='2'  ">唐玉婷</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 18059 && item.adopter !='2'  ">曲楠楠</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19027 && item.adopter !='2'  ">曹永荣</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19026 && item.adopter !='2'  ">张文婷</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19028 && item.adopter !='2'  ">陈金涛</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19133 && item.adopter !='2'  ">朱业顺</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 20002 && item.adopter !='2'  ">翟晓涵</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 20283 && item.adopter !='2'  ">王孟阳</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 21155 && item.adopter !='2'  ">穆小青</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 22074 && item.adopter !='2'  ">徐慧玲</el-button>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="item.adopter2 == '0' && item.adopter !='2'">审核状态: <span style="color: lightsalmon ">审核中</span></span>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="item.adopter2 == '1' && item.adopter !='2'">审核状态: <span style="color: green ">通过</span></span>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="item.adopter2 == '2' && item.adopter !='2'">审核状态: <span style="color: red ">驳回</span></span>
                  </div>
                </div>
                <div v-for="(item, index) in this.mentalScore2" v-viewer="{movable:true}" style="display: inline;vertical-align: middle">
                  <ul id="yihangxianshiduotu">
                    <li><el-image z-index="3000" v-if="item.adopter != '2'" :fit="fit" :src="'https://static-nfuca-1302505692.cos-website.ap-guangzhou.myqcloud.com/img/upload/raw/' + item.id+'.jpg'" style="width:200px;height:150px;"/></li>
                  </ul>
                </div>
              </template>
              <template v-else>
                <el-input autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                <el-input-number value="0.0" :precision="1" :step="0.1" :disabled="true" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
              </template>
            </el-form-item>
          </el-row>

          <el-descriptions title="发展性素质测评（满分45 分）"></el-descriptions>

          <!--11、荣誉称号加分-->
          <el-row>
            <el-form-item label="11、荣誉称号加分">
              <template v-if="this.honoraryScore2.length > 0">
                <div style="display: inline-block;width: 45%">
                  <div v-for="(item,index) in this.honoraryScore2" >
                    <el-input v-model="item.title" v-if="item.adopter !='2' " autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                    <el-input-number v-model="item.score" v-if="item.adopter != '2' " :precision="1" :step="0.1" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
                    <el-dialog width="50%" title="驳回理由"
                               :visible.sync="innerVisibleHonorary"
                               append-to-body>
                      <el-input type="textarea" placeholder="请输入内容" v-model="Reason" maxlength="100" show-word-limit></el-input>
                      <div slot="footer" class="dialog-footer" >
                        <el-button @click="innerVisibleHonorary = false" type="info">取消</el-button>
                        <el-popconfirm style="margin-left: 5px" confirm-button-text='确定咯' cancel-button-text='取消' icon="el-icon-info" icon-color="red" title="您确定要驳回吗？"
                                       v-hasPermi="['test:major:back']"
                                       @confirm="doubleHonorary(form.no,Reason,item.openId)"
                        >
                          <el-button  type="danger" slot="reference">确定</el-button>
                        </el-popconfirm>
                      </div>
                    </el-dialog>

                    <div slot="footer" class="dialog-footer" style="display: inline;  vertical-align: middle" >
                      <el-button type="warning" size="mini" v-if="item.adopter !='2' " style="margin-left: 5px" @click="showTest11(item.id)">驳回</el-button>
                    </div>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle "      v-if="gly === 13060 && item.adopter !='2'  ">唐玉婷</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 18059 && item.adopter !='2'  ">曲楠楠</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19027 && item.adopter !='2'  ">曹永荣</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19026 && item.adopter !='2'  ">张文婷</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19028 && item.adopter !='2'  ">陈金涛</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19133 && item.adopter !='2'  ">朱业顺</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 20002 && item.adopter !='2'  ">翟晓涵</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 20283 && item.adopter !='2'  ">王孟阳</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 21155 && item.adopter !='2'  ">穆小青</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 22074 && item.adopter !='2'  ">徐慧玲</el-button>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="item.adopter2 == '0' && item.adopter !='2'">审核状态: <span style="color: lightsalmon ">审核中</span></span>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="item.adopter2 == '1' && item.adopter !='2'">审核状态: <span style="color: green ">通过</span></span>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="item.adopter2 == '2' && item.adopter !='2'">审核状态: <span style="color: red ">驳回</span></span>
                  </div>
                </div>
                <div v-for="(item, index) in this.honoraryScore2" v-viewer="{movable:true}" style="display: inline;vertical-align: middle">
                  <ul id="yihangxianshiduotu">
                    <li><el-image z-index="3000" v-if="item.adopter != '2'" :fit="fit" :src="'https://static-nfuca-1302505692.cos-website.ap-guangzhou.myqcloud.com/img/upload/raw/' + item.id+'.jpg'" style="width:200px;height:150px;"/></li>
                  </ul>
                </div>
              </template>
              <template v-else>
                <el-input autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                <el-input-number value="0.0" :precision="1" :step="0.1" :disabled="true" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
              </template>
            </el-form-item>
          </el-row>

          <!--12、文体艺术等竞赛或活动加分-->
          <el-row>
            <el-form-item label="12、文体艺术等竞赛或活动加分">
              <template v-if="this.competitionScore2.length > 0">
                <div style="display: inline-block;width: 45%">
                  <div v-for="(item,index) in this.competitionScore2" >
                    <el-input v-model="item.title" v-if="item.adopter !='2' " autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                    <el-input-number v-model="item.score" v-if="item.adopter != '2' " :precision="1" :step="0.1" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
                    <el-dialog width="50%" title="驳回理由"
                               :visible.sync="innerVisibleCompetition"
                               append-to-body>
                      <el-input type="textarea" placeholder="请输入内容" v-model="Reason" maxlength="100" show-word-limit></el-input>
                      <div slot="footer" class="dialog-footer" >
                        <el-button @click="innerVisibleCompetition = false" type="info">取消</el-button>
                        <el-popconfirm style="margin-left: 5px" confirm-button-text='确定咯' cancel-button-text='取消' icon="el-icon-info" icon-color="red" title="您确定要驳回吗？"
                                       v-hasPermi="['test:major:back']"
                                       @confirm="doubleCompetition(form.no,Reason,item.openId)"
                        >
                          <el-button  type="danger" slot="reference">确定</el-button>
                        </el-popconfirm>
                      </div>
                    </el-dialog>

                    <div slot="footer" class="dialog-footer" style="display: inline;  vertical-align: middle" >
                      <el-button type="warning" size="mini" v-if="item.adopter !='2' " style="margin-left: 5px" @click="showTest12(item.id)">驳回</el-button>
                    </div>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle "      v-if="gly === 13060 && item.adopter !='2'  ">唐玉婷</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 18059 && item.adopter !='2'  ">曲楠楠</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19027 && item.adopter !='2'  ">曹永荣</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19026 && item.adopter !='2'  ">张文婷</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19028 && item.adopter !='2'  ">陈金涛</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19133 && item.adopter !='2'  ">朱业顺</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 20002 && item.adopter !='2'  ">翟晓涵</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 20283 && item.adopter !='2'  ">王孟阳</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 21155 && item.adopter !='2'  ">穆小青</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 22074 && item.adopter !='2'  ">徐慧玲</el-button>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="item.adopter2 == '0' && item.adopter !='2'">审核状态: <span style="color: lightsalmon ">审核中</span></span>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="item.adopter2 == '1' && item.adopter !='2'">审核状态: <span style="color: green ">通过</span></span>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="item.adopter2 == '2' && item.adopter !='2'">审核状态: <span style="color: red ">驳回</span></span>
                  </div>
                </div>
                <div v-for="(item, index) in this.competitionScore2" v-viewer="{movable:true}" style="display: inline;vertical-align: middle">
                  <ul id="yihangxianshiduotu">
                    <li><el-image z-index="3000" v-if="item.adopter != '2'" :fit="fit" :src="'https://static-nfuca-1302505692.cos-website.ap-guangzhou.myqcloud.com/img/upload/raw/' + item.id+'.jpg'" style="width:200px;height:150px;"/></li>
                  </ul>
                </div>
              </template>
              <template v-else>
                <el-input autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                <el-input-number value="0.0" :precision="1" :step="0.1" :disabled="true" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
              </template>
            </el-form-item>
          </el-row>


          <!--13、社会工作加分-->
          <el-row>
            <el-form-item label="12、文体艺术等竞赛或活动加分">
              <template v-if="this.socialWorkScore2.length > 0">
                <div style="display: inline-block;width: 45%">
                  <div v-for="(item,index) in this.socialWorkScore2" >
                    <el-input v-model="item.title" v-if="item.adopter !='2' " autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                    <el-input-number v-model="item.score" v-if="item.adopter != '2' " :precision="1" :step="0.1" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
                    <el-dialog width="50%" title="驳回理由"
                               :visible.sync="innerVisibleSocialWork"
                               append-to-body>
                      <el-input type="textarea" placeholder="请输入内容" v-model="Reason" maxlength="100" show-word-limit></el-input>
                      <div slot="footer" class="dialog-footer" >
                        <el-button @click="innerVisibleSocialWork = false" type="info">取消</el-button>
                        <el-popconfirm style="margin-left: 5px" confirm-button-text='确定咯' cancel-button-text='取消' icon="el-icon-info" icon-color="red" title="您确定要驳回吗？"
                                       v-hasPermi="['test:major:back']"
                                       @confirm="doubleSocialWork(form.no,Reason,item.openId)"
                        >
                          <el-button  type="danger" slot="reference">确定</el-button>
                        </el-popconfirm>
                      </div>
                    </el-dialog>

                    <div slot="footer" class="dialog-footer" style="display: inline;  vertical-align: middle" >
                      <el-button type="warning" size="mini" v-if="item.adopter !='2' " style="margin-left: 5px" @click="showTest13(item.id)">驳回</el-button>
                    </div>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle "      v-if="gly === 13060 && item.adopter !='2'  ">唐玉婷</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 18059 && item.adopter !='2'  ">曲楠楠</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19027 && item.adopter !='2'  ">曹永荣</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19026 && item.adopter !='2'  ">张文婷</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19028 && item.adopter !='2'  ">陈金涛</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19133 && item.adopter !='2'  ">朱业顺</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 20002 && item.adopter !='2'  ">翟晓涵</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 20283 && item.adopter !='2'  ">王孟阳</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 21155 && item.adopter !='2'  ">穆小青</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 22074 && item.adopter !='2'  ">徐慧玲</el-button>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="item.adopter2 == '0' && item.adopter !='2'">审核状态: <span style="color: lightsalmon ">审核中</span></span>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="item.adopter2 == '1' && item.adopter !='2'">审核状态: <span style="color: green ">通过</span></span>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="item.adopter2 == '2' && item.adopter !='2'">审核状态: <span style="color: red ">驳回</span></span>
                  </div>
                </div>
                <div v-for="(item, index) in this.socialWorkScore2" v-viewer="{movable:true}" style="display: inline;vertical-align: middle">
                  <ul id="yihangxianshiduotu">
                    <li><el-image z-index="3000" v-if="item.adopter != '2'" :fit="fit" :src="'https://static-nfuca-1302505692.cos-website.ap-guangzhou.myqcloud.com/img/upload/raw/' + item.id+'.jpg'" style="width:200px;height:150px;"/></li>
                  </ul>
                </div>
              </template>
              <template v-else>
                <el-input autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                <el-input-number value="0.0" :precision="1" :step="0.1" :disabled="true" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
              </template>
            </el-form-item>
          </el-row>


          <!--14、知识与技能加分-->
          <el-row>
            <el-form-item label="14、知识与技能加分">
              <template v-if="this.knowlScore2.length > 0">
                <div style="display: inline-block;width: 45%">
                  <div v-for="(item,index) in this.knowlScore2" >
                    <el-input v-model="item.title" v-if="item.adopter !='2' " autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                    <el-input-number v-model="item.score" v-if="item.adopter != '2' " :precision="1" :step="0.1" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
                    <el-dialog width="50%" title="驳回理由"
                               :visible.sync="innerVisibleKnowl"
                               append-to-body>
                      <el-input type="textarea" placeholder="请输入内容" v-model="Reason" maxlength="100" show-word-limit></el-input>
                      <div slot="footer" class="dialog-footer" >
                        <el-button @click="innerVisibleKnowl = false" type="info">取消</el-button>
                        <el-popconfirm style="margin-left: 5px" confirm-button-text='确定咯' cancel-button-text='取消' icon="el-icon-info" icon-color="red" title="您确定要驳回吗？"
                                       v-hasPermi="['test:major:back']"
                                       @confirm="doubleKnowl(form.no,Reason,item.openId)"
                        >
                          <el-button  type="danger" slot="reference">确定</el-button>
                        </el-popconfirm>
                      </div>
                    </el-dialog>

                    <div slot="footer" class="dialog-footer" style="display: inline;  vertical-align: middle" >
                      <el-button type="warning" size="mini" v-if="item.adopter !='2' " style="margin-left: 5px" @click="showTest14(item.id)">驳回</el-button>
                    </div>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle "      v-if="gly === 13060 && item.adopter !='2'  ">唐玉婷</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 18059 && item.adopter !='2'  ">曲楠楠</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19027 && item.adopter !='2'  ">曹永荣</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19026 && item.adopter !='2'  ">张文婷</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19028 && item.adopter !='2'  ">陈金涛</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19133 && item.adopter !='2'  ">朱业顺</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 20002 && item.adopter !='2'  ">翟晓涵</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 20283 && item.adopter !='2'  ">王孟阳</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 21155 && item.adopter !='2'  ">穆小青</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 22074 && item.adopter !='2'  ">徐慧玲</el-button>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="item.adopter2 == '0' && item.adopter !='2'">审核状态: <span style="color: lightsalmon ">审核中</span></span>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="item.adopter2 == '1' && item.adopter !='2'">审核状态: <span style="color: green ">通过</span></span>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="item.adopter2 == '2' && item.adopter !='2'">审核状态: <span style="color: red ">驳回</span></span>
                  </div>
                </div>
                <div v-for="(item, index) in this.knowlScore2" v-viewer="{movable:true}" style="display: inline;vertical-align: middle">
                  <ul id="yihangxianshiduotu">
                    <li><el-image z-index="3000" v-if="item.adopter != '2'" :fit="fit" :src="'https://static-nfuca-1302505692.cos-website.ap-guangzhou.myqcloud.com/img/upload/raw/' + item.id+'.jpg'" style="width:200px;height:150px;"/></li>
                  </ul>
                </div>
              </template>
              <template v-else>
                <el-input autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                <el-input-number value="0.0" :precision="1" :step="0.1" :disabled="true" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
              </template>
            </el-form-item>
          </el-row>

          <!--15、日常操行减分-->
          <el-row>
            <el-form-item label="15、日常操行减分">
              <template v-if="this.dailyScore2.length > 0">
                <div style="display: inline-block;width: 45%">
                  <div v-for="(item,index) in this.dailyScore2" >
                    <el-input v-model="item.title" v-if="item.adopter !='2' " autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                    <el-input-number v-model="item.score" v-if="item.adopter != '2' " :precision="1" :step="0.1" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
                    <el-dialog width="50%" title="驳回理由"
                               :visible.sync="innerVisibleDaily"
                               append-to-body>
                      <el-input type="textarea" placeholder="请输入内容" v-model="Reason" maxlength="100" show-word-limit></el-input>
                      <div slot="footer" class="dialog-footer" >
                        <el-button @click="innerVisibleDaily = false" type="info">取消</el-button>
                        <el-popconfirm style="margin-left: 5px" confirm-button-text='确定咯' cancel-button-text='取消' icon="el-icon-info" icon-color="red" title="您确定要驳回吗？"
                                       v-hasPermi="['test:major:back']"
                                       @confirm="doubleDaily(form.no,Reason,item.openId)"
                        >
                          <el-button  type="danger" slot="reference">确定</el-button>
                        </el-popconfirm>
                      </div>
                    </el-dialog>

                    <div slot="footer" class="dialog-footer" style="display: inline;  vertical-align: middle" >
                      <el-button type="warning" size="mini" v-if="item.adopter !='2' " style="margin-left: 5px" @click="showTest15(item.id)">驳回</el-button>
                    </div>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle "      v-if="gly === 13060 && item.adopter !='2'  ">唐玉婷</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 18059 && item.adopter !='2'  ">曲楠楠</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19027 && item.adopter !='2'  ">曹永荣</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19026 && item.adopter !='2'  ">张文婷</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19028 && item.adopter !='2'  ">陈金涛</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 19133 && item.adopter !='2'  ">朱业顺</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 20002 && item.adopter !='2'  ">翟晓涵</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 20283 && item.adopter !='2'  ">王孟阳</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 21155 && item.adopter !='2'  ">穆小青</el-button>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-else-if="gly === 22074 && item.adopter !='2'  ">徐慧玲</el-button>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="item.adopter2 == '0' && item.adopter !='2'">审核状态: <span style="color: lightsalmon ">审核中</span></span>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="item.adopter2 == '1' && item.adopter !='2'">审核状态: <span style="color: green ">通过</span></span>
                    <span size="mini" style="width:70px;margin-left: 5px; vertical-align: middle " v-if="item.adopter2 == '2' && item.adopter !='2'">审核状态: <span style="color: red ">驳回</span></span>
                  </div>
                </div>
                <div v-for="(item, index) in this.dailyScore2" v-viewer="{movable:true}" style="display: inline;vertical-align: middle">
                  <ul id="yihangxianshiduotu">
                    <li><el-image z-index="3000" v-if="item.adopter != '2'" :fit="fit" :src="'https://static-nfuca-1302505692.cos-website.ap-guangzhou.myqcloud.com/img/upload/raw/' + item.id+'.jpg'" style="width:200px;height:150px;"/></li>
                  </ul>
                </div>
              </template>
              <template v-else>
                <el-input autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                <el-input-number value="0.0" :precision="1" :step="0.1" :disabled="true" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
              </template>
            </el-form-item>
          </el-row>

          <el-row>
            <el-form-item label="16、期末成绩">
              <el-input v-model="this.achievement" autocomplete="off" type="textarea"  :disabled="true" :autosize="{ minRows: 1}" ></el-input>
              <el-label >加权平均分为：</el-label>
              <el-label style="color:red">{{this.score}}/100</el-label>
            </el-form-item>
          </el-row>

        </el-col>



      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="outerVisible = false" type="info">取消</el-button>
        <el-button
          @click="handleAccess2(form.no,form.id,form.score)"
          type="success"
          v-has-permi="['test:major:access2']"
          style="margin: 0 100px 0 50px" >通过</el-button>
      </div>
    </el-dialog>


  </div>
</template>

<style>
  .getdialogstyle{
    width: 80%;
  }

  #yihangxianshiduotu {
    display: inline;
  }

  #yihangxianshiduotu li
  {
    display: inline;
  }


  img{
    width: 300px;
    height: 200px;
    margin: 10px;
  }

  /*各个点内容的input的样式*/
  .inputMsg {
    width:300px;
    margin-right: 5px;
    vertical-align: middle;
  }
  /*将el-form-item__content的margin-left为120px修改为10px*/
  .reMarginLeft .el-form-item__content {
    margin-left: 10px !important;
  }
  /*各个点前面字体的样式*/
  .TitleMsg {
    vertical-align: middle;
    font-size: 14px;
    font-weight: 700;
    margin-right: 5px;
    color:#606266;
    width: 130px;
    display: inline-block;
    text-align: right
  }


</style>
<script>
  import { TestUser, backUser, accessUser,accessUser2, getAwards,pushmsg,updateScore } from "@/api/test/major";
  import { getToken } from "@/utils/auth";
  import EditTable from "../../tool/gen/editTable.vue";
  import * as ElementUI from "element-ui";
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
        gradeOptions:gradeOptionsini.gradeOptions,
        gradeValue:'',
        //驳回原因
        reason:'',
        // 遮罩层
        loading: true,
        gly:0,
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
        //用户奖项数据
        awardsList: [],
        Reason:"",
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
        politicsid:[],
        moralityid:[],
        organid:[],
        lawid:[],
        developmentid:[],
        scientificid:[],
        physicalid:[],
        mentalid:[],
        honoraryid:[],
        competitionid:[],
        socialWorkid:[],
        knowlid:[],
        ideologyid:[],
        /*
       * 各个小点的成绩数组
       * */
        politicsScore2:[],
        ideologyScore2: [],
        moralityScore2: [],
        organScore2: [],
        lawScore2: [],
        learningScore2: [],
        developmentScore2: [],
        scientificScore2: [],
        physicalScore2: [],
        mentalScore2: [],
        honoraryScore2: [],
        competitionScore2: [],
        socialWorkScore2: [],
        knowlScore2: [],
        dailyScore2: [],
        achievementScore2: [],
        dailyScore2id:'',
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
        innerVisible: false,

        innerVisiblePolitics: false,
        innerVisibleIdeology: false,
        innerVisibleMorality: false,
        innerVisibleOrgan: false,
        innerVisibleLaw: false,
        innerVisibleLearning: false,
        innerVisibleDevelopment: false,
        innerVisibleScientific: false,
        innerVisiblePhysical: false,
        innerVisibleMental: false,
        innerVisibleHonorary: false,
        innerVisibleCompetition: false,
        innerVisibleSocialWork: false,
        innerVisibleKnowl: false,
        innerVisibleDaily: false,

        tupianid:"",

      };
    },
    created() {
      this.getList();
    },
    methods: {
      /** 查询用户列表 */
      getList() {
        this.loading = true;
        const major = this.$store.state.user.major;
        const classId = this.$store.state.user.classname;
        this.queryParams.major=major
        this.queryParams.classId=classId
        TestUser(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.testList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
        this.gly = Number(this.$store.state.user.name);
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
      handleBack(id,no,reason) {
        backUser(id,no,reason);
        this.getList();
        this.reason=''; /*驳回后重置驳回原因*/
      },

      /**
       * 批量通过按钮
       * @param row
       */
      handleAccess(row) {
        const nos = row.no || this.nos;
        let operator =Number(this.$store.state.user.name)
        let zifupingjie = nos.toString();
        accessUser(zifupingjie,operator)
        this.getList();
      },

      /**
       * 通过按钮
       * @param no 学生学号
       */
      handleAccess2(no){
        var testid = this.$store.state.user.name;
        accessUser2(no,Number(testid));
        this.updateAllScore();
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
      handleGetAwards(no){
        this.resetAwards();
        this.resetScore();
        getAwards(no).then(response => {
          this.awardsList = response;
          console.log(response)
          for(var i = 0; i < this.awardsList.length; i++){
            switch (this.awardsList[i].module){
              case "politics":
                this.politicsid.push(this.awardsList[i].id)
                this.politicsScore2.push(this.awardsList[i])
                break;
              case "morality":
                this.moralityid.push(this.awardsList[i].id)
                this.moralityScore2.push(this.awardsList[i])
                break;
              case "organ":
                this.organScore2.push(this.awardsList[i])
                this.organid.push(this.awardsList[i].id)

                break;

              case "law":
                this.lawScore2.push(this.awardsList[i])
                this.lawid.push(this.awardsList[i].id)

                break;
              case "learning":
                this.learningScore2.push(this.awardsList[i])

                break;
              case "development":
                this.developmentScore2.push(this.awardsList[i])
                this.developmentid.push(this.awardsList[i].id)

                break;
              case "scientific":
                this.scientificScore2.push(this.awardsList[i])
                this.scientificid.push(this.awardsList[i].id)

                break;
              case "physical":
                this.physicalScore2.push(this.awardsList[i])
                this.physicalid.push(this.awardsList[i].id)

                break;
              case "mental":
                this.mentalScore2.push(this.awardsList[i])
                this.mentalid.push(this.awardsList[i].id)
                break;
              case "honorary":
                this.honoraryScore2.push(this.awardsList[i])
                this.honoraryid.push(this.awardsList[i].id)

                break;
              case "competition":
                this.competitionScore2.push(this.awardsList[i])
                this.competitionid.push(this.awardsList[i].id)

                break;
              case "socialWork":
                this.socialWorkScore2.push(this.awardsList[i])
                this.socialWorkid.push(this.awardsList[i].id)


                break;
              case "knowl":
                this.knowlScore2.push(this.awardsList[i])
                this.knowlid.push(this.awardsList[i].id)

                break;
              case "daily":
                this.dailyScore2.push(this.awardsList[i])
                this.dailyScore2id=this.awardsList[i].id

                break;
              case "ideology":
                this.ideologyScore2.push(this.awardsList[i])
                this.ideologyid.push(this.awardsList[i].id)
                break;
              case "achievement":
                this.achievement = this.awardsList[i].detail;
                this.score =this.awardsList[i].score/100;
                this.awardsList[i].id='';
                console.log(this.score)
                console.log(this.awardsList[i].score)
                console.log(this.achievement)
                break;
            }


          }

          this.updateAllPointScore()



          let result = this.awardsList.map(current => {return {id: current.id}});
          for(let i=result.length-1;i>=0;i--){
            if(result[i].id==""){
              //   console.log(i)
              result.splice(i,1)
            }
          }
          console.log(this.developmentid)

          // console.log(result)
          this.img=result

          /**
           * 计算总评成绩
           * @type {number}
           */
          var totalscore=0;
          var total = 0;
          for (var i = 0; i < this.awardsList.length; i++) {
            total += this.awardsList[i].score
          }
          console.log(this.score)
          this.totalscore = (((total-this.score*100)*0.15)/100 + (this.score*0.85)).toFixed(2)
        })

      },
      /*
            * 修改分数为个位数
            * */
      updateAllPointScore(){
        if (this.organScore2.length>0){
          for (let j = 0; j < this.organScore2.length; j++) {
            this.organScore2[j].score = this.organScore2[j].score/100
            console.log(this.organScore2[j].score)
          }
        }
        if(this.politicsScore2.length>0){
          for (let j = 0; j < this.politicsScore2.length; j++) {
            this.politicsScore2[j].score = this.politicsScore2[j].score/100
          }
        }
        if(this.ideologyScore2.length>0){
          for (let j = 0; j < this.ideologyScore2.length; j++) {
            this.ideologyScore2[j].score = this.ideologyScore2[j].score/100
          }
        }
        if(this.moralityScore2.length>0){
          for (let j = 0; j < this.moralityScore2.length; j++) {
            this.moralityScore2[j].score = this.moralityScore2[j].score/100
          }
        }
        if(this.lawScore2.length>0){
          for (let j = 0; j < this.lawScore2.length; j++) {
            this.lawScore2[j].score = this.lawScore2[j].score/100
          }
        }

        if(this.developmentScore2.length>0){
          for (let j = 0; j < this.developmentScore2.length; j++) {
            this.developmentScore2[j].score = this.developmentScore2[j].score/100
          }
        }
        if(this.scientificScore2.length>0){
          for (let j = 0; j < this.scientificScore2.length; j++) {
            this.scientificScore2[j].score = this.scientificScore2[j].score/100
          }
        }
        if(this.mentalScore2.length>0){
          for (let j = 0; j < this.mentalScore2.length; j++) {
            this.mentalScore2[j].score = this.mentalScore2[j].score/100
          }
        }
        if(this.honoraryScore2.length>0){
          for (let j = 0; j < this.honoraryScore2.length; j++) {
            this.honoraryScore2[j].score = this.honoraryScore2[j].score/100
          }
        }
        if(this.competitionScore2.length>0){
          for (let j = 0; j < this.competitionScore2.length; j++) {
            this.competitionScore2[j].score = this.competitionScore2[j].score/100
          }
        }
        if(this.socialWorkScore2.length>0){
          for (let j = 0; j < this.socialWorkScore2.length; j++) {
            this.socialWorkScore2[j].score = this.socialWorkScore2[j].score/100
          }
        }
        if(this.knowlScore2.length>0){
          for (let j = 0; j < this.knowlScore2.length; j++) {
            this.knowlScore2[j].score = this.knowlScore2[j].score/100
          }
        }
        if(this.dailyScore2.length>0){
          for (let j = 0; j < this.dailyScore2.length; j++) {
            this.dailyScore2[j].score = this.dailyScore2[j].score/100
          }

        }

        if(this.physicalScore2.length>0){
          for (let j = 0; j < this.physicalScore2.length; j++) {
            this.physicalScore2[j].score = this.physicalScore2[j].score/100
          }
        }


      },
      //编辑
      handleEdit(row){
        this.handleGetAwards(row.no);
        this.form = Object.assign({},row)
        //this.dialogFormVisible = true
        this.outerVisible = true
      },

      //获取所有修改后或者没有修改的成绩
      updateAllScore(){

        if(this.politicsScore2.length>0){
          for (let i = 0; i < this.politicsScore2.length; i++) {
            let id = this.politicsScore2[i].id;
            let score = this.politicsScore2[i].score*100;
            updateScore(id,score);
          }
        }

        if(this.organScore2.length>0){
          for (let i = 0; i < this.organScore2.length; i++) {
            updateScore(this.organScore2[i].id,this.organScore2[i].score*100);
          }
        }

        if(this.ideologyScore2.length>0){
          for (let i = 0; i < this.ideologyScore2.length; i++) {
            updateScore(this.ideologyScore2[i].id,this.ideologyScore2[i].score*100);
          }
        }
        if(this.moralityScore2.length>0){
          for (let i = 0; i < this.moralityScore2.length; i++) {
            updateScore(this.moralityScore2[i].id,this.moralityScore2[i].score*100);
          }
        }
        if(this.lawScore2.length>0){
          for (let i = 0; i < this.lawScore2.length; i++) {
            updateScore(this.lawScore2[i].id,this.lawScore2[i].score*100);
          }
        }

        if(this.developmentScore2.length>0){
          for (let i = 0; i < this.developmentScore2.length; i++) {
            updateScore(this.developmentScore2[i].id,this.developmentScore2[i].score*100);
          }
        }
        if(this.scientificScore2.length>0){
          for (let i = 0; i < this.scientificScore2.length; i++) {
            updateScore(this.scientificScore2[i].id,this.scientificScore2[i].score*100);
          }
        }
        if(this.mentalScore2.length>0){
          for (let i = 0; i < this.mentalScore2.length; i++) {
            updateScore(this.mentalScore2[i].id,this.mentalScore2[i].score*100);
          }
        }
        if(this.honoraryScore2.length>0){
          for (let i = 0; i < this.honoraryScore2.length; i++) {
            updateScore(this.honoraryScore2[i].id,this.honoraryScore2[i].score*100);
          }
        }
        if(this.competitionScore2.length>0){
          for (let i = 0; i < this.competitionScore2.length; i++) {
            updateScore(this.competitionScore2[i].id,this.competitionScore2[i].score*100);
          }
        }
        if(this.socialWorkScore2.length>0){
          for (let i = 0; i < this.socialWorkScore2.length; i++) {
            updateScore(this.socialWorkScore2[i].id,this.socialWorkScore2[i].score*100);
          }
        }
        if(this.knowlScore2.length>0){
          for (let i = 0; i < this.knowlScore2.length; i++) {
            updateScore(this.knowlScore2[i].id,this.knowlScore2[i].score*100);
          }
        }
        if(this.dailyScore2.length>0){
          for (let i = 0; i < this.dailyScore2.length; i++) {
            updateScore(this.dailyScore2id,this.dailyScore2[i].score*100);
          }

        }

        if(this.physicalScore2.length>0){
          for (let i = 0; i < this.physicalScore2.length; i++) {
            updateScore(this.physicalScore2[i].id,this.physicalScore2[i].score*100);
          }
        }
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
        this.imgid = "";
        this.score1 = "";
        this.totalscore = "";
        this.developmentid=[];
        this.competitionid=[];
        this.scientificid=[];
        this.socialWorkid=[];
        this.physicalid=[];
        this.ideologyid=[];
        this.knowlid=[];
        this.politicsid=[];
        this.organid=[];
        this.lawid=[];
        this.mentalid=[];
        this.moralityid=[];
        this.honoraryid=[];
        this.politicsScore2=[];
        this.ideologyScore2=[];
        this.moralityScore2=[];
        this.organScore2=[];
        this.lawScore2=[];
        this.learningScore2=[];
        this.developmentScore2= [];
        this.scientificScore=[];
        this.physicalScore2=[];
        this.mentalScore2=[];
        this.honoraryScore2= [];
        this.competitionScore2= [];
        this.socialWorkScore2= [];
        this.knowlScore2= [];
        this.dailyScore2= [];
        this.achievementScore2=[];
        this.scientificScore2=[];
        this.dailyScore2id='';
      },

      /*
      * 重置成绩
      * */
      resetScore(){
        this.politicsScore=[],
          this.ideologyScore= [],
          this.moralityScore= [],
          this.organScore= [],
          this.lawScore= [],
          this.learningScore= [],
          this.developmentScore= [],
          this.scientificScore= [],
          this.physicalScore= [],
          this.mentalScore= [],
          this.honoraryScore= [],
          this.competitionScore= [],
          this.socialWorkScore= [],
          this.knowlScore= [],
          this.dailyScore= [],
          this.achievementScore= [],
          this.politicsScore1=0,
          this.ideologyScore1= 0,
          this.moralityScore1= 0,
          this.organScore1= 0,
          this.lawScore1= 0,
          this.learningScore1= 0,
          this.developmentScore1= 0,
          this.scientificScore1= 0,
          this.physicalScore1= 0,
          this.mentalScore1= 0,
          this.honoraryScore1= 0,
          this.competitionScore1= 0,
          this.socialWorkScore1= 0,
          this.knowlScore1= 0,
          this.dailyScore1= 0,
          this.achievementScore1= 0
      },
      /*
      *所有小点的驳回按钮
      * */

      doublePolitics(no,Reason,openId){
        var testid = this.$store.state.user.name;
        var id = this.tupianid
        this.handleBack(id,no,Reason,Number(testid))
        this.handlePush(openId,Reason)
        this.handleGetAwards(no);
        this.Reason=''
        this.tupianid=''
        this.innerVisiblePolitics = false;

      },

      doubleIdeology(no,Reason,openId){
        var testid = this.$store.state.user.name;
        var id = this.tupianid
        this.handleBack(id,no,Reason,Number(testid))
        this.handlePush(openId,Reason)
        this.handleGetAwards(no);
        this.Reason=''
        this.tupianid=''
        this.innerVisibleIdeology = false;

      },
      doubleMorality(no,Reason,openId){
        var testid = this.$store.state.user.name;
        var id = this.tupianid
        this.handleBack(id,no,Reason,Number(testid))
        this.handlePush(openId,Reason)
        this.handleGetAwards(no);
        this.Reason=''
        this.tupianid=''
        this.innerVisibleMorality = false;

      },
      doubleOrgan(no,Reason,openId){
        var testid = this.$store.state.user.name;
        var id = this.tupianid
        this.handleBack(id,no,Reason,Number(testid))
        this.handlePush(openId,Reason)
        this.handleGetAwards(no);
        this.Reason=''
        this.tupianid=''
        this.innerVisibleOrgan = false;

      },

      doubleLaw(no,Reason,openId){
        var testid = this.$store.state.user.name;
        var id = this.tupianid
        this.handleBack(id,no,Reason,Number(testid))
        this.handlePush(openId,Reason)
        this.handleGetAwards(no);
        this.Reason=''
        this.tupianid=''
        this.innerVisibleLaw = false;

      },
      doubleLearning(no,Reason,openId){
        var testid = this.$store.state.user.name;
        var id = this.tupianid
        this.handleBack(id,no,Reason,Number(testid))
        this.handlePush(openId,Reason)
        this.handleGetAwards(no);
        this.Reason=''
        this.tupianid=''
        this.innerVisibleLearning = false;

      },
      doubleDevelopment(no,Reason,openId){
        var testid = this.$store.state.user.name;
        var id = this.tupianid
        this.handleBack(id,no,Reason,Number(testid))
        this.handlePush(openId,Reason)
        this.handleGetAwards(no);
        this.Reason=''
        this.tupianid=''
        this.innerVisibleDevelopment = false;

      },

      doubleScientific(no,Reason,openId){
        var testid = this.$store.state.user.name;
        var id = this.tupianid
        this.handleBack(id,no,Reason,Number(testid))
        this.handlePush(openId,Reason)
        this.handleGetAwards(no);
        this.Reason=''
        this.tupianid=''

        this.innerVisibleScientific = false;
      },

      doublePhysical(no,Reason,openId){
        var testid = this.$store.state.user.name;
        var id = this.tupianid
        this.handleBack(id,no,Reason,Number(testid))
        this.handlePush(openId,Reason)
        this.handleGetAwards(no);
        this.Reason=''
        this.tupianid=''
        this.innerVisiblePhysical = false;

      },
      doubleMental(no,Reason,openId){
        var testid = this.$store.state.user.name;
        var id = this.tupianid
        this.handleBack(id,no,Reason,Number(testid))
        this.handlePush(openId,Reason)
        this.handleGetAwards(no);
        this.Reason=''
        this.tupianid=''
        this.innerVisibleMental = false;

      },
      doubleHonorary(no,Reason,openId){
        var testid = this.$store.state.user.name;
        var id = this.tupianid
        this.handleBack(id,no,Reason,Number(testid))
        this.handlePush(openId,Reason)
        this.handleGetAwards(no);
        this.Reason=''
        this.tupianid=''
        this.innerVisibleHonorary = false;

      },
      doubleCompetition(no,Reason,openId){
        var testid = this.$store.state.user.name;
        var id = this.tupianid
        this.handleBack(id,no,Reason,Number(testid))
        this.handlePush(openId,Reason)
        this.handleGetAwards(no);
        this.Reason=''
        this.tupianid=''
        this.innerVisibleCompetition = false;

      },
      doubleSocialWork(no,Reason,openId){
        var testid = this.$store.state.user.name;
        var id = this.tupianid
        this.handleBack(id,no,Reason,Number(testid))
        this.handlePush(openId,Reason)
        this.handleGetAwards(no);
        this.Reason=''
        this.tupianid=''
        this.innerVisibleSocialWork = false;

      },
      doubleKnowl(no,Reason,openId){
        var testid = this.$store.state.user.name;
        var id = this.tupianid
        this.handleBack(id,no,Reason,Number(testid))
        this.handlePush(openId,Reason)
        this.handleGetAwards(no);
        this.Reason=''
        this.tupianid=''
        this.innerVisibleKnowl = false;

      },
      doubleDaily(no,Reason,openId){
        var testid = this.$store.state.user.name;
        var id = this.tupianid
        this.handleBack(id,no,Reason,Number(testid))
        this.handlePush(openId,Reason)
        this.handleGetAwards(no);
        this.Reason=''
        this.tupianid=''
        this.innerVisibleDaily = false;

      },

      /*
      * 弹开窗口
      * */
      showTest1(id){
        this.tupianid = id;
        this.innerVisiblePolitics =  true
      },
      showTest2(id){
        this.tupianid = id;
        this.innerVisibleIdeology = true
      },
      showTest3(id){
        this.tupianid = id;
        this.innerVisibleMorality = true
      },
      showTest4(id){
        this.tupianid = id;
        this.innerVisibleOrgan = true
      },
      showTest5(id){
        this.tupianid = id;
        this.innerVisibleLaw = true
      },
      showTest6(id){
        this.tupianid = id;
        this.innerVisibleLearning = true
      },
      showTest7(id){
        this.tupianid = id;
        this.innerVisibleDevelopment = true
      },
      showTest8(id){
        this.tupianid = id;
        this.innerVisibleScientific = true
      },
      showTest9(id){
        this.tupianid = id;
        this.innerVisiblePhysical = true
      },
      showTest10(id){
        this.tupianid = id;
        this.innerVisibleMental = true
      },
      showTest11(id){
        this.tupianid = id;
        this.innerVisibleHonorary = true
      },
      showTest12(id){
        this.tupianid = id;
        this.innerVisibleCompetition = true
      },
      showTest13(id){
        this.tupianid = id;
        this.innerVisibleSocialWork = true
      },
      showTest14(id){
        this.tupianid = id;
        this.innerVisibleKnowl = true
      },
      showTest15(id){
        this.tupianid = id;
        this.innerVisibleDaily = true
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
      //选班级态按钮
      selectClass(classValue){
        console.log(classValue)
      },

      //选择状态按钮
      selectGrade(gradeValue){
        console.log(gradeValue)
      },
      /*
      *推送功能
      */
      handlePush(openId,Reason){
        pushmsg(openId,Reason).then(response=>{
          console.log(response)
        })
      },
    },
    //截取逗号
    splitJoin(res){
      return res.split(',');

    },
    components: { EditTable }
  };
</script>
