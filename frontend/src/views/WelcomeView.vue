<template>
    <div style="display: flex;height: 100vh;justify-content: center;" v-show="showImage">
        <div style="flex: 33;position: relative;">
            <div class="img" :style="{ backgroundImage: 'url(/login.png)' }"></div>
            <div class="welcome-title">
                <div style="font-size: 30px;font-weight: bold">声骸评分系统</div>
                <div style="margin-top: 10px">作者：星开祈灵</div>
            </div>
        </div>
        <div style="flex: 10;background-color: white;z-index: 1">
            <router-view v-slot="{ Component }">
                <transition name="el-fade-in-linear" mode="out-in">
                    <component :is="Component" style="height: 100%;"/>
                </transition>
            </router-view>
        </div>
    </div>
    <div style="display: flex;height: 100vh;justify-content: center;" v-show="!showImage">
        <div style="background-color: white;z-index: 1;width: 400px;">
            <router-view v-slot="{ Component }">
                <transition name="el-fade-in-linear" mode="out-in">
                    <component :is="Component" style="height: 100%;"/>
                </transition>
            </router-view>
        </div>
    </div>
</template>



<script setup>
import { ref, onMounted } from 'vue';

const showImage = ref(true);

const updateLayout = () => {
    const width = window.innerWidth, height = window.innerHeight;
    showImage.value = width >= height;
};

onMounted(() => {
    window.addEventListener('resize', updateLayout);
    updateLayout();
});
</script>



<style scoped>
.img {
    width: 100%;
    height: 100%;
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
}

.welcome-title {
    position: absolute;
    bottom: 30px;
    left: 30px;
    color: white;
    text-shadow: 0 0 10px black;
}
</style>