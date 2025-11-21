<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="我的待办" name="todo">
        <el-table :data="todoList" v-loading="loading">
          <el-table-column label="任务ID" prop="id" />
          <el-table-column label="任务名称" prop="name" />
          <el-table-column label="创建时间" prop="createTime" />
          <el-table-column label="流程实例ID" prop="processInstanceId" />
          <el-table-column label="操作">
            <template #default="scope">
              <el-button type="primary" link @click="handleComplete(scope.row)">办理</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="我的已办" name="done">
        <el-table :data="doneList" v-loading="loading">
          <el-table-column label="任务ID" prop="id" />
          <el-table-column label="任务名称" prop="name" />
          <el-table-column label="开始时间" prop="startTime" />
          <el-table-column label="结束时间" prop="endTime" />
          <el-table-column label="流程实例ID" prop="processInstanceId" />
        </el-table>
      </el-tab-pane>
    </el-tabs>

    <el-dialog title="办理任务" v-model="open" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="审批意见">
          <el-radio-group v-model="form.approved">
            <el-radio :label="true">同意</el-radio>
            <el-radio :label="false">驳回</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注">
          <el-input type="textarea" v-model="form.comment" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button type="primary" @click="submitComplete">确 定</el-button>
        <el-button @click="open = false">取 消</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { getMyTasks, getHistory, completeTask } from "@/api/workflow";
import { ElMessage } from 'element-plus';
import useUserStore from '@/store/modules/user'

const userStore = useUserStore()
const activeName = ref('todo');
const todoList = ref([]);
const doneList = ref([]);
const loading = ref(false);
const open = ref(false);
const form = reactive({
  taskId: null,
  approved: true,
  comment: ''
});

function getList() {
  loading.value = true;
  const assignee = userStore.name || 'admin'; // Fallback to admin if store not ready
  if (activeName.value === 'todo') {
    getMyTasks(assignee).then(response => {
      todoList.value = response;
      loading.value = false;
    });
  } else {
    getHistory(assignee).then(response => {
      doneList.value = response;
      loading.value = false;
    });
  }
}

function handleClick(tab, event) {
  getList();
}

function handleComplete(row) {
  form.taskId = row.id;
  form.approved = true;
  form.comment = '';
  open.value = true;
}

function submitComplete() {
  const variables = {
    approved: form.approved,
    comment: form.comment
  };
  completeTask({ taskId: form.taskId, variables: variables }).then(() => {
    ElMessage.success("办理成功");
    open.value = false;
    getList();
  });
}

onMounted(() => {
  getList();
});
</script>
