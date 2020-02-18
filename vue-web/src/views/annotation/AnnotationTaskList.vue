<template>
  <div class="app-container">
    <el-row>
      <el-col :span="24">
        <el-table
          :data="tableData.filter(data => !search || data.news_title.toString().toLowerCase().includes(search.toLowerCase()))"
          height="600"
          border
          style="width: 100%"
        >
          <el-table-column
            prop="id"
            label="No."
            width="60"
          />
          <el-table-column
            prop="_id"
            label="mongo_id"
            width="180"
          />
          <el-table-column
            prop="_id"
            label="标注人1"
            width="120"
          />
          <el-table-column
            prop="_id"
            label="标注人2"
            width="120"
          />
          <el-table-column
            prop="_id"
            label="标注对比情况"
            width="180"
          />

          <el-table-column
            prop="state"
            label="状态"
            width="120"
            sortable
            :filters="[{ text: '待标注', value: 0 }, { text: '待校对', value: 1 },{ text: '已完成', value: 2 }]"
            :filter-method="filterState"
            filter-placement="bottom-end"
          >
            <template slot-scope="scope">
              <el-tag
                :type="scope.row.state === 0 ? 'primary' : scope.row.state === 1 ? 'success' : 'danger'"
                disable-transitions
              >{{ scope.row.state === 0 ? '待标注' : scope.row.state === 1 ? '待校对' : '已完成' }}
              </el-tag>
            </template>
          </el-table-column>

          <el-table-column align="center">
            <template slot="header" slot-scope="scope">
              <el-input
                v-model="search"
                size="mini"
                placeholder="输入关键字搜索"
              />
            </template>
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="success"
                @click="onEditRow(scope.$index, scope.row)"
              >编辑
              </el-button>
              <el-button
                size="mini"
                type="danger"
                @click="onDeleteRow(scope.$index, scope.row)"
              >删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'AnnotationTaskList',

  data() {
    return {
      tableData: [],
      search: ''
    }
  },

  mounted: function() {

  },

  methods: {
    filterState(value, row) {
      return row.state === value
    },
    onDeleteRow(index, row) {
      const articleInfo = []; const msg = []
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
    }

  }
}
</script>

<style scoped>

</style>
