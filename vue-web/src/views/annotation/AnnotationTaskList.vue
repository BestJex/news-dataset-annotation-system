<template>
  <div class="app-container">
    <el-row style="margin-bottom: 10px" :gutter="20">
      <el-col :span="5">
        <el-input
          v-model="search"
          size="medium"
          placeholder="输入关键字搜索"
        />
      </el-col>
      <el-col :span="7">
        <el-button size="medium" type="primary" @click="checkAnnotation">一键校验</el-button>
        <el-button size="medium" type="primary" icon="el-icon-refresh" @click="initTable"></el-button>
      </el-col>
      <el-col :span="12" style="margin: 5px auto;vertical-align: middle;text-align: right;display:table-cell">
        <div style="height: 100%">
          <el-pagination
            style="display: inline-block"
            background
            layout="total, prev, pager, next"
            :total="tableData.length"
          />
        </div>
      </el-col>

    </el-row>
    <el-row>
      <el-col :span="24">
        <el-table
          v-loading="loading"
          :data="tableData"
          height="550"
          border
          style="width: 100%"
          :loading="loading"
        >
          <el-table-column
            label="No."
            type="index"
            width="50"
          />
          <el-table-column
            prop="id"
            label="ObjectId"
            width="120"
            :show-overflow-tooltip="true"
          >
            <template slot-scope="scope">
              <a
                style="text-underline: none;color: #409EFF"
                @click="onCopySuccess(scope.row.id)"
              >{{ scope.row.id }}</a>
            </template>
          </el-table-column>
          <el-table-column
            prop="news_title_translate_cn"
            label="标题"
            width="280"
          />
          <el-table-column
            prop="news_country"
            label="国家"
            width="100"
          />
          <el-table-column
            prop="news_publish_timestamp"
            label="新闻发布时间"
            width="170"
          >
            <template slot-scope="scope">
              {{ new Date(scope.row.news_publish_timestamp*1000).Format('yy-MM-dd hh:mm:ss') }}
            </template>
          </el-table-column>
          <el-table-column
            prop="users"
            label="标注人"
            width="150"
          >
            <template slot-scope="scope">
              <div class="tag-list">
                <el-tag
                  v-for="(item,index) in scope.row.users.filter(user=>{return user!=='FinalCheck'})"
                  :key="index"
                  size="small"
                  :type="scope.row.news_position[index]===null?'info':'success'"
                  disable-transitions
                >{{ item }}
                </el-tag>
              </div>
            </template>
          </el-table-column>
          <el-table-column
            prop="_id"
            label="标注进度"
            width="80"
          >
            <template slot-scope="scope">
              {{ scope.row.news_annotation_done.filter(data=>{return data!=null}).length }} /
              {{ scope.row.news_annotation_done.length - 1 }}
            </template>
          </el-table-column>

          <el-table-column
            prop="news_state"
            label="状态"
            width="120"
            sortable
            :filters="[{ text: '待标注', value: 0 }, { text: '标注中', value: 1 },{ text: '待校对', value: 2 },{ text: '已完成', value: 3 }]"
            :filter-method="filterState"
            filter-placement="bottom-end"
          >
            <template slot-scope="scope">
              <el-tag
                :type="news_state_tag[scope.row.news_state]"
                disable-transitions
              >{{ news_state[scope.row.news_state.toString()] }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            prop="_id"
            label="操作"
            align="center"
          >
            <template slot-scope="scope">
              <el-popover
                placement="right"
                width="1000"
                trigger="hover"
              >
                <table class="annotation-compare-table">
                  <tr>
                    <td class="column">标注人</td>
                    <td class="column">情感</td>
                    <td class="column">立场</td>
                    <td class="column">主题</td>
                    <td class="column">文章类型</td>
                    <td class="column">是否中国</td>
                    <td class="column">情感依据段落</td>
                  </tr>
                  <tr v-for="(item,index) in scope.row.users" :key="index">
                    <td class="column">{{ item }}</td>
                    <td style="width: 300px">{{ scope.row.news_emotion[index]==null?'待标注':scope.row.news_emotion[index]
                      }}
                    </td>
                    <td>{{ scope.row.news_position[index]==null?'待标注':scope.row.news_position[index] }}</td>
                    <td>{{ scope.row.news_subject[index]==null?'待标注':scope.row.news_subject[index] }}</td>
                    <td>{{ scope.row.news_type[index]==null?'待标注':scope.row.news_type[index] }}</td>
                    <td>{{ scope.row.news_about_china[index]==null?'待标注':scope.row.news_about_china[index] }}</td>
                    <td style="width: 250px">{{ scope.row.news_emotion_basis[index]==null?'待标注':scope.row.news_emotion_basis[index] }}</td>
                  </tr>
                </table>
                <!--                <el-button type="primary">仲裁</el-button>-->
                <!--                <el-button>取消</el-button>-->
                <el-button
                  slot="reference"
                  size="mini"
                  type="primary"
                  @mouseenter.native="showAnnotationTable(scope.$index,scope.row)"
                >预览
                </el-button>
              </el-popover>
              <el-popover
                placement="right"
                width="900"
                trigger="hover"
              >
                <table class="annotation-compare-table news-detail-table">
                  <tr>
                    <td class="column">ObjectId</td>
                    <td>
                      <a
                        style="text-underline: none;color: #409EFF"
                        @click="onCopySuccess(scope.row.id)"
                      >{{ scope.row.id }}</a>
                    </td>
                  </tr>
                  <tr>
                    <td class="column">新闻标题翻译</td>
                    <td>{{ scope.row.news_title_translate_cn }}</td>
                  </tr>
                  <tr>
                    <td class="column">发布时间</td>
                    <td>{{ new Date(scope.row.news_publish_timestamp*1000).Format('yy-MM-dd hh:mm:ss') }}</td>
                  </tr>
                  <tr>
                    <td class="column">原文链接</td>
                    <td><a :href="scope.row.news_detail_url" target="_blank"> {{ scope.row.news_detail_url }}</a></td>
                  </tr>
                  <tr>
                    <td class="column">正文翻译</td>
                    <td>
                      <el-input type="textarea" rows="15" :value="scope.row.news_content_translate_cn" />
                    </td>
                  </tr>
                </table>
                <el-button slot="reference" size="mini" type="success">更多</el-button>
              </el-popover>
            </template>
          </el-table-column>
          <!--          <el-table-column align="center">-->

          <!--            <template slot-scope="scope">-->
          <!--              <el-button-->
          <!--                size="mini"-->
          <!--                type="success"-->
          <!--                @click="onEditRow(scope.$index, scope.row)"-->
          <!--              >编辑-->
          <!--              </el-button>-->
          <!--              <el-button-->
          <!--                size="mini"-->
          <!--                type="danger"-->
          <!--                @click="onDeleteRow(scope.$index, scope.row)"-->
          <!--              >删除-->
          <!--              </el-button>-->
          <!--            </template>-->
          <!--          </el-table-column>-->
        </el-table>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getUsername } from '@/utils/authorize'
