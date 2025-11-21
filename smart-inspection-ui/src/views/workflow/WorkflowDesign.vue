<template>
  <div class="app-container">
    <div class="toolbar">
      <el-button type="primary" icon="Upload" @click="handleDeploy">部署流程</el-button>
      <el-button icon="Download" @click="handleDownload">下载XML</el-button>
      <el-input v-model="resourceName" placeholder="流程名称 (e.g. process.bpmn20.xml)" style="width: 200px; margin-left: 10px;" />
    </div>
    <div class="canvas" ref="canvas"></div>
  </div>
</template>

<script setup>
import { ref, onMounted, getCurrentInstance } from 'vue';
import BpmnModeler from 'bpmn-js/lib/Modeler';
import 'bpmn-js/dist/assets/diagram-js.css';
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn.css';
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn-embedded.css';
import { deployProcess } from "@/api/workflow";
import { ElMessage } from 'element-plus';

const { proxy } = getCurrentInstance();
const canvas = ref(null);
const resourceName = ref("process.bpmn20.xml");
let bpmnModeler = null;

const initialDiagram = `<?xml version="1.0" encoding="UTF-8"?>
<bpmn:definitions xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:bpmn="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:dc="http://www.omg.org/spec/DD/20100524/DC" xmlns:di="http://www.omg.org/spec/DD/20100524/DI" id="Definitions_1" targetNamespace="http://bpmn.io/schema/bpmn">
  <bpmn:process id="Process_1" isExecutable="false">
    <bpmn:startEvent id="StartEvent_1" />
  </bpmn:process>
  <bpmndi:BPMNDiagram id="BPMNDiagram_1">
    <bpmndi:BPMNPlane id="BPMNPlane_1" bpmnElement="Process_1">
      <bpmndi:BPMNShape id="_BPMNShape_StartEvent_2" bpmnElement="StartEvent_1">
        <dc:Bounds x="173" y="102" width="36" height="36" />
      </bpmndi:BPMNShape>
    </bpmndi:BPMNPlane>
  </bpmndi:BPMNDiagram>
</bpmn:definitions>`;

onMounted(() => {
  bpmnModeler = new BpmnModeler({
    container: canvas.value
  });

  bpmnModeler.importXML(initialDiagram).then(() => {
    // canvas.value.zoom('fit-viewport');
  }).catch(err => {
    console.error(err);
  });
});

function handleDeploy() {
  bpmnModeler.saveXML({ format: true }).then(({ xml }) => {
    deployProcess({ resourceName: resourceName.value, xml: xml }).then(response => {
      ElMessage.success("部署成功");
    });
  });
}

function handleDownload() {
  bpmnModeler.saveXML({ format: true }).then(({ xml }) => {
    const blob = new Blob([xml], { type: 'application/xml' });
    const link = document.createElement('a');
    link.href = URL.createObjectURL(blob);
    link.download = resourceName.value;
    link.click();
  });
}
</script>

<style scoped>
.canvas {
  width: 100%;
  height: 600px;
  border: 1px solid #ccc;
}
.toolbar {
  margin-bottom: 10px;
}
</style>
