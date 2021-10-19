<template>
  <div class="app-container">
    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input v-model="listQuery.title" clearable class="filter-item" style="width: 200px;" placeholder="请输入兑换码标题" />
      <el-select v-model="listQuery.type" clearable style="width: 200px" class="filter-item" placeholder="请选择兑换码类型" >
        <el-option v-for="(item,index) in couponTypeMap" :key="index" :label="item.name" :value="item.value" />
      </el-select>
      <el-select v-model="listQuery.status" clearable style="width: 200px" class="filter-item" placeholder="请选择兑换码状态" >
        <el-option v-for="(item,index) in couponStatusMap" :key="index" :label="item.name" :value="item.value" />
      </el-select>
      <el-button
        v-permission="['promote:coupon:query']"
        class="filter-item"
        type="primary"
        icon="el-icon-search"
        @click="handleFilter"
      >查找</el-button>
      <el-button
        v-permission="['promote:coupon:create']"
        class="filter-item"
        type="primary"
        icon="el-icon-edit"
        @click="handleCreate"
      >添加</el-button>
      <!-- <el-button
        :loading="downloadLoading"
        class="filter-item"
        type="primary"
        icon="el-icon-download"
        @click="handleDownload"
      >当前页导出</el-button> -->
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
      style="white-space: pre-line"
    >
      <el-table-column align="center" label="兑换码ID" prop="id" sortable />

      <el-table-column align="center" label="兑换码" prop="code" />

      <el-table-column align="center" label="介绍" prop="description" />

      <el-table-column align="center" label="可用状态" prop="status">
        <template slot-scope="scope">
          <el-tag> {{ scope.row.status | formatStatus }} </el-tag>
        </template>
      </el-table-column>

      <el-table-column align="center" label="领取人" prop="userId">
        <template slot-scope="scope">
          <el-tag> {{ scope.row.userId || '未被领取' }} </el-tag>
        </template>
      </el-table-column>

      <el-table-column align="center" label="对应优惠券" prop="couponId" width="100">
        <template slot-scope="scope">
          <el-tag> {{ getCodeStr(scope.row.couponId) }} </el-tag>
        </template>
      </el-table-column>

      <el-table-column align="center" label="领券开始时间" prop="gmtStart">
        <template slot-scope="scope">{{ scope.row.gmtStart | formatGmt }}</template>
      </el-table-column>
      <el-table-column align="center" label="领券结束时间" prop="gmtEnd">
        <template slot-scope="scope">{{ scope.row.gmtEnd | formatGmt }}</template>
      </el-table-column>

      <el-table-column align="center" label="操作" width="300" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <!-- <el-button
            v-permission="['promote:coupon:update']"
            type="primary"
            size="mini"
            @click="handleStatus(scope.row)"
          >{{ scope.row.status | formatStatusBtn }}</el-button> -->
          <el-button
            v-permission="['promote:coupon:update']"
            type="info"
            size="mini"
            @click="handleRead(scope.row)"
          >查看</el-button>
          <el-button
            v-permission="['promote:coupon:delete']"
            type="danger"
            size="mini"
            @click="handleDelete(scope.row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.pageNo"
      :limit.sync="listQuery.limit"
      @pagination="getList"
    />

    <!-- 添加或修改对话框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="dataForm"
        status-icon
        label-position="left"
        label-width="100px"
        style="width: 400px; margin-left:50px;"
      >
        <el-form-item label="兑换码" prop="code">
          <el-input v-model="dataForm.code" :disabled="dialogStatus === 'update'"/>
        </el-form-item>
        <el-form-item label="介绍" prop="description" >
          <el-input v-model="dataForm.description" :disabled="dialogStatus === 'update'"/>
        </el-form-item>
        <el-form-item label="兑换码状态" prop="status">
          <el-select v-model="dataForm.status" :disabled="dialogStatus === 'update'">
            <el-option v-for="(item,index) in couponStatusOptions" :key="index" :label="item.name" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="对应优惠码" prop="status">
          <el-select v-model="dataForm.couponId" :disabled="dialogStatus === 'update'">
            <el-option v-for="(item) in couponList" :key="item.id" :label="item.title" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item prop="time">
          <el-col :span="11" >
            <el-date-picker
              :disabled="dialogStatus === 'update'"
              v-model="dataForm.gmtStart"
              type="datetime"
              placeholder="选择日期"
              style="width: 100%;"
            />
          </el-col>
          <el-col :span="2" class="line">至</el-col>
          <el-col :span="11">
            <el-date-picker
              :disabled="dialogStatus === 'update'"
              v-model="dataForm.gmtEnd"
              type="datetime"
              placeholder="选择日期"
              style="width: 100%;"
            />
          </el-col>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createData">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<style>
