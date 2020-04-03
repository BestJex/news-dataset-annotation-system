<template>
  <div>
    <el-row>
      <el-steps :active="activeIndex" simple>
        <el-step title="了解基础" icon="el-icon-s-opportunity"></el-step>
        <el-step title="创建数据集" icon="el-icon-s-promotion"></el-step>
        <el-step title="分配任务" icon="el-icon-s-claim"></el-step>
        <el-step title="模型训练" icon="el-icon-s-flag"></el-step>
        <el-step title="上传结果" icon="el-icon-upload"></el-step>
        <el-step title="成绩评定" icon="el-icon-star-on"></el-step>
      </el-steps>
    </el-row>
    <el-row style="margin: 20px;text-align: center">
      <el-tag style="font-size: 16px">{{ stepIntroduction[activeIndex] }}</el-tag>
    </el-row>
    <div v-if="activeIndex === 0 && activeIndex <= user.machineLearningTaskState">
      <el-row>
        <el-col :xs="3" :sm="6" :md="6">&nbsp;</el-col>
        <el-col :xs="18" :sm="12" :md="12">
          <el-checkbox-group v-model="user.foundationSkill" :max="1" style="margin-left50px;padding: 10px 60px">
            <el-checkbox style="display:block;line-height: 40px;" label="0">
              我是新手
            </el-checkbox>
            <el-checkbox style="display:block;line-height: 40px;" label="1">
              我了解基本的机器学习原理
            </el-checkbox>
            <el-checkbox style="display:block;line-height: 40px;" label="2">
              我进行过机器学习模型的编程
            </el-checkbox>
            <el-checkbox style="display:block;line-height: 40px;" label="3">
              我熟练掌握机器学习原理及编程方法
            </el-checkbox>
          </el-checkbox-group>
        </el-col>
      </el-row>
      <el-row class="center-button" style="margin-top: 100px">
        <el-button @click="submitFoundationSkill"
                   :disabled="user.foundationSkill==null||user.foundationSkill.length===0" type="primary">
          下一步
        </el-button>
      </el-row>
    </div>

    <div v-if="activeIndex === 1 && activeIndex <= user.machineLearningTaskState">
      <el-row style="margin-top: 100px">
        <el-col :xs="3" :sm="7" :md="8">&nbsp;</el-col>
        <el-col :xs="18" :sm="10" :md="8">
          <el-card class="box-card" :body-style="{ padding: '0px' }">
            <div class="card-left-icon">
              <i class="el-icon-s-claim svg-icon"></i>
            </div>
            <div class="card-right-text">
              <div class="card-right-title">待标注任务</div>
              <div class="card-right-data">{{ user.undoTaskCount }}篇</div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-row class="center-button" style="margin-top: 200px">
        <el-button @click="activeIndex<0?null:activeIndex-=1">上一步</el-button>
        <el-button type="primary" @click="$router.push('/annotation/new')">开始标注数据</el-button>
        <el-button type="success" @click="updateMachineLearningTaskState">下一步</el-button>
      </el-row>
    </div>

    <div v-if="activeIndex === 2 && activeIndex <= user.machineLearningTaskState">
      <el-row type="flex" justify="center" :gutter="40" style="padding: 0 100px">
        <div>
          <el-col v-for="(item, index) in machineLearningModelIntroduction" :key="index" :md="8" :xs="24" :sm="8">
            <el-card v-if="index===user.machineLearningModel||user.machineLearningModel===null"
                     :body-style="{ padding: '0px' }"
                     style="margin-bottom: 30px">
              <img
                :src="item.img"
                class="image">
              <div style="padding: 14px;">
                <span>{{ item.title }}</span>
                <div>
                  <span class="text-introduction">
                    {{ item.content }}
                  </span>
                </div>
              </div>
            </el-card>
            <el-card v-else :body-style="{ padding: '0px' }" style="margin-bottom: 30px" class="mask">
              <img
                :src="item.img"
                class="image">
              <div style="padding: 14px;">
                <span>{{ item.title }}</span>
                <div>
                  <span class="text-introduction">
                    {{ item.content }}
                  </span>
                </div>
              </div>
            </el-card>

          </el-col>
        </div>
      </el-row>
      <el-row class="center-button">
        <el-button @click="activeIndex-=1">上一步</el-button>
        <!--        <el-button type="primary" @click="getRandom">随机重置</el-button>-->
        <el-button type="success" icon="el-icon-download" @click="activeIndex+=1">开始训练</el-button>
      </el-row>
    </div>

    <div v-if="activeIndex === 3 && activeIndex <= user.machineLearningTaskState">
      <el-row>
        <el-col :xs="6" :sm="7" :md="9">&nbsp;</el-col>
        <el-col :xs="12" :sm="10" :md="6">
          <el-card :body-style="{ padding: '0px' }">
            <img
              :src="machineLearningModelIntroduction[user.machineLearningModel].img"
              class="image">
            <div style="padding: 14px;">
              <span>{{ machineLearningModelIntroduction[user.machineLearningModel].title }}</span>
              <div>
                <span class="text-introduction">
                  {{ machineLearningModelIntroduction[user.machineLearningModel].content }}
                </span>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-row class="center-button">
        <el-button @click="activeIndex-=1">上一步</el-button>
        <el-button type="primary" icon="el-icon-download"><a href="/user/test/xxxx.txt" download="数据集.csv">下载数据集</a>
        </el-button>
        <el-button type="success" @click="activeIndex+=1">我已完成训练</el-button>
      </el-row>
    </div>

    <div v-if="activeIndex === 4 && activeIndex <= user.machineLearningTaskState">
      <el-row>
        <div>
          <el-form ref="form" :model="user" :rules="formRules" auto-complete="on" label-width="120px">
            <el-form-item label="模型名称" prop="modelName">
              <el-col :span="8">
                <el-input v-model="user.modelName" placeholder="例如：基于SVM的情感分类预测模型" />
              </el-col>
            </el-form-item>
            <el-form-item label="模型介绍" prop="modelIntroduction">
              <el-col :span="14">
                <el-input rows="8" type="textarea" v-model="user.modelIntroduction"
                          placeholder="简单描述一下你的模型，思路方法、正确率等" />
              </el-col>
            </el-form-item>
            <el-form-item label="上传代码">
              <el-col :span="8">
                <el-upload
                  ref="upload"
                  :multiple="false"
                  :headers="{ 'X-Token': token }"
                  :file-list="user.modelFileList"
                  action=""
                  accept="application/zip,application/x-gtar,application/x-gzip"
                  :limit="1"
                  :on-remove="handleRemove"
                  :before-remove="handleBeforeRemove"
                  :on-exceed="handleOnExceed"
                  :auto-upload="false"
                  :http-request="submitUpload"
                >
                  <el-button slot="trigger" size="small" type="primary">选取文件</el-button>

                  <div slot="tip" class="el-upload__tip">
                    <div>
                      <el-button size="small" type="success" @click="$refs.upload.submit()">上传到COS存储服务器</el-button>
                    </div>
                    只能上传.zip压缩文件，且不超过10MB
                    <!--                  <el-progress v-show="startUpload" :percentage="uploadProgress"-->
                    <!--                               :status="uploadProgress===100?'success':null" />-->
                  </div>

                </el-upload>
              </el-col>
              <el-col :span="2" />
            </el-form-item>
          </el-form>
        </div>
      </el-row>
      <el-row class="center-button" style="margin-top: 0px !important;">
        <el-button @click="activeIndex-=1">上一步</el-button>
        <el-button @click="submitModelInfo" type="primary">提交实验</el-button>
      </el-row>
    </div>

    <div v-if="activeIndex === 5 && activeIndex <= user.machineLearningTaskState">
      <el-row class="center-button">
        <el-icon style="font-size: 30px;color: #838a92" class="el-icon-loading"></el-icon>
      </el-row>
      <el-row class="center-button">
        <el-button @click="activeIndex<0?null:activeIndex-=1">上一步</el-button>
      </el-row>
    </div>

  </div>
