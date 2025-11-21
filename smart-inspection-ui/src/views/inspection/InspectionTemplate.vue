<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="模板名称" prop="templateName">
        <el-input
          v-model="queryParams.templateName"
          placeholder="请输入模板名称"
          clearable
          @keyup.enter="handleQuery"
        />
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
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
        >删除</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="templateList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="模板ID" align="center" prop="templateId" />
      <el-table-column label="模板名称" align="center" prop="templateName" />
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
            <el-tag v-if="scope.row.status === 1" type="success">启用</el-tag>
            <el-tag v-else type="info">停用</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
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

    <!-- 添加或修改模板对话框 -->
    <el-dialog :title="title" v-model="open" width="700px" append-to-body>
      <el-form ref="templateRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="模板名称" prop="templateName">
          <el-input v-model="form.templateName" placeholder="请输入模板名称" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="检查项">
            <el-transfer
                v-model="form.itemIds"
                :data="itemOptions"
                :titles="['未选项目', '已选项目']"
                :props="{ key: 'itemId', label: 'itemName' }"
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
import { listTemplate, getTemplate, delTemplate, addTemplate, updateTemplate } from "@/api/inspection/template";
import { listItem } from "@/api/inspection/item";
import { ElMessage, ElMessageBox } from 'element-plus';

const { proxy } = getCurrentInstance();

const templateList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const itemOptions = ref([]);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    templateName: undefined
  },
  rules: {
    templateName: [{ required: true, message: "模板名称不能为空", trigger: "blur" }]
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询模板列表 */
function getList() {
  loading.value = true;
  listTemplate(queryParams.value).then(response => {
    templateList.value = response.records;
    total.value = response.total;
    loading.value = false;
  });
}

/** 查询所有检查项 */
function getAllItems() {
    listItem({ pageNum: 1, pageSize: 1000 }).then(response => {
        itemOptions.value = response.records;
    });
}

/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}

/** 表单重置 */
function reset() {
  form.value = {
    templateId: undefined,
    templateName: undefined,
    status: 1,
    itemIds: []
  };
  proxy.resetForm("templateRef");
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

/** 多选框选中数据 */
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.templateId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  getAllItems();
  open.value = true;
  title.value = "添加模板";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  getAllItems();
  const templateId = row.templateId || ids.value;
  getTemplate(templateId).then(response => {
    form.value = response;
    open.value = true;
    title.value = "修改模板";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["templateRef"].validate(valid => {
    if (valid) {
      if (form.value.templateId != undefined) {
        updateTemplate(form.value).then(response => {
          ElMessage.success("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addTemplate(form.value).then(response => {
          ElMessage.success("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const templateIds = row.templateId || ids.value;
  ElMessageBox.confirm('是否确认删除模板编号为"' + templateIds + '"的数据项？').then(function() {
    return delTemplate(templateIds);
  }).then(() => {
    getList();
    ElMessage.success("删除成功");
  }).catch(() => {});
}

getList();
</script>
