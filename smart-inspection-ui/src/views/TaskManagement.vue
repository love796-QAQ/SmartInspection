<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="任务名称" prop="taskName">
        <el-input
          v-model="queryParams.taskName"
          placeholder="请输入任务名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="待开始" :value="0" />
          <el-option label="进行中" :value="1" />
          <el-option label="已完成" :value="2" />
          <el-option label="已过期" :value="3" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
        >指派任务</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="taskList">
      <el-table-column label="任务ID" align="center" prop="taskId" width="80" />
      <el-table-column label="任务名称" align="center" prop="taskName" :show-overflow-tooltip="true" />
      <el-table-column label="所属部门" align="center" prop="deptName" />
      <el-table-column label="巡检员" align="center" prop="inspectorName" />
      <el-table-column label="模板" align="center" prop="templateName" />
      <el-table-column label="截止时间" align="center" prop="deadline" width="160" />
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
            <el-tag v-if="scope.row.status === 0" type="info">待开始</el-tag>
            <el-tag v-else-if="scope.row.status === 1" type="primary">进行中</el-tag>
            <el-tag v-else-if="scope.row.status === 2" type="success">已完成</el-tag>
            <el-tag v-else type="danger">已过期</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="View" @click="handleView(scope.row)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 指派任务对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form ref="taskRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="任务名称" prop="taskName">
          <el-input v-model="form.taskName" placeholder="请输入任务名称" />
        </el-form-item>
        <el-form-item label="所属部门" prop="deptId">
           <el-tree-select
              v-model="form.deptId"
              :data="deptOptions"
              :props="{ value: 'deptId', label: 'deptName', children: 'children' }"
              value-key="deptId"
              placeholder="选择部门"
              check-strictly
           />
        </el-form-item>
        <el-form-item label="巡检员" prop="inspectorId">
          <el-select v-model="form.inspectorId" placeholder="请选择巡检员">
            <el-option
              v-for="item in userOptions"
              :key="item.userId"
              :label="item.realName"
              :value="item.userId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="巡检模板" prop="templateId">
          <el-select v-model="form.templateId" placeholder="请选择模板">
            <el-option
              v-for="item in templateOptions"
              :key="item.templateId"
              :label="item.templateName"
              :value="item.templateId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="截止时间" prop="deadline">
          <el-date-picker
            v-model="form.deadline"
            type="datetime"
            placeholder="选择截止时间"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
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
import { listTask, assignTask } from "@/api/task";
import { listDept } from "@/api/dept";
import { listUser } from "@/api/user";
import { listTemplate } from "@/api/inspection/template";
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';

const { proxy } = getCurrentInstance();
const router = useRouter();

const taskList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const total = ref(0);
const title = ref("");
const deptOptions = ref([]);
const userOptions = ref([]);
const templateOptions = ref([]);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    taskName: undefined,
    status: undefined
  },
  rules: {
    taskName: [{ required: true, message: "任务名称不能为空", trigger: "blur" }],
    deptId: [{ required: true, message: "部门不能为空", trigger: "change" }],
    inspectorId: [{ required: true, message: "巡检员不能为空", trigger: "change" }],
    templateId: [{ required: true, message: "模板不能为空", trigger: "change" }],
    deadline: [{ required: true, message: "截止时间不能为空", trigger: "change" }]
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询任务列表 */
function getList() {
  loading.value = true;
  listTask(queryParams.value).then(response => {
    taskList.value = response.records;
    total.value = response.total;
    loading.value = false;
  });
}

/** 查询基础数据 */
function getBaseData() {
    listDept().then(response => {
        deptOptions.value = handleTree(response, "deptId");
    });
    listUser({ pageNum: 1, pageSize: 1000 }).then(response => {
        userOptions.value = response.records;
    });
    listTemplate({ pageNum: 1, pageSize: 1000 }).then(response => {
        templateOptions.value = response.records;
    });
}

function handleTree(data, id, parentId, children, rootId) {
  id = id || 'id'
  parentId = parentId || 'parentId'
  children = children || 'children'
  rootId = rootId || 0
  const cloneData = JSON.parse(JSON.stringify(data))
  const treeData =  cloneData.filter(father => {
    let branchArr = cloneData.filter(child => {
      return father[id] === child[parentId]
    });
    if (branchArr.length > 0) {
      father[children] = branchArr;
    }
    return father[parentId] === rootId;
  });
  return treeData != "" ? treeData : data;
}

/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}

/** 表单重置 */
function reset() {
  form.value = {
    taskId: undefined,
    taskName: undefined,
    deptId: undefined,
    inspectorId: undefined,
    templateId: undefined,
    deadline: undefined
  };
  proxy.resetForm("taskRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  getBaseData();
  open.value = true;
  title.value = "指派任务";
}

/** 查看详情 */
function handleView(row) {
    const taskId = row.taskId;
    router.push("/task/detail/" + taskId);
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["taskRef"].validate(valid => {
    if (valid) {
      assignTask(form.value).then(response => {
        ElMessage.success("指派成功");
        open.value = false;
        getList();
      });
    }
  });
}

getList();
</script>
