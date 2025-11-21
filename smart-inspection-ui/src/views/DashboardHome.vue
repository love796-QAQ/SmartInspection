<template>
  <div>
    <h2>仪表盘概览</h2>
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header>今日巡检</template>
          <div class="stat-value">12</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header>待整改问题</template>
          <div class="stat-value text-danger">5</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header>本月绩效</template>
          <div class="stat-value text-success">98.5</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.stat-value {
  font-size: 24px;
  font-weight: bold;
  text-align: center;
}
.text-danger { color: #F56C6C; }
.text-success { color: #67C23A; }
</style>
