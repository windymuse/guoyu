<template>
  <div class="app-container">
    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <!--<el-input v-model="listQuery.userId" clearable class="filter-item" style="width: 200px;" placeholder="请输入用户ID"/>-->
      <el-button
        v-permission="['operation:freight:create']"
        class="filter-item"
        type="primary"
        icon="el-icon-search"
        @click="createHandle"
      >添加</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table
      v-loading="listLoading"
      :data="list"
      size="small"
      element-loading-text="正在查询中。。。"
      border
      fit
      highlight-current-row
    >
      <el-table-column align="center" label="模板编号" prop="freightTemplateDO.id" />

      <el-table-column align="center" label="模板名称" prop="freightTemplateDO.templateName" />

      <el-table-column align="center" label="发货期限" prop="freightTemplateDO.deliveryDeadline">
        <template slot-scope="scope">{{ scope.row.freightTemplateDO.deliveryDeadline }}天</template>
      </el-table-column>
      <el-table-column align="center" label="默认包邮门栏" prop="freightTemplateDO.defaultFreePrice" >
        <template slot-scope="scope">{{ scope.row.freightTemplateDO.defaultFreePrice | defaultFreePriceFilter }}</template>
      </el-table-column>
      <el-table-column align="center" label="免费配送距离(公里)" prop="freightTemplateDO.deliverDistanceFree" />
      <el-table-column align="center" label="最大配送距离(公里)" prop="freightTemplateDO.deliverDistanceMax" />
      <el-table-column align="center" label="默认计费首次发货数量" prop="freightTemplateDO.defaultFirstNum" >
        <template slot-scope="scope">{{ scope.row.freightTemplateDO.defaultFirstNum }}件</template>
      </el-table-column>
      <el-table-column align="center" label="默认计费首次发货价格" prop="freightTemplateDO.defaultFirstMoney">
        <template slot-scope="scope">{{ scope.row.freightTemplateDO.defaultFirstMoney }}元</template>
      </el-table-column>
      <el-table-column align="center" label="默认计费续件数量" prop="freightTemplateDO.defaultContinueNum" >
        <template slot-scope="scope">{{ scope.row.freightTemplateDO.defaultContinueNum }}件</template>
      </el-table-column>
      <el-table-column align="center" label="默认计费续件价格" prop="freightTemplateDO.defaultContinueMoney">
        <template slot-scope="scope">{{ scope.row.freightTemplateDO.defaultContinueMoney }}元</template>
      </el-table-column>
      <el-table-column align="center" label="指定地区数量" prop="freightTemplateCarriageDOList.length"/>
      <el-table-column align="center" label="操作" width="200" fixed="right" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <!-- <el-button
            v-permission="['operation:freight:query']"
            type="primary"
            size="mini"
            @click="(scope.row)"
          >阅读</el-button> -->
          <el-button
            v-permission="['operation:freight:update']"
            type="primary"
            size="mini"
            @click="updateBtn(scope.row)"
          >修改</el-button>
          <el-button
            v-permission="['operation:freight:delete']"
            type="danger"
            size="mini"
            @click="deleteBtn(scope.row)"
          >删除</el-button>
        </template>

      </el-table-column>
    </el-table>

    <!-- 添加或修改对话框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" :fullscreen="true" :close-on-press-escape="false">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="dataForm"
        status-icon
        label-position="left"
        label-width="150px"
        style="width: 700px; margin-left:50px;"
      >
        <el-form-item v-if="dialogStatus === 'update'" label="隐藏的用户id" prop="templateId" hidden>
          <el-input v-model="dataForm.templateId" clearable placeholder=""/>
        </el-form-item>
        <el-form-item label="模板名称" prop="templateName">
          <el-input v-model="dataForm.templateName" clearable placeholder=""/>
        </el-form-item>
        <el-form-item label="发货地址" prop="spuLocation">
          <el-input v-model="dataForm.spuLocation" clearable placeholder=""/>
        </el-form-item>
        <el-form-item label="发货期限" prop="deliveryDeadline">
          <el-input v-model.number="dataForm.deliveryDeadline" clearable placeholder="">
            <template slot="append">天</template>
          </el-input>
        </el-form-item>
        <el-form-item label="是否包邮" prop="isFree">
          <template>
            <el-radio-group v-model="dataForm.isFree">
              <el-radio :label="-1">坚决不包邮</el-radio>
              <el-radio :label="0">卖家承担包邮</el-radio>
              <el-radio :label="1">设定满额包邮</el-radio>
              <el-radio :label="-2">按距离包邮</el-radio>
            </el-radio-group>
          </template>
        </el-form-item>
        <el-form-item v-if="dataForm.isFree === 1" label="最低包邮门槛" prop="defaultFreePrice">
          <el-input v-model.number="dataForm.defaultFreePrice" placeholder="最低包邮门槛">
            <template slot="append">元</template>
          </el-input>
        </el-form-item>
        <el-form-item v-if="dataForm.isFree === -2" label="免费配送距离" prop="deliverDistanceFree">
          <el-input v-model.number="dataForm.deliverDistanceFree" placeholder="免费配送距离">
            <template slot="append">公里</template>
          </el-input>
        </el-form-item>
        <el-form-item v-if="dataForm.isFree === -2" label="最大配送距离" prop="deliverDistanceMax">
          <el-input v-model.number="dataForm.deliverDistanceMax" placeholder="最大配送距离">
            <template slot="append">公里</template>
          </el-input>
        </el-form-item>
        <el-form-item label="计费首次数量" prop="defaultFirstNum">
          <el-input v-model.number="dataForm.defaultFirstNum" clearable placeholder="">
            <template slot="append">件</template>
          </el-input>
        </el-form-item>
        <el-form-item label="计费首次价格" prop="defaultFirstPrice">
          <el-input v-model.number="dataForm.defaultFirstPrice" clearable placeholder="">
            <template slot="append">元</template>
          </el-input>
        </el-form-item>
        <el-form-item label="计费续件数量" prop="defaultContinueNum">
          <el-input v-model.number="dataForm.defaultContinueNum" clearable placeholder="">
            <template slot="append">件</template>
          </el-input>
        </el-form-item>
        <el-form-item label="计费续件价格" prop="defaultContinuePrice">
          <el-input v-model.number="dataForm.defaultContinuePrice" clearable placeholder="">
            <template slot="append">元</template>
          </el-input>
        </el-form-item>
        <el-form-item label="指定地区价格" prop="freightTemplateCarriageDOList" style="width:150%">
          <el-button :plain="true" type="primary" @click="handleSpecified">添加</el-button>
          <el-table :data="dataForm.freightTemplateCarriageDOList">
            <el-table-column v-if="dialogStatus==='update'" property="id" label="指定地区ID" />
            <el-table-column property="designatedArea" label="指定省份" />
            <el-table-column property="firstNum" label="首次数量" />
            <el-table-column property="firstMoney" label="首次价格" />
            <el-table-column property="continueNum" label="续件数量" />
            <el-table-column property="continueMoney" label="续件价格" />
            <el-table-column property="freePrice" label="满额包邮门栏" />
            <el-table-column
              align="center"
              label="操作"
              width="100"
              class-name="small-padding fixed-width"
            >
              <template slot-scope="scope">
                <el-button type="danger" size="mini" @click="handleSpecDelete(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>

          <!-- 添加指定地区的Dialog -->
          <el-dialog :visible.sync="specVisiable" :modal="false" :append-to-body="true" top="10vh" width="70%" title="添加指定地区">
            <el-form
              ref="specForm"
              :model="specForm"
              :rules="specRules"
              status-icon
              label-position="left"
              label-width="100px"
              style="width: 400px; margin-left:50px;"
            >
              <el-form-item label="包邮门栏" prop="freePrice">
                <el-input v-model.number="specForm.freePrice" clearable >
                  <template slot="append">元</template>
                </el-input>
              </el-form-item>
              <el-form-item label="首次数量" prop="firstNum">
                <el-input v-model.number="specForm.firstNum" clearable >
                  <template slot="append">件</template>
                </el-input>
              </el-form-item>
              <el-form-item label="首次价格" prop="firstMoney">
                <el-input v-model.number="specForm.firstMoney" placeholder="" clearable>
                  <template slot="append">元</template>
                </el-input>
              </el-form-item>
              <el-form-item label="续件数量" prop="continueNum">
                <el-input v-model.number="specForm.continueNum" placeholder="" clearable>
                  <template slot="append">件</template>
                </el-input>
              </el-form-item>
              <el-form-item label="续件价格" prop="continueMoney">
                <el-input v-model.number="specForm.continueMoney" placeholder="" clearable>
                  <template slot="append">元</template>
                </el-input>
              </el-form-item>
              <el-form-item label="指定地区" prop="designatedAreaList" style="width:150%">
                <template>
                  <el-checkbox-group
                    v-model="specForm.designatedAreaList">
                    <el-checkbox v-for="(item, index) in provs" :label="item" :key="index" >{{ item }}</el-checkbox>
                  </el-checkbox-group>
                </template>
              </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
              <el-button @click="specVisiable = false">取消</el-button>
              <el-button type="primary" @click="handleSpecAdd">确定</el-button>
            </div>
          </el-dialog>

        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createData">确定</el-button>
        <el-button v-else type="primary" @click="updateDate">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<style>
