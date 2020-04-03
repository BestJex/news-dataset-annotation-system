<template>
  <div>
    <el-row style="padding: 20px">
      <el-col :span="12">
        <span style="font-size: 14px">标注进度 - {{ currentNo }}/{{ totalNo }}</span>
        <el-progress :text-inside="true" :stroke-width="5" :percentage="percentage" />
      </el-col>
      <el-col :span="12">
        <el-button-group style="float:right;">
          <el-button type="primary" icon="el-icon-arrow-left" :disabled="preNewsDisabled" @click="onPreNews">上一篇
          </el-button>
          <el-button type="primary" :disabled="nextNewsDisabled" @click="onNextNews">下一篇<i
            class="el-icon-arrow-right el-icon--right"
          /></el-button>
        </el-button-group>
      </el-col>
    </el-row>
    <el-row style="padding: 20px" :gutter="20">
      <el-col :span="12">
        <el-tabs v-loading="loading" type="border-card">
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
                &nbsp;&nbsp;
                时间:
                {{ new Date(news['news_publish_timestamp']*1000).Format('yy-MM-dd hh:mm:ss') }}
                &nbsp;&nbsp;
                国家:
                {{ news['news_country'] }}
              </span>
              <p style="color: dodgerblue;font-size: 14px;">标题：{{ news['news_title_translate_cn'] }}</p>
            </div>
            <el-input
              v-model="news['news_content_translate_cn']"
              type="textarea"
              style="text-indent:2em;"
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
                &nbsp;&nbsp;
                时间:
                {{ new Date(news['news_publish_timestamp']*1000).Format('yy-MM-dd hh:mm:ss') }}
                &nbsp;&nbsp;
                国家:
                {{ news['news_country'] }}
              </span>
              <p style="color: dodgerblue;font-size: 14px;">标题：{{ news['news_title_translate_en'] }}</p>
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
          <el-form-item label="情感" prop="news_emotion">
            <el-checkbox-group v-model="form.news_emotion" :max="emotionMax" @change="emotionChange()">
              <el-checkbox style="width: 100%;" label="agreeable">
                尊敬、赞扬、认同&nbsp;（等相似情感）
                <el-badge
                  v-show="emotionBadge['agreeable']>0"
                  key="agreeable"
                  ref="agreeable"
                  :value="emotionBadge['agreeable']"
                  class="badge-item"
                />
              </el-checkbox>
              <el-checkbox style="width: 100%;" label="believable">
                祝愿、祝福、相信、鼓励&nbsp;（等相似情感）
                <el-badge
                  v-show="emotionBadge['believable']>0"
                  key="believable"
                  ref="believable"
                  :value="emotionBadge['believable']"
                  class="badge-item"
                />
              </el-checkbox>
              <el-checkbox style="width: 100%;" label="good">
                感激、崇拜、正面惊讶&nbsp;（等相似情感）
                <el-badge
                  v-show="emotionBadge['good']>0"
                  key="good"
                  ref="good"
                  :value="emotionBadge['good']"
                  class="badge-item"
                />
              </el-checkbox>
              <el-checkbox style="width: 100%;" label="hated">
                谴责、批评、愤怒、怀疑、嫉妒、憎恨、反对&nbsp;（等相似情感）
                <el-badge
                  v-show="emotionBadge['hated']>0"
                  key="hated"
                  ref="hated"
                  :value="emotionBadge['hated']"
                  class="badge-item"
                />
              </el-checkbox>
              <el-checkbox style="width: 100%;" label="sad">
                悲伤、失望、内疚、思念&nbsp;（等相似情感）
                <el-badge
                  v-show="emotionBadge['sad']>0"
                  key="sad"
                  ref="sad"
                  :value="emotionBadge['sad']"
                  class="badge-item"
                />
              </el-checkbox>
              <el-checkbox style="width: 100%;" label="worried">
                慌张、恐惧、恐慌、担忧、担心、害怕、慌张的惊讶&nbsp;（等相似情感）
                <el-badge
                  v-show="emotionBadge['worried']>0"
                  key="worried"
                  ref="worried"
                  :value="emotionBadge['worried']"
                  class="badge-item"
                />
              </el-checkbox>
              <el-checkbox style="width: 100%;" label="objective">
                客观
                <el-badge
                  v-show="emotionBadge['objective']>0"
                  key="objective"
                  ref="objective"
                  :value="emotionBadge['objective']"
                  class="badge-item"
                />
              </el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item label="立场" prop="news_position">
            <el-checkbox-group v-model="form.news_position" :max="1">
              <el-checkbox label="positive">积极</el-checkbox>
              <el-checkbox label="neutral">中级</el-checkbox>
              <el-checkbox label="negative">消极</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item label="事件主体" prop="news_about_china">
            <!--            <el-select v-model="form.aboutChina" placeholder="请选择事件主体">-->
            <!--              <el-option label="中国" value="yes" />-->
            <!--              <el-option label="非中国" value="no" />-->
            <!--            </el-select>-->
            <el-checkbox-group v-model="form.news_about_china" :max="1">
              <el-checkbox label="true">中国</el-checkbox>
              <el-checkbox label="false">非中国</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item label="主题" prop="news_subject">
            <el-checkbox-group v-model="form.news_subject" :max="1">
              <el-checkbox style="width: 100%;" label="politics">党政、军事、外交（包括撤侨、出入境管理）</el-checkbox>
              <el-checkbox style="width: 100%;" label="society">民生、民众日常生活、医疗（民生方面）、政府命令实施（民生方面）</el-checkbox>
              <el-checkbox style="width: 100%;" label="technology">互联网、工业、科技创新、医疗（生物科技方面）</el-checkbox>
              <el-checkbox style="width: 100%;" label="economy">经济、商业、金融证券（包括金融政策）、财政</el-checkbox>
              <el-checkbox style="width: 100%;" label="sports">体育</el-checkbox>
              <el-checkbox style="width: 100%;" label="humanity">历史、教育、人文、艺术（音乐、绘画）</el-checkbox>
              <el-checkbox style="width: 100%;" label="entertainment">娱乐</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item label="类型" prop="news_type">
            <el-checkbox-group v-model="form.news_type" :max="1">
              <el-checkbox label="fact">陈述客观事实</el-checkbox>
              <el-checkbox label="interview">采访记录</el-checkbox>
              <el-checkbox label="essay">散文、讲故事</el-checkbox>
              <el-checkbox label="remark">评论、论述</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item label="情感依据" prop="news_emotion_basis">
            <el-checkbox-group v-model="form.news_emotion_basis" :disabled="emotionBasisDisabled">
              <el-checkbox v-for="index in emotion_basis_count" :key="index" :label="index.toString()">第&nbsp;{{ index
                }}&nbsp;段
              </el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSubmit">立即提交</el-button>
            <el-button @click="resetForm">重置</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>

    <el-dialog
      title="请标注两篇新闻是否讲述同一事件"
      width="90%"
      style="margin-top: -10vh !important;"
      :center="true"
      :fullscreen="false"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :show-close="false"
      :visible.sync="compareDialogVisible"
    >
      <el-row style="padding: 0px 20px" :gutter="20">
        <el-col :span="12">
          <div style="color: dodgerblue;font-size: 16px;margin-bottom: 5px">
            新闻A&nbsp;&nbsp;
            时间:
            {{ new Date(compare1.timestamp*1000).Format('yyyy-MM-dd hh:mm:ss') }}
            &nbsp;&nbsp;国家:
            {{ compare1.country }}
            <p style="color: dodgerblue;font-size: 14px;">标题：{{ compare1.title }}</p>
          </div>
          <el-input
            v-model="compare1.content"
            style="margin-bottom: 20px"
            type="textarea"
            :rows="15"
          />
        </el-col>
        <el-col :span="12">
          <div style="color: dodgerblue;font-size: 16px;margin-bottom: 5px">
            新闻B&nbsp;&nbsp;
            时间:
            {{ new Date(compare2.timestamp*1000).Format('yyyy-MM-dd hh:mm:ss') }}
            &nbsp;&nbsp;国家:
            {{ compare2.country }}
            <p style="color: dodgerblue;font-size: 14px;">标题：{{ compare2.title }}</p>
          </div>
          <el-input
            v-model="compare2.content"
            type="textarea"
            :rows="15"
          />
        </el-col>
      </el-row>
      <el-row>
        <el-form ref="similarityForm" :model="similarity" :rules="similarityRules" label-width="120px">
          <el-form-item label="是否同一事件" prop="is_related">
            <el-checkbox-group v-model="similarity.is_related" :max="1" @change="isRelatedChanged">
              <el-checkbox label="same">是</el-checkbox>
              <el-checkbox label="different">否</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item label="A B的发展关系" prop="relation">
            <el-checkbox-group
              v-model="similarity.relation"
              :max="1"
              :disabled="relation_disabled"
              @change="relationChanged"
            >
              <el-checkbox label="A -> B">A -> B</el-checkbox>
              <el-checkbox label="B -> A">B -> A</el-checkbox>
              <el-checkbox label="no-connection">无发展关系</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </el-form>
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitSimilarity">立即提交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

