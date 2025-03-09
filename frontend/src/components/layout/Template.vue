<template>
    <el-backtop :right="100" :bottom="100" />
    <el-container style="width: 1680px;margin: 0 auto;">
        <el-header class="header">
            <Header />
        </el-header>
        <el-container>
            <el-aside class="sidebar" width="200px">
                <Sidebar />
            </el-aside>
            <el-main style="margin-left: 200px; padding: 20px">
                <div class="main">
                    <slot></slot>
                </div>
            </el-main>
        </el-container>
    </el-container>
</template>



<script setup>
import Header from './Header.vue'
import Sidebar from './Sidebar.vue'
</script>



<style scoped>
body {
    margin: 0;
    padding: 0;
    font-family: Arial, Helvetica, sans-serif;
}

.main {
    width: 1430px;
}

.header {
    background-color: #409EFF;
    color: white;
    padding: 0;
}

.sidebar {
    background-color: #f4f4f4;
    position: fixed;
    top: 0;
}
</style>
