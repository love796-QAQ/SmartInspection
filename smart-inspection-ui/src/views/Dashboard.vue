<template>
  <div class="common-layout">
    <el-container>
      <el-header class="header">
        <div class="logo">智慧巡检系统</div>
        <div class="user-info">管理员</div>
      </el-header>
      <el-container>
        <el-aside width="200px" class="aside">
          <el-menu default-active="/" class="el-menu-vertical-demo" router>
            <el-menu-item index="/">
              <el-icon><icon-menu /></el-icon>
              <span>仪表盘</span>
            </el-menu-item>
            <el-menu-item index="/config">
              <el-icon><setting /></el-icon>
              <span>巡检配置</span>
            </el-menu-item>
            <el-menu-item index="/users">
              <el-icon><user /></el-icon>
              <span>用户管理</span>
            </el-menu-item>
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
import { Menu as IconMenu, Setting, User } from '@element-plus/icons-vue'
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
