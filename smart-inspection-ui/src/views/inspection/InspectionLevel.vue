<template>
  <div class="app-container">
    <el-form :inline="true">
      <el-form-item>
        <el-button type="primary" icon="Plus" @click="handleAdd">新增</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="levelList">
      <el-table-column label="等级ID" align="center" prop="levelId" />
      <el-table-column label="等级名称" align="center" prop="levelName" />
      <el-table-column label="颜色" align="center" prop="color">
        <template #default="scope">
            <el-tag :color="scope.row.color" style="color: #fff">{{ scope.row.color }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="分值权重" align="center" prop="scoreWeight" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改等级对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="levelRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="等级名称" prop="levelName">
          <el-input v-model="form.levelName" placeholder="请输入等级名称" />
        </el-form-item>
        <el-form-item label="颜色" prop="color">
          <el-color-picker v-model="form.color" />
        </el-form-item>
        <el-form-item label="分值权重" prop="scoreWeight">
          <el-input-number v-model="form.scoreWeight" :min="0" :max="100" />
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
import { listLevel, addLevel, updateLevel, delLevel } from "@/api/inspection/base";
import { ElMessage, ElMessageBox } from 'element-plus';

const { proxy } = getCurrentInstance();

const levelList = ref([]);
const open = ref(false);
const loading = ref(true);
const title = ref("");

const data = reactive({
  form: {},
  rules: {
    levelName: [{ required: true, message: "等级名称不能为空", trigger: "blur" }],
    scoreWeight: [{ required: true, message: "分值权重不能为空", trigger: "blur" }]
  }
});

const { form, rules } = toRefs(data);

/** 查询等级列表 */
function getList() {
  loading.value = true;
  listLevel().then(response => {
    levelList.value = response;
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
    levelId: undefined,
    levelName: undefined,
    color: '#409EFF',
    scoreWeight: 10
  };
  proxy.resetForm("levelRef");
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加等级";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  form.value = JSON.parse(JSON.stringify(row));
  open.value = true;
  title.value = "修改等级";
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["levelRef"].validate(valid => {
    if (valid) {
      if (form.value.levelId != undefined) {
        updateLevel(form.value).then(response => {
          ElMessage.success("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addLevel(form.value).then(response => {
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
  ElMessageBox.confirm('是否确认删除等级编号为"' + row.levelId + '"的数据项？').then(function() {
    return delLevel(row.levelId);
  }).then(() => {
    getList();
    ElMessage.success("删除成功");
  }).catch(() => {});
}

getList();
</script>
