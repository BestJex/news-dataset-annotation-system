<template>
  <div>
    <el-row style="padding: 20px">
      <el-col :span="12">
        <span style="font-size: 14px">标注进度 - {{ currentNo }}/600</span>
        <el-progress :text-inside="true" :stroke-width="5" :percentage="42" />
      </el-col>
      <el-col :span="12">
        <el-button-group style="float:right;">
          <el-button type="primary" icon="el-icon-arrow-left" @click="onPreNews">上一篇</el-button>
          <el-button type="primary" @click="onNextNews">下一篇<i class="el-icon-arrow-right el-icon--right" /></el-button>
        </el-button-group>
      </el-col>
    </el-row>
    <el-row style="padding: 20px" :gutter="20">
      <el-col :span="12">
        <div style="color: dodgerblue;font-size: 16px;margin-bottom: 5px">原文</div>
        <el-input
          v-model="content"
          style="margin-bottom: 20px"
          type="textarea"
          :rows="15"
          placeholder="请输入内容"
        />
        <div style="color: dodgerblue;font-size: 16px;margin-bottom: 5px">翻译</div>
        <el-input
          v-model="content_translate"
          type="textarea"
          :rows="15"
          placeholder="请输入内容"
        />
      </el-col>

      <el-col :span="12">
        <el-form ref="form" :model="form" :rules="formRules" label-width="80px">
          <el-form-item label="情感">
            <el-checkbox-group v-model="form.emotion">
              <el-checkbox label="快乐(PA)" name="emotion" />
              <el-checkbox label="安心(PE)" name="emotion" />

              <el-checkbox label="尊敬(PD)" name="emotion" />
              <el-checkbox label="赞扬(PH)" name="emotion" />
              <el-checkbox label="相信(PG)" name="emotion" />
              <el-checkbox label="喜爱(PB)" name="emotion" />
              <el-checkbox label="祝愿(PK)" name="emotion" />

              <el-checkbox label="愤怒(NA)" name="emotion" />

              <el-checkbox label="悲伤(NB)" name="emotion" />
              <el-checkbox label="失望(NJ)" name="emotion" />
              <el-checkbox label="内疚(NH)" name="emotion" />
              <el-checkbox label="思念(PF)" name="emotion" />

              <el-checkbox label="恐慌(NI)" name="emotion" />
              <el-checkbox label="恐惧(NC)" name="emotion" />
              <el-checkbox label="羞愧(NG)" name="emotion" />

              <el-checkbox label="烦闷(NE)" name="emotion" />
              <el-checkbox label="憎恶(ND)" name="emotion" />
              <el-checkbox label="贬责(NN)" name="emotion" />
              <el-checkbox label="妒忌(NK)" name="emotion" />
              <el-checkbox label="怀疑(NL)" name="emotion" />

              <el-checkbox label="惊奇(PC)" name="emotion" />
            </el-checkbox-group>
          </el-form-item>
          <el-form-item label="立场">
            <el-select v-model="form.position" placeholder="请选择新闻立场">
              <el-option label="消极" value="negative" />
              <el-option label="中级" value="nature" />
              <el-option label="积极" value="positive" />
            </el-select>
          </el-form-item>
          <el-form-item label="事件主体">
            <el-select v-model="form.eventSubject" placeholder="请选择事件主体">
              <el-option label="中国" value="China" />
              <el-option label="非中国" value="No-China" />
            </el-select>
          </el-form-item>
          <el-form-item label="主题">
            <el-radio-group v-model="form.theme">
              <el-radio label="国际" />
              <el-radio label="体育" />
              <el-radio label="娱乐" />
              <el-radio label="社会" />
              <el-radio label="财经" />
              <el-radio label="时事" />
              <el-radio label="科技" />
              <el-radio label="情感" />
              <el-radio label="教育" />
              <el-radio label="军事" />
              <el-radio label="文化" />
              <el-radio label="历史" />
            </el-radio-group>
          </el-form-item>
          <el-form-item label="类型">
            <el-select v-model="form.type" placeholder="请选择新闻类型">
              <el-option label="事实" value="1" />
              <el-option label="短文" value="2" />
              <el-option label="采访" value="3" />
              <el-option label="无关新闻" value="4" />

            </el-select>
          </el-form-item>
          <el-form-item label="情感依据">
            <el-input v-model="form.emotion_basis" type="textarea" :rows="6" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSubmit">立即提交</el-button>
            <el-button>重置</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>

    <el-dialog title="请标注两篇新闻是否讲述同一事件" width="80%" :visible.sync="compareDialogVisible">
      <el-row style="padding: 20px" :gutter="20">
        <el-col :span="12">
          <div style="color: dodgerblue;font-size: 16px;margin-bottom: 5px">上一篇新闻</div>
          <el-input
            v-model="content_translate"
            style="margin-bottom: 20px"
            type="textarea"
            :rows="15"
            placeholder="请输入内容"
          />
        </el-col>
        <el-col :span="12">
          <div style="color: dodgerblue;font-size: 16px;margin-bottom: 5px">当前新闻</div>
          <el-input
            v-model="content_translate"
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
import { Message } from 'element-ui'