import { checkAnnotation, getAnnotationListByUsername } from '@/api/annotation'

export default {
  name: 'AnnotationTaskList',

  data() {
    return {
      taskList: [],
      search: '',
      username: '',
      annotationTable: [],
      loading: true,
      news_state: {
        '0': '待标注',
        '1': '标注中',
        '2': '待校验',
        '4': '待仲裁',
        '10': '已完成'
      },
      news_state_tag: {
        '0': 'info',
        '1': 'primary',
        '2': 'warning',
        '4': 'danger',
        '10': 'success'
      }
    }
  },

  computed: {
    tableData: function() {
      return this.taskList.filter(data => !this.search ||
        data.id.toString().toLowerCase().includes(this.search.toLowerCase()) ||
        data.news_title_translate_cn.toString().toLowerCase().includes(this.search.toLowerCase()) ||
        data.news_country.toString().toLowerCase().includes(this.search.toLowerCase()) ||
        data.news_publish_timestamp.toString().toLowerCase().includes(this.search.toLowerCase()) ||
        data.news_content_translate_cn.toString().toLowerCase().includes(this.search.toLowerCase()))
    }
  },

  mounted: function() {
    this.username = getUsername()
    this.initTable()
  },

  methods: {
    initTable() {
      this.loading = true
      getAnnotationListByUsername().then(response => {
        const data = response.data
        this.taskList = data
      }).then(() => {
        this.$nextTick(() => {
          this.$message.info('已更新最新数据')
          this.loading = false
        })
      })
    },
    checkAnnotation() {
      checkAnnotation().then(response => {
        this.$message.success('成功校验 ' + response.data.complete + ' 条数据')
      })
    },
    filterState(value, row) {
      return row.news_state === value
    },
    onDeleteRow(index, row) {
      const articleInfo = []
      const msg = []
      articleInfo.push('要删除的文章信息如下')
      articleInfo.push('文章名：' + row.title)
      articleInfo.push('创建时间：' + row.createTime)
      // articleInfo.push('浏览量 ：' + row.readTimes)
      articleInfo.push('此操作将永久删除该文章信息, 是否继续?')
      const h = this.$createElement
      for (const i in articleInfo) {
        if (articleInfo.hasOwnProperty(i)) {
          if (articleInfo[i].indexOf('文章名') !== -1) {
            msg.push(h('p', { style: 'color: #F56C6C' }, articleInfo[i]))
          } else {
            msg.push(h('p', null, articleInfo[i]))
          }
        }
      }
      this.$confirm('警告提示', {
        title: '警告提示',
        message: h('div', null, msg),
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        // deleteArticle(row).then(response => {
        //   this.tableData.splice(this.tableData.indexOf(row), 1)
        //   this.$message({
        //     message: response.msg,
        //     type: 'success',
        //     center: true,
        //     duration: 2000
        //   })
        // })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },

    showAnnotationTable(index, row) {
      console.log(row)
    },

    onCopySuccess(content) {
      this.$copyText(content).then(() => {
        this.$message({
          message: '已复制: ' + content,
          type: 'success'
        })
      }, function(e) {
        this.$message({
          message: '复制失败',
          type: 'error'
        })
      })
    },

    onCopyError() {

    }

  }
}
</script>

<style scoped>
.tag-list > span {
  margin-right: 10px;
  margin-top: 5px;
}

.annotation-compare-table, .annotation-compare-table tr, .annotation-compare-table tr td {
  border: 1px solid #E6EAEE;
  border-collapse: collapse;
}

.annotation-compare-table {
  font-size: 14px;
  color: #71787E;
}

.annotation-compare-table tr td {
  border: 1px solid #E6EAEE;
  width: 150px;
  height: 35px;
  line-height: 35px;
  box-sizing: border-box;
  padding: 0 10px;
}

.annotation-compare-table {
  margin: 20px;
}

.annotation-compare-table tr td.column {
  width: 170px !important;
  background-color: #EFF3F6;
  color: #393C3E;
}

.news-detail-table tr td {
  width: 700px !important;
}
</style>
