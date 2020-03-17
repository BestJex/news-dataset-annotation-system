<template>
  <div class="app-container">
    <el-row>
      <el-col :span="6">
        <el-card class="box-card" :body-style="{ padding: '0px' }">
          <div class="card-left-icon">
            <i class="el-icon-s-help svg-icon"></i>
          </div>
          <div class="card-right-text">
            <div class="card-right-title">新闻总数</div>
            <div class="card-right-data">2390</div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="box-card" :body-style="{ padding: '0px' }">
          <div class="card-left-icon">
            <i class="el-icon-s-claim svg-icon"></i>
          </div>
          <div class="card-right-text">
            <div class="card-right-title">累计标注</div>
            <div class="card-right-data">358</div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="box-card" :body-style="{ padding: '0px' }">
          <div class="card-left-icon">
            <i class="el-icon-s-check svg-icon"></i>
          </div>
          <div class="card-right-text">
            <div class="card-right-title">待校验</div>
            <div class="card-right-data">18</div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="box-card" :body-style="{ padding: '0px' }">
          <div class="card-left-icon">
            <i class="el-icon-s-promotion svg-icon"></i>
          </div>
          <div class="card-right-text">
            <div class="card-right-title">进行中</div>
            <div class="card-right-data">0</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row style="margin-top: 20px">
      <el-col :span="24">
        <el-table
          :data="tableData.filter(data => !search || data.userId.toString().toLowerCase().includes(search.toLowerCase())
            || data.username.toLowerCase().includes(search.toLowerCase())
            || data.name.toLowerCase().includes(search.toLowerCase()))"
          border
          v-loading="loading"
          max-height="600"
          style="width: 100%"
        >
          <el-table-column
            prop="user.userId"
            label="No."
            width="60"
          />
          <el-table-column
            prop="user.username"
            label="用户名"
            width="120"
          />
          <el-table-column
            prop="user.name"
            label="姓名"
            width="120"
          />
          <el-table-column
            prop="user.role"
            label="角色"
            width="180"
            sortable
          >
            <template slot-scope="scope">
              <div class="tag-list">
                <el-tag
                  v-for="(item,index) in scope.row.user.role"
                  :key="index"
                  :type="item === 'admin' ? 'success' : 'primary'"
                  disable-transitions
                >{{ item === 'admin' ? '管理员' : '标注用户' }}
                </el-tag>
              </div>
            </template>
          </el-table-column>
          <el-table-column
            prop="completeTaskCount"
            label="总计完成"
            width="120"
            sortable
          />
          <el-table-column
            prop="revisionTaskCount"
            label="待校对"
            width="100"
            sortable
          />
          <el-table-column
            prop="undoTaskCount"
            label="进行中"
            width="100"
            sortable
          />
          <el-table-column
            prop="undoTaskCount"
            label="进度"
            width="200"
            sortable
          >
            <template slot-scope="scope">
              <el-progress
                :percentage="parseInt(100*scope.row.revisionTaskCount /(( scope.row.undoTaskCount + scope.row.revisionTaskCount)==0?1:( scope.row.undoTaskCount + scope.row.revisionTaskCount)))"
                :color="customColors"></el-progress>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
          >
            <template slot="header">
              <el-input
                v-model="search"
                size="mini"
                placeholder="输入关键字搜索"
              />
            </template>
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="danger"
                @click="onDeleteRow(scope.$index, scope.row)"
              >重置标注
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
    <div v-if="showDialog">
      <el-dialog title="编辑信息" :visible.sync="showDialog">
        <el-form :model="editRow" label-width="120px">
          <el-form-item label="备注信息">
            <el-col :span="16">
              <el-input v-model="editRow.remark" type="textarea" rows="5" />
            </el-col>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="showDialog = false">取 消</el-button>
          <el-button type="primary" @click="onSaveEditRow()">保 存</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import { deepCopy } from '@/utils'
import { deleteUser, getUserList, updateUser } from '@/api/user'

export default {
  name: 'UserList',

  data() {
    return {
      tableData: [],
      search: '',
      showDialog: false,
      editRow: null,
      customColors: [
        { color: '#f56c6c', percentage: 20 },
        { color: '#e6a23c', percentage: 40 },
        { color: '#5cb87a', percentage: 60 },
        { color: '#1989fa', percentage: 80 },
        { color: '#6f7ad3', percentage: 100 }
      ],
      loading: true
    }
  },

  mounted: function() {
    const _this = this
    getUserList().then(response => {
      _this.tableData = response.data
      _this.loading = false
    }).catch(() => {
    })
  },

  methods: {
    filterLabel(value, row) {
      return row.label === value
    },
    filterCategory(value, row) {
      return row.category === value
    },
    onEditRow(index, row) {
      this.editRow = deepCopy(row)
      this.showDialog = true
    },
    onSaveEditRow() {
      const _this = this
      const row = this.editRow
      updateUser(this.editRow).then(response => {
        console.log(this.editRow.username)
        this.tableData.filter(function(item) {
          if (item.id === row.id) {
            item.remark = row.remark
            _this.$message({
              message: '保存成功！',
              type: 'success',
              center: true,
              duration: 2000
            })
          }
          return item.id === row.id
        })
      }).then(() => {
        _this.editRow = null
        _this.showDialog = false
      })
    },
    onDeleteRow(index, row) {
      const userinfo = []
      const msg = []
      userinfo.push('要删除的用户信息如下')
      userinfo.push('用户ID ：' + row.userId)
      userinfo.push('用户名 ：' + row.username)
      userinfo.push('姓名 ：' + row.name)
      userinfo.push('此操作将永久删除该用户信息, 是否继续?')
      const h = this.$createElement
      for (const i in userinfo) {
        msg.push(h('p', null, userinfo[i]))
      }
      this.$confirm('警告提示', {
        title: '警告提示',
        message: h('div', null, msg),
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        deleteUser(row.username).then(response => {
          this.tableData.splice(this.tableData.indexOf(row), 1)
          this.$message({
            message: response.msg,
            type: 'success',
            center: true,
            duration: 2000
          })
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    }
  }
}
</script>

<style scoped>
.tag-list >span {
  margin-right: 10px;
  margin-top: 5px;
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
.card-right-text{
  float: right;
  text-align: right;
  /*margin: 2rem 8rem 8rem 0;*/
}
.card-left-icon{
  margin: 5% 0;
  padding: 2%;
  float: left;
  /*background-color: #1482f0;*/
  /*border-radius: 50%;*/
}
.card-left-icon .svg-icon{
  font-size: 40px;
  color: #ffffff;
}
.card-right-title{
  color: #ffffff;
  font-size: 15px;
  font-weight: bold;
  padding: 5px 0;
}
.card-right-data{
  color: #ffffff;
  font-size: 26px;
  font-weight: bolder;
}
</style>