</template>

<script>
import { getToken } from '@/utils/authorize'
import {
  createFoundationSkill, createModelInfo,
  getAndUpdateMachineLearningTaskState,
  getOrSetMachineLearningModel, getUserTaskInfo
} from '@/api/annotation'
import { getUserInfo } from '@/api/login'

export default {
  name: 'NewMachineLearningTask',

  data() {
    return {
      user: {
        undoTaskCount: 1000,
        foundationSkill: [],
        machineLearningModel: null,
        machineLearningTaskState: 0,
        modelName: '',
        modelIntroduction: '',
        modelFileList: [],
        modelFileUrl: 'sad'
      },
      checkboxDisable: false,
      activeIndex: 0,
      stepIntroduction: [
        '请先选择你的机器学习基础，帮助我们更准确地给你分配机器学习任务',
        '我们将稍后为你分发数据，请自行标注并创建训练集',
        '根据你的标注，我们为你分配了以下机器学习任务模型',
        '你可以随时下载自己标注的数据，请在线下完成模型的训练',
        '上传机器学习代码及模型结果，这将作为考核的一部分',
        '当前进度：请等待管理员评定成绩'
      ],
      machineLearningModelIntroduction: [
        {
          'title': '新闻细粒度情感分析',
          'content': '建立一个半监督模型，对未标注的新闻进行情感的细粒度分类',
          'img': 'http://img.mukewang.com/szimg/5dd35a9d0908d98a12000676-360-202.jpg'
        },
        {
          'title': '新闻立场检测',
          'content': '建立一个半监督模型，对未标注的新闻进行立场检测',
          'img': 'http://img.mukewang.com/szimg/5d1978e80875170506000338-360-202.jpg'
        },
        {
          'title': '新闻话题分类',
          'content': '建立一个半监督模型，对未标注的新闻进行话题分类',
          'img': 'http://img.mukewang.com/szimg/5ce7e7970894f48706000338-360-202.jpg'
        }
        // {
        //   'title': '新闻实体抽取',
        //   'content': '识别出新闻中出现的专有名称和有意义的数量短语并加以归类',
        //   'img': 'http://img.mukewang.com/szimg/5c904ad40884c53506000338-360-202.jpg'
        // }
        // {
        //   'title': '新闻实体抽取',
        //   'content': '识别出新闻中出现的专有名称和有意义的数量短语并加以归类',
        //   'img': 'http://img.mukewang.com/szimg/5b061fe30001db4505400300-360-202.jpg'
        // },
        // {
        //   'title': '新闻实体抽取',
        //   'content': '识别出新闻中出现的专有名称和有意义的数量短语并加以归类',
        //   'img': 'http://img.mukewang.com/szimg/5aec33fd0001c86805400300-360-202.jpg'
        // }
      ],
      token: getToken(),
      fullscreenLoading: false,
      formRules: {
        modelName: [{ required: true, trigger: 'blur', message: '模型名称不能为空' }],
        modelIntroduction: [{ required: true, trigger: 'blur', message: '模型介绍不能为空' }]
      }
    }
  },

  mounted: function() {
    this.activeIndex = this.machineLearningTaskState
    getUserTaskInfo().then(response => {
      this.user.undoTaskCount = response.data
    })
    getUserInfo().then(response => {
      const data = response.data
      if (data.hasOwnProperty('foundationSkill')) {
        this.checkboxDisable = true
        this.user.foundationSkill = [response.data.foundationSkill.toString()]
      }
      if (data.hasOwnProperty('machineLearningTaskState')) {
        this.user.machineLearningTaskState = response.data.machineLearningTaskState
        this.activeIndex = this.user.machineLearningTaskState
      }
      if (data.hasOwnProperty('machineLearningModel')) {
        this.user.machineLearningModel = response.data.machineLearningModel
      }
      if (data.hasOwnProperty('modelName')) {
        this.user.modelName = response.data.modelName
      }
      if (data.hasOwnProperty('modelIntroduction')) {
        this.user.modelIntroduction = response.data.modelIntroduction
      }
      if (data.hasOwnProperty('modelFileUrl')) {
        this.user.modelFileUrl = response.data.modelFileUrl
      }
    })
  },

  methods: {
    // 上传数量超出时的钩子
    handleOnExceed() {
      this.$message.warning('单个轮播只能上传一张图片')
    },
    // 移除上传文件前的钩子
    handleBeforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`)
    },
    // 移除上传列表文件的钩子
    handleRemove(file, fileList) {
      // this.clearUploadProgress()
    },

    submitUpload(fileInfo) {
      console.log(fileInfo)
      if (fileInfo.file == null) {
        this.$message.error('请先选择要上传的文件')
        return
      }
    },

    getRandom() {
      this.$nextTick(() => {
        this.user.machineLearningModel = Math.floor(Math.random() * (4))
      })
    },
    openFullScreen(text, timeout) {
      const loading = this.$loading({
        lock: true,
        text: text,
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })
      setTimeout(() => {
        loading.close()
      }, timeout)
    },
    submitFoundationSkill() {
      getUserTaskInfo().then(response => {
        this.user.undoTaskCount = response.data
      })
      // if (this.user.machineLearningTaskState > 0) {
      //   this.activeIndex += 1
      //   return
      // }
      if (this.user.foundationSkill == null || this.user.foundationSkill.length === 0) {
        this.$message.error('请先选择你的基础信息')
      } else {
        createFoundationSkill(parseInt(this.user.foundationSkill[0])).then(response => {
          if (response.data === 0) {
            this.$message.info(response.msg)
            this.activeIndex = 1
            return
          }
          this.$message.success(response.msg)
          this.user.machineLearningTaskState = 1
          this.activeIndex = 1
        })
      }
    },

    updateMachineLearningTaskState() {
      if (this.user.machineLearningTaskState > 1) {
        this.activeIndex += 1
        return
      }
      let loading = this.$loading({
        lock: true,
        text: '校验中...请稍后',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })
      getUserTaskInfo().then(response => {
        this.user.undoTaskCount = response.data
      })
      setTimeout(() => {
        loading.close()
      }, 1000)
      getAndUpdateMachineLearningTaskState().then(response => {
        this.user.machineLearningTaskState = response.data.machineLearningTaskState
      }).then(() => {
        setTimeout(() => {
          loading.close()
        }, 800)
        this.activeIndex = 2
        this.user.machineLearningModel = null
        setTimeout(() => {
          loading = this.$loading({
            lock: true,
            text: '正在为你分配合适的模型任务',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 80%, 0.5)'
          })
        }, 2000)
        setTimeout(() => {
          loading.close()
          getOrSetMachineLearningModel().then(res => {
            this.user.machineLearningModel = res.data
          })
          this.$message.success('模型任务已分配')
        }, 4000)
      })
    },

    submitModelInfo() {
      this.$refs.form.validate(valid => {
        if (valid) {
          createModelInfo(this.user.modelName, this.user.modelIntroduction, this.user.modelFileUrl).then(response => {
            if (this.user.modelName === '' || this.user.modelIntroduction === '') {
              this.$message.error('请填写必要的模型名称和介绍')
              return
            } else if (this.user.modelFileUrl === '') {
              this.$message.error('请先上传文件再提交')
              return
            }
            this.$message.success(response.msg)
            this.user.machineLearningTaskState = 5
            this.activeIndex += 1
          })
        }
      })

    }

  }
}
</script>

<style scoped>

.bottom {
  margin-top: 13px;
  line-height: 12px;
}

.text-introduction {
  color: #909399;
  font-size: 12px;
  line-height: 20px;
  padding: 0;
  text-align: center;
}

.image {
  width: 100%;
  display: block;
}

.center-button {
  margin-top: 100px;
  left: 50%;
  text-align: center;
  -webkit-transform: translate(-50%, -50%);
}

.mask {
  width: 100%;
  height: 90%;
  background: #efefef;
  -moz-opacity: 0.5;
  opacity: 0.5;
  -webkit-filter: blur(3px); /* Chrome, Opera */
  -ms-filter: blur(10px);
  /*filter: blur(1px);*/
}

.unmask {
  width: 110%;
  height: 100%;
}

.box-card {
  width: 80%;
  padding: 5%;
  margin: 0 auto;
  /*background-color: #ffffff;*/
  background-image: linear-gradient(135deg, #45b2ff 0%, #039bf4 75%, #338ce8 100%);
  /*background-image: linear-gradient(to top, #f3e7e9 0%, #e3eeff 99%, #e3eeff 100%);*/
  /*background-image: linear-gradient(-45deg, #FFC796 0%, #FF6B95 100%);*/
}

.card-right-text {
  float: right;
  text-align: right;
  /*margin: 2rem 8rem 8rem 0;*/
}

.card-left-icon {
  margin: 5% 0;
  padding: 2%;
  float: left;
  /*background-color: #1482f0;*/
  /*border-radius: 50%;*/
}

.card-left-icon .svg-icon {
  font-size: 40px;
  color: #ffffff;
}

.card-right-title {
  color: #ffffff;
  font-size: 15px;
  font-weight: bold;
  padding: 5px 0;
}

.card-right-data {
  color: #ffffff;
  font-size: 26px;
  font-weight: bolder;
}
</style>