export default {
  name: 'NewArticle',

  data() {
    return {
      content: 'The main European stock exchanges were this Friday morning, awaiting the employment report in the United States and concerned about the spread of the coronavirus.\n' +
        'Nearby 08h55 in Lisbon, EuroStoxx 600 descended 0.11% to 425,01 points. The stock exchanges in London and Frankfurt retreated 0,14% and 0,28% as well as those in Madrid and Milan which descended 0,13% and 0,07% respectively. Paris was the exception since it was going up 0,04%.\n' +
        'After opening downwards, the Lisbon Stock Exchange continued the trend and, around the 8h55, the main index, the PSI20, retreated 0.20% to 5.277,10 points.\n' +
        'China\'s president, Xi Jinping, has informed US counterpart Donald Trump that the economic development of his country will not be affected in the long term due to the coronavirus crisis. In recent days, the market had doubted China\'s ability to meet the commitments made in the trade agreement between the two countries due to the impact of the coronavirus.\n' +
        'The epidemic continues to attract the attention of investors, yet today they are waiting for the employment report in the United States. The spread of the coronavirus has already affected consumption, industrial production and other economic sectors, and the consequences are also reflected in the raw material market, where there is expected to be a decline in oil demand.\n' +
        'However, it was found today that German exports grew less than expected in December and that in Spain industrial production continued to slow down in 2019.\n' +
        'The New York Stock Exchange ended on Thursday with the Dow Jones moving forward 0.30% to 29.379,77 points, a new maximum since it was created in 1896. In the same sense, Nasdaq closed to value itself 0,67% to 9.572,15 points, a new maximum ever.\n' +
        'At the exchange rate level, the euro today opened downwards in the Frankfurt exchange market, quoting itself at 10968 dollars, against 10975 dollars on Thursday\n' +
        'Yeah. Brent\'s oil barrel for delivery in April 2020 opened today low, quoting 58,16 dollars in the London Intercontinental Exchange Futures (ICE) against 58,16 dollars on Thursday and 53,96 dollars on Tuesday, a minimum since 26 in December 2018.',
      content_translate: '欧洲主要证券交易所本周五上午在美国等待就业报告，并担心冠状病毒的传播。\n' +
        '里斯本08h55附近，欧洲斯托克600指数下跌0.11%，至425.01点。伦敦和法兰克福股市分别下跌0.14%和0.28%，马德里和米兰股市分别下跌0.13%和0.07%。巴黎是个例外，因为它上涨了0.04%。\n' +
        '里斯本证交所在开盘下跌后，延续了这一趋势，在8时55分左右，主要股指PSI20下跌0.20%，至5.277,10点。\n' +
        '中国国家主席习近平已经通知美国总统特朗普，中国的经济发展在长期内不会因为冠状病毒危机而受到影响。最近几天，由于冠状病毒的影响，市场怀疑中国是否有能力履行两国在贸易协定中所作的承诺。\n' +
        '这一流行病继续引起投资者的注意，但今天他们正在等待美国的就业报告。冠状病毒的传播已经影响到消费、工业生产和其他经济部门，其后果也反映在原材料市场，预计那里的石油需求将下降。\n' +
        '不过，今天发现，德国12月出口增长低于预期，西班牙工业生产在2019年继续放缓。\n' +
        '纽约证券交易所星期四结束，道琼斯工业指数（琼斯）上涨0.30%，至27.799点，77点，创下1896以来的新高。同样意义上，纳斯达克收盘值为0.67，至7.57点15点，创下历史新高。\n' +
        '在汇率水平上，今天欧元在法兰克福外汇市场低开，报10968美元，周四为10975美元\n' +
        '是 啊。布伦特2020年4月交货的石油桶今日低开，伦敦洲际交易所期货（ICE）报58,16美元，周四报58,16美元，周二报53,96美元，为2018年12月26日以来最低。',
      form: {
        emotion: [],
        position: '',
        eventSubject: '',
        theme: '',
        type: '',
        emotion_basis: ''
      },
      currentNo: 253,
      compareDialogVisible: false,
      formLabelWidth: '120px',
      formRules: {
        title: [{ required: true, trigger: 'blur', message: '姓名不能为空' }],
        category: [{ required: true, trigger: 'blur', message: '性别不能为空' }],
        label: [{ required: true, trigger: 'blur', message: '打卡类型不能为空' }]
      }
    }
  },

  watch: {
    currentNo: function(oldVal, newVal) {
      if (newVal % 2 === 0) {
        this.compareDialogVisible = true
      }
    }
  },

  methods: {
    onSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          // addAttendance(this.form).then(response => {
          //   this.$message({
          //     message: '打卡成功！',
          //     type: 'success',
          //     center: true,
          //     duration: 3000
          //   })
          const _this = this
          this.$message({
            message: '打卡成功！',
            type: 'success',
            center: true,
            duration: 3000
          })
          setTimeout(function() {
            _this.$router.push('/annotation/list')
          }, 1000)
        } else {
          console.log('输入数据不合法！')
          return false
        }
      })
    },

    onPreNews() {
      this.currentNo -= 1
    },

    onNextNews() {
      this.currentNo += 1
    }
  }
}
</script>

<style scoped>
.el-radio {
  margin-bottom: 10px;
}
</style>
