<template>
  <div class="app-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>任务详情</span>
          <el-button style="float: right; padding: 3px 0" type="text" @click="router.back()">返回</el-button>
        </div>
      </template>
      <el-descriptions title="基本信息" :column="2" border>
        <el-descriptions-item label="任务名称">{{ task.taskName }}</el-descriptions-item>
        <el-descriptions-item label="状态">
            <el-tag v-if="task.status === 0" type="info">待开始</el-tag>
            <el-tag v-else-if="task.status === 1" type="primary">进行中</el-tag>
            <el-tag v-else-if="task.status === 2" type="success">已完成</el-tag>
            <el-tag v-else type="danger">已过期</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="巡检员">{{ task.inspectorName }}</el-descriptions-item>
        <el-descriptions-item label="所属部门">{{ task.deptName }}</el-descriptions-item>
        <el-descriptions-item label="模板名称">{{ task.templateName }}</el-descriptions-item>
        <el-descriptions-item label="截止时间">{{ task.deadline }}</el-descriptions-item>
      </el-descriptions>

      <el-divider content-position="left">检查项快照</el-divider>
      
      <el-table :data="taskItems" style="width: 100%">
        <el-table-column prop="itemName" label="检查项名称" />
        <el-table-column prop="standardDesc" label="标准描述" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getTask, getTaskItems } from "@/api/task";

const route = useRoute();
const router = useRouter();
const taskId = route.params.taskId;

const task = ref({});
const taskItems = ref([]);

function getInfo() {
    getTask(taskId).then(response => {
        task.value = response;
    });
    getTaskItems(taskId).then(response => {
        taskItems.value = response;
    });
}

onMounted(() => {
    if (taskId) {
        getInfo();
    }
});
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
