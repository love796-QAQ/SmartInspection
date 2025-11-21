<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="角色名称" prop="roleName">
        <el-input
          v-model="queryParams.roleName"
          placeholder="请输入角色名称"
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

    <el-table v-loading="loading" :data="roleList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="角色编号" prop="roleId" width="120" />
      <el-table-column label="角色名称" prop="roleName" :show-overflow-tooltip="true" width="150" />
      <el-table-column label="权限字符" prop="roleCode" :show-overflow-tooltip="true" width="150" />
      <el-table-column label="显示顺序" prop="level" width="100" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" />
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

    <!-- 添加或修改角色配置对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form ref="roleRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="form.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="权限字符" prop="roleCode">
          <el-input v-model="form.roleCode" placeholder="请输入权限字符" />
        </el-form-item>
        <el-form-item label="角色等级" prop="level">
          <el-input-number v-model="form.level" controls-position="right" :min="0" />
        </el-form-item>
        <el-form-item label="数据权限">
          <el-select v-model="form.dataScope" placeholder="请选择数据权限">
            <el-option label="全部数据权限" value="1" />
            <el-option label="本部门及以下数据权限" value="2" />
            <el-option label="本部门数据权限" value="3" />
            <el-option label="仅本人数据权限" value="4" />
            <el-option label="自定义数据权限" value="5" />
          </el-select>
        </el-form-item>
        <el-form-item label="数据范围" v-show="form.dataScope == 5">
          <el-tree
            class="tree-border"
            :data="deptOptions"
            show-checkbox
            default-expand-all
            ref="deptRef"
            node-key="deptId"
            :check-strictly="false"
            empty-text="加载中，请稍候"
            :props="{ label: 'deptName', children: 'children' }"
          ></el-tree>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.description" type="textarea" placeholder="请输入内容" />
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
import { ref, reactive, toRefs, getCurrentInstance, nextTick } from 'vue';
import { listRole, getRole, delRole, addRole, updateRole } from "@/api/role";
import { listDept } from "@/api/dept";
import { ElMessage, ElMessageBox } from 'element-plus';

const { proxy } = getCurrentInstance();

const roleList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const deptOptions = ref([]);
const deptRef = ref(null);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    roleName: undefined,
    roleKey: undefined,
    status: undefined
  },
  rules: {
    roleName: [{ required: true, message: "角色名称不能为空", trigger: "blur" }],
    roleCode: [{ required: true, message: "权限字符不能为空", trigger: "blur" }],
    level: [{ required: true, message: "角色顺序不能为空", trigger: "blur" }]
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询角色列表 */
function getList() {
  loading.value = true;
  listRole(queryParams.value).then(response => {
    roleList.value = response.records;
    total.value = response.total;
    loading.value = false;
  });
}

/** 查询部门树结构 */
function getDeptTree() {
  listDept().then(response => {
    deptOptions.value = handleTree(response, "deptId");
  });
}

/** 转换部门数据结构 */
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
  if (deptRef.value != undefined) {
    deptRef.value.setCheckedKeys([]);
  }
  form.value = {
    roleId: undefined,
    roleName: undefined,
    roleCode: undefined,
    level: 0,
    status: "0",
    dataScope: "1",
    deptIds: [],
    description: undefined
  };
  proxy.resetForm("roleRef");
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
  ids.value = selection.map(item => item.roleId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  getDeptTree();
  open.value = true;
  title.value = "添加角色";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const roleId = row.roleId || ids.value;
  const roleDeptTree = getDeptTree();
  getRole(roleId).then(response => {
    form.value = response;
    open.value = true;
    nextTick(() => {
      if (form.value.deptIds) {
        deptRef.value.setCheckedKeys(form.value.deptIds);
      }
    });
    title.value = "修改角色";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["roleRef"].validate(valid => {
    if (valid) {
      if (form.value.roleId != undefined) {
        form.value.deptIds = getDeptIdChecked();
        updateRole(form.value).then(response => {
          ElMessage.success("修改成功");
          open.value = false;
          getList();
        });
      } else {
        form.value.deptIds = getDeptIdChecked();
        addRole(form.value).then(response => {
          ElMessage.success("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}

function getDeptIdChecked() {
  // Currently checked nodes
  let checkedKeys = deptRef.value.getCheckedKeys();
  // Half checked nodes (parents)
  let halfCheckedKeys = deptRef.value.getHalfCheckedKeys();
  checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys);
  return checkedKeys;
}

/** 删除按钮操作 */
function handleDelete(row) {
  const roleIds = row.roleId || ids.value;
  ElMessageBox.confirm('是否确认删除角色编号为"' + roleIds + '"的数据项？').then(function() {
    return delRole(roleIds);
  }).then(() => {
    getList();
    ElMessage.success("删除成功");
  }).catch(() => {});
}

getList();
</script>
