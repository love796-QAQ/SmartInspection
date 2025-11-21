<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
        >新增处罚建议</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="punishList">
      <el-table-column label="ID" align="center" prop="suggestionId" />
      <el-table-column label="被处罚人" align="center" prop="userId" />
      <el-table-column label="建议金额" align="center" prop="suggestedAmount" />
      <el-table-column label="原因" align="center" prop="reason" />
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
            <el-tag v-if="scope.row.status === 0" type="warning">审批中</el-tag>
            <el-tag v-else-if="scope.row.status === 1" type="success">已通过</el-tag>
            <el-tag v-else type="danger">已驳回</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="流程实例ID" align="center" prop="processInstanceId" />
    </el-table>

    <!-- 新增对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="punishRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="被处罚人" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户ID" />
        </el-form-item>
        <el-form-item label="建议金额" prop="suggestedAmount">
          <el-input-number v-model="form.suggestedAmount" :min="0" />
        </el-form-item>
        <el-form-item label="原因" prop="reason">
          <el-input type="textarea" v-model="form.reason" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, toRefs, getCurrentInstance } from 'vue';
import request from '@/utils/request';
import { ElMessage } from 'element-plus';

const { proxy } = getCurrentInstance();

const punishList = ref([]);
const open = ref(false);
const loading = ref(true);
const title = ref("");

const data = reactive({
  form: {},
  rules: {
    userId: [{ required: true, message: "用户ID不能为空", trigger: "blur" }],
    suggestedAmount: [{ required: true, message: "金额不能为空", trigger: "blur" }]
  }
});

const { form, rules } = toRefs(data);

function getList() {
  loading.value = true;
  request({
    url: '/admin/punish/list',
    method: 'get'
  }).then(response => {
    punishList.value = response;
    loading.value = false;
  });
}

function cancel() {
  open.value = false;
  reset();
}

function reset() {
  form.value = {
    userId: undefined,
    suggestedAmount: 0,
    reason: undefined
  };
  proxy.resetForm("punishRef");
}

function handleAdd() {
  reset();
  open.value = true;
  title.value = "新增处罚建议";
}

function submitForm() {
  proxy.$refs["punishRef"].validate(valid => {
    if (valid) {
      request({
        url: '/admin/punish/submit',
        method: 'post',
        data: form.value
      }).then(response => {
        ElMessage.success("提交成功，流程已启动");
        open.value = false;
        getList();
      });
    }
  });
}

getList();
</script>
