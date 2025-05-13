<template>
    <div class="main-container">
        <el-tabs v-model="activeName" type="card" class="demo-tabs">
            <el-tab-pane label="产品使用手册" name="first">
                <div class="html-md" v-html="html1"></div>
            </el-tab-pane>
            <el-tab-pane label="技术开发文档" name="second">
                <div class="html-md" v-html="html2"></div>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>



<script setup>
import { ref, onMounted } from 'vue';

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
.html-md {
    zoom: 1.5;
}
</style>