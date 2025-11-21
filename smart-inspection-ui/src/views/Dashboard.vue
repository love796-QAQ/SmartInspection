<template>
  <div class="common-layout">
    <el-container>
      <el-header class="header">
        <div class="logo">智慧巡检系统</div>
        
        <!-- Dept Selector -->
        <div class="dept-selector" v-if="showDeptSelector" style="margin-left: 20px; flex: 1;">
           <el-tree-select
              v-model="currentDeptId"
              :data="deptOptions"
              :props="{ value: 'deptId', label: 'deptName', children: 'children' }"
              value-key="deptId"
              placeholder="切换部门查看"
              check-strictly
              :render-after-expand="false"
              style="width: 240px"
              @change="handleDeptChange"
           />
        </div>

        <div class="user-info">
          <el-dropdown @command="handleCommand">
            <span class="el-dropdown-link" style="color: white; cursor: pointer;">
              {{ userStore.name || 'Admin' }}
              <el-icon class="el-icon--right"><arrow-down /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-container>
        <el-aside width="200px" class="aside">
          <el-menu default-active="/" class="el-menu-vertical-demo" router>
            <el-menu-item index="/">
              <el-icon><icon-menu /></el-icon>
              <span>仪表盘</span>
            </el-menu-item>
            <el-sub-menu index="2">
              <template #title>
                <el-icon><setting /></el-icon>
                <span>巡检配置</span>
              </template>
              <el-menu-item index="/inspection/level">等级管理</el-menu-item>
              <el-menu-item index="/inspection/category">类别管理</el-menu-item>
              <el-menu-item index="/inspection/item">项目管理</el-menu-item>
              <el-menu-item index="/inspection/template">模板管理</el-menu-item>
            </el-sub-menu>
            <el-menu-item index="/task/list">
              <el-icon><list /></el-icon>
              <span>任务管理</span>
            </el-menu-item>
            <el-sub-menu index="4">
              <template #title>
                <el-icon><share /></el-icon>
                <span>工作流</span>
              </template>
              <el-menu-item index="/workflow/design">流程设计</el-menu-item>
              <el-menu-item index="/workflow/task">我的待办</el-menu-item>
            </el-sub-menu>
            <el-menu-item index="/punish/list">
              <el-icon><warning /></el-icon>
              <span>处罚管理</span>
            </el-menu-item>
            <el-sub-menu index="3">
              <template #title>
                <el-icon><user /></el-icon>
                <span>系统管理</span>
              </template>
              <el-menu-item index="/users">用户管理</el-menu-item>
              <el-menu-item index="/roles">角色管理</el-menu-item>
              <el-menu-item index="/depts">部门管理</el-menu-item>
            </el-sub-menu>
          </el-menu>
        </el-aside>
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { Menu as IconMenu, Setting, User, List, Share, Warning, ArrowDown } from '@element-plus/icons-vue'
import useUserStore from '@/store/modules/user'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ref, onMounted, computed } from 'vue'
import { listDept } from '@/api/dept'

const userStore = useUserStore()
const router = useRouter()
const deptOptions = ref([])
const showDeptSelector = ref(false)
const currentDeptId = ref(userStore.currentDeptId)

const handleCommand = (command) => {
  if (command === 'logout') {
    userStore.logout().then(() => {
      ElMessage.success('退出成功')
      router.push('/login')
    })
  } else if (command === 'profile') {
    router.push('/user/profile')
  }
}

const handleDeptChange = (val) => {
  userStore.setCurrentDeptId(val)
  // Optionally reload current page data if needed, or let components watch the store
  ElMessage.success('已切换部门视图')
}

// Tree Helper
const handleTree = (data, id, parentId, children) => {
  let config = {
    id: id || 'deptId',
    parentId: parentId || 'parentId',
    childrenList: children || 'children'
  }
  var childrenListMap = {}
  var nodeIds = {}
  var tree = []

  for (let d of data) {
    let parentId = d[config.parentId]
    if (childrenListMap[parentId] == null) {
      childrenListMap[parentId] = []
    }
    nodeIds[d[config.id]] = d
    childrenListMap[parentId].push(d)
  }

  for (let d of data) {
    let parentId = d[config.parentId]
    if (nodeIds[parentId] == null) {
      tree.push(d)
    }
  }

  for (let t of tree) {
    adaptToChildrenList(t)
  }

  function adaptToChildrenList(o) {
    if (childrenListMap[o[config.id]] !== null) {
      o[config.childrenList] = childrenListMap[o[config.id]]
    }
    if (o[config.childrenList]) {
      for (let c of o[config.childrenList]) {
        adaptToChildrenList(c)
      }
    }
  }
  return tree
}

// Check if dept has children
const hasChildren = (deptId, list) => {
    return list.some(d => d.parentId === deptId)
}

onMounted(() => {
  listDept().then(response => {
    const data = response.data
    deptOptions.value = handleTree(data, "deptId")
    
    // Visibility Logic
    const dataScope = userStore.dataScope
    const userDeptId = userStore.user.deptId
    
    if (dataScope === '1' || dataScope === '2') { // All or Custom
        showDeptSelector.value = true
    } else if (dataScope === '4') { // Dept + Sub
        if (hasChildren(userDeptId, data)) {
            showDeptSelector.value = true
        }
    }
    // Default hide for '3' (Dept only) and '5' (Self)
  })
})
</script>

<style scoped>
.header {
  background-color: #409EFF;
  color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.aside {
  height: calc(100vh - 60px);
  border-right: 1px solid #e6e6e6;
}
</style>
