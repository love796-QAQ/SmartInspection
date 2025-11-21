<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card class="box-card">
          <template #header>
            <div class="clearfix">
              <span>个人信息</span>
            </div>
          </template>
          <div class="text-center">
            <el-avatar :size="100" :src="userStore.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'"></el-avatar>
          </div>
          <ul class="list-group list-group-striped">
            <li class="list-group-item">
              用户名称
              <div class="pull-right">{{ userStore.user.username }}</div>
            </li>
            <li class="list-group-item">
              真实姓名
              <div class="pull-right">{{ userStore.user.realName }}</div>
            </li>
            <li class="list-group-item">
              所属部门
              <div class="pull-right">{{ userStore.user.deptId }}</div>
            </li>
            <li class="list-group-item">
              所属角色
              <div class="pull-right">{{ userStore.user.roleId }}</div>
            </li>
          </ul>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card>
          <template #header>
            <div class="clearfix">
              <span>基本资料</span>
            </div>
          </template>
          <el-tabs v-model="activeTab">
            <el-tab-pane label="基本资料" name="userinfo">
              <el-form ref="formRef" :model="form" label-width="80px">
                <el-form-item label="用户昵称" prop="realName">
                  <el-input v-model="form.realName" maxlength="30" />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="submit">保存</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>
            <el-tab-pane label="修改密码" name="resetPwd">
              <el-form ref="pwdFormRef" :model="pwdForm" :rules="pwdRules" label-width="80px">
                <el-form-item label="旧密码" prop="oldPassword">
                  <el-input v-model="pwdForm.oldPassword" type="password" show-password />
                </el-form-item>
                <el-form-item label="新密码" prop="newPassword">
                  <el-input v-model="pwdForm.newPassword" type="password" show-password />
                </el-form-item>
                <el-form-item label="确认密码" prop="confirmPassword">
                  <el-input v-model="pwdForm.confirmPassword" type="password" show-password />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="submitPwd">保存</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import useUserStore from '@/store/modules/user'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const activeTab = ref('userinfo')

const form = reactive({
  realName: userStore.user.realName,
  userId: userStore.user.userId
})

const pwdForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const pwdRules = {
  oldPassword: [{ required: true, message: '请输入旧密码', trigger: 'blur' }],
  newPassword: [{ required: true, message: '请输入新密码', trigger: 'blur' }],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== pwdForm.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

const submit = () => {
  ElMessage.success('模拟保存成功')
  // TODO: Call API
}

const submitPwd = () => {
  ElMessage.success('模拟修改密码成功')
  // TODO: Call API
}
</script>

<style scoped>
.list-group-item {
  border-bottom: 1px solid #e7eaec;
  border-top: 1px solid #e7eaec;
  margin-bottom: -1px;
  padding: 11px 0;
  font-size: 13px;
  display: flex;
  justify-content: space-between;
}
.text-center {
  text-align: center;
  padding-bottom: 20px;
}
</style>
