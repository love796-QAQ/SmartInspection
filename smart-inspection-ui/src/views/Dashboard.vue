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
