<template>
  <div class="app-container">
    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-select
        v-model="listQuery.status"
        style="width: 200px"
        class="filter-item"
        placeholder="请选择分店状态"
      >
        <el-option v-for="(key,index) in adStatusMap" :key="index" :label="key.name" :value="key.value" />
      </el-select>
      <el-select
        v-model="listQuery.adType"
        style="width: 200px"
        class="filter-item"
        placeholder="请选择分店类型"
      >
        <el-option v-for="(key,index) in adTypeMap" :key="index" :label="key.name" :value="key.value" />
      </el-select>
      <el-button
        v-permission="['promote:advertisement:query']"
        class="filter-item"
        type="primary"
        icon="el-icon-search"
        @click="handleFilter"
      >查找</el-button>

      <el-button
        v-permission="['promote:advertisement:create']"
        class="filter-item"
        type="primary"
        icon="el-icon-edit"
        @click="handleCreate"
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
      <el-table-column align="center" label="分店ID" prop="id" sortable />

      <el-table-column align="center" label="店铺名" prop="title" />

      <el-table-column align="center" label="店铺logo" prop="logoUrl">
        <template slot-scope="scope">
          <img v-if="scope.row.logoUrl" :src="scope.row.logoUrl" width="80" >
        </template>
      </el-table-column>

      <el-table-column align="center" label="店铺地址" prop="address" />
      <el-table-column align="center" label="店铺描述" prop="description" />
      <el-table-column align="center" label="店铺经度" prop="longitude" />
      <el-table-column align="center" label="店铺维度" prop="latitude" />

      <el-table-column align="center" label="操作" width="200" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-permission="['promote:mall:update']"
            type="primary"
            size="mini"
            @click="handleUpdate(scope.row)"
          >编辑</el-button>
          <el-button
            v-permission="['promote:mall:delete']"
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
        <el-form-item label="隐藏的分店id" prop="id" hidden>
          <el-input v-model="dataForm.id" />
        </el-form-item>
        <el-form-item label="分店标题" prop="title">
          <el-input v-model="dataForm.title" />
        </el-form-item>
        <el-form-item label="分店图片" prop="logoUrl">
          <el-upload
            :headers="headers"
            :action="uploadPath"
            :show-file-list="false"
            :on-success="uploadSuccessHandle"
            :before-upload="onBeforeUpload"
            class="avatar-uploader"
            accept=".jpg, .jpeg, .png, .gif"
          >
            <img v-if="dataForm.logoUrl" ref="adImg" :src="dataForm.logoUrl" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon" />
          </el-upload>
        </el-form-item>
        <el-form-item label="分店地址" prop="address">
          <el-input v-model="dataForm.address" />
        </el-form-item>
        <el-form-item label="分店描述" prop="description">
          <el-input v-model="dataForm.description" />
        </el-form-item>
        <el-form-item label="分店经度" prop="longitude">
          <el-input v-model="dataForm.longitude" />
        </el-form-item>
        <el-form-item label="分店维度" prop="latitude">
          <el-input v-model="dataForm.latitude" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createData">确定</el-button>
        <el-button v-else type="primary" @click="updateData">确定</el-button>
      </div>
    </el-dialog>

    <canvas ref="canvas" hidden/>
  </div>
</template>

<style>
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
  width: 145px;
  height: 145px;
  display: block;
}
</style>

<script>
import { listMall, createMall, updateMall, deleteMall } from '@/api/mall'
import { spuTree } from '@/api/goods'
import { uploadPath } from '@/api/storage'
import { getToken } from '@/utils/auth'
import { clearTreeEmptyChildren } from '@/utils/index'
import Pagination from '@/components/Pagination'
import ElOption from '../../../node_modules/element-ui/packages/select/src/option' // Secondary package based on el-pagination

const adTypeMap = [{ value: 1, unionType: 3, name: '轮播' }, { value: '', name: '全部' }]
const adStatusMap = [{ value: 0, name: '冻结' }, { value: 1, name: '激活' }, { value: '', name: '全部' }]

