<template>
  <div class="app-container">
    <el-row>
      <el-col :span="18">
        <el-form
          ref="form"
          :model="form"
          label-position="right"
          :rules="formRules"
          auto-complete="on"
          label-width="120px"
        >
          <el-form-item label="未分配新闻总量">
            <el-col :span="8">
              <span>{{ totalNewsCount }}</span>
            </el-col>
          </el-form-item>
          <el-form-item label="分配总数" prop="newsCount">
            <el-col :span="8">
              <el-input-number v-model="form.newsCount" />
            </el-col>
          </el-form-item>
          <el-form-item label="几人一篇" prop="newsCount">
            <el-col :span="8">
              <el-input-number v-model="form.ratio" />
            </el-col>
          </el-form-item>
          <el-form-item label="选择标注用户">
            <el-checkbox-group v-model="form.userTask">
              <div v-for="(item,index) in userTask" :key="index">
                <el-checkbox :label="item">
                  {{ item.username }} - {{ item.name }}
                </el-checkbox>
                <el-form-item label="任务量" label-width="80px" label-position="left">
                  <el-input-number
                    v-model="item.taskCount"
                    size="small"
                    :disabled="!form.userTask.includes(item)"
                    placeholder="请输入任务数"
                  />
                </el-form-item>
              </div>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item>
            <el-button icon="el-icon-refresh-left" round @click="onReset">重置</el-button>

            <el-button icon="el-icon-finished" type="primary" round :disabled="!formChanged" @click="onSubmit">注册
            </el-button>
          </el-form-item>

        </el-form>
      </el-col>
      <el-col :span="6">
        <el-row>
          <el-card class="box-card" :body-style="{ padding: '0px' }">
            <div class="card-left-icon">
              <i class="el-icon-s-help svg-icon" />
            </div>
            <div class="card-right-text">
              <div class="card-right-title">总任务量</div>
              <div class="card-right-data">{{ totalTaskCount }}</div>
            </div>
          </el-card>
        </el-row>
        <el-row style="margin-top: 20px">
          <el-card class="box-card" :body-style="{ padding: '0px' }">
            <div class="card-left-icon">
              <i class="el-icon-s-claim svg-icon" />
            </div>
            <div class="card-right-text">
              <div class="card-right-title">已分配</div>
              <div class="card-right-data">{{ totalUserTaskCount }}</div>
            </div>
          </el-card>
        </el-row>
      </el-col>
    </el-row>
  </div>
</template>

<script>

import { getUserList } from '@/api/user'

export default {
  name: 'NewTask',

  data() {
    const validNewsCount = (rule, value, callback) => {
      if (!value) {
        callback(new Error('标注任务总数不能为空'))
      } else if (this.form.newsCount > this.totalNewsCount) {
        callback(new Error('发布任务量之和不能大于未标注新闻总数'))
      } else {
        callback()
      }
    }
    return {
      message: '',
      userTask: [],
      totalNewsCount: 1000,
      form: {
        newsCount: 0,
        ratio: 3,
        userTask: []
      },
      formRules: {
        newsCount: [{ required: true, trigger: 'blur', validator: validNewsCount }],
        ratio: [{ required: true, trigger: 'blur', message: '请选择每篇新闻需要几人标注' }],
        userTask: [{ required: true, trigger: 'blur', message: '请选择标注人' }]
      },
      showDialog: false,
      formChanged: false
    }
  },

  computed: {
    totalTaskCount: function() {
      return this.form.newsCount * this.form.ratio
    },
    totalUserTaskCount: function() {
      let total = 0
      for (const user of this.form.userTask) {
        total += user.taskCount
      }
      return total
    }
  },

  watch: {
    form: {
      handler: function(form, oldval) {
        this.formChanged = true
      },
      deep: true, // 对象内部的属性监听，也叫深度监听
      immediate: false
    }
  },
  mounted: function() {
    getUserList().then(response => {
      const data = response.data
      const userTask = []
      for (const userVo of data) {
        userTask.push({
          username: userVo.user.username,
          name: userVo.user.name,
          taskCount: 0
        })
      }
      this.userTask = userTask
    })
  },

  methods: {
    onReset() {
      this.form = {
        newsCount: 0,
        userTask: []
      }
      for (const user of this.userTask) {
        user.taskCount = 0
      }
      this.formChanged = false
      // 清除所有校验结果
      this.$refs.form.clearValidate()
    },
    onSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          let total = 0
          if (this.form.newsCount > this.totalNewsCount) {
            this.$message.error('发布任务量之和不能大于未标注新闻总数')
            return false
          }
          for (const user of this.form.userTask) {
            total += user.taskCount
          }
          if (total !== this.form.newsCount) {
            this.$message.error('所有标注者任务量之和不等于发布任务总数')
            return false
          } else {

          }
        } else {
          this.$message.error('输入的信息有误，请检查后提交')
          return false
        }
      })
    }
  }
}
</script>

<style scoped>

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

