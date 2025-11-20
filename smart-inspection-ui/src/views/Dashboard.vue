<template>
  <div class="common-layout">
    <el-container>
      <el-header class="header">
        <div class="logo">Smart Inspection System</div>
        <div class="user-info">Admin</div>
      </el-header>
      <el-container>
        <el-aside width="200px" class="aside">
          <el-menu default-active="1" class="el-menu-vertical-demo">
            <el-menu-item index="1">
              <el-icon><icon-menu /></el-icon>
              <span>Dashboard</span>
            </el-menu-item>
            <el-menu-item index="2">
              <el-icon><setting /></el-icon>
              <span>Inspection Config</span>
            </el-menu-item>
            <el-menu-item index="3">
              <el-icon><user /></el-icon>
              <span>User Management</span>
            </el-menu-item>
          </el-menu>
        </el-aside>
        <el-main>
          <el-card>
            <h2>Welcome to Smart Inspection System</h2>
            <p>Select a module from the left menu.</p>
          </el-card>
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
