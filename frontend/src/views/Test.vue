<template>
    <Template>
        <div class="html-md" ref="htmlContainer"></div>
    </Template>
</template>



<script setup>
import { ref, onMounted } from 'vue';
import Template from "@/components/module/Template.vue";


const htmlContent = ref('');
const htmlContainer = ref(null);

onMounted(async () => {
    try {
        const response = await fetch('/项目文档.html');
        const html = await response.text();
        htmlContent.value = html;
        if (htmlContainer.value) {
            const shadowRoot = htmlContainer.value.attachShadow({ mode: 'open' });
            shadowRoot.innerHTML = htmlContent.value;
        }
    } catch (error) {
        console.error('加载 HTML 错误：', error);
    }
});
</script>


<style scoped>
.html-md {
    zoom: 1.2;
}
</style>