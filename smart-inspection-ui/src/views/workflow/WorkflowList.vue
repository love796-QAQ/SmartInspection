<template>
  <div class="app-container">
    <el-table :data="definitionList" v-loading="loading">
      <el-table-column label="流程ID" prop="id" />
      <el-table-column label="流程名称" prop="name" />
      <el-table-column label="Key" prop="key" />
      <el-table-column label="版本" prop="version" />
      <el-table-column label="部署时间" prop="deploymentTime" />
      <el-table-column label="操作">
        <template #default="scope">
          <el-button type="primary" link>启动</el-button>
          <el-button type="danger" link>挂起</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
// import { listDefinition } from "@/api/workflow"; // Need to implement this API

const definitionList = ref([]);
const loading = ref(false);

function getList() {
  // loading.value = true;
  // listDefinition().then(response => {
  //   definitionList.value = response;
  //   loading.value = false;
  // });
}

onMounted(() => {
  getList();
});
</script>
