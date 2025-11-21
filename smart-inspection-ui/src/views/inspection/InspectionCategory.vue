<template>
  <div class="app-container">
    <el-form :inline="true">
      <el-form-item>
        <el-button type="primary" icon="Plus" @click="handleAdd">新增</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="categoryList">
      <el-table-column label="类别ID" align="center" prop="categoryId" />
      <el-table-column label="类别名称" align="center" prop="categoryName" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改类别对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="categoryRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="类别名称" prop="categoryName">
          <el-input v-model="form.categoryName" placeholder="请输入类别名称" />
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
import { listCategory, addCategory, updateCategory, delCategory } from "@/api/inspection/base";
import { ElMessage, ElMessageBox } from 'element-plus';

const { proxy } = getCurrentInstance();

const categoryList = ref([]);
const open = ref(false);
const loading = ref(true);
const title = ref("");

const data = reactive({
  form: {},
  rules: {
    categoryName: [{ required: true, message: "类别名称不能为空", trigger: "blur" }]
  }
});

const { form, rules } = toRefs(data);

/** 查询类别列表 */
function getList() {
  loading.value = true;
  listCategory().then(response => {
    categoryList.value = response;
    loading.value = false;
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
    categoryId: undefined,
    categoryName: undefined
  };
  proxy.resetForm("categoryRef");
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加类别";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  form.value = JSON.parse(JSON.stringify(row));
  open.value = true;
  title.value = "修改类别";
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["categoryRef"].validate(valid => {
    if (valid) {
      if (form.value.categoryId != undefined) {
        updateCategory(form.value).then(response => {
          ElMessage.success("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addCategory(form.value).then(response => {
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
  ElMessageBox.confirm('是否确认删除类别编号为"' + row.categoryId + '"的数据项？').then(function() {
    return delCategory(row.categoryId);
  }).then(() => {
    getList();
    ElMessage.success("删除成功");
  }).catch(() => {});
}

getList();
</script>
