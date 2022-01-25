<template>
  <div class="app-container">
    <el-card class="box-card">
      <h3>商铺信息</h3>
      <el-form ref="dataForm" :rules="rules" :model="dataForm" label-width="150px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="dataForm.title" />
        </el-form-item>
        <el-form-item label="商铺logo" prop="logoUrl">
          <el-upload
            :action="uploadPath"
            :show-file-list="false"
            :on-success="uploadSuccessHandle"
            :before-upload="onBeforeUpload"
            class="avatar-uploader"
            accept=".jpg, .jpeg, .png, .gif"
          >
            <img v-if="list.logoUrl" :src="dataForm.logoUrl" class="avatar" >
            <i v-else class="el-icon-plus avatar-uploader-icon" />
          </el-upload>
        </el-form-item>

        <el-form-item label="描述" prop="description">
          <el-input v-model="dataForm.description" />
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="dataForm.address" />
        </el-form-item>
        <el-form-item label="经度" prop="longitude">
          <el-input v-model="dataForm.longitude" />
        </el-form-item>
        <el-form-item label="维度" prop="latitude">
          <el-input v-model="dataForm.latitude" />
        </el-form-item>
        <el-form-item label="标语" prop="slogan">
          <el-input v-model="dataForm.slogan" />
        </el-form-item>
        <el-form-item label="开始营业时间" prop="startTime">
          <!-- <el-input v-model="dataForm.startTime" /> -->
          <el-time-picker v-model="dataForm.startTime" placeholder="选择时间" format="HH:mm" value-format="HH:mm" />
        </el-form-item>
        <el-form-item label="结束营业时间" prop="endTime">
          <el-time-picker
            v-model="dataForm.endTime"
            :picker-options="{ selectableRange:`${dataForm.startTime ? dataForm.startTime +':00' : '00:00:00'}-23:59:00`}"
            :disabled="!dataForm.startTime"
            placeholder="选择时间"
            format="HH:mm"
            value-format="HH:mm" />
        </el-form-item>
        <!-- <el-form-item label="展示方式" prop="showType">
          <el-radio-group v-model="dataForm.showType">
            <el-radio :label="1">商品列表</el-radio>
            <el-radio :label="2">点餐列表</el-radio>
          </el-radio-group>
        </el-form-item> -->
      </el-form>
    </el-card>

    <div class="op-container">
      <el-button v-permission="['promote:merchant:create']" type="primary" @click="handleEdit">保存更改</el-button>
    </div>
  </div>
</template>

<style>
.el-tag + .el-tag {
  margin-left: 10px;
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
  width: 145px;
  height: 145px;
  display: block;
}
</style>

<script>
import { uploadPath } from '@/api/storage'
import Editor from '@tinymce/tinymce-vue'
import { getToken } from '@/utils/auth'
import { listMerchant, updateMerchant } from '@/api/merchant'

export default {
  name: 'GoodsEdit',
  components: { Editor },
  data() {
    const validateLongitude = (rule, value, callback) => {
      // 经度,整数部分为0-180小数部分为0到15位
      var longreg = /^(\-|\+)?(((\d|[1-9]\d|1[0-7]\d|0{1,3})\.\d{0,15})|(\d|[1-9]\d|1[0-7]\d|0{1,3})|180\.0{0,15}|180)$/
      if (!longreg.test(value)) {
        callback(new Error('经度整数部分为0-180,小数部分为0到15位!'))
      }
      callback()
    }
    const validateLatitude = (rule, value, callback) => {
      // 纬度,整数部分为0-90小数部分为0到15位
      var latreg = /^(\-|\+)?([0-8]?\d{1}\.\d{0,15}|90\.0{0,15}|[0-8]?\d{1}|90)$/
      if (!latreg.test(value)) {
        callback(new Error('纬度整数部分为0-90,小数部分为0到15位!'))
      }
      callback()
    }
    return {
      uploadPath,
      list: [],
      dataForm: {
        title: undefined,
        logoUrl: undefined,
        description: undefined,
        address: undefined,
        longitude: undefined,
        latitude: undefined,
        showType: 1,
        startTime: undefined,
        endTime: undefined
      },
      rules: {
        //     showType: [{ required: true, message: '商铺展示类型不能为空', trigger: 'blur' }],
        address: [{ required: true, message: '商铺地址不能为空', trigger: 'blur' }],
        title: [{ required: true, message: '商铺标题不能为空', trigger: 'blur' }],
        description: [{ required: true, message: '商铺描述不能为空', trigger: 'blur' }],
        longitude: [
          { required: true, validator: validateLongitude, trigger: 'blur' },
          { validator: validateLongitude, trigger: 'change' }],
        latitude: [
          { required: true, validator: validateLatitude, trigger: 'blur' },
          { validator: validateLatitude, trigger: 'change' }],
        slogan: [{ required: true, message: '商铺标语不能为空', trigger: 'blur' }],
        startTime: [{ required: true, message: '开始营业时间不能为空', trigger: 'blur' }],
        endTime: [
          { required: true, message: '结束营业时间不能为空', trigger: 'blur' }]
      },
      listLoading: false
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

    getList: function() {
      this.listLoading = true
      listMerchant()
        .then(response => {
          this.list = response.data.data
          this.dataForm = Object.assign({}, this.list)
          this.listLoading = false
        })
        .catch(() => {
          this.list = []
          this.listLoading = false
        })
    },
    handleEdit: function() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          updateMerchant(this.dataForm)
            .then(response => {
              this.$notify.success({
                title: '成功',
                message: '更改成功'
              })
            })
            .catch(response => {
              this.$notify.error({
                title: '失败',
                message: '更改失败'
              })
            })
        } else {
          this.$notify.error({
            title: '失败',
            message: '请完善表单'
          })
        }
      })
    },
    // 上传图片了处理图片
    uploadSuccessHandle(e) {
      this.list.logoUrl = e.url
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
