<template>
    <Template>
        <div class="main-container">
            <el-tabs v-model="activeName" type="card" class="demo-tabs">
                <el-tab-pane label="产品使用手册" name="first">
                    <iframe :srcdoc="html1" class="html-md-iframe"></iframe>
                </el-tab-pane>
                <el-tab-pane label="技术开发文档" name="second">
                    <iframe :srcdoc="html2" class="html-md-iframe"></iframe>
                </el-tab-pane>
            </el-tabs>
        </div>
    </Template>
</template>



<script setup>
import { ref, onMounted } from 'vue';
import Template from "@/components/layout/Template.vue";

const activeName = ref('first');
const html1 = ref(''), html2 = ref('');

onMounted(async () => {
    const r1 = await fetch('/产品使用手册.html');
    const r2 = await fetch('/技术开发文档.html');
    html1.value = await r1.text();
    html2.value = await r2.text();
});
</script>



<style scoped>
.html-md-iframe {
    width: 100%;
    height: 600px;
    border: none;
    zoom: 1.5;
}
</style>