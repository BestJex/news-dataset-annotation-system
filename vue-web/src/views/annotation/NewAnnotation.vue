<template>
  <div>
    <el-row style="padding: 20px">
      <el-col :span="12">
        <span style="font-size: 14px">标注进度 - {{ currentNo+1 }}/{{ totalNo }}</span>
        <el-progress :text-inside="true" :stroke-width="5" :percentage="percentage" />
      </el-col>
      <el-col :span="12">
        <el-button-group style="float:right;">
          <el-button type="primary" icon="el-icon-arrow-left" @click="onPreNews">上一篇</el-button>
          <el-button type="primary" @click="onNextNews" :disabled="nextNewsDisabled">下一篇<i
            class="el-icon-arrow-right el-icon--right" /></el-button>
        </el-button-group>
      </el-col>
    </el-row>
    <el-row style="padding: 20px" :gutter="20">
      <el-col :span="12">
        <el-tabs type="border-card">
          <el-tab-pane label="翻译">
            <div style="margin-bottom: 10px">
              <span style="color: dodgerblue;font-size: 14px;">
                _id:
                <el-tooltip class="item" effect="dark" content="点击复制" placement="right-end">
                  <a
                    v-clipboard:copy="news['id']"
                    v-clipboard:success="onCopySuccess"
                    v-clipboard:error="onCopyError"
                    style="text-underline: none"
                  >{{ news['id'] }}</a>
                </el-tooltip>
                时间:
                {{new Date(news['news_publish_timestamp']*1000).Format('yy-MM-dd hh:mm:ss') }}
                国家:
                {{ news['news_country'] }}
              </span>
            </div>
            <el-input
              v-model="news['news_content_translate_cn']"
              type="textarea"
              :rows="35"
              placeholder="请输入内容"
            />
          </el-tab-pane>
          <el-tab-pane label="原文">
            <div style="margin-bottom: 10px">
              <span style="color: dodgerblue;font-size: 14px;">
                _id:
                <el-tooltip class="item" effect="dark" content="点击复制" placement="right-end">
                  <a
                    v-clipboard:copy="news['id']"
                    v-clipboard:success="onCopySuccess"
                    v-clipboard:error="onCopyError"
                    style="text-underline: none"
                  >{{ news['id'] }}</a>
                </el-tooltip>
                时间:
                {{new Date(news['news_publish_timestamp']*1000).Format('yy-MM-dd hh:mm:ss') }}
                国家:
                {{ news['news_country'] }}
              </span>
            </div>
            <el-input
              v-model="news['news_content_translate_en']"
              style="margin-bottom: 20px"
              type="textarea"
              :rows="35"
              placeholder="请输入内容"
            />
          </el-tab-pane>
          <el-tab-pane label="其他信息">文章的其他信息</el-tab-pane>
        </el-tabs>
      </el-col>

      <el-col :span="12">
        <el-form ref="form" :model="form" :rules="formRules" label-width="80px">
          <el-form-item label="情感">
            <el-checkbox-group v-model="form.news_emotion" :max="2" @change="emotionChange()">
              <el-checkbox style="width: 100%;" label="agreeable">
                尊敬、赞扬、认同
                <el-badge
                  v-show="emotionBadge['agreeable']>0"
                  ref="agreeable"
                  :value="emotionBadge['agreeable']"
                  class="badge-item"
                />
              </el-checkbox>
              <el-checkbox style="width: 100%;" label="believable">
                祝愿、祝福、相信、鼓励
                <el-badge
                  v-show="emotionBadge['believable']>0"
                  ref="believable"
                  :value="emotionBadge['believable']"
                  class="badge-item"
                />
              </el-checkbox>
              <el-checkbox style="width: 100%;" label="good">
                感激、崇拜、正面惊讶
                <el-badge
                  v-show="emotionBadge['good']>0"
                  ref="good"
                  :value="emotionBadge['good']"
                  class="badge-item"
                />
              </el-checkbox>
              <el-checkbox style="width: 100%;" label="hated">
                谴责、批评、愤怒、怀疑、嫉妒、憎恨、反对
                <el-badge
                  v-show="emotionBadge['hated']>0"
                  ref="hated"
                  :value="emotionBadge['hated']"
                  class="badge-item"
                />
              </el-checkbox>
              <el-checkbox style="width: 100%;" label="sad">
                悲伤、失望、内疚、思念
                <el-badge
                  v-show="emotionBadge['sad']>0"
                  ref="sad"
                  :value="emotionBadge['sad']"
                  class="badge-item"
                />
              </el-checkbox>
              <el-checkbox style="width: 100%;" label="worried">
                慌张、恐惧、恐慌、担忧、担心、害怕、慌张的惊讶
                <el-badge
                  v-show="emotionBadge['worried']>0"
                  ref="worried"
                  :value="emotionBadge['worried']"
                  class="badge-item"
                />
              </el-checkbox>
              <el-checkbox style="width: 100%;" label="objective">
                客观
                <el-badge
                  v-show="emotionBadge['objective']>0"
                  ref="objective"
                  :value="emotionBadge['objective']"
                  class="badge-item"
                />
              </el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item label="立场">
            <el-checkbox-group v-model="form.news_position" :max="1">
              <el-checkbox label="positive">积极</el-checkbox>
              <el-checkbox label="neutral">中级</el-checkbox>
              <el-checkbox label="negative">消极</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item label="事件主体">
            <!--            <el-select v-model="form.aboutChina" placeholder="请选择事件主体">-->
            <!--              <el-option label="中国" value="yes" />-->
            <!--              <el-option label="非中国" value="no" />-->
            <!--            </el-select>-->
            <el-checkbox-group v-model="form.news_about_china" :max="1">
              <el-checkbox label="true">中国</el-checkbox>
              <el-checkbox label="false">非中国</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item label="主题">
            <el-checkbox-group v-model="form.news_subject" :max="1">
              <el-checkbox label="politics">政治</el-checkbox>
              <el-checkbox label="society">社会</el-checkbox>
              <el-checkbox label="technology">科技</el-checkbox>
              <el-checkbox label="economy">经济</el-checkbox>
              <el-checkbox label="sports">体育</el-checkbox>
              <el-checkbox label="humanity">人文</el-checkbox>
              <el-checkbox label="arts">艺术</el-checkbox>
              <el-checkbox label="entertainment">娱乐</el-checkbox>
            </el-checkbox-group>
            <!--            <el-radio-group v-model="form.subject">-->
            <!--              <el-radio label="politics">政治</el-radio>-->
            <!--              <el-radio label="society">社会</el-radio>-->
            <!--              <el-radio label="technology">科技</el-radio>-->
            <!--              <el-radio label="economy">经济</el-radio>-->
            <!--              <el-radio label="sports">体育</el-radio>-->
            <!--              <el-radio label="humanity">人文</el-radio>-->
            <!--              <el-radio label="arts">艺术</el-radio>-->
            <!--              <el-radio label="entertainment">娱乐</el-radio>-->
            <!--            </el-radio-group>-->
          </el-form-item>
          <el-form-item label="类型">
            <el-checkbox-group v-model="form.news_type" :max="1">
              <el-checkbox label="fact">事实</el-checkbox>
              <el-checkbox label="essay">短文</el-checkbox>
              <el-checkbox label="interview">采访</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item label="情感依据">
            <el-input v-model="form.news_emotion_basis" type="textarea" :rows="6" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSubmit">立即提交</el-button>
            <el-button @click="resetForm">重置</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>

    <el-dialog title="请标注两篇新闻是否讲述同一事件" width="80%" :visible.sync="compareDialogVisible">
      <el-row style="padding: 20px" :gutter="20">
        <el-col :span="12">
          <div style="color: dodgerblue;font-size: 16px;margin-bottom: 5px">上一篇新闻</div>
          <el-input
            v-model="compare1"
            style="margin-bottom: 20px"
            type="textarea"
            :rows="15"
            placeholder="请输入内容"
          />
        </el-col>
        <el-col :span="12">
          <div style="color: dodgerblue;font-size: 16px;margin-bottom: 5px">当前新闻</div>
          <el-input
            v-model="compare2"
            type="textarea"
            :rows="15"
            placeholder="请输入内容"
          />
        </el-col>
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogFormVisible = false">相同事件</el-button>
        <el-button type="danger" @click="dialogFormVisible = false">非相同事件</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

