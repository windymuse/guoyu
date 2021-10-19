<template>
  <div class="app-container">
    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-select
        v-model="listQuery.status"
        style="width: 200px"
        class="filter-item"
        placeholder="请选择用户状态"
      >
        <el-option v-for="(key,index) in statusDic" :key="index" :label="key.name" :value="key.value" />
      </el-select>
      <el-select
        v-model="listQuery.level"
        style="width: 200px"
        class="filter-item"
        placeholder="请选择用户会员等级"
      >
        <el-option v-for="(key,index) in levelDic" :key="index" :label="key.name" :value="key.value" />
      </el-select>
      <el-select
        v-model="listQuery.gender"
        style="width: 200px"
        class="filter-item"
        placeholder="请选择用户性别"
      >
        <el-option v-for="(key,index) in genderDic" :key="index" :label="key.name" :value="key.value" />
      </el-select>
      <el-input
        v-model="listQuery.id"
        clearable
        class="filter-item"
        style="width: 200px;"
        placeholder="请输入用户Id"
      />
      <el-input
        v-model="listQuery.name"
        clearable
        class="filter-item"
        style="width: 200px;"
        placeholder="请输入昵称"
      />
      <el-button
        v-permission="['system:member_level:query']"
        class="filter-item"
        type="primary"
        icon="el-icon-search"
        @click="handleFilter"
      >查找</el-button>
      <!--添加用户-->
      <el-button
        v-permission="['system:member_level:create']"
        class="filter-item"
        type="primary"
        icon="el-icon-edit"
        @click="handleCreate"
      >添加会员等级</el-button>
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
      <!-- <el-table-column align="center" width="100px" label="用户ID" prop="id" sortable /> -->

      <el-table-column align="center" label="会员等级称号" prop="name" />

      <el-table-column align="center" label="会员等级" prop="degree" />

      <el-table-column align="center" label="通用折扣" prop="percent">
        <template slot-scope="scope">
          {{ scope.row.percent / 100 }}
        </template>
      </el-table-column>

      <el-table-column align="center" label="等级图片" prop="img">
        <template slot-scope="scope">
          <img :src="scope.row.img" style="height: 60px;" alt="">
        </template>
      </el-table-column>

      <el-table-column align="center" label="等级描述" prop="description" />

      <el-table-column align="center" label="操作" width="300" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-permission="['system:member_level:update']"
            type="primary"
            size="mini"
            @click="handleUpdate(scope.row)"
          >编辑</el-button>
          <el-button
            v-permission="['system:member_level:delete']"
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
        label-width="120px"
        style="width: 400px; margin-left:50px;"
      >
        <el-form-item label="隐藏的用户id" prop="id" hidden>
          <el-input v-model="dataForm.id" />
        </el-form-item>
        <el-form-item label="会员等级称号" prop="name">
          <el-input v-model="dataForm.name" />
        </el-form-item>
        <el-form-item label="会员等级" prop="degree">
          <el-input v-model="dataForm.degree" />
        </el-form-item>
        <el-form-item label="通用折扣" prop="percent">
          <el-input v-model="dataForm.percent" />
        </el-form-item>
        <el-form-item label="会员等级描述" prop="description">
          <el-input v-model="dataForm.description" type="textarea" />
        </el-form-item>
        <el-form-item label="商品图片">
          <el-upload
            :action="uploadPath"
            :headers="headers"
            :limit="1"
            :file-list="imgsFileList"
            :on-exceed="uploadOverrun"
            :on-success="handleimgsUrl"
            :on-remove="handleRemove"
            :class="{ hide: hideUpload }"
            accept=".jpg, .jpeg, .png, .gif"
            list-type="picture-card"
          >
            <i v-if="imgsFileList.length == 0" class="el-icon-plus" />
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createData">确定</el-button>
        <el-button v-else type="primary" @click="updateData">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listMemberLevel, createMemberLevel, updateMemberLevel, deleteMemberLevel } from '@/api/member_level'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import { uploadPath } from '@/api/storage'
import { getToken } from '@/utils/auth'

