<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="项目名称" prop="itemName">
        <el-input
          v-model="queryParams.itemName"
          placeholder="请输入项目名称"
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

    <el-table v-loading="loading" :data="itemList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="项目ID" align="center" prop="itemId" />
      <el-table-column label="项目名称" align="center" prop="itemName" :show-overflow-tooltip="true" />
      <el-table-column label="类别" align="center" prop="categoryId">
        <template #default="scope">
            {{ getCategoryName(scope.row.categoryId) }}
        </template>
      </el-table-column>
      <el-table-column label="等级" align="center" prop="levelId">
        <template #default="scope">
            {{ getLevelName(scope.row.levelId) }}
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

    <!-- 添加或修改项目对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form ref="itemRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="项目名称" prop="itemName">
          <el-input v-model="form.itemName" placeholder="请输入项目名称" />
        </el-form-item>
        <el-form-item label="所属类别" prop="categoryId">
          <el-select v-model="form.categoryId" placeholder="请选择类别">
            <el-option
              v-for="item in categoryOptions"
              :key="item.categoryId"
              :label="item.categoryName"
              :value="item.categoryId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="所属等级" prop="levelId">
          <el-select v-model="form.levelId" placeholder="请选择等级">
            <el-option
              v-for="item in levelOptions"
              :key="item.levelId"
              :label="item.levelName"
              :value="item.levelId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="标准描述" prop="standardDesc">
          <el-input v-model="form.standardDesc" type="textarea" placeholder="请输入标准描述" />
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
import { listItem, getItem, delItem, addItem, updateItem } from "@/api/inspection/item";
import { listCategory, listLevel } from "@/api/inspection/base";
import { ElMessage, ElMessageBox } from 'element-plus';

const { proxy } = getCurrentInstance();

const itemList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const categoryOptions = ref([]);
const levelOptions = ref([]);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    itemName: undefined
  },
  rules: {
    itemName: [{ required: true, message: "项目名称不能为空", trigger: "blur" }],
    categoryId: [{ required: true, message: "类别不能为空", trigger: "change" }],
    levelId: [{ required: true, message: "等级不能为空", trigger: "change" }]
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询项目列表 */
function getList() {
  loading.value = true;
  listItem(queryParams.value).then(response => {
    itemList.value = response.records;
    total.value = response.total;
    loading.value = false;
  });
}

/** 查询基础数据 */
function getBaseData() {
    listCategory().then(response => {
        categoryOptions.value = response;
    });
    listLevel().then(response => {
        levelOptions.value = response;
    });
}

function getCategoryName(id) {
    const found = categoryOptions.value.find(item => item.categoryId === id);
    return found ? found.categoryName : id;
}

function getLevelName(id) {
    const found = levelOptions.value.find(item => item.levelId === id);
    return found ? found.levelName : id;
}

/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}

/** 表单重置 */
function reset() {
  form.value = {
    itemId: undefined,
    itemName: undefined,
    categoryId: undefined,
    levelId: undefined,
    standardDesc: undefined
  };
  proxy.resetForm("itemRef");
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
  ids.value = selection.map(item => item.itemId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加项目";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const itemId = row.itemId || ids.value;
  getItem(itemId).then(response => {
    form.value = response;
    open.value = true;
    title.value = "修改项目";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["itemRef"].validate(valid => {
    if (valid) {
      if (form.value.itemId != undefined) {
        updateItem(form.value).then(response => {
          ElMessage.success("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addItem(form.value).then(response => {
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
  const itemIds = row.itemId || ids.value;
  ElMessageBox.confirm('是否确认删除项目编号为"' + itemIds + '"的数据项？').then(function() {
    return delItem(itemIds);
  }).then(() => {
    getList();
    ElMessage.success("删除成功");
  }).catch(() => {});
}

getBaseData();
getList();
</script>
