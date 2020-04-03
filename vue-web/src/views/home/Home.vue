<template>
  <div id="Home">
    <div class="dashboard-container">
      <div class="dashboard-text">name:{{ name }}</div>
      <div class="dashboard-text">
        roles:<span v-for="(role,index) in roles" :key="index">{{ role }}</span>
      </div>
      <el-row>
        <el-button type="primary" @click="dialogVisible=true">修改密码</el-button>
      </el-row>
      <div>
        <el-dialog
          title="请标注两篇新闻是否讲述同一事件"
          width="60%"
          :center="true"
          :close-on-press-escape="false"
          :visible.sync="dialogVisible">
          <el-row>
            <el-form ref="form" :model="form" :rules="formRules" label-width="80px">
              <el-form-item label="新密码" prop="password">
                <el-input v-model="form.password"></el-input>
              </el-form-item>
              <el-form-item label="再次输入" prop="passwordAgain">
                <el-input v-model="form.passwordAgain"></el-input>
              </el-form-item>
            </el-form>
          </el-row>
          <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="submitForm">确认修改</el-button>
          </div>
        </el-dialog>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { changePassword } from '@/api/user'

export default {
  name: 'Home',
  data() {
    const validPassword = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入新密码'))
      } else if (value.toString().length < 6 || value.toString().length > 30) {
        callback(new Error('密码长度为6 - 30个字符'))
      } else {
        callback()
      }
    }
    const validPasswordAgain = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.form.password) {
        callback(new Error('两次输入密码不一致'))
      } else {
        callback()
      }
    }
    return {
      dialogVisible: false,
      formRules: {
        password: [{ required: true, trigger: 'blur', validator: validPassword }],
        passwordAgain: [{ required: true, trigger: 'blur', validator: validPasswordAgain }]
      },
      form: {
        password: '',
        passwordAgain: ''
      }

    }
  },
  methods: {
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          changePassword(this.form.password).then(response => {
            this.$message.success(response.msg)
          })
        }
      })
    }
  },
  computed: {
    ...mapGetters(['name', 'roles'])
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }

  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
</style>

<style scoped></style>