import { addSimilarity, getAnnotation, getAnnotationIdList, saveAnnotation } from '@/api/annotation'
import { getUsername } from '@/utils/authorize'

export default {
  name: 'NewArticle',

  data() {
    const validEmotionBasis = (rule, value, callback) => {
      if (this.form.news_emotion.indexOf('objective') !== -1) {
        callback()
      } else if (this.emotion_basis_count === 0) {
        this.form.news_emotion_basis = []
        callback()
      } else if (this.form.news_emotion_basis.length === 0) {
        callback(new Error('至少选择一个选择情感依据'))
      } else {
        callback()
      }
    }
    return {
      news: {},
      compare1: {
        'id': '',
        'content': '',
        'country': '',
        'timestamp': 0,
        'title': ''
      },
      compare2: {
        'id': '',
        'content': '',
        'country': '',
        'timestamp': 0,
        'title': ''
      },
      compareCount: 0,
      form: {
        news_emotion: [],
        news_position: [],
        news_subject: [],
        news_about_china: [],
        news_type: [],
        news_emotion_basis: []
      },
      similarity: {
        id_A: '',
        id_B: '',
        is_related: [],
        relation: []
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
      doneNo: -1,
      totalNo: 1,
      compareDialogVisible: false,
      formLabelWidth: '120px',
      formRules: {
        news_emotion: [{ type: 'array', required: true, trigger: 'blur', message: '至少标注一个情感' }],
        news_position: [{ type: 'array', required: true, trigger: 'blur', message: '立场标注不能为空' }],
        news_type: [{ type: 'array', required: true, trigger: 'blur', message: '类型标注不能为空' }],
        news_subject: [{ type: 'array', required: true, trigger: 'blur', message: '主题标注不能为空' }],
        news_about_china: [{ type: 'array', required: true, trigger: 'blur', message: '请选择是否有关中国' }],
        news_emotion_basis: [{ type: 'array', required: true, trigger: 'blur', validator: validEmotionBasis }]
      },
      similarityRules: {
        is_related: [{ type: 'array', required: true, trigger: 'blur', message: '请标注是否同一事件' }],
        relation: [{ type: 'array', required: true, trigger: 'blur', message: 'A、B新闻不是同一事件时，关联关系不能为空' }]
      },
      emotion_basis_count: 0,
      username: '',
      loading: true,
      relation_disabled: false,
      emotionMax: 2,
      emotionBasisDisabled: false,
      completeSimilarityQueue: []
    }
  },

  computed: {
    percentage: function() {
      if (this.currentNo < 0) {
        return 0
      }
      return this.currentNo / this.totalNo * 100
    },
    nextNewsDisabled: function() {
      return this.currentNo - 1 > this.doneNo
    },
    preNewsDisabled: function() {
      return this.currentNo <= 1
    }
  },

  watch: {
    currentNo: {
      handler(newVal, oldVal) {
        if (newVal <= 0) {
          return
        }
        const username = this.username
        this.resetForm()
        this.loading = true
        getAnnotation(this.ids[newVal - 1]['id']).then(response => {
          this.$nextTick(() => { // 以服务的方式调用的 Loading 需要异步关闭
            this.loading = false
          })
          const data = response.data
          if (data === null) {
            this.$message({
              message: response.data,
              type: 'error',
              center: true,
              duration: 5000
            })
            return
          }
          if (data.hasOwnProperty('news_content_translate_cn')) {
            data['news_content_translate_cn'] = data['news_content_translate_cn'].trim()
            data['news_content_translate_cn'].replace(/\r/g, '\n')
            const temp = data['news_content_translate_cn'].split('\n')
            data['news_content_translate_cn'] = ''
            if (temp.length > 1 && temp[temp.length - 1].trim() === '') {
              temp.splice(-1, 1)
            }
            this.emotion_basis_count = temp.length
            for (let i = 0; i < temp.length; i++) {
              if (i === 0) {
                data['news_content_translate_cn'] += '\n【第 ' + (i + 1) + ' 段】  ' + temp[i]
              } else if (temp[i].trim() !== '') {
                data['news_content_translate_cn'] += '\n\n【第 ' + (i + 1) + ' 段】  ' + temp[i]
              }
            }
          }
          if (!this.completeSimilarityQueue.includes(data['id']) && newVal > oldVal) {
            this.compareCount += 1
            if (this.compareCount === 1) {
              this.completeSimilarityQueue.push(data['id'])
              this.compare1.id = data['id']
              this.compare1.content = data['news_content_translate_cn']
              this.compare1.country = data['news_country']
              this.compare1.timestamp = data['news_publish_timestamp']
              this.compare1.title = data['news_title_translate_cn']
            } else {
              this.completeSimilarityQueue.push(data['id'])
              this.compare2.id = data['id']
              this.compare2.content = data['news_content_translate_cn']
              this.compare2.country = data['news_country']
              this.compare2.timestamp = data['news_publish_timestamp']
              this.compare2.title = data['news_title_translate_cn']
            }
          }
          // 去掉emotion中的none
          const index = data['users'].indexOf(username)

          if (data['news_emotion'][index] !== null && data['news_emotion'][index].includes('none')) {
            for (let i = 0; i < data['news_emotion'][index].length; i++) {
              if (data['news_emotion'][index][i] === 'none') {
                data['news_emotion'][index].splice(i, 1)
              }
            }
          }
          this.news = data
          if (index >= 0 && data['news_annotation_done'][index] !== null) {
            this.form.news_emotion = data['news_emotion'][index]
            this.form.news_position = [data['news_position'][index]]
            this.form.news_subject = [data['news_subject'][index]]
            this.form.news_type = [data['news_type'][index]]
            this.form.news_about_china = [data['news_about_china'][index].toString()]
            this.form.news_emotion_basis = data['news_emotion_basis'][index]
            this.emotionChange()
          }

        })
      },
      immediate: true
    }
  },

  mounted: function() {
    this.username = getUsername()
    getAnnotationIdList().then(response => {
      this.ids = response.data
      this.totalNo = response.data.length
      if (this.ids.length === 0) {
        this.showNoDataMessage()
        return
      }
      let noData = true
      for (let i = 0; i < this.ids.length; i++) {
        if (this.ids[i]['state'] === 0) {
          noData = false
          this.currentNo = i + 1
          this.doneNo = i - 1
          break
        }
      }
      if (noData) {
        this.currentNo = this.totalNo
        this.doneNo = this.totalNo
        this.showNoDataMessage()
        return
      }
    })
  },

  methods: {
    showNoDataMessage() {
      this.$alert('当前没有标注任务，请先联系管理员进行分配。', '提示', {
        confirmButtonText: '确定',
        callback: action => {
          this.$message({
            type: 'info',
            message: '该账号暂无标注任务'
          })
        }
      })
    },

    onSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const annotation = {
            _id: this.ids[this.currentNo - 1]['id'],
            news_emotion: this.form.news_emotion,
            news_position: this.form.news_position[0],
            news_subject: this.form.news_subject[0],
            news_about_china: this.form.news_about_china[0],
            news_type: this.form.news_type[0],
            news_emotion_basis: this.form.news_emotion_basis
          }
          if (annotation['news_emotion'].length === 1) {
            annotation['news_emotion'].push('none')
          }
          saveAnnotation(annotation).then(response => {
            this.$message({
              message: response.msg,
              type: 'success',
              center: true,
              duration: 3000
            })
            if (this.compareCount >= 2 && this.compare1.id !== this.compare2.id && this.compare1.content !== '' && this.compare2.content !== '') {
              this.relation_disabled = false
              this.resetSimilarityForm()
              this.closeSideBar()
              this.compareDialogVisible = true
              this.compareCount = 0
            }
          }).then(() => {
            if (this.compareDialogVisible === false) {
              if (this.currentNo === this.totalNo) {
                this.$message.success('已完成了所有标注任务')
                this.doneNo += 1
              } else {
                this.doneNo += 1
                this.onNextNews()
              }
            }
          })
        } else {
          return false
        }
      })
    },

    submitSimilarity() {
      this.$refs.similarityForm.validate(valid => {
        if (valid) {
          const sim = {
            id_A: '',
            id_B: '',
            time_A: '',
            time_B: '',
            is_related: [],
            relation: []
          }
          sim.id_A = this.compare1.id
          sim.id_B = this.compare2.id
          sim.is_related = this.similarity.is_related[0]
          sim.relation = this.similarity.relation[0]
          sim.time_A = this.compare1.timestamp
          sim.time_B = this.compare2.timestamp
          // this.completeSimilarityQueue.push(sim.id_A)
          // this.completeSimilarityQueue.push(sim.id_B)
          addSimilarity(sim).then(response => {
            this.$message.success(response.msg)
            this.compareDialogVisible = false
            this.resetSimilarityForm()
          }).then(() => {
            if (this.currentNo === this.totalNo) {
              this.$message.success('已完成了所有标注任务')
              this.doneNo += 1
            } else {
              this.doneNo += 1
              this.onNextNews()
            }
          })
        }
      })
    },

    resetSimilarityForm() {
      this.similarity = {
        is_related: [],
        relation: []
      }
    },

    isRelatedChanged() {
      const is_related = this.similarity.is_related
      if (is_related !== null && is_related.length > 0) {
        if (is_related[0] === 'same') {
          this.relation_disabled = true
          this.similarity.relation = ['no-connection']
          return
        }
      }
      this.similarity.relation = []
      this.relation_disabled = false
    },

    relationChanged(newVal) {
      if (newVal !== null && newVal.length > 0) {

      }
    },

    onPreNews() {
      if (this.currentNo === 1) {
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
      if (this.form.news_position == null || this.form.news_position.length === 0 ||
        this.form.news_emotion == null || this.form.news_emotion.length === 0 ||
        this.form.news_subject == null || this.form.news_subject.length === 0 ||
        this.form.news_about_china == null || this.form.news_about_china.length === 0 ||
        this.form.news_type == null || this.form.news_type.length === 0) {
        this.$message({
          message: '表单有改动，请先提交当前新闻',
          type: 'error'
        })
        return
      }
      if (this.currentNo === this.totalNo) {
        this.$message({
          message: '已经是最后一篇了！',
          type: 'warning'
        })
      } else {
        this.currentNo += 1
      }
    },

    resetForm() {
      this.form = {
        news_emotion: [],
        news_position: [],
        news_subject: [],
        news_about_china: [],
        news_type: [],
        news_emotion_basis: []
      }
      this.emotionBasisDisabled = false
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
    closeSideBar() {
      this.$store.dispatch('closeSideBar', { withoutAnimation: false })
    },
    emotionChange() {
      let emotion = this.form.news_emotion
      if (emotion.indexOf('objective') !== -1) {
        this.$nextTick(() => {
          this.form.news_emotion = ['objective']
          this.form.news_emotion_basis = []
          this.emotionBasisDisabled = true
          this.emotionMax = 1
        })
        emotion = ['objective']

      } else {
        this.$nextTick(() => {
          this.emotionMax = 2
          this.emotionBasisDisabled = false
        })
      }
      const _this = this
      let count = 1
      this.resetEmotionBadge()
      emotion.forEach(item => {
        // _this.$refs[item].hidden = false
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
