<template>
    <Template>
        <el-tabs
            v-model="activeName"
            type="card"
            class="demo-tabs"
        >
            <el-tab-pane label="产品使用手册" name="first">
                <div class="html-md" ref="ref11"></div>
            </el-tab-pane>
            <el-tab-pane label="技术开发文档" name="second">
                <div class="html-md" ref="ref22"></div>
            </el-tab-pane>
        </el-tabs>
    </Template>
</template>



<script setup>
import { ref, onMounted } from 'vue';
import Template from "@/components/layout/Template.vue";
import { ElLoading } from "element-plus";

const activeName = ref('first')
const ref1 = ref(''), ref11 = ref(null);
const ref2 = ref(''), ref22 = ref(null);

onMounted(async () => {
    const loading = ElLoading.service({
        lock: true,
        text: 'Loading',
        background: 'rgba(0, 0, 0, 0.7)',
    })
    try {
        const r1 = await fetch('/产品使用手册.html'), r2 = await fetch('/技术开发文档.html')
        const h1 = await r1.text(), h2 = await r2.text()
        ref1.value = h1;
        ref2.value = h2;
        if (ref11.value) {
            const shadowRoot = ref11.value.attachShadow({ mode: 'open' });
            shadowRoot.innerHTML = ref1.value;
        }
        if (ref22.value) {
            const shadowRoot = ref22.value.attachShadow({ mode: 'open' });
            shadowRoot.innerHTML = ref2.value;
        }
    } catch (error) {
        console.error('加载 HTML 错误：', error);
    } finally {
        loading.close()
    }
});
</script>


<style scoped>
.html-md {
    zoom: 1.2;
}
</style>