import { getAnnotation, getAnnotationIdList, initMongo, saveAnnotation } from '@/api/annotation'
import { getUsername } from '@/utils/authorize'

export default {
  name: 'NewArticle',

  data() {
    return {
      news: {},
      compare1: '',
      compare2: '',
      form: {
        news_emotion: [],
        news_position: [],
        news_subject: [],
        news_about_china: [],
        news_type: [],
        news_emotion_basis: ''
      },
      emotionBadge: {
        'agreeable': 0,
        'believable': 0,
        'good': 0,
        'hated': 0,
        'sad': 0,
        'worried': 0,
        'objective': 0
      },
      showEmotionBadge: false,
      ids: [],
      currentNo: 0,
      doneNo: 0,
      totalNo: 1,
      compareDialogVisible: false,
      formLabelWidth: '120px',
      formRules: {
        news_emotion: [{ required: true, trigger: 'blur', message: '至少标注一个情感' }],
        news_position: [{ required: true, trigger: 'blur', message: '立场标注不能为空' }],
        news_type: [{ required: true, trigger: 'blur', message: '类型标注不能为空' }],
        news_subject: [{ required: true, trigger: 'blur', message: '主题标注不能为空' }],
        news_about_china: [{ required: true, trigger: 'blur', message: '请选择是否有关中国' }]
      },
      username: ''
    }
  },

  mounted: function() {
    this.username = getUsername()
    initMongo()
    getAnnotationIdList().then(response => {
      this.ids = response.data
      this.totalNo = response.data.length
      console.log(this.ids)
      for (let i = 0; i < this.ids.length; i++) {
        if (this.ids[i].state === 0) {
          this.currentNo = i
          this.doneNo = i - 1
          break
        }
      }
    })
  },

  watch: {
    currentNo: {
      handler(newVal, oldVal) {
        if (this.ids.length === 0) {
          return
        }
        const username = this.username
        this.resetForm()
        getAnnotation(this.ids[newVal]['id']).then(response => {
          const data = response.data
          this.news = data
          if (data !== null) {
            if (newVal % 2 === 0) {
              this.compare2 = data['news_content_translate_cn']
            } else {
              this.compare1 = data['news_content_translate_cn']
            }
            const index = data['users'].indexOf(username)
            if (index >= 0 && data['news_annotation_done'][index] === true) {
              this.form.news_emotion = data['news_emotion'][index]
              this.form.news_position = [data['news_position'][index]]
              this.form.news_subject = [data['news_subject'][index]]
              this.form.news_type = [data['news_type'][index]]
              this.form.news_about_china = [data['news_about_china'][index]]
              this.form.news_emotion_basis = data['news_emotion_basis'][index]
              this.emotionChange()
            }
          } else {
            this.$message({
              message: response.data,
              type: 'error',
              center: true,
              duration: 5000
            })
          }
        })
      },
      immediate: true
    }
  },

  computed: {
    percentage: function() {
      return this.currentNo / this.totalNo * 100
    },
    nextNewsDisabled: function() {
      return this.currentNo > this.doneNo
    }
  },

  methods: {
    onSubmit() {
      // this.$refs.form.validate(valid => {
      //   if (valid) {
      //     // addAttendance(this.form).then(response => {
      //     //   this.$message({
      //     //     message: '打卡成功！',
      //     //     type: 'success',
      //     //     center: true,
      //     //     duration: 3000
      //     //   })
      //     const _this = this
      //     this.$message({
      //       message: '打卡成功！',
      //       type: 'success',
      //       center: true,
      //       duration: 3000
      //     })
      //     setTimeout(function() {
      //       _this.$router.push('/annotation/list')
      //     }, 1000)
      //   } else {
      //     console.log('输入数据不合法！')
      //     return false
      //   }
      // })
      const currentNo = this.currentNo
      const annotation = {
        _id: this.ids[this.currentNo]['id'],
        news_emotion: this.form.news_emotion,
        news_position: this.form.news_position[0],
        news_subject: this.form.news_subject[0],
        news_about_china: this.form.news_about_china[0],
        news_type: this.form.news_type[0],
        news_emotion_basis: this.form.news_emotion_basis
      }
      saveAnnotation(annotation).then(response => {
        this.$message({
          message: response.msg,
          type: 'success',
          center: true,
          duration: 3000
        })
        if (currentNo % 2 === 0) {
          this.compareDialogVisible = true
        }
      }).then(() => {
        this.doneNo += 1
        this.onNextNews()
      })
    },

    onPreNews() {
      if (this.currentNo === 0) {
        this.$message({
          message: '已经是第一篇了！',
          type: 'error'
        })
      } else {
        this.currentNo -= 1
        this.content_translate = ''
      }
    },

    onNextNews() {
      if (this.form.news_position[0] == null) {
        this.$message({
          message: '请先提交当前新闻',
          type: 'error'
        })
        return
      }
      if (this.currentNo === this.totalNo - 1) {
        this.$message({
          message: '已经是最后一篇了！',
          type: 'error'
        })
      } else {
        this.currentNo += 1
        this.content_translate = ''
      }
    },

    resetForm() {
      this.form = {
        news_emotion: [],
        news_position: [],
        news_subject: [],
        news_about_china: [],
        news_type: [],
        news_emotion_basis: ''
      }
      this.resetEmotionBadge()
    },

    onCopySuccess() {
      this.$message({
        message: '复制成功',
        type: 'success'
      })
    },

    onCopyError() {
      this.$message({
        message: '复制失败',
        type: 'error'
      })
    },

    emotionChange() {
      const emotion = this.form.news_emotion
      const _this = this
      let count = 1
      this.resetEmotionBadge()
      emotion.forEach(item => {
        _this.$refs[item].hidden = false
        _this.emotionBadge[item] = count
        count += 1
      })
    },

    resetEmotionBadge() {
      this.emotionBadge = {
        'agreeable': 0,
        'believable': 0,
        'good': 0,
        'hated': 0,
        'sad': 0,
        'worried': 0,
        'objective': 0
      }
    }

  }
}
</script>

<style scoped>
/*.el-radio {*/
/*  margin-bottom: 10px;*/
/*}*/
.badge-item {
  margin-top: 8px;
}
</style>
