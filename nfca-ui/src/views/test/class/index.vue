<template>

  <div class="app-container">
    <el-row :gutter="20">
      <!--班级学生数据-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryParams" size="small" :rules="rules" :inline="true" v-show="showSearch" class="formMassage" label-width="68px">
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

          <el-form-item  prop="status" label="状态">
            <el-select v-model="queryParams.status" clearable placeholder="请选择" @change="selectStatus(value)" >
              <el-option @click.native="handleQuery('queryParams')"
                         v-for="item in options"
                         :key="item.value"
                         :label="item.label"
                         :value="item.value"
                         @keyup.enter.native="handleQuery"
              >
              </el-option>
            </el-select>
          </el-form-item>


          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery('queryParams')">搜索</el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="warning" icon="el-icon-refresh" size="mini" @click="reset">重置</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8" style="display: inline-block">
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
        <el-row :gutter="10" class="mb8" style="display: inline-block; margin-left: 20px">
          <el-col :span="1.5">
            <el-button
              type="warning"
              icon="el-icon-download"
              size="mini"
              @click="handleExport"
            >导出</el-button>
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
          <el-row >
            <el-form-item label="1、政治素质">
              <template v-if="this.politicsScore2.length > 0">
                <div style="display: inline-block;width: 45%">
                  <div v-for="(item,index) in this.politicsScore2" style="display: inline; vertical-align: middle ">
                    <el-input v-model="politicsScore2[index].title" autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"></el-input>
                    <el-input-number v-model="politicsScore2[index].score" :precision="1" :step="0.1"  size="mini" style="width:100px; vertical-align: middle "></el-input-number>
                    <!--<el-button type="warning" slot="footer" size="mini" style="margin-left: 5px" @click="politicsdialogVisible = true">驳回</el-button>-->
                    <el-dialog
                      width="50%"
                      title="驳回理由"
                      :visible.sync="innerVisiblePolitics"
                      append-to-body>
                      <el-input
                        type="textarea"
                        placeholder="请输入内容"
                        v-model="Reason"
                        maxlength="100"
                        show-word-limit
                      >
                      </el-input>
                      <div slot="footer" class="dialog-footer" >
                        <el-button @click="innerVisiblePolitics = false" type="info">取消</el-button>
                        <el-popconfirm
                          style="margin-left: 5px"
                          confirm-button-text='确定咯'
                          cancel-button-text='取消'
                          icon="el-icon-info"
                          icon-color="red"
                          title="您确定要驳回吗？"
                          @confirm="doublePolitics(politicsScore2[index].id,form.no,Reason)"
                        >
                          <el-button  type="danger" slot="reference">确定</el-button>
                        </el-popconfirm>
                      </div>
                    </el-dialog>
                    <div slot="footer" class="dialog-footer" style="display: inline;  vertical-align: middle" >
                      <el-button type="warning" size="mini" style="margin-left: 5px" @click="innerVisiblePolitics = true">驳回</el-button>
                    </div>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle ">{{politicsScore2[index].operator}}</el-button>

                  </div>
                </div>
                <div v-for="(imgs, index) in politicsid" v-viewer="{movable:true}" style="display: inline;  vertical-align: middle">
                  <ul id="yihangxianshiduotu" style="display: inline">
                    <li style="display: inline"><el-image z-index="3000" :fit="fit" :src="'https://static-nfuca-1302505692.cos-website.ap-guangzhou.myqcloud.com/img/upload/raw/' + politicsid[index]+'.jpg'" style="width:200px;height:150px;"/></li>
                  </ul>
                </div>
              </template>
              <template v-else>
                <el-input autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                <el-input-number :precision="1" :step="0.1" :disabled="true" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>

              </template>
            </el-form-item>
          </el-row>

          <el-row >
            <el-form-item label="2、思想素质">
              <template v-if="this.ideologyScore2.length > 0">
                <div style="display: inline-block;width: 45%">
                  <div v-for="(item,index) in this.ideologyScore2">
                    <el-input v-model="ideologyScore2[index].title" autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                    <el-input-number v-model="ideologyScore2[index].score" :precision="1" :step="0.1" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
                    <el-dialog
                      width="50%"
                      title="驳回理由"
                      :visible.sync="innerVisibleIdeology"
                      append-to-body>

                      <el-input
                        type="textarea"
                        placeholder="请输入内容"
                        v-model="Reason"
                        maxlength="100"
                        show-word-limit
                      >
                      </el-input>
                      <div slot="footer" class="dialog-footer" >
                        <el-button @click="innerVisibleIdeology = false" type="info">取消</el-button>
                        <el-popconfirm
                          style="margin-left: 5px"
                          confirm-button-text='确定咯'
                          cancel-button-text='取消'
                          icon="el-icon-info"
                          icon-color="red"
                          title="您确定要驳回吗？"
                          @confirm="doubleIdeology(ideologyScore2[index].id,form.no,Reason)"
                        >
                          <el-button  type="danger" slot="reference">确定</el-button>
                        </el-popconfirm>

                      </div>
                    </el-dialog>
                    <div slot="footer" class="dialog-footer" style="display: inline;  vertical-align: middle" >
                      <el-button type="warning" size="mini" style="margin-left: 5px" @click="innerVisibleIdeology = true">驳回</el-button>
                    </div>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle ">{{ideologyScore2[index].operator}}</el-button>

                  </div>
                </div>
                <div v-for="(imgs, index) in ideologyid" v-viewer="{movable:true}" style="display: inline;vertical-align: middle">
                  <ul id="yihangxianshiduotu">
                    <li><el-image  z-index="3000" :fit="fit" :src="'https://static-nfuca-1302505692.cos-website.ap-guangzhou.myqcloud.com/img/upload/raw/' + ideologyid[index]+'.jpg'" style="width:200px;height:150px;"/></li>
                  </ul>
                </div>
              </template>
              <template v-else>
                <el-input autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                <el-input-number :precision="1" :step="0.1" :disabled="true" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
              </template>
            </el-form-item>
          </el-row>

          <el-row >
            <el-form-item  label="3、道德素质">
              <template v-if="this.moralityScore2.length > 0">
                <div style="display: inline-block;width: 45%">
                  <div v-for="(item,index) in this.moralityScore2">
                    <el-input v-model="moralityScore2[index].title" autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                    <el-input-number v-model="moralityScore2[index].score" :precision="1" :step="0.1" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
                    <el-dialog
                      width="50%"
                      title="驳回理由"
                      :visible.sync="innerVisibleMorality"
                      append-to-body>

                      <el-input
                        type="textarea"
                        placeholder="请输入内容"
                        v-model="Reason"
                        maxlength="100"
                        show-word-limit
                      >
                      </el-input>
                      <div slot="footer" class="dialog-footer" >
                        <el-button @click="innerVisibleMorality = false" type="info">取消</el-button>
                        <el-popconfirm
                          style="margin-left: 5px"
                          confirm-button-text='确定咯'
                          cancel-button-text='取消'
                          icon="el-icon-info"
                          icon-color="red"
                          title="您确定要驳回吗？"
                          @confirm="doubleMorality(moralityScore2[index].id,form.no,Reason)"
                        >
                          <el-button  type="danger" slot="reference">确定</el-button>
                        </el-popconfirm>

                      </div>
                    </el-dialog>
                    <div slot="footer" class="dialog-footer" style="display: inline;  vertical-align: middle" >
                      <el-button type="warning" size="mini" style="margin-left: 5px" @click="innerVisibleMorality = true">驳回</el-button>
                    </div>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle ">{{moralityScore2[index].operator}}</el-button>

                  </div>
                </div>
                <div v-for="(imgs, index) in moralityid" v-viewer="{movable:true}" style="display: inline;vertical-align: middle">
                  <ul id="yihangxianshiduotu">
                    <li><el-image  z-index="3000" :fit="fit" :src="'https://static-nfuca-1302505692.cos-website.ap-guangzhou.myqcloud.com/img/upload/raw/' + ideologyid[index]+'.jpg'" style="width:200px;height:150px;"/></li>
                  </ul>
                </div>
              </template>
              <template v-else>
                <el-input autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                <el-input-number :precision="1" :step="0.1" :disabled="true" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>

              </template>
            </el-form-item>
          </el-row>

          <el-row >
            <el-form-item  label="4、组织素质">
              <template v-if="this.organScore2.length > 0" >
                <div style="display: inline-block;width: 45%">
                  <div v-for="(item,index) in this.organScore2">
                    <el-input v-model="organScore2[index].title" autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                    <el-input-number v-model="organScore2[index].score" :precision="1" :step="0.1" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
                    <el-dialog
                      width="50%"
                      title="驳回理由"
                      :visible.sync="innerVisibleOrgan"
                      append-to-body>
                      <el-input
                        type="textarea"
                        placeholder="请输入内容"
                        v-model="Reason"
                        maxlength="100"
                        show-word-limit
                      >
                      </el-input>
                      <div slot="footer" class="dialog-footer" >
                        <el-button @click="innerVisibleOrgan = false" type="info">取消</el-button>
                        <el-popconfirm
                          style="margin-left: 5px"
                          confirm-button-text='确定咯'
                          cancel-button-text='取消'
                          icon="el-icon-info"
                          icon-color="red"
                          title="您确定要驳回吗？"
                          @confirm="doubleOrgan(organScore2[index].id,form.no,Reason)"
                        >
                          <el-button  type="danger" slot="reference">确定</el-button>
                        </el-popconfirm>

                      </div>
                    </el-dialog>
                    <div slot="footer" class="dialog-footer" style="display: inline;  vertical-align: middle" >
                      <el-button type="warning" size="mini" style="margin-left: 5px" @click="innerVisibleOrgan = true">驳回</el-button>
                    </div>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle ">{{organScore2[index].operator}}</el-button>
                  </div>
                </div>
                <div v-for="(imgs, index) in organid" v-viewer="{movable:true}" style="display: inline;vertical-align: middle">
                  <ul id="yihangxianshiduotu">
                    <li>
                    <el-image z-index="3000"  v-viewer="{movable:true}" :fit="fit" :src="'https://static-nfuca-1302505692.cos-website.ap-guangzhou.myqcloud.com/img/upload/raw/' + organScore2[index].id+'.jpg'" style="width:200px;height:150px;"/>
                    </li>
                  </ul>
                </div>
              </template>
              <template v-else>
                <el-input autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                <el-input-number :precision="1" :step="0.1" :disabled="true" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>

              </template>
            </el-form-item>
          </el-row>

          <el-row >
            <el-form-item label="5、法纪素质">
              <template v-if="this.lawScore2.length > 0">
                <div style="display: inline-block;width: 45%">
                  <div v-for="(item,index) in this.lawScore2" >
                    <el-input v-model="lawScore2[index].title" autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                    <el-input-number v-model="lawScore2[index].score" :precision="1" :step="0.1" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
                    <el-dialog
                      width="50%"
                      title="驳回理由"
                      :visible.sync="innerVisibleLaw"
                      append-to-body>

                      <el-input
                        type="textarea"
                        placeholder="请输入内容"
                        v-model="Reason"
                        maxlength="100"
                        show-word-limit
                      >
                      </el-input>
                      <div slot="footer" class="dialog-footer" >
                        <el-button @click="innerVisibleLaw = false" type="info">取消</el-button>
                        <el-popconfirm
                          style="margin-left: 5px"
                          confirm-button-text='确定咯'
                          cancel-button-text='取消'
                          icon="el-icon-info"
                          icon-color="red"
                          title="您确定要驳回吗？"
                          @confirm="doubleLaw(lawScore2[index].id,form.no,Reason)"
                        >
                          <el-button  type="danger" slot="reference">确定</el-button>
                        </el-popconfirm>

                      </div>
                    </el-dialog>
                    <div slot="footer" class="dialog-footer" style="display: inline;  vertical-align: middle" >
                      <el-button type="warning" size="mini" style="margin-left: 5px" @click="innerVisibleLaw = true">驳回</el-button>
                    </div>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle ">{{lawScore2[index].operator}}</el-button>

                  </div>
                </div>
                <div v-for="(imgs, index) in lawid" v-viewer="{movable:true}" style="display: inline;vertical-align: middle">
                  <ul id="yihangxianshiduotu">
                    <li><el-image z-index="3000"  :fit="fit" :src="'https://static-nfuca-1302505692.cos-website.ap-guangzhou.myqcloud.com/img/upload/raw/' + lawid[index]+'.jpg'" style="width:200px;height:150px;"/></li>
                  </ul>
                </div>
              </template>
              <template v-else>
                <el-input autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                <el-input-number :precision="1" :step="0.1" :disabled="true" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>

              </template>
            </el-form-item>
          </el-row>

          <el-row >
            <el-form-item label="6、学习与生活素质" >
              <template v-if="this.learningScore2.length > 0">
                <div style="display: inline-block;width: 45%">
                  <div v-for="(item,index) in this.learningScore2" >
                    <el-input v-model="learningScore2[index].title" autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                    <el-input-number v-model="learningScore2[index].score" :precision="1" :step="0.1" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
                    <el-dialog
                      width="50%"
                      title="驳回理由"
                      :visible.sync="innerVisibleLearning"
                      append-to-body>

                      <el-input
                        type="textarea"
                        placeholder="请输入内容"
                        v-model="Reason"
                        maxlength="100"
                        show-word-limit
                      >
                      </el-input>
                      <div slot="footer" class="dialog-footer" >
                        <el-button @click="innerVisibleLearning = false" type="info">取消</el-button>
                        <el-popconfirm
                          style="margin-left: 5px"
                          confirm-button-text='确定咯'
                          cancel-button-text='取消'
                          icon="el-icon-info"
                          icon-color="red"
                          title="您确定要驳回吗？"
                          @confirm="doubleLearning(learningScore2[index].id,form.no,Reason)"
                        >
                          <el-button  type="danger" slot="reference">确定</el-button>
                        </el-popconfirm>

                      </div>
                    </el-dialog>
                    <div slot="footer" class="dialog-footer" style="display: inline;  vertical-align: middle" >
                      <el-button type="warning" size="mini" style="margin-left: 5px" @click="innerVisibleLearning = true">驳回</el-button>
                    </div>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle ">{{learningScore2[index].operator}}</el-button>

                  </div>
                </div>
              </template>
              <template v-else>
                <el-input autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                <el-input-number :precision="1" :step="0.1" :disabled="true" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>

              </template>
            </el-form-item>
          </el-row>

          <el-row >
            <el-form-item label="7、实践与创新素质">
              <template v-if="this.developmentScore2.length > 0">
                <div style="display: inline-block;width: 45%">
                  <div v-for="(item,index) in this.developmentScore2" style="margin-top: 10px">
                    <el-input v-model="developmentScore2[index].title" autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"></el-input>
                    <el-input-number v-model="developmentScore2[index].score" :precision="1" :step="0.1" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
                    <el-dialog
                      width="50%"
                      title="驳回理由"
                      :visible.sync="innerVisibleDevelopment"
                      append-to-body>

                      <el-input
                        type="textarea"
                        placeholder="请输入内容"
                        v-model="Reason"
                        maxlength="100"
                        show-word-limit
                      >
                      </el-input>
                      <div slot="footer" class="dialog-footer" >
                        <el-button @click="innerVisibleDevelopment = false" type="info">取消</el-button>
                        <el-popconfirm
                          style="margin-left: 5px"
                          confirm-button-text='确定咯'
                          cancel-button-text='取消'
                          icon="el-icon-info"
                          icon-color="red"
                          title="您确定要驳回吗？"
                          @confirm="doubleDevelopment(developmentScore2[index].id,form.no,Reason)"
                        >
                          <el-button  type="danger" slot="reference">确定</el-button>
                        </el-popconfirm>

                      </div>
                    </el-dialog>
                    <div slot="footer" class="dialog-footer" style="display: inline;  vertical-align: middle" >
                      <el-button type="warning" size="mini" style="margin-left: 5px" @click="innerVisibleDevelopment = true">驳回</el-button>
                    </div>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle ">{{developmentScore2[index].operator}}</el-button>

                  </div>
                </div>
                <div v-for="(imgs, index) in developmentid" v-viewer="{movable:true}" style="display: inline;vertical-align: middle">
                  <ul id="yihangxianshiduotu">
                    <li><el-image z-index="3000"  :fit="fit" :src="'https://static-nfuca-1302505692.cos-website.ap-guangzhou.myqcloud.com/img/upload/raw/' + developmentid[index]+'.jpg'" style="width:200px;height:150px;"/></li>
                  </ul>
                </div>
              </template>
              <template v-else>
                <el-input autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                <el-input-number :precision="1" :step="0.1" :disabled="true" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>

              </template>
            </el-form-item>
          </el-row>

          <el-row >
            <el-form-item label="8、科学文化素质">
              <template v-if="this.scientificScore2.length > 0">
                <div style="display: inline-block;width: 45%">
                  <div v-for="(item,index) in this.scientificScore2" >
                    <el-input v-model="scientificScore2[index].title" autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                    <el-input-number v-model="scientificScore2[index].score" :precision="1" :step="0.1" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
                    <el-dialog
                      width="50%"
                      title="驳回理由"
                      :visible.sync="innerVisibleScientific"
                      append-to-body>
                      <el-input
                        type="textarea"
                        placeholder="请输入内容"
                        v-model="Reason"
                        maxlength="100"
                        show-word-limit
                      >
                      </el-input>
                      <div slot="footer" class="dialog-footer" >
                        <el-button @click="innerVisibleScientific = false" type="info">取消</el-button>
                        <el-popconfirm
                          style="margin-left: 5px"
                          confirm-button-text='确定咯'
                          cancel-button-text='取消'
                          icon="el-icon-info"
                          icon-color="red"
                          title="您确定要驳回吗？"
                          @confirm="doubleScientific(scientificScore2[index].id,form.no,Reason)"
                        >
                          <el-button  type="danger" slot="reference">确定</el-button>
                        </el-popconfirm>

                      </div>
                    </el-dialog>
                    <div slot="footer" class="dialog-footer" style="display: inline;  vertical-align: middle" >
                      <el-button type="warning" size="mini" style="margin-left: 5px" @click="innerVisibleScientific = true">驳回</el-button>
                    </div>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle ">{{scientificScore2[index].operator}}</el-button>

                  </div>
                </div>
                <div v-for="(imgs, index) in scientificid" v-viewer="{movable:true}" style="display: inline;vertical-align: middle">
                  <ul id="yihangxianshiduotu">
                    <li><el-image z-index="3000"  :fit="fit" :src="'https://static-nfuca-1302505692.cos-website.ap-guangzhou.myqcloud.com/img/upload/raw/' + scientificid[index]+'.jpg'" style="width:200px;height:150px;"/></li>
                  </ul>
                </div>
              </template>
              <template v-else>
                <el-input autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                <el-input-number :precision="1" :step="0.1" :disabled="true" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>

              </template>
            </el-form-item>
          </el-row>

          <el-row >
            <el-form-item label="9、身体素质">
              <template v-if="this.physicalScore2.length > 0">
                  <div style="display: inline-block;width: 45%">
                  <div v-for="(item,index) in this.physicalScore2" >
                    <el-input v-model="physicalScore2[index].title" autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                    <el-input-number v-model="physicalScore2[index].score" :precision="1" :step="0.1" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
                    <el-dialog
                      width="50%"
                      title="驳回理由"
                      :visible.sync="innerVisiblePhysical"
                      append-to-body>

                      <el-input
                        type="textarea"
                        placeholder="请输入内容"
                        v-model="Reason"
                        maxlength="100"
                        show-word-limit
                      >
                      </el-input>
                      <div slot="footer" class="dialog-footer" >
                        <el-button @click="innerVisiblePhysical = false" type="info">取消</el-button>
                        <el-popconfirm
                          style="margin-left: 5px"
                          confirm-button-text='确定咯'
                          cancel-button-text='取消'
                          icon="el-icon-info"
                          icon-color="red"
                          title="您确定要驳回吗？"
                          @confirm="doublePhysical(physicalScore2[index].id,form.no,Reason)"
                        >
                          <el-button  type="danger" slot="reference">确定</el-button>
                        </el-popconfirm>

                      </div>
                    </el-dialog>
                    <div slot="footer" class="dialog-footer" style="display: inline;  vertical-align: middle" >
                      <el-button type="warning" size="mini" style="margin-left: 5px" @click="innerVisiblePhysical = true">驳回</el-button>
                    </div>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle ">{{physicalScore2[index].operator}}</el-button>

                  </div>
                </div>
                <div v-for="(imgs, index) in physicalid" v-viewer="{movable:true}" style="display: inline;vertical-align: middle">
                  <ul id="yihangxianshiduotu">
                    <li><el-image z-index="3000"  :fit="fit" :src="'https://static-nfuca-1302505692.cos-website.ap-guangzhou.myqcloud.com/img/upload/raw/' + physicalid[index]+'.jpg'" style="width:200px;height:150px;"/></li>
                  </ul>
                </div>
              </template>
              <template v-else>
                <el-input autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                <el-input-number :precision="1" :step="0.1" :disabled="true" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>

              </template>
            </el-form-item>
          </el-row>

          <el-row >
            <el-form-item label="10、心理素质">
              <template v-if="this.mentalScore2.length > 0">
                <div style="display: inline-block;width: 45%">
                  <div v-for="(item,index) in this.mentalScore2" >
                    <el-input v-model="mentalScore2[index].title" autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                    <el-input-number v-model="mentalScore2[index].score" :precision="1" :step="0.1" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
                    <el-dialog
                      width="50%"
                      title="驳回理由"
                      :visible.sync="innerVisibleMental"
                      append-to-body>
                      <el-input
                        type="textarea"
                        placeholder="请输入内容"
                        v-model="Reason"
                        maxlength="100"
                        show-word-limit
                      >
                      </el-input>
                      <div slot="footer" class="dialog-footer" >
                        <el-button @click="innerVisibleMental = false" type="info">取消</el-button>
                        <el-popconfirm
                          style="margin-left: 5px"
                          confirm-button-text='确定咯'
                          cancel-button-text='取消'
                          icon="el-icon-info"
                          icon-color="red"
                          title="您确定要驳回吗？"
                          @confirm="doubleMental(mentalScore2[index].id,form.no,Reason)"
                        >
                          <el-button  type="danger" slot="reference">确定</el-button>
                        </el-popconfirm>
                      </div>
                    </el-dialog>
                    <div slot="footer" class="dialog-footer" style="display: inline;  vertical-align: middle" >
                      <el-button type="warning" size="mini" style="margin-left: 5px" @click="innerVisibleMental = true">驳回</el-button>
                    </div>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle ">{{mentalScore2[index].operator}}</el-button>

                  </div>
                </div>
                <div v-for="(imgs, index) in mentalid" v-viewer="{movable:true}" style="display: inline;vertical-align: middle">
                  <ul id="yihangxianshiduotu">
                    <li><el-image z-index="3000"  :fit="fit" :src="'https://static-nfuca-1302505692.cos-website.ap-guangzhou.myqcloud.com/img/upload/raw/' + mentalid[index]+'.jpg'" style="width:200px;height:150px;"/></li>
                  </ul>
                </div>
              </template>
              <template v-else>
                <el-input autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                <el-input-number :precision="1" :step="0.1" :disabled="true" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>

              </template>
            </el-form-item>
          </el-row>

          <el-descriptions title="发展性素质测评（满分45 分）"></el-descriptions>


          <el-row>
            <el-form-item label="11、荣誉称号加分">
              <template v-if="this.honoraryScore2.length > 0">
                <div style="display: inline-block;width: 45%">
                  <div v-for="(item,index) in this.honoraryScore2" >

                    <el-input v-model="honoraryScore2[index].title" autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                    <el-input-number v-model="honoraryScore2[index].score" :precision="1" :step="0.1" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
                    <el-dialog
                      width="50%"
                      title="驳回理由"
                      :visible.sync="innerVisibleHonorary"
                      append-to-body>
                      <el-input
                        type="textarea"
                        placeholder="请输入内容"
                        v-model="Reason"
                        maxlength="100"
                        show-word-limit
                      >
                      </el-input>
                      <div slot="footer" class="dialog-footer" >
                        <el-button @click="innerVisibleHonorary = false" type="info">取消</el-button>
                        <el-popconfirm
                          style="margin-left: 5px"
                          confirm-button-text='确定咯'
                          cancel-button-text='取消'
                          icon="el-icon-info"
                          icon-color="red"
                          title="您确定要驳回吗？"
                          @confirm="doubleHonorary(honoraryScore2[index].id,form.no,Reason)"
                        >
                          <el-button  type="danger" slot="reference">确定</el-button>
                        </el-popconfirm>

                      </div>
                    </el-dialog>
                    <div slot="footer" class="dialog-footer" style="display: inline;  vertical-align: middle" >
                      <el-button type="warning" size="mini" style="margin-left: 5px" @click="innerVisibleHonorary = true">驳回</el-button>
                    </div>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle ">{{honoraryScore2[index].operator}}</el-button>

                  </div>
                </div>
                <div v-for="(imgs, index) in honoraryid" v-viewer="{movable:true}" style="display: inline;vertical-align: middle">
                  <ul id="yihangxianshiduotu">
                    <li><el-image z-index="3000"  :fit="fit" :src="'https://static-nfuca-1302505692.cos-website.ap-guangzhou.myqcloud.com/img/upload/raw/' + honoraryid[index]+'.jpg'" style="width:200px;height:150px;"/></li>
                  </ul>
                </div>
              </template>
              <template v-else>
                <el-input autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                <el-input-number :precision="1" :step="0.1" :disabled="true" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
              </template>

            </el-form-item>
          </el-row>

          <el-row>
            <el-form-item label="12、文体艺术等竞赛或活动加分">
              <template v-if="this.competitionScore2.length > 0">
                <div style="display: inline-block;width: 45%">
                  <div v-for="(item,index) in this.competitionScore2" >
                    <el-input v-model="competitionScore2[index].title" autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                    <el-input-number v-model="competitionScore2[index].score" :precision="1" :step="0.1" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
                    <el-dialog
                      width="50%"
                      title="驳回理由"
                      :visible.sync="innerVisibleCompetition"
                      append-to-body>
                      <el-input
                        type="textarea"
                        placeholder="请输入内容"
                        v-model="Reason"
                        maxlength="100"
                        show-word-limit
                      >
                      </el-input>
                      <div slot="footer" class="dialog-footer" >
                        <el-button @click="innerVisibleCompetition = false" type="info">取消</el-button>
                        <el-popconfirm
                          style="margin-left: 5px"
                          confirm-button-text='确定咯'
                          cancel-button-text='取消'
                          icon="el-icon-info"
                          icon-color="red"
                          title="您确定要驳回吗？"
                          @confirm="doubleCompetition(competitionScore2[index].id,form.no,Reason)"
                        >
                          <el-button  type="danger" slot="reference">确定</el-button>
                        </el-popconfirm>

                      </div>
                    </el-dialog>
                    <div slot="footer" class="dialog-footer" style="display: inline;  vertical-align: middle" >
                      <el-button type="warning" size="mini" style="margin-left: 5px" @click="innerVisibleCompetition = true">驳回</el-button>
                    </div>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle ">{{competitionScore2[index].operator}}</el-button>

                  </div>
                </div>
                <div v-for="(imgs, index) in competitionid" v-viewer="{movable:true}" style="display: inline;vertical-align: middle">
                  <ul id="yihangxianshiduotu">
                    <li><el-image  z-index="3000" :fit="fit" :src="'https://static-nfuca-1302505692.cos-website.ap-guangzhou.myqcloud.com/img/upload/raw/' + competitionid[index]+'.jpg'" style="width:200px;height:150px;"/></li>
                  </ul>
                </div>
              </template>
              <template v-else>
                <el-input autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                <el-input-number :precision="1" :step="0.1" :disabled="true" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>

              </template>
            </el-form-item>
          </el-row>

          <el-row>
            <el-form-item label="13、社会工作加分">
              <template v-if="this.socialWorkScore2.length > 0">
                <div style="display: inline-block;width: 45%">
                  <div v-for="(item,index) in this.socialWorkScore2" >
                    <el-input v-model="socialWorkScore2[index].title" autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                    <el-input-number v-model="socialWorkScore2[index].score" :precision="1" :step="0.1" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
                    <el-dialog
                      width="50%"
                      title="驳回理由"
                      :visible.sync="innerVisibleSocialWork"
                      append-to-body>

                      <el-input
                        type="textarea"
                        placeholder="请输入内容"
                        v-model="Reason"
                        maxlength="100"
                        show-word-limit
                      >
                      </el-input>
                      <div slot="footer" class="dialog-footer" >
                        <el-button @click="innerVisibleSocialWork = false" type="info">取消</el-button>
                        <el-popconfirm
                          style="margin-left: 5px"
                          confirm-button-text='确定咯'
                          cancel-button-text='取消'
                          icon="el-icon-info"
                          icon-color="red"
                          title="您确定要驳回吗？"
                          @confirm="doubleSocialWork(socialWorkScore2[index].id,form.no,Reason)"
                        >
                          <el-button  type="danger" slot="reference">确定</el-button>
                        </el-popconfirm>

                      </div>
                    </el-dialog>
                    <div slot="footer" class="dialog-footer" style="display: inline;  vertical-align: middle" >
                      <el-button type="warning" size="mini" style="margin-left: 5px" @click="innerVisibleSocialWork = true">驳回</el-button>
                    </div>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle ">{{socialWorkScore2[index].operator}}</el-button>

                  </div>
                </div>
                <div v-for="(imgs, index) in socialWorkid" v-viewer="{movable:true}" style="display: inline;vertical-align: middle">
                  <ul id="yihangxianshiduotu">
                    <li><el-image z-index="3000"  :fit="fit" :src="'https://static-nfuca-1302505692.cos-website.ap-guangzhou.myqcloud.com/img/upload/raw/' + socialWorkid[index]+'.jpg'" style="width:200px;height:150px;"/></li>
                  </ul>
                </div>
              </template>
              <template v-else>
                <el-input autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                <el-input-number :precision="1" :step="0.1" :disabled="true" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>

              </template>
            </el-form-item>
          </el-row>

          <el-row>
            <el-form-item label="14、知识与技能加分">
              <template v-if="this.knowlScore2.length > 0">
                <div style="display: inline-block;width: 45%">
                  <div v-for="(item,index) in this.knowlScore2" >
                    <el-input v-model="knowlScore2[index].title" autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                    <el-input-number v-model="knowlScore2[index].score" :precision="1" :step="0.1" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
                    <el-dialog
                      width="50%"
                      title="驳回理由"
                      :visible.sync="innerVisibleKnowl"
                      append-to-body>
                      <el-input
                        type="textarea"
                        placeholder="请输入内容"
                        v-model="Reason"
                        maxlength="100"
                        show-word-limit
                      >
                      </el-input>
                      <div slot="footer" class="dialog-footer" >
                        <el-button @click="innerVisibleKnowl = false" type="info">取消</el-button>
                        <el-popconfirm
                          style="margin-left: 5px"
                          confirm-button-text='确定咯'
                          cancel-button-text='取消'
                          icon="el-icon-info"
                          icon-color="red"
                          title="您确定要驳回吗？"
                          @confirm="doubleKnowl(knowlScore2[index].id,form.no,Reason)"
                        >
                          <el-button  type="danger" slot="reference">确定</el-button>
                        </el-popconfirm>

                      </div>
                    </el-dialog>
                    <div slot="footer" class="dialog-footer" style="display: inline;  vertical-align: middle" >
                      <el-button type="warning" size="mini" style="margin-left: 5px" @click="innerVisibleKnowl = true">驳回</el-button>
                    </div>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle ">{{knowlScore2[index].operator}}</el-button>

                  </div>
                </div>
                <div v-for="(imgs, index) in knowlid" v-viewer="{movable:true}" style="display: inline;vertical-align: middle">
                  <ul id="yihangxianshiduotu">
                    <li><el-image  z-index="3000" :fit="fit" :src="'https://static-nfuca-1302505692.cos-website.ap-guangzhou.myqcloud.com/img/upload/raw/' + knowlid[index]+'.jpg'" style="width:200px;height:150px;"/></li>
                  </ul>
                </div>
              </template>
              <template v-else>
                <el-input autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                <el-input-number :precision="1" :step="0.1" :disabled="true" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>

              </template>

            </el-form-item>
          </el-row>

          <el-row>
            <el-form-item label="15、日常操行减分" >
              <template v-if="this.dailyScore2.length > 0">
                <div style="display: inline-block;width: 45%">
                  <div v-for="(item,index) in this.dailyScore2" >
                    <el-input v-model="dailyScore2[index].title" autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
                    <el-input-number v-model="dailyScore2[index].score" :precision="1" :step="0.1" :max="6" size="mini" style="width:100px; vertical-align: middle "></el-input-number>
                    <el-dialog
                      width="50%"
                      title="驳回理由"
                      :visible.sync="innerVisibleDaily"
                      append-to-body>
                      <el-input
                        type="textarea"
                        placeholder="请输入内容"
                        v-model="Reason"
                        maxlength="100"
                        show-word-limit
                      >
                      </el-input>
                      <div slot="footer" class="dialog-footer" >
                        <el-button @click="innerVisibleDaily = false" type="info">取消</el-button>
                        <el-popconfirm
                          style="margin-left: 5px"
                          confirm-button-text='确定咯'
                          cancel-button-text='取消'
                          icon="el-icon-info"
                          icon-color="red"
                          title="您确定要驳回吗？"
                          @confirm="doubleDaily(dailyScore2[index].id,form.no,Reason)"
                        >
                          <el-button  type="danger" slot="reference">确定</el-button>
                        </el-popconfirm>
                      </div>
                    </el-dialog>
                    <div slot="footer" class="dialog-footer" style="display: inline;  vertical-align: middle" >
                      <el-button type="warning" size="mini" style="margin-left: 5px" @click="innerVisibleDaily = true">驳回</el-button>
                    </div>
                    <el-button size="mini" style="width:70px;margin-left: 5px; vertical-align: middle ">{{dailyScore2[index].operator}}</el-button>

                  </div>
                </div>
              </template>
              <template v-else>
                <el-input autocomplete="off" type="textarea" :disabled="true" :autosize="{ minRows: 1}" class="inputMsg"/>
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

          <el-row>
            <el-form-item label="17、总评">
              <el-label style="color: red">总评为：{{ this.totalscore }}</el-label>
            </el-form-item>
          </el-row>

        </el-col>



      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="outerVisible = false" type="info">取消</el-button>
        <el-button @click="handleAccess2(form.no,form.id,form.score)" type="success" style="margin: 0 100px 0 50px">通过</el-button>
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
  import { TestUser,  backUser, accessUser, accessUser2, getAwards,updateScore,exportUser} from "@/api/test/class";
  import { getToken } from "@/utils/auth";
  import EditTable from "../../tool/gen/editTable.vue";
  import * as ElementUI from "element-ui";


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
        //用户奖项数据
        awardsList: [],
        //驳回原因
        Reason:"",
        /*
        * 各个小点的名称
        * */
        politics: "", //1、政治素质
        ideology: "", //2、思想素质
        morality: "", //3、道德素质
        organ: "",  //4、组织素质
        law: "",  //5、法纪素质
        learning: "", //6、学习与生活素质
        development: "",  //7、实践与创新素质
        scientific: "", //8、科学文化素质
        physical: "", //9、身体素质
        mental: "", //10、心理素质
        honorary: "", //11、荣誉称号加分
        competition: "",  //12、文体艺术等竞赛或活动加分
        socialWork: "", //13、社会工作加分
        knowl: "",  //14、知识与技能加分
        daily: "",  //15、日常操行减分
        achievement: "",//16、期末成绩
        img:[], //图片
        score:"",//加权平均分
        socre1:"",
        totalscore:"",//总评
        addScore:"",
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
        politicsScore:[],
        ideologyScore: [],
        moralityScore: [],
        organScore: [],
        lawScore: [],
        learningScore: [],
        developmentScore: [],
        scientificScore: [],
        physicalScore: [],
        mentalScore: [],
        honoraryScore: [],
        competitionScore: [],
        socialWorkScore: [],
        knowlScore: [],
        dailyScore: [],
        achievementScore: [],
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
        /*
        * 各个小点的成绩
        * */
        politicsScore1:0,
        ideologyScore1: 0,
        moralityScore1: 0,
        organScore1: 0,
        lawScore1: 0,
        learningScore1: 0,
        developmentScore1: 0,
        scientificScore1: 0,
        physicalScore1: 0,
        mentalScore1: 0,
        honoraryScore1: 0,
        competitionScore1: 0,
        socialWorkScore1: 0,
        knowlScore1: 0,
        dailyScore1: 0,
        achievementScore1: 0,
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
          majorValue:[],
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
      };
    },
    created() {
      this.getList();
    },
    methods: {
      /** 查询用户列表 */
      getList() {
        this.loading = true;
        const classId = this.$store.state.user.classname;
        const major = this.$store.state.user.major;
        this.queryParams.major=major
        this.queryParams.classId=classId
        TestUser(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.testList = response.rows;
          this.total = response.total;
          this.loading = false;
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
       * @param id 唯一标识(图片id)
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
        accessUser(nos)
        this.getList();

      },

      /**
       * 通过按钮
       * @param no 学生学号
       */
      handleAccess2(no){
        accessUser2(no)
        this.updateAllScore();
        this.outerVisible = false
        this.getList()
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
                this.awardsList[i].id='';

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
                this.awardsList[i].id='';

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
        if(this.learningScore2.length>0){
          for (let j = 0; j < this.learningScore2.length; j++) {
            this.learningScore2[j].score = this.learningScore2[j].score/100
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
         if(this.learningScore2.length>0){
           for (let i = 0; i < this.learningScore2.length; i++) {
             updateScore(this.learningScore2[i].id,this.learningScore2[i].score*100);
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
             updateScore(this.dailyScore2[i].id,this.dailyScore2[i].score*100);
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
      doublePolitics(id,no,Reason){
        this.handleBack(id,no,Reason)
        this.Reason=''
        this.innerVisiblePolitics = false;
        this.outerVisible = false;
      },

      doubleIdeology(id,no,Reason){
        this.handleBack(id,no,Reason)
        this.Reason=''
        this.innerVisibleIdeology = false;
        this.outerVisible = false;
      },
      doubleMorality(id,no,Reason){
        this.handleBack(id,no,Reason)
        this.Reason=''
        this.innerVisibleMorality = false;
        this.outerVisible = false;
      },
      doubleOrgan(id,no,Reason){
        this.handleBack(id,no,Reason)
        this.Reason=''
        this.innerVisibleOrgan = false;
        this.outerVisible = false;
      },

      doubleLaw(id,no,Reason){
        this.handleBack(id,no,Reason)
        this.Reason=''
        this.innerVisibleLaw = false;
        this.outerVisible = false;
      },
      doubleLearning(id,no,Reason){
        this.handleBack(id,no,Reason)
        this.Reason=''
        this.innerVisibleLearning = false;
        this.outerVisible = false;
      },
      doubleDevelopment(id,no,Reason){
        this.handleBack(id,no,Reason)
        this.Reason=''
        this.innerVisibleDevelopment = false;
        this.outerVisible = false;
      },
      doubleScientific(id,no,Reason){
        this.handleBack(id,no,Reason)
        this.Reason=''
        this.innerVisibleScientific = false;
        this.outerVisible = false;
      },

      doublePhysical(id,no,Reason){
        this.handleBack(id,no,Reason)
        this.Reason=''
        this.innerVisiblePhysical = false;
        this.outerVisible = false;
      },
      doubleMental(id,no,Reason){
        this.handleBack(id,no,Reason)
        this.Reason=''
        this.innerVisibleMental = false;
        this.outerVisible = false;
      },
      doubleHonorary(id,no,Reason){
        this.handleBack(id,no,Reason)
        this.Reason=''
        this.innerVisibleHonorary = false;
        this.outerVisible = false;
      },
      doubleCompetition(id,no,Reason){
        this.handleBack(id,no,Reason)
        this.Reason=''
        this.innerVisibleCompetition = false;
        this.outerVisible = false;
      },
      doubleSocialWork(id,no,Reason){
        this.handleBack(id,no,Reason)
        this.Reason=''
        this.innerVisibleSocialWork = false;
        this.outerVisible = false;
      },
      doubleKnowl(id,no,Reason){
        this.handleBack(id,no,Reason)
        this.Reason=''
        this.innerVisibleKnowl = false;
        this.outerVisible = false;
      },
      doubleDaily(id,no,Reason){
        this.handleBack(id,no,Reason)
        this.Reason=''
        this.innerVisibleDaily = false;
        this.outerVisible = false;
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
      /*
      * 导出功能
      * */
      handleExport(){
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有用户数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportUser(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
      }
    },

  };


</script>