const genderDic = [{ value: 1, name: '女' }, { value: 2, name: '男' }, { value: '', name: '全部' }]
const levelDic = [{ value: 0, name: '普通会员' }, { value: 1, name: 'VIP会员' }, { value: '', name: '全部' }]
const statusDic = [{ value: 0, name: '冻结' }, { value: 1, name: '激活' }, { value: '', name: '全部' }]
export default {
  name: 'MemberLevel',
  components: { Pagination },
  filters: {
    genderDicFilter(code) {
      if (code === 1 || code === 2) {
        return genderDic[code - 1].name
      } else {
        return '未知性别'
      }
    },
    levelDicFilter(code) {
      if (code === 0 || code === 1) {
        return levelDic[code].name
      } else {
        return '未知等级'
      }
    }
  },
  data() {
    return {
      uploadPath,
      imgsFileList: [],
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        pageNo: 1,
        limit: 20,
        id: undefined,
        name: undefined,
        degree: undefined,
        percent: undefined,
        description: undefined
      },
      dataForm: {
        id: undefined,
        name: undefined,
        degree: undefined,
        percent: undefined,
        description: undefined,
        img: undefined
      },
      downloadLoading: false,
      genderDic,
      levelDic,
      statusDic,
      textMap: { update: '编辑', create: '创建' },
      dialogFormVisible: false,
      dialogStatus: '',
      rules: {
        name: [{ required: true, message: '用户昵称不能为空', trigger: 'blur' }],
        phone: [{ required: true, message: '用户手机不能为空', trigger: 'blur' }, { pattern: /^1[3456789]\d{9}$/, message: '请输入正确电话' }],
        gender: [{ required: true, message: '请选择用户性别类型', trigger: 'blur' }],
        level: [{ required: true, message: '请选择用户会员等级状态', trigger: 'blur' }],
        status: [{ required: true, message: '请选择用户状态', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }, { pattern: /^[0-9a-zA-Z$./]{8}/, message: '至少八个数字和字母' }]
      }
    }
  },
  computed: {
    headers() {
      return {
        accessToken: getToken()
      }
    },
    hideUpload() {
      return this.imgsFileList.length === 1
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      listMemberLevel(this.listQuery)
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
    resetForm() {
      this.dataForm = {
        id: undefined,
        name: undefined,
        degree: undefined,
        percent: undefined,
        description: undefined,
        img: undefined
      }
    },
    handleFilter() {
      this.listQuery.pageNo = 1
      this.getList()
    },
    handleCreate() {
      this.resetForm()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          createMemberLevel(this.dataForm)
            .then(response => {
              this.list.unshift(response.data.data)
              this.dialogFormVisible = false
              this.$notify.success({
                title: '成功',
                message: '创建成功'
              })
              this.listQuery.name = this.dataForm.name
              this.getList()
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
    uploadOverrun: function() {
      this.$message({
        type: 'error',
        message: '上传文件个数超出限制!最多上传1张图片!'
      })
    },
    handleimgsUrl(response, file, fileList) {
      if (response.errno === 200) {
        console.log(response.url)
        this.dataForm.img = response.url
      }
    },
    handleRemove: function(file, fileList) {
      for (var i = 0; i < this.goods.imgList.length; i++) {
        // 这里存在两种情况
        // 1. 如果所删除图片是刚刚上传的图片，那么图片地址是file.response.url
        //    此时的file.url虽然存在，但是是本机地址，而不是远程地址。
        // 2. 如果所删除图片是后台返回的已有图片，那么图片地址是file.url
        var url
        if (file.response === undefined) {
          url = file.url
        } else {
          url = file.response.url
        }

        if (this.goods.imgList[i] === url) {
          this.goods.imgList.splice(i, 1)
        }
      }
      if (this.goods.imgList.length > 0) {
        this.goods.img = this.goods.imgList[0]
      }
    },
    handleUpdate(row) {
      this.dataForm = Object.assign({}, row)
      this.imgsFileList.push({ name: 'avatar', url: row.img })
      console.log([row.img])
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          updateMemberLevel(this.dataForm)
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
                message: '更新用户成功'
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
      this.$confirm('此操作将永久删除该用户---' + row.name + '---, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteMemberLevel(row.id, row.name)
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
    }
  }
}
</script>

<style scoped>
.hide>.el-upload {
  display: none !important;
}
</style>