.el-table .cell {
  white-space: pre-line;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #20a0ff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
}
.avatar {
  width: 120px;
  height: 120px;
  display: block;
}
</style>

<script>
import { listCoupon } from '@/api/coupon'
import { listCouponCode, createCouponCode, deleteCouponCode } from '@/api/coupon_code'
import { categoryTree } from '@/api/category'
import { formatDateAndTime } from '@/filters'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination

export default {
  name: 'Coupon',
  components: { Pagination },
  filters: {
    formatGmt(time) {
      if (time == null || time === '') {
        return '无'
      }
      return formatDateAndTime(time)
    },
    formatStatus(status) {
      if (status === 0) {
        return '下架'
      } else if (status === 1) {
        return '正常'
      } else if (status < 0) {
        return '已过期'
      } else {
        return '错误状态'
      }
    },
    formatStatusBtn(status) {
      if (status === 1) {
        return '冻结'
      } else if (status === 0) {
        return '激活'
      } else if (status < 0) {
        return '已过期'
      } else {
        return '错误状态'
      }
    },
    formatGmtType(gmtType) {
      if (gmtType === 1) {
        return '直接领取'
      } else if (gmtType === 2) {
        return '激活码领取'
      } else {
        return '不可领取'
      }
    }
  },
  data() {
    return {
      couponTypeMap: [{ value: 1, name: '满减卷' }, { value: 2, name: '折扣卷' }, { value: '', name: '全部' }],
      couponGmtTypeMap: [{ value: 1, name: '直接领取' }, { value: 2, name: '激活码领取' }],
      couponStatusMap: [{ value: 0, name: '下架' }, { value: 1, name: '正常' }, { value: -1, name: '已过期' }, { value: '', name: '全部' }],
      couponStatusOptions: [{ value: 1, name: '可兑换' }, { value: 2, name: '已兑换' }],
      couponTypeOptions: [{ value: 1, name: '满减卷' }, { value: 2, name: '折扣卷' }],
      couponGmtTypeOptions: [{ value: 1, name: '直接领取' }, { value: 2, name: '激活码领取' }],
      couponList: [],
      couponListQuery: {
        pageNo: 1,
        limit: 2000,
        gmtType: 2
      },
      list: undefined,
      total: 0,
      listLoading: true,
      listQuery: {
        pageNo: 1,
        limit: 20,
        status: undefined,
        title: undefined
      },
      options: [],
      dataForm: {
        id: undefined,
        couponId: undefined,
        code: '',
        description: '',
        gmtStart: null,
        gmtEnd: null
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '创建'
      },
      rules: {
        code: [{ required: true, message: '兑换码不能为空', trigger: 'blur' }],
        status: [{ required: true, message: '兑换码状态不能为空', trigger: 'blur' }]
      },
      downloadLoading: false
    }
  },
  created() {
    this.getCouponList()
    this.getList()
  },
  methods: {
    getCodeStr(couponId) {
      for (let i = 0; i < this.couponList.length; i++) {
        if (this.couponList[i].id === couponId) {
          return this.couponList[i].description
        }
      }
      return '未定义'
    },
    getCouponList() {
      this.listLoading = true
      listCoupon(this.couponListQuery)
        .then(response => {
          // 为过期优惠卷赋负值
          response.data.data.items.forEach(item => {
            var now = new Date()
            if (item.gmtEnd < now) {
              item.status = -1
            }
            item.discount = item.discount / 100
            item.min = item.min / 100
          })
          this.couponList = response.data.data.items
          console.log(this.couponList)
          // this.total = response.data.data.total
          // this.listLoading = false
        })
        .catch(() => {
          this.couponList = []
          // this.total = 0
          // this.listLoading = false
        })
    },
    getList() {
      this.listLoading = true
      listCouponCode(this.listQuery)
        .then(response => {
          this.list = response.data.data.items
          this.total = response.data.data.total
          this.listLoading = false
        })
        .catch(() => {
          this.list = []
          this.total = 0
          this.listLoading = false
        })
    },
    handleFilter() {
      this.listQuery.pageNo = 1
      this.getList()
    },
    resetForm() {
      this.dataForm = {
        id: undefined,
        couponId: undefined,
        code: '',
        description: '',
        gmtStart: null,
        gmtEnd: null
      }
    },
    refreshOptions() {
      if (this.options.length === 0) {
        categoryTree().then(response => {
          this.options = response.data.data
        })
      }
    },
    handleLink(e) {
      if (e !== undefined) {
        const tag = e[e.length - 1]
        this.dataForm.categoryId = tag // 回调指定分类
      }
    },
    handleCreate() {
      this.resetForm()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.refreshOptions()
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      // 时间
      if (this.dataForm.gmtStart == null || this.dataForm.gmtEnd == null) {
        this.$notify.error({
          title: '失败',
          message: '请填写优惠卷可用区间'
        })
        return false
      }
      var start = new Date(this.dataForm.gmtStart)
      var end = new Date(this.dataForm.gmtEnd)
      this.dataForm.gmtStart = start.getTime()
      this.dataForm.gmtEnd = end.getTime()

      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          console.log(this.dataForm)
          createCouponCode(this.dataForm)
            .then(response => {
              this.getList()
              this.dialogFormVisible = false
              this.$notify.success({
                title: '成功',
                message: '创建兑换码成功'
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
    handleRead(row) {
      this.dataForm = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.refreshOptions()
      if (this.dataForm.days === 0 || this.dataForm.days == null) {
        this.dataForm.timeType = 1
      } else {
        this.dataForm.timeType = 0
      }
      if (this.dataForm.categoryId === '' || this.dataForm.categoryId == null) {
        this.dataForm.goodsType = 0
      } else {
        this.dataForm.goodsType = 1
      }
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    // 本来用来修改的，但是技术有限，暂且放置
    // updateData() {
    //   this.$refs['dataForm'].validate(valid => {
    //     if (valid) {
    //       updateCoupon(this.dataForm)
    //         .then(() => {
    //           for (const v of this.list) {
    //             if (v.id === this.dataForm.id) {
    //               const index = this.list.indexOf(v)
    //               this.list.splice(index, 1, this.dataForm)
    //               break
    //             }
    //           }
    //           this.dialogFormVisible = false
    //           this.$notify.success({
    //             title: '成功',
    //             message: '更新兑换码成功'
    //           })
    //         })
    //         .catch(response => {
    //           this.$notify.error({
    //             title: '失败',
    //             message: response.data.errmsg
    //           })
    //         })
    //     }
    //   })
    // },
    // 删除优惠卷
    handleDelete(row) {
      this.$confirm('此操作将永久删除该优惠卷' + row.description + ', 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteCouponCode(row)
          .then(response => {
            this.$notify.success({
              title: '成功',
              message: '删除兑换码成功'
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
    // 改变优惠卷状态
    // handleStatus(row) {
    //   // 如果优惠卷已经过期
    //   if (row.status < 0) {
    //     this.$notify.error({
    //       title: '失败',
    //       message: '过期优惠卷，建议删除'
    //     })
    //     return false
    //   }
    //   this.dataForm = Object.assign({}, row)
    //   this.dataForm.status = this.dataForm.status === 1 ? 0 : 1
    //   activeCoupon(this.dataForm)
    //     .then(response => {
    //       this.$notify.success({
    //         title: '成功',
    //         message: '修改兑换码成功'
    //       })
    //       // this.getList()
    //       row.status = row.status === 1 ? 0 : 1
    //     })
    //     .catch(response => {
    //       this.$notify.error({
    //         title: '失败',
    //         message: response.data.errmsg
    //       })
    //     })
    // },
    // 到处excl表
    handleDownload() {
      var temp = new Date()
      var date = temp.getFullYear() + '-' + temp.getMonth() + '-' + temp.getDate()
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = [
          '兑换码ID',
          '名称',
          '介绍',
          '类型',
          '最低消费',
          '减免金额',
          '每人限领',
          '兑换码数量',
          '剩余数量',
          '状态',
          '使用类目'
        ]
        const filterVal = [
          'id',
          'title',
          'description',
          'type',
          'min',
          'discount',
          'limit',
          'total',
          'surplus',
          'status',
          'cateotry'
        ]
        excel.export_json_to_excel2(
          tHeader,
          this.list,
          filterVal,
          date + ' 兑换码信息'
        )
        this.downloadLoading = false
      })
    }
  }
}
</script>