export default {
  name: 'Ad',
  components: {
    ElOption,
    Pagination
  },
  filters: {
    adTypeFilter(code) {
      return adTypeMap[code - 1]['name']
    },
    adStatusFilter(code) {
      return adStatusMap[code]['name']
    }
  },
  data() {
    return {
      uploadPath,
      options: [],
      value: [],
      list: [],
      total: 0,
      listLoading: true,
      linkUnion: undefined,
      adTypeMap,
      adStatusMap,
      listQuery: {
        pageNo: 1,
        limit: 20,
        status: undefined,
        adType: undefined
      },
      dataForm: {
        id: undefined,
        adType: undefined,
        title: undefined,
        logoUrl: undefined,
        address: '',
        description: '',
        longitude: undefined,
        latitude: undefined
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '创建'
      },
      rules: {
        title: [{ required: true, message: '分店标题不能为空', trigger: 'blur' }]
      },
      downloadLoading: false
    }
  },
  computed: {
    headers() {
      return {
        accessToken: getToken()
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      listMall(this.listQuery)
        .then(response => {
          this.list = response.data.data.items
          this.total = response.data.data.total
          console.log(response.data.data.items)
          this.listLoading = false
        })
        .catch(() => {
          this.list = []
          this.total = 0
          this.listLoading = false
        })
    },
    handleLink(e) {
      if (e === undefined || e === null) {
        return
      }
      const tag = e[e.length - 1]
      let url = ''
      if (tag.startsWith('C')) {
        if (e.length < 3) {
          this.$notify.error({
            title: '提示',
            message: '请关联第三级类目或者商品'
          })
          return
        }
        url = '/pages/product/list?tid=' + e[2].substring(2)
      } else {
        url = '/pages/product/detail?id=' + (e[3].substring(2))
      }
      this.dataForm.url = url
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    resetForm() {
      this.dataForm = {
        id: undefined,
        adType: undefined,
        title: undefined,
        url: '',
        imgUrl: undefined,
        status: undefined,
        color: undefined
      }
      this.linkUnion = undefined
    },
    refreshOptions() {
      if (this.options.length === 0) {
        spuTree().then(response => {
          this.options = clearTreeEmptyChildren(response.data.data)
        })
      }
    },
    handleCreate() {
      this.resetForm()
      this.refreshOptions()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    uploadUrl: function(response) {
      this.dataForm.imgUrl = response.url
    },
    createData() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          createMall(this.dataForm)
            .then(response => {
              this.getList()
              this.dialogFormVisible = false
              this.$notify.success({
                title: '成功',
                message: '创建成功'
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
    checkAdType() {
      // 检测关联选项是否是三级目录或商品
      if (this.linkUnion === undefined || this.linkUnion === null || this.linkUnion.length < 3) {
        this.$notify.error({
          title: '失败',
          message: '请关联三级目录或者商品'
        })
        return false
      }

      for (let i = 0; i < this.adTypeMap.length; i++) {
        const item = this.adTypeMap[i]
        if (item.value === this.dataForm.adType) {
          if (item.unionType === 1 && this.linkUnion.length === 4) {
            this.$notify.error({
              title: '失败',
              message: '此类分店只能关联三级类目'
            })
            return false
          } else if (this.unionType === 2 && this.linkUnion.length === 3) {
            this.$notify.error({
              title: '失败',
              message: '此类分店只能关联商品'
            })
            return false
          }
        }
      }
      return true
    },
    // 点击编辑按钮时的处理
    handleUpdate(row) {
      this.dataForm = Object.assign({}, row)
      this.refreshOptions()
      this.dialogStatus = 'update'
      // if (this.dataForm.url.indexOf('tid') >= 0) {
      //   this.linkUnion = 'C_' + this.dataForm.url.replace(/[^0-9]/ig, '')
      // } else { this.linkUnion = 'G_' + this.dataForm.url.replace(/[^0-9]/ig, '') }
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          updateMall(this.dataForm)
            .then(() => {
              for (const v of this.list) {
                if (v.id === this.dataForm.id) {
                  const index = this.list.indexOf(v)
                  this.list.splice(index, 1, this.dataForm)
                  break
                }
              }
              this.dialogFormVisible = false
              this.$notify.success({
                title: '成功',
                message: '更新分店成功'
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
    handleDelete(row) {
      this.$confirm('此操作将永久删除该分店--' + row.title + '--, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteMall(row.id, row.adType)
          .then(response => {
            this.$notify.success({
              title: '成功',
              message: '删除成功'
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
    // 上传图片了处理图片
    uploadSuccessHandle(e, file) {
      console.log(e)
      console.log(file)
      this.dataForm.logoUrl = e.url
      this.dialogFormVisible = false
      this.dialogFormVisible = true
    },
    onBeforeUpload(file) {
      const isIMAGE = file.type === 'image/jpeg' || 'image/gif' || 'image/png' || 'image/jpg'
      const isLt1M = file.size / 1024 / 1024 < 1

      if (!isIMAGE) {
        this.$message.error('上传文件只能是图片格式!')
      }
      if (!isLt1M) {
        this.$message.error('上传文件大小不能超过 1MB!')
      }
      return isIMAGE && isLt1M
    }
  }
}
</script>