</style>

<script>
import { listFreight, createFreight, deleteFreight, updateFreight } from '@/api/freight'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination

export default {
  name: 'Freight',
  components: { Pagination },
  filters: {
    defaultFreePriceFilter(code) {
      if (code === -1) {
        return '不包邮'
      } else if (code === -2) {
        return '按距离包邮'
      } else if (code === 0) {
        return '包邮'
      } else {
        return '满' + code + '元包邮'
      }
    }
  },
  data() {
    // 校验是否大于零小于10亿
    var isNum = (rule, value, callback) => {
      if (parseInt(value) <= 0) {
        callback(new Error('需要大于零'))
      }
      if (parseInt(value) > 10000000) {
        callback(new Error('需要小于1千万'))
      }
      callback()
    }
    // 校验是否大于等于零小于10亿
    var isPrice = (rule, value, callback) => {
      if (parseInt(value) < 0) {
        callback(new Error('需要大于等于零'))
      }
      if (parseInt(value) > 10000000) {
        callback(new Error('需要小于1千万'))
      }
      callback()
    }
    return {
      list: undefined,
      total: 0,
      listLoading: true,
      carriage: undefined,
      dialogStatus: '',
      dialogFormVisible: false,
      specList: undefined,
      specVisiable: false,
      listQuery: {
      },
      textMap: { update: '编辑', create: '创建' },
      dataForm: {
        templateId: undefined,
        templateName: undefined,
        spuLocation: undefined,
        isFree: 1,
        deliveryDeadline: undefined,
        defaultFreePrice: undefined,
        defaultFirstPrice: undefined,
        defaultFirstNum: undefined,
        defaultContinuePrice: undefined,
        defaultContinueNum: undefined,
        freightTemplateCarriageDOList: []
      },
      specForm: {
        designatedAreaList: [],
        designatedArea: undefined,
        freePrice: undefined,
        firstNum: undefined,
        firstMoney: undefined,
        continueNum: undefined,
        continueMoney: undefined
      },
      provs: ['北京市', '天津市', '河北省', '山西省', '内蒙古自治区', '辽宁省', '吉林省', '黑龙江省', '上海市', '江苏省', '浙江省', '安徽省', '福建省', '江西省', '山东省', '河南省', '湖北省', '湖南省', '广东省', '广西壮族自治区', '海南省', '重庆市', '四川省', '贵州省', '云南省', '西藏自治区', '陕西省', '甘肃省', '青海省', '宁夏回族自治区', '新疆维吾尔自治区', '台湾省', '香港特别行政区', '澳门特别行政区'],
      rules: {
        templateName: [{ required: true, message: '模板名称不能为空', trigger: 'blur' }],
        deliveryDeadline: [{ required: true, message: '发货期限不能为空', trigger: 'blur' }, { pattern: /^[0-9]*$/, message: '请输入整数' }, { validator: isNum, trigger: 'blur' }],
        defaultFreePrice: [{ required: true, message: '包邮门栏额度不能为空', trigger: 'blur' }, { pattern: /^[0-9]*$/, message: '请输入整数' }, { validator: isPrice, trigger: 'blur' }],
        deliverDistanceFree: [{ required: true, message: '免费配送距离不能为空', trigger: 'blur' }, { pattern: /^[0-9]*$/, message: '请输入整数' }, { validator: isPrice, trigger: 'blur' }],
        deliverDistanceMax: [{ required: true, message: '最大配送距离不能为空', trigger: 'blur' }, { pattern: /^[0-9]*$/, message: '请输入整数' }, { validator: isPrice, trigger: 'blur' }],
        defaultFirstNum: [{ required: true, message: '首次计费数量不能为空', trigger: 'blur' }, { pattern: /^[0-9]*$/, message: '请输入整数' }, { validator: isNum, trigger: 'blur' }],
        defaultFirstPrice: [{ required: true, message: '首次计费价格不能为空', trigger: 'blur' }, { pattern: /^[0-9]*$/, message: '请输入整数' }, { validator: isPrice, trigger: 'blur' }],
        defaultContinueNum: [{ required: true, message: '续件计费数量不能为空', trigger: 'blur' }, { pattern: /^[0-9]*$/, message: '请输入整数' }, { validator: isNum, trigger: 'blur' }],
        defaultContinuePrice: [{ required: true, message: '续件计费价格不能为空', trigger: 'blur' }, { pattern: /^[0-9]*$/, message: '请输入整数' }, { validator: isPrice, trigger: 'blur' }]
      },
      specRules: {
        freePrice: [{ required: true, message: '包邮门栏额度不能为空', trigger: 'blur' }, { pattern: /^[0-9]*$/, message: '请输入整数' }, { validator: isPrice, trigger: 'blur' }],
        firstNum: [{ required: true, message: '首次计费数量不能为空', trigger: 'blur' }, { pattern: /^[0-9]*$/, message: '请输入整数' }, { validator: isNum, trigger: 'blur' }],
        firstMoney: [{ required: true, message: '首次计费价格不能为空', trigger: 'blur' }, { pattern: /^[0-9]*$/, message: '请输入整数' }, { validator: isPrice, trigger: 'blur' }],
        continueNum: [{ required: true, message: '续件计费数量不能为空', trigger: 'blur' }, { pattern: /^[0-9]*$/, message: '请输入整数' }, { validator: isNum, trigger: 'blur' }],
        continueMoney: [{ required: true, message: '续件计费价格不能为空', trigger: 'blur' }, { pattern: /^[0-9]*$/, message: '请输入整数' }, { validator: isPrice, trigger: 'blur' }],
        designatedAreaList: [{ required: true, message: '至少选择一个地区', trigger: 'blur' }]
      }

    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      listFreight(this.listQuery)
        .then(response => {
          response.data.data.forEach(item => {
            item.freightTemplateDO.defaultContinueMoney = item.freightTemplateDO.defaultContinueMoney / 100
            item.freightTemplateDO.defaultFirstMoney = item.freightTemplateDO.defaultFirstMoney / 100
            item.freightTemplateDO.defaultFreePrice = item.freightTemplateDO.defaultFreePrice / 100

            item.freightTemplateCarriageDOList.forEach(carriageItem => {
              carriageItem.freePrice = carriageItem.freePrice / 100
              carriageItem.firstMoney = carriageItem.firstMoney / 100
              carriageItem.continueMoney = carriageItem.continueMoney / 100
            })
          })
          this.list = response.data.data
          this.listLoading = false
        })
        .catch(() => {
          this.list = []
          this.listLoading = false
        })
    },
    resetData() {
      this.dataForm = {
        templateId: undefined,
        templateName: undefined,
        spuLocation: undefined,
        isFree: 1,
        deliveryDeadline: undefined,
        defaultFreePrice: undefined,
        defaultFirstPrice: undefined,
        defaultFirstNum: undefined,
        defaultContinuePrice: undefined,
        defaultContinueNum: undefined,
        freightTemplateCarriageDOList: []
      }
    },
    resetSpec() {
      this.specForm = {
        designatedAreaList: [],
        designatedArea: undefined,
        freePrice: undefined,
        firstNum: undefined,
        firstMoney: undefined,
        continueNum: undefined,
        continueMoney: undefined
      }
    },
    deleteBtn(row) {
      this.$confirm('此操作将永久删除该运费模板---' + row.freightTemplateDO.templateName + '---, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteFreight(row.freightTemplateDO.id)
          .then(response => {
            this.$notify.success({
              title: '成功',
              message: '删除运费模板成功'
            })
            const index = this.list.indexOf(row)
            this.list.splice(index, 1)
          })
          .catch(response => {
            this.$notify.error({
              title: '失败',
              message: response.data.errmsg
            })
          })
      }).catch(() => {
        return false
      })
    },
    createHandle() {
      this.resetData()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      if (this.dataForm.isFree <= 0) {
        this.dataForm.defaultFreePrice = this.dataForm.isFree
      }
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          this.multiplyHundred(this.dataForm)
          createFreight(this.dataForm)
            .then(response => {
              this.getList()
              this.dialogFormVisible = false
              this.$notify.success({
                title: '成功',
                message: '创建运费模板成功'
              })
            })
            .catch(response => {
              this.$notify.error({
                title: '失败',
                message: response.data.errmsg
              })
            })
        }
      })
    },
    handleSpecified() {
      this.specVisiable = true
      this.resetSpec()
      this.$nextTick(() => {
        this.$refs['specForm'].clearValidate()
      })
    },
    // 打开修改模态框
    updateBtn(row) {
      this.resetData()
      console.log(row)
      this.dataForm = Object.assign({}, {
        templateId: row.freightTemplateDO.id,
        templateName: row.freightTemplateDO.templateName,
        spuLocation: row.freightTemplateDO.spuLocation,
        deliveryDeadline: row.freightTemplateDO.deliveryDeadline,
        defaultFreePrice: row.freightTemplateDO.defaultFreePrice,
        defaultFirstPrice: row.freightTemplateDO.defaultFirstMoney,
        defaultFirstNum: row.freightTemplateDO.defaultFirstNum,
        defaultContinuePrice: row.freightTemplateDO.defaultContinueMoney,
        defaultContinueNum: row.freightTemplateDO.defaultContinueNum,
        freightTemplateCarriageDOList: row.freightTemplateCarriageDOList,
        deliverDistanceFree: row.freightTemplateDO.deliverDistanceFree,
        deliverDistanceMax: row.freightTemplateDO.deliverDistanceMax,
        isFree: row.freightTemplateDO.defaultFreePrice > 0 ? 1 : row.freightTemplateDO.defaultFreePrice
      })

      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateDate() {
      if (this.dataForm.isFree <= 0) {
        this.dataForm.defaultFreePrice = this.dataForm.isFree
      }
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          this.multiplyHundred(this.dataForm)
          updateFreight(this.dataForm)
            .then(response => {
              this.getList()
              this.dialogFormVisible = false
              this.$notify.success({
                title: '成功',
                message: '修改运费模板成功'
              })
            })
            .catch(response => {
              this.$notify.error({
                title: '失败',
                message: response.data.errmsg
              })
            })
        }
      })
    },
    handleSpecAdd() {
      this.$refs['specForm'].validate(valid => {
        if (valid) {
          this.specForm.designatedArea = this.specForm.designatedAreaList.join(',')
          var temp = Object.assign({}, this.specForm)
          this.dataForm.freightTemplateCarriageDOList.unshift(temp)
          this.specVisiable = false
        }
      })
    },
    // 在模态框删除指定地区
    handleSpecDelete(row) {
      this.$confirm('此操作将删除该指定地区, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const index = this.dataForm.freightTemplateCarriageDOList.indexOf(row)
        this.dataForm.freightTemplateCarriageDOList.splice(index, 1)
      }).catch(() => {
        return false
      })
    },
    // 用于配合后台数据需要将提交数据乘以100
    multiplyHundred(data) {
      data.defaultFreePrice = data.defaultFreePrice * 100
      data.defaultFirstPrice = data.defaultFirstPrice * 100
      data.defaultContinuePrice = data.defaultContinuePrice * 100
      for (let i = 0; i < data.freightTemplateCarriageDOList.length; i++) {
        data.freightTemplateCarriageDOList[i].freePrice = data.freightTemplateCarriageDOList[i].freePrice * 100
        data.freightTemplateCarriageDOList[i].firstMoney = data.freightTemplateCarriageDOList[i].firstMoney * 100
        data.freightTemplateCarriageDOList[i].continueMoney = data.freightTemplateCarriageDOList[i].continueMoney * 100
      }
      return data
    }
  }

}
</script>
