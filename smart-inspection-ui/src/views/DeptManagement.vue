<template>
  <div class="app-container">
    <el-form :inline="true">
      <el-form-item>
        <el-button type="primary" icon="Plus" @click="handleAdd">新增</el-button>
      </el-form-item>
    </el-form>

    <el-table
      v-loading="loading"
      :data="deptList"
      row-key="deptId"
      default-expand-all
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column prop="deptName" label="部门名称" width="260"></el-table-column>
      <el-table-column prop="deptId" label="部门ID" width="100"></el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="200"></el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button link type="primary" icon="Plus" @click="handleAdd(scope.row)">新增</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改部门对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form ref="deptRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="上级部门" prop="parentId">
           <el-tree-select
              v-model="form.parentId"
              :data="deptOptions"
              :props="{ value: 'deptId', label: 'deptName', children: 'children' }"
              value-key="deptId"
              placeholder="选择上级部门"
              check-strictly
           />
        </el-form-item>
        <el-form-item label="部门名称" prop="deptName">
          <el-input v-model="form.deptName" placeholder="请输入部门名称" />
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
import { listDept, getDept, delDept, addDept, updateDept } from "@/api/dept";
import { ElMessage, ElMessageBox } from 'element-plus';

const { proxy } = getCurrentInstance();

const deptList = ref([]);
const deptOptions = ref([]);
const open = ref(false);
const loading = ref(true);
const title = ref("");

const data = reactive({
  form: {},
  rules: {
    parentId: [{ required: true, message: "上级部门不能为空", trigger: "blur" }],
    deptName: [{ required: true, message: "部门名称不能为空", trigger: "blur" }]
  }
});

const { form, rules } = toRefs(data);

/** 查询部门列表 */
function getList() {
  loading.value = true;
  listDept().then(response => {
    deptList.value = handleTree(response, "deptId");
    loading.value = false;
  });
}

/** 转换部门数据结构 */
function handleTree(data, id, parentId, children, rootId) {
  id = id || 'id'
  parentId = parentId || 'parentId'
  children = children || 'children'
  rootId = rootId || 0
  // Deep copy
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
    deptId: undefined,
    parentId: undefined,
    deptName: undefined,
    orderNum: 0,
    status: "0"
  };
  proxy.resetForm("deptRef");
}

/** 新增按钮操作 */
function handleAdd(row) {
  reset();
  if (row != undefined) {
    form.value.parentId = row.deptId;
  }
  open.value = true;
  title.value = "添加部门";
  listDept().then(response => {
    deptOptions.value = handleTree(response, "deptId");
  });
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  getDept(row.deptId).then(response => {
    form.value = response;
    open.value = true;
    title.value = "修改部门";
    listDept().then(response => {
        deptOptions.value = handleTree(response, "deptId");
    });
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["deptRef"].validate(valid => {
    if (valid) {
      if (form.value.deptId != undefined) {
        updateDept(form.value).then(response => {
          ElMessage.success("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addDept(form.value).then(response => {
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
  ElMessageBox.confirm('是否确认删除名称为"' + row.deptName + '"的数据项？').then(function() {
    return delDept(row.deptId);
  }).then(() => {
    getList();
    ElMessage.success("删除成功");
  }).catch(() => {});
}

getList();
</script>